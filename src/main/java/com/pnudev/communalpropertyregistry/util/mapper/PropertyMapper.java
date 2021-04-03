package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationDto;
import com.pnudev.communalpropertyregistry.dto.response.AttachmentResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.exception.PropertyAdminException;
import com.pnudev.communalpropertyregistry.exception.ServiceApiException;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import com.pnudev.communalpropertyregistry.service.AttachmentCategoryService;
import com.pnudev.communalpropertyregistry.service.AttachmentService;
import com.pnudev.communalpropertyregistry.service.CategoryByPurposeService;
import com.querydsl.core.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;
import static java.util.Objects.nonNull;

@Component
public class PropertyMapper {

    private final CategoryByPurposeRepository categoryByPurposeRepository;

    private final AttachmentService attachmentService;

    private final AttachmentCategoryService attachmentCategoryService;

    private final CategoryByPurposeService categoryByPurposeService;

    @Autowired
    public PropertyMapper(CategoryByPurposeRepository categoryByPurposeRepository,
                          AttachmentService attachmentService,
                          AttachmentCategoryService attachmentCategoryService,
                          CategoryByPurposeService categoryByPurposeService) {

        this.categoryByPurposeRepository = categoryByPurposeRepository;
        this.attachmentService = attachmentService;
        this.attachmentCategoryService = attachmentCategoryService;
        this.categoryByPurposeService = categoryByPurposeService;
    }

    public PropertyLocationDto mapToPropertyLocationDto(Tuple tuple) {

        return PropertyLocationDto.builder()
                .propertyId(tuple.get(property.id))
                .propertyStatus(Property.PropertyStatus.valueOf(tuple.get(property.propertyStatus)))
                .lat(tuple.get(property.lat))
                .lon(tuple.get(property.lon))
                .build();
    }

    public List<Property> mapToProperties(List<Tuple> properties) {
        return properties.stream().map(this::mapToProperty).collect(Collectors.toList());
    }

    public Property mapToProperty(Tuple tuple) {

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
                .leaseAgreementEndDate(
                        Objects.nonNull(tuple.get(property.leaseAgreementEndDate)) ?
                        tuple.get(property.leaseAgreementEndDate).toLocalDate() : null)
                .amountOfRent(tuple.get(property.amountOfRent))
                .isAreaTransferredPubliclyViewable(tuple.get(property.isAreaTransferredPubliclyViewable))
                .isBalanceHolderPubliclyViewable(tuple.get(property.isBalanceHolderPubliclyViewable))
                .isOwnerPubliclyViewable(tuple.get(property.isOwnerPubliclyViewable))
                .isLeaseAgreementEndDatePubliclyViewable(tuple.get(property.isLeaseAgreementEndDatePubliclyViewable))
                .isAmountOfRentPubliclyViewable(tuple.get(property.isAmountOfRentPubliclyViewable))
                .build();
    }

    public List<PropertyAdminDto> mapToPropertiesAdminDto(List<Property> properties) {

        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeRepository.findAll();

        return properties.stream()
                .map(property -> mapToPropertyAdminDto(property, categoriesByPurpose))
                .collect(Collectors.toList());
    }

    public PropertyAdminDto mapToPropertyAdminDto(Property property) {

        Optional<CategoryByPurpose> categoryByPurpose = categoryByPurposeRepository.findById(property.getCategoryByPurposeId());

        return this.mapToPropertyAdminDto(property, Collections.singletonList(categoryByPurpose.orElseThrow(
                () -> new PropertyAdminException("Категорії за призначенням не знайдено!"))));
    }

    public PropertyResponseDto mapToPropertyResponseDto(Property property) {

        List<Attachment> attachments = attachmentService.findByPropertyId(property.getId());
        List<AttachmentCategory> attachmentCategories = attachmentCategoryService.findAll();
        CategoryByPurpose categoryByPurpose = categoryByPurposeService.findById(property.getCategoryByPurposeId());

        return buildPropertyResponseDto(property, categoryByPurpose, attachments, attachmentCategories);
    }

    public Page<PropertyResponseDto> mapToPropertyResponseDto(Page<Property> properties, Boolean onlyPublicFields) {

        List<Attachment> filteredAttachments = attachmentService.findByPropertyIdIn(properties.getContent().stream()
                .map(Property::getId)
                .collect(Collectors.toList()));

        List<AttachmentCategory> attachmentCategories = attachmentCategoryService.findAll();

        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeService.findAll();

        return properties.map(property -> mapToPropertyResponseDto(property, filteredAttachments,
                attachmentCategories, categoriesByPurpose, onlyPublicFields));
    }

    private PropertyResponseDto mapToPropertyResponseDto(Property property,
                                                         List<Attachment> attachments,
                                                         List<AttachmentCategory> attachmentCategories,
                                                         List<CategoryByPurpose> categoriesByPurpose,
                                                         Boolean onlyPublicFields) {

        List<Attachment> propertyAttachments = attachments.stream()
                .filter(attachment -> attachment.getPropertyId().equals(property.getId()))
                .collect(Collectors.toList());

        CategoryByPurpose propertyCategoryByPurpose = categoriesByPurpose.stream()
                .filter(category -> category.getId().equals(property.getCategoryByPurposeId()))
                .findFirst()
                .orElseThrow(() -> new ServiceApiException("Категорія за призначенням не знайдено!"));

        if (onlyPublicFields) {
            return buildPropertyResponseDto(property, propertyCategoryByPurpose,
                    propertyAttachments, attachmentCategories);
        } else {
            return buildFullPropertyResponseDto(property, propertyCategoryByPurpose,
                    propertyAttachments, attachmentCategories);
        }
    }

