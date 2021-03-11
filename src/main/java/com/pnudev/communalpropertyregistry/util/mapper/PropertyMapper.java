package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationDto;
import com.pnudev.communalpropertyregistry.dto.response.AttachmentResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import com.pnudev.communalpropertyregistry.service.AttachmentCategoryService;
import com.pnudev.communalpropertyregistry.service.AttachmentService;
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

    private final CategoryByPurposeRepository categoryByPurposeRepository;

    @Autowired
    public PropertyMapper(AttachmentService attachmentService,
                          AttachmentCategoryService attachmentCategoryService,
                          CategoryByPurposeRepository categoryByPurposeRepository) {

        this.attachmentService = attachmentService;
        this.attachmentCategoryService = attachmentCategoryService;
        this.categoryByPurposeRepository = categoryByPurposeRepository;
    }

    public PropertyResponseDto mapToPropertyResponseDto(Property property) {

        List<Attachment> attachments = attachmentService.findByPropertyId(property.getId());
        List<AttachmentCategory> attachmentCategories = attachmentCategoryService.findAll();
        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeRepository.findAll();

        return mapToPropertyResponseDto(property,
                attachments,
                attachmentCategories,
                categoriesByPurpose);
    }

    public List<PropertyResponseDto> mapToPropertyResponseDto(List<Property> properties) {

        List<Attachment> attachments = attachmentService
                .findByPropertyIdIn(properties.stream()
                        .map(Property::getId)
                        .collect(Collectors.toList()));

        List<AttachmentCategory> attachmentCategories = attachmentCategoryService.findAll();
        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeRepository.findAll();

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

    public Property mapTupleToProperty(Tuple tuple) {

        Property.PropertyLocation propertyLocation = Property.PropertyLocation.builder()
                .lat(tuple.get(property.lat))
                .lon(tuple.get(property.lon))
                .build();

        return Property.builder()
                .id(tuple.get(property.id))
                .imageUrl(tuple.get(property.imageUrl))
                .address(tuple.get(property.address))
                .propertyLocation(propertyLocation)
                .name(tuple.get(property.name))
                .categoryByPurposeId(tuple.get(property.categoryByPurposeId))
                .propertyStatus(Property.PropertyStatus.valueOf(tuple.get(property.propertyStatus)))
                .area(tuple.get(property.area))
                .areaTransferred(tuple.get(property.areaTransferred))
                .balanceHolder(tuple.get(property.balanceHolder))
                .owner(tuple.get(property.owner))
                .leaseAgreementEndDate(tuple.get(property.leaseAgreementEndDate).toLocalDate())
                .amountOfRent(tuple.get(property.amountOfRent))
                .isAreaTransferredPubliclyViewable(tuple.get(property.isAreaTransferredPubliclyViewable))
                .isBalanceHolderPubliclyViewable(tuple.get(property.isBalanceHolderPubliclyViewable))
                .isOwnerPubliclyViewable(tuple.get(property.isOwnerPubliclyViewable))
                .isLeaseAgreementEndDatePubliclyViewable(tuple.get(property.isLeaseAgreementEndDatePubliclyViewable))
                .isAmountOfRentPubliclyViewable(tuple.get(property.isAmountOfRentPubliclyViewable))
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

    private List<AttachmentResponseDto> createAttachmentResponseDto(Property property,
                                                                    List<Attachment> attachments,
                                                                    List<AttachmentCategory> attachmentCategories) {

        List<Attachment> filteredAttachments = attachments.stream()
                .filter(attachment -> attachment.getPropertyId().equals(property.getId()))
                .collect(Collectors.toList());

        return attachmentCategories.stream()
                .filter(AttachmentCategory::isPubliclyViewable)
                .map((category) -> createAttachmentResponseDto(filteredAttachments, category))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private PropertyResponseDto mapToPropertyResponseDto(Property property,
                                                         List<Attachment> attachments,
                                                         List<AttachmentCategory> attachmentCategories,
                                                         List<CategoryByPurpose> categoriesByPurpose) {

        String categoryByPurposeName = categoriesByPurpose.stream()
                .filter(category -> category.getId().equals(property.getCategoryByPurposeId()))
                .findFirst()
                .get()
                .getName();

        return PropertyResponseDto.builder()
                .id(property.getId())
                .area(property.getArea())
                .name(property.getName())
                .address(property.getAddress())
                .imageUrl(property.getImageUrl())
                .propertyStatus(property.getPropertyStatus())
                .propertyLocation(property.getPropertyLocation())
                .owner(property.isOwnerPubliclyViewable() ? property.getOwner() : null)
                .amountOfRent(property.isAmountOfRentPubliclyViewable() ?
                        property.getAmountOfRent() : null)
                .balanceHolder(property.isBalanceHolderPubliclyViewable() ?
                        property.getBalanceHolder() : null)
                .areaTransferred(property.isAreaTransferredPubliclyViewable() ?
                        property.getAreaTransferred() : null)
                .leaseAgreementEndDate(property.isLeaseAgreementEndDatePubliclyViewable()
                        ? property.getLeaseAgreementEndDate() : null)
                .categoryByPurposeName(categoryByPurposeName)
                .attachments(createAttachmentResponseDto(property, attachments, attachmentCategories))
                .build();
    }

}
