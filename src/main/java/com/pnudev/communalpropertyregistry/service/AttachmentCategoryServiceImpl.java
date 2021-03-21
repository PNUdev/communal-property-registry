package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.repository.AttachmentCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentCategoryServiceImpl implements AttachmentCategoryService {

    private final AttachmentCategoryRepository attachmentCategoryRepository;

    @Autowired
    public AttachmentCategoryServiceImpl(AttachmentCategoryRepository attachmentCategoryRepository) {
        this.attachmentCategoryRepository = attachmentCategoryRepository;
    }

    @Override
    public List<AttachmentCategory> findAll() {
        return attachmentCategoryRepository.findAll();
    }

}
