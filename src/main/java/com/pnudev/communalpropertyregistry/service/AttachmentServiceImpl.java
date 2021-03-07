package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.exception.ServiceException;
import com.pnudev.communalpropertyregistry.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public List<Attachment> findByPropertyId(Long id) {
        return Optional.ofNullable(
                attachmentRepository.findAttachmentsByPropertyIdEquals(id))
                .orElseThrow(() -> new ServiceException("Attachment with such id doesn't exist"));
    }

}
