package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeDto;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposePageDto;
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
    public CategoryByPurposePageDto findAll(Pageable pageable) {

        Page<CategoryByPurpose> pagination = categoryByPurposeRepository
                .findAll(pageable);

        return CategoryByPurposePageDto.builder()
                .content(pagination.getContent())
                .page(pagination.getNumber())
                .totalPages(pagination.getTotalPages())
                .isFirstPage(pagination.isFirst())
                .isLastPage(pagination.isLast())
                .build();
    }

    @Override
    public CategoryByPurpose findById(Long id) {
        return categoryByPurposeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Категорію не знайдено"));
    }

    @Override
    public void create(CategoryByPurposeDto categoryByPurposeDto) {

        validateCategoryName(categoryByPurposeDto.getName());

        CategoryByPurpose categoryByPurpose = CategoryByPurpose.builder()
                .name(categoryByPurposeDto.getName())
                .build();

        categoryByPurposeRepository.save(categoryByPurpose);
    }

    @Override
    public void update(CategoryByPurposeDto categoryByPurposeDto, Long categoryId) {

        CategoryByPurpose category = findById(categoryId);

        validateCategoryName(categoryByPurposeDto.getName());

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

    private void validateCategoryName(String name) {

        if (categoryByPurposeRepository.existsByName(name)) {
            throw new ServiceException("Категорія з даною назвою уже існує");
        }
    }

}
