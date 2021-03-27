package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.dto.AttachmentAdminDto;
import com.pnudev.communalpropertyregistry.exception.AttachmentAdminException;
import com.pnudev.communalpropertyregistry.repository.AttachmentCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AttachmentMapper {

    private final AttachmentCategoryRepository attachmentCategoryRepository;

    @Autowired
    public AttachmentMapper(AttachmentCategoryRepository attachmentCategoryRepository) {
        this.attachmentCategoryRepository = attachmentCategoryRepository;
    }

    public AttachmentAdminDto mapToAttachmentAdminDto(Attachment attachment) {

        AttachmentCategory attachmentCategory = attachmentCategoryRepository.findById(attachment.getAttachmentCategoryId())
                .orElseThrow(() -> new AttachmentAdminException("Категорії прикріплення не існує!", attachment.getPropertyId()));

        return AttachmentAdminDto.builder()
                .id(attachment.getId())
                .note(attachment.getNote())
                .link(attachment.getLink())
                .attachmentCategoryName(attachmentCategory.getName())
                .isAttachmentCategoryPubliclyViewable(attachmentCategory.isPubliclyViewable())
                .isPubliclyViewable(attachment.isPubliclyViewable())
                .build();
    }

    public List<AttachmentAdminDto> mapToAttachmentsAdminDto(List<Attachment> attachments) {
        List<AttachmentCategory> attachmentCategories = attachmentCategoryRepository.findAll();

        Map<Long, AttachmentCategory> attachmentCategoriesMap = attachmentCategories.stream()
                .collect(Collectors.toMap(AttachmentCategory::getId, attachmentCategory -> attachmentCategory));

        return attachments.stream().map(
                attachment -> mapToAttachmentAdminDto(attachment, attachmentCategoriesMap.get(attachment.getAttachmentCategoryId()))
        ).collect(Collectors.toList());
    }

    private AttachmentAdminDto mapToAttachmentAdminDto(Attachment attachment, AttachmentCategory attachmentCategory) {
        return AttachmentAdminDto.builder()
                .id(attachment.getId())
                .note(attachment.getNote())
                .link(attachment.getLink())
                .attachmentCategoryName(attachmentCategory.getName())
                .isAttachmentCategoryPubliclyViewable(attachmentCategory.isPubliclyViewable())
                .isPubliclyViewable(attachment.isPubliclyViewable())
                .build();
    }

}
