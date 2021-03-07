package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationDto;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Component;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;
import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.response.AttachmentResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.service.AttachmentCategoryService;
import com.pnudev.communalpropertyregistry.service.AttachmentService;
import com.pnudev.communalpropertyregistry.service.CategoryByPurposeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        List<AttachmentCategory> categories = attachmentCategoryService.findAll();

        List<Attachment> attachments = attachmentService
                .findByPropertyId(property.getId());

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
                .owner(property.isOwnerPubliclyViewable() ? property.getOwner() : null)
                .amountOfRent(property.isAmountOfRentPubliclyViewable() ?
                        property.getAmountOfRent() : null)
                .balanceHolder(property.isBalanceHolderPubliclyViewable() ?
                        property.getBalanceHolder() : null)
                .areaTransferred(property.isAreaTransferredPubliclyViewable() ?
                        property.getAreaTransferred() : null)
                .leaseAgreementEndDate(property.isLeaseAgreementEndDatePubliclyViewable()
                        ? property.getLeaseAgreementEndDate() : null)
                .categoryByPurposeName(categoryByPurpose.getName())
                .attachments(categories.stream()
                        .filter(AttachmentCategory::isPubliclyViewable)
                        .map((c) -> createAttachmentResponseWithCategory(attachments, c))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .build();
    }

    public List<PropertyResponseDto> mapToPropertyResponseDto(List<Property> properties) {

        return properties.stream()
                .map(this::mapToPropertyResponseDto)
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

    private AttachmentResponseDto createAttachmentResponseWithCategory(List<Attachment> attachments,
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

}
