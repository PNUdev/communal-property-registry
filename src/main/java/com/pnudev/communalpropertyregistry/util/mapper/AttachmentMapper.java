package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.dto.AttachmentAdminDto;
import com.pnudev.communalpropertyregistry.exception.PropertyAdminException;
import com.pnudev.communalpropertyregistry.repository.AttachmentCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
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
                .orElseThrow(() -> new PropertyAdminException("Категорії не існує!"));

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

        return attachments.stream().map(
                attachment -> mapToAttachmentAdminDto(attachment, attachmentCategories)
        ).collect(Collectors.toList());
    }

    private AttachmentAdminDto mapToAttachmentAdminDto(Attachment attachment,
                                                       List<AttachmentCategory> attachmentCategories) {

        AttachmentCategory attachmentCategory = findAttachmentCategoryById(attachment.getAttachmentCategoryId(), attachmentCategories);

        return AttachmentAdminDto.builder()
                .id(attachment.getId())
                .note(attachment.getNote())
                .link(attachment.getLink())
                .attachmentCategoryName(attachmentCategory.getName())
                .isAttachmentCategoryPubliclyViewable(attachmentCategory.isPubliclyViewable())
                .isPubliclyViewable(attachment.isPubliclyViewable())
                .build();
    }

    private AttachmentCategory findAttachmentCategoryById(Long id, List<AttachmentCategory> attachmentCategories) {
        return attachmentCategories.stream()
                .filter(attachmentCategory -> Objects.equals(attachmentCategory.getId(), id))
                .findFirst()
        .orElseThrow(() -> new PropertyAdminException("Категорія прикріплення не знайдена!"));
    }

}
