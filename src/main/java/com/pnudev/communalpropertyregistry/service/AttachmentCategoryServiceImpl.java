package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.dto.AttachmentCategoryDto;
import com.pnudev.communalpropertyregistry.exception.AttachmentCategoryException;
import com.pnudev.communalpropertyregistry.repository.AttachmentCategoryRepository;
import com.pnudev.communalpropertyregistry.repository.AttachmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AttachmentCategoryServiceImpl implements AttachmentCategoryService {

    private final AttachmentCategoryRepository attachmentCategoryRepository;

    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentCategoryServiceImpl(AttachmentCategoryRepository attachmentCategoryRepository,
                                         AttachmentRepository attachmentRepository) {

        this.attachmentCategoryRepository = attachmentCategoryRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public List<AttachmentCategory> findAll() {
        return attachmentCategoryRepository.findAll();
    }


    @Override
    public Page<AttachmentCategory> findAll(Pageable pageable) {
        return attachmentCategoryRepository.findAll(pageable);
    }

    @Override
    public AttachmentCategory findById(Long id) {
        return attachmentCategoryRepository.findById(id).orElseThrow(
                ()-> new AttachmentCategoryException("Категорію не знайдено"));
    }

    @Override
    public void updateById(Long id, AttachmentCategoryDto attachmentCategoryDto) {

        AttachmentCategory attachmentCategory = findById(id);

        if(attachmentCategoryRepository.existsByName(attachmentCategoryDto.getName()) &&
                !attachmentCategoryDto.getName().equals(attachmentCategory.getName())){

            throw new AttachmentCategoryException("Категорія з таким іменем уже існує");
        }

        attachmentCategory.setName(attachmentCategoryDto.getName());
        attachmentCategory.setPubliclyViewable(attachmentCategoryDto.isPubliclyViewable());
        attachmentCategoryRepository.save(attachmentCategory);

        log.info("Attachment category {} was updated", attachmentCategoryDto.getName());
    }

    @Override
    public void create(AttachmentCategoryDto attachmentCategoryDto) {

        if(attachmentCategoryRepository.existsByName(attachmentCategoryDto.getName())){
            throw new AttachmentCategoryException("Категорія з таким іменем уже існує");
        }

        AttachmentCategory attachmentCategory = AttachmentCategory.builder()
                .name(attachmentCategoryDto.getName())
                .isPubliclyViewable(attachmentCategoryDto.isPubliclyViewable())
                .build();
        attachmentCategoryRepository.save(attachmentCategory);

        log.info("Attachment category {} was created", attachmentCategoryDto.getName());
    }

    @Override
    public void delete(Long id) {

        if(attachmentRepository.existsByAttachmentCategoryId(id)){
            throw new AttachmentCategoryException("Дана категорія використовується, її не можливо видалити");
        }

        attachmentCategoryRepository.deleteById(id);

    }

}
