package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeDto;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposePaginationDto;
import com.pnudev.communalpropertyregistry.exception.ServiceException;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryByPurposeServiceImpl implements CategoryByPurposeService {

    private final CategoryByPurposeRepository categoryByPurposeRepository;
    private final PropertyRepository propertyRepository;

    @Autowired
    public CategoryByPurposeServiceImpl(CategoryByPurposeRepository categoryByPurposeRepository,
                                        PropertyRepository propertyRepository) {

        this.categoryByPurposeRepository = categoryByPurposeRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public CategoryByPurposePaginationDto findAll(Pageable pageable) {

        Page<CategoryByPurpose> pagination = categoryByPurposeRepository
                .findAll(pageable);

        boolean isFirst = false;
        boolean isLast = false;

        int firstPage = pagination.getNumber() - 5;
        int lastPage = pagination.getNumber() + 5;

        if (firstPage - 1 < 0) {
            firstPage = 0;
            isFirst = true;
        }

        if (lastPage > pagination.getTotalPages() - 2) {
            lastPage = pagination.getTotalPages() - 1;
            isLast = true;
        }

        return CategoryByPurposePaginationDto.builder()
                .content(pagination.getContent())
                .page(pagination.getNumber())
                .totalPages(pagination.getTotalPages())
                .firstVisiblePage(firstPage)
                .lastVisiblePage(lastPage)
                .isFirstPage(isFirst)
                .isLastPage(isLast)
                .build();
    }

    @Override
    public CategoryByPurpose findById(Long id) {
        return categoryByPurposeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Category by id not found"));
    }

    @Override
    public void create(CategoryByPurposeDto categoryCreateDto) {

        if (categoryByPurposeRepository.existsByName(categoryCreateDto.getName())) {
            throw new ServiceException("Category with such name already exists");
        }

        CategoryByPurpose categoryByPurpose = CategoryByPurpose.builder()
                .name(categoryCreateDto.getName())
                .build();

        categoryByPurposeRepository.save(categoryByPurpose);
    }

    @Override
    public void update(CategoryByPurposeDto categoryByPurposeDto, Long categoryId) {

        CategoryByPurpose category = categoryByPurposeRepository
                .findById(categoryId)
                .orElseThrow(() -> new ServiceException("Category by id not found"));

        if (categoryByPurposeRepository.existsByName(categoryByPurposeDto.getName())) {
            throw new ServiceException("Category with such name already exists");
        }

        CategoryByPurpose updatedCategory = category.toBuilder()
                .name(categoryByPurposeDto.getName())
                .build();

        categoryByPurposeRepository.save(updatedCategory);
    }

    @Override
    public void delete(Long id) {

        if (propertyRepository.existsByCategoryByPurposeId(id)) {
            throw new ServiceException("Неможливо видалити категорію допоки є маєтки позначені нею.");
        }

        categoryByPurposeRepository.deleteById(id);
        log.info("Category with id {} was deleted", id);
    }

}
