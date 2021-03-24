package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public List<Attachment> findByPropertyId(Long id) {
        return attachmentRepository.findAttachmentsByPropertyId(id);
    }

    @Override
    public List<Attachment> findByPropertyIdIn(List<Long> propertyIds) {
        return attachmentRepository.findAttachmentsByPropertyIdIn(propertyIds);
    }

}