    private PropertyResponseDto buildPropertyResponseDto(Property property,
                                                         CategoryByPurpose categoryByPurpose,
                                                         List<Attachment> attachments,
                                                         List<AttachmentCategory> attachmentCategories) {

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
                .attachments(createAttachmentResponseDtos(property.getId(), attachments, attachmentCategories))
                .build();
    }

    private PropertyResponseDto buildFullPropertyResponseDto(Property property,
                                                             CategoryByPurpose categoryByPurpose,
                                                             List<Attachment> attachments,
                                                             List<AttachmentCategory> attachmentCategories) {

            return PropertyResponseDto.builder()
                    .id(property.getId())
                    .area(property.getArea())
                    .name(property.getName())
                    .address(property.getAddress())
                    .imageUrl(property.getImageUrl())
                    .propertyStatus(property.getPropertyStatus())
                    .propertyLocation(property.getPropertyLocation())
                    .owner(property.getOwner())
                    .amountOfRent(property.getAmountOfRent())
                    .balanceHolder(property.getBalanceHolder())
                    .areaTransferred(property.getAreaTransferred())
                    .leaseAgreementEndDate(property.getLeaseAgreementEndDate())
                    .categoryByPurposeName(categoryByPurpose.getName())
                    .attachments(createFullAttachmentResponseDtos(property.getId(), attachments, attachmentCategories))
                    .build();
    }

    private List<AttachmentResponseDto> createAttachmentResponseDtos(Long propertyId,
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

    private List<AttachmentResponseDto> createFullAttachmentResponseDtos(Long propertyId,
                                                                         List<Attachment> attachments,
                                                                         List<AttachmentCategory> attachmentCategories) {

        List<Attachment> filteredAttachments = attachments.stream()
                .filter(attachment -> attachment.getPropertyId().equals(propertyId))
                .collect(Collectors.toList());

        return attachmentCategories.stream()
                .map((category) -> createFullAttachmentResponseDto(filteredAttachments, category))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    private AttachmentResponseDto createAttachmentResponseDto(List<Attachment> attachments,
                                                              AttachmentCategory attachmentCategory) {

        Attachment selectedAttachment = attachments.stream()
                .filter(attachment -> attachment.getAttachmentCategoryId().equals(attachmentCategory.getId())
                        && attachment.isPubliclyViewable())
                .findFirst()
                .orElse(null);

        if (nonNull(selectedAttachment)) {
            return AttachmentResponseDto.builder()
                    .categoryName(attachmentCategory.getName())
                    .link(selectedAttachment.getLink())
                    .note(selectedAttachment.getNote())
                    .build();
        }

        return null;
    }

    private AttachmentResponseDto createFullAttachmentResponseDto(List<Attachment> attachments,
                                                                  AttachmentCategory attachmentCategory) {

        Attachment selectedAttachment = attachments.stream()
                .filter(attachment -> attachment.getAttachmentCategoryId().equals(attachmentCategory.getId()))
                .findFirst()
                .orElse(null);

        if (nonNull(selectedAttachment)) {
            return AttachmentResponseDto.builder()
                    .categoryName(attachmentCategory.getName())
                    .link(selectedAttachment.getLink())
                    .note(selectedAttachment.getNote())
                    .build();
        }

        return null;
    }

        private PropertyAdminDto mapToPropertyAdminDto(Property property, List<CategoryByPurpose> categoriesByPurpose) {

        return PropertyAdminDto.builder()
                .id(property.getId())
                .imageUrl(property.getImageUrl())
                .address(property.getAddress())
                .name(property.getName())
                .categoryByPurposeName(findById(categoriesByPurpose, property.getCategoryByPurposeId()).getName())
                .propertyStatus(property.getPropertyStatus())
                .area(property.getArea())
                .areaTransferred(property.getAreaTransferred())
                .balanceHolder(property.getBalanceHolder())
                .owner(property.getOwner())
                .leaseAgreementEndDate(property.getLeaseAgreementEndDate())
                .amountOfRent(property.getAmountOfRent())
                .isAreaTransferredPubliclyViewable(property.isAreaTransferredPubliclyViewable())
                .isBalanceHolderPubliclyViewable(property.isBalanceHolderPubliclyViewable())
                .isOwnerPubliclyViewable(property.isOwnerPubliclyViewable())
                .isLeaseAgreementEndDatePubliclyViewable(property.isLeaseAgreementEndDatePubliclyViewable())
                .isAmountOfRentPubliclyViewable(property.isAmountOfRentPubliclyViewable())
                .build();
    }

    private CategoryByPurpose findById(List<CategoryByPurpose> categoriesByPurposes, Long id) {

        Optional<CategoryByPurpose> categoryByPurpose = categoriesByPurposes.stream()
                .filter(category -> category.getId().equals(id)).findFirst();

        return categoryByPurpose.orElseThrow(
                () -> new PropertyAdminException("Категорію за призначенням не знайдено!"));
    }

    private <T> T validate(Boolean condition, T object) {
        return condition ? object : null;
    }

}
