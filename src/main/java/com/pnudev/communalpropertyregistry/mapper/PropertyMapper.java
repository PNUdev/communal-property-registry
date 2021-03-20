package com.pnudev.communalpropertyregistry.mapper;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationDto;
import com.pnudev.communalpropertyregistry.dto.response.AttachmentResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.service.AttachmentCategoryService;
import com.pnudev.communalpropertyregistry.service.AttachmentService;
import com.pnudev.communalpropertyregistry.service.CategoryByPurposeService;
import com.querydsl.core.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;
import static java.util.Objects.nonNull;

@Component
public class PropertyMapper {

    private final AttachmentService attachmentService;

    private final AttachmentCategoryService attachmentCategoryService;

    private final CategoryByPurposeService categoryByPurposeService;

    @Autowired
    public PropertyMapper(AttachmentService attachmentService,
                          AttachmentCategoryService attachmentCategoryService,
                          CategoryByPurposeService categoryByPurposeService) {

        this.attachmentService = attachmentService;
        this.attachmentCategoryService = attachmentCategoryService;
        this.categoryByPurposeService = categoryByPurposeService;
    }

    public PropertyResponseDto mapToPropertyResponseDto(Property property) {

        List<Attachment> attachments = attachmentService.findByPropertyId(property.getId());
        List<AttachmentCategory> attachmentCategories = attachmentCategoryService.findAll();
        CategoryByPurpose categoryByPurpose = categoryByPurposeService
                .findById(property.getCategoryByPurposeId());

        return PropertyResponseDto.builder()
                .id(property.getId())
                .area(property.getArea())
                .name(property.getName())
                .address(property.getAddress())
                .imageUrl(property.getImageUrl())
                .propertyStatus(property.getPropertyStatus())
                .propertyLocation(property.getPropertyLocation())
                .owner(validate(
                        property.isOwnerPubliclyViewable(),
                        property.getOwner()))
                .amountOfRent(validate(
                        property.isAmountOfRentPubliclyViewable(),
                        property.getAmountOfRent()))
                .balanceHolder(validate(
                        property.isBalanceHolderPubliclyViewable(),
                        property.getBalanceHolder()))
                .areaTransferred(validate(
                        property.isAreaTransferredPubliclyViewable(),
                        property.getAreaTransferred()))
                .leaseAgreementEndDate(validate(
                        property.isLeaseAgreementEndDatePubliclyViewable(),
                        property.getLeaseAgreementEndDate()))
                .categoryByPurposeName(categoryByPurpose.getName())
                .attachments(createAttachmentResponseDto(property.getId(), attachments, attachmentCategories))
                .build();
    }

    public List<PropertyResponseDto> mapToPropertyResponseDto(List<Tuple> properties) {

        List<Attachment> attachments = attachmentService
                .findByPropertyIdIn(properties.stream()
                        .map(prop -> prop.get(property.id))
                        .collect(Collectors.toList()));

        List<AttachmentCategory> attachmentCategories = attachmentCategoryService.findAll();
        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeService.finAllCategories();

        return properties.stream()
                .map((property) -> mapToPropertyResponseDto(
                        property,
                        attachments,
                        attachmentCategories,
                        categoriesByPurpose))
                .collect(Collectors.toList());
    }

    public PropertyLocationDto mapToPropertyLocationDto(Tuple tuple) {

        return PropertyLocationDto.builder()
                .propertyId(tuple.get(property.id))
                .propertyStatus(Property.PropertyStatus.valueOf(tuple.get(property.propertyStatus)))
                .lat(tuple.get(property.lat))
                .lon(tuple.get(property.lon))
                .build();
    }

    private AttachmentResponseDto createAttachmentResponseDto(List<Attachment> attachments,
                                                              AttachmentCategory attachmentCategory) {

        Attachment attachment = attachments.stream()
                .filter((a) -> a.getAttachmentCategoryId().equals(attachmentCategory.getId())
                        && a.isPubliclyViewable())
                .findFirst()
                .orElse(null);

        if (nonNull(attachment)) {
            return AttachmentResponseDto.builder()
                    .categoryName(attachmentCategory.getName())
                    .link(attachment.getLink())
                    .note(attachment.getNote())
                    .build();
        }

        return null;
    }

    private List<AttachmentResponseDto> createAttachmentResponseDto(Long propertyId,
                                                                    List<Attachment> attachments,
                                                                    List<AttachmentCategory> attachmentCategories) {

        List<Attachment> filteredAttachments = attachments.stream()
                .filter(attachment -> attachment.getPropertyId().equals(propertyId))
                .collect(Collectors.toList());

        return attachmentCategories.stream()
                .filter(AttachmentCategory::isPubliclyViewable)
                .map((category) -> createAttachmentResponseDto(filteredAttachments, category))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private PropertyResponseDto mapToPropertyResponseDto(Tuple propertyTuple,
                                                         List<Attachment> attachments,
                                                         List<AttachmentCategory> attachmentCategories,
                                                         List<CategoryByPurpose> categoriesByPurpose) {

        CategoryByPurpose categoryByPurpose = categoriesByPurpose.stream()
                .filter(category -> category.getId()
                        .equals(propertyTuple.get(property.id)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Категорія не знайдена!"));

        Property.PropertyStatus propertyStatus = Property.PropertyStatus
                .fromName(propertyTuple.get(property.propertyStatus))
                .orElseThrow(() -> new RuntimeException("Статус не існує!"));

        Property.PropertyLocation propertyLocation = Property.PropertyLocation.builder()
                .lat(propertyTuple.get(property.lat))
                .lon(propertyTuple.get(property.lon))
                .build();

        return PropertyResponseDto.builder()
                .id(propertyTuple.get(property.id))
                .area(propertyTuple.get(property.area))
                .name(propertyTuple.get(property.name))
                .address(propertyTuple.get(property.address))
                .imageUrl(propertyTuple.get(property.imageUrl))
                .propertyStatus(propertyStatus)
                .propertyLocation(propertyLocation)
                .owner(validate(
                        propertyTuple.get(property.isOwnerPubliclyViewable),
                        propertyTuple.get(property.owner)))
                .amountOfRent(validate(
                        propertyTuple.get(property.isAmountOfRentPubliclyViewable),
                        propertyTuple.get(property.amountOfRent)))
                .balanceHolder(validate(
                        propertyTuple.get(property.isBalanceHolderPubliclyViewable),
                        propertyTuple.get(property.balanceHolder)))
                .areaTransferred(validate(
                        propertyTuple.get(property.isAreaTransferredPubliclyViewable),
                        propertyTuple.get(property.areaTransferred)))
                .leaseAgreementEndDate(validate(
                        propertyTuple.get(property.isLeaseAgreementEndDatePubliclyViewable),
                        propertyTuple.get(property.leaseAgreementEndDate).toLocalDate()))
                .categoryByPurposeName(categoryByPurpose.getName())
                .attachments(
                        createAttachmentResponseDto(
                                propertyTuple.get(property.id), attachments, attachmentCategories))
                .build();
    }

    private <T> T validate(Boolean condition, T object) {
        return condition ? object : null;
    }

}
