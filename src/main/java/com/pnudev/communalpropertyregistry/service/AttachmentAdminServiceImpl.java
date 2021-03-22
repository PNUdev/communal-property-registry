package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.dto.AttachmentAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.AttachmentAdminFormDto;
import com.pnudev.communalpropertyregistry.exception.AttachmentAdminException;
import com.pnudev.communalpropertyregistry.repository.AttachmentRepository;
import com.pnudev.communalpropertyregistry.util.mapper.AttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentAdminServiceImpl implements AttachmentAdminService {

    private final AttachmentRepository attachmentRepository;

    private final AttachmentMapper attachmentMapper;

    @Autowired
    public AttachmentAdminServiceImpl(AttachmentRepository attachmentRepository,
                                      AttachmentMapper attachmentMapper) {

        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
    }

    @Override
    public void create(Long propertyId, AttachmentAdminFormDto attachmentAdminFormDto) {

        if (attachmentRepository.existsByAttachmentCategoryIdAndPropertyId(
                attachmentAdminFormDto.getAttachmentCategoryId(), propertyId)) {
            throw new AttachmentAdminException("Майно має прикріплення даної категорії!", propertyId);
        }

        Attachment attachment = Attachment.builder()
                .note(attachmentAdminFormDto.getNote())
                .link(attachmentAdminFormDto.getLink())
                .propertyId(propertyId)
                .isPubliclyViewable(attachmentAdminFormDto.isPubliclyViewable())
                .attachmentCategoryId(attachmentAdminFormDto.getAttachmentCategoryId())
                .build();

        attachmentRepository.save(attachment);
    }

    @Override
    public AttachmentAdminDto findById(Long attachmentId, Long propertyId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new AttachmentAdminException("Прикріплення не існує!", propertyId));

        return attachmentMapper.mapToAttachmentAdminDto(attachment);
    }

    @Override
    public List<AttachmentAdminDto> findAllByPropertyId(Long id) {
        return attachmentMapper.mapToAttachmentsAdminDto(
                attachmentRepository.findAllByPropertyId(id)
        );
    }

    @Override
    public void updateById(Long attachmentId, Long propertyId, AttachmentAdminFormDto attachmentAdminFormDto) {

        if (attachmentRepository.existsByAttachmentCategoryIdAndPropertyIdAndIdNot(
                attachmentAdminFormDto.getAttachmentCategoryId(), propertyId, attachmentId)) {
            throw new AttachmentAdminException("Майно має прикріплення даної категорії!", propertyId);
        }

        Attachment attachmentFromDb = attachmentRepository.findByIdAndPropertyId(attachmentId, propertyId)
                .orElseThrow(() -> new AttachmentAdminException("Прикріплення не існує!", propertyId));

        Attachment attachment = attachmentFromDb.toBuilder()
                .note(attachmentAdminFormDto.getNote())
                .link(attachmentAdminFormDto.getLink())
                .attachmentCategoryId(attachmentAdminFormDto.getAttachmentCategoryId())
                .isPubliclyViewable(attachmentAdminFormDto.isPubliclyViewable())
                .build();

        attachmentRepository.save(attachment);
    }

    @Override
    public void deleteById(Long id) {
        attachmentRepository.deleteById(id);
    }

}
