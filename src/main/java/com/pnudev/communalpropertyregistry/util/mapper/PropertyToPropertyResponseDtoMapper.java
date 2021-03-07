package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.response.AttachmentResponseDto;
import com.pnudev.communalpropertyregistry.dto.response.PropertyResponseDto;
import com.pnudev.communalpropertyregistry.exception.ServiceException;
import com.pnudev.communalpropertyregistry.repository.AttachmentCategoryRepository;
import com.pnudev.communalpropertyregistry.repository.AttachmentRepository;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public class PropertyToPropertyResponseDtoMapper {

    // TODO: 22.02.21 Replace with services
    private final AttachmentRepository attachmentRepository;
    private final AttachmentCategoryRepository attachmentCategoryRepository;
    private final CategoryByPurposeRepository categoryByPurposeRepository;

    @Autowired
    public PropertyToPropertyResponseDtoMapper(AttachmentRepository attachmentRepository,
                                               AttachmentCategoryRepository attachmentCategoryRepository,
                                               CategoryByPurposeRepository categoryByPurposeRepository) {

        this.attachmentRepository = attachmentRepository;
        this.attachmentCategoryRepository = attachmentCategoryRepository;
        this.categoryByPurposeRepository = categoryByPurposeRepository;
    }

    public PropertyResponseDto map(Property property) {

        List<AttachmentCategory> categories = (List<AttachmentCategory>)
                attachmentCategoryRepository.findAll();

        List<Attachment> attachments = attachmentRepository
                .findAttachmentsByPropertyIdEquals(property.getId());

        CategoryByPurpose categoryByPurpose = categoryByPurposeRepository
                .findById(property.getCategoryByPurposeId())
                .orElseThrow(() -> new ServiceException("Category with such id doesn't exist"));

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

    public List<PropertyResponseDto> map(List<Property> properties) {

        return properties.stream()
                    .map(this::map)
                    .collect(Collectors.toList());
    }

    private AttachmentResponseDto createAttachmentResponseWithCategory(List<Attachment> attachments,
                                                                       AttachmentCategory attachmentCategory) {

        AttachmentResponseDto attachmentResponseDto = null;

        Attachment attachment = attachments.stream()
                .filter((a) -> a.getAttachmentCategoryId().equals(attachmentCategory.getId())
                        && a.isPubliclyViewable())
                .findFirst()
                .orElse(null);

        if (nonNull(attachment)) {
            attachmentResponseDto = AttachmentResponseDto.builder()
                    .categoryName(attachmentCategory.getName())
                    .link(attachment.getLink())
                    .note(attachment.getNote())
                    .build();
        }

        return attachmentResponseDto;
    }
}
