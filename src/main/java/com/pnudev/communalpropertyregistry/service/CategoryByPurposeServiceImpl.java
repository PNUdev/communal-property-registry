package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeCreateDto;
import com.pnudev.communalpropertyregistry.dto.PagingCategoryByPurposeDto;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeUpdateDto;
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
    public PagingCategoryByPurposeDto findAll(Pageable pageable) {

        Page<CategoryByPurpose> pagination = categoryByPurposeRepository
                .findAll(pageable);

        int firstPage = (pagination.getNumber() < 5) ? 0 : pagination.getNumber() - 4;

        int lastPage = (pagination.getTotalPages() - pagination.getNumber() < 5)
                ? pagination.getTotalPages() - 1
                : pagination.getNumber() + 4;

        return PagingCategoryByPurposeDto.builder()
                .content(pagination.getContent())
                .page(pagination.getNumber())
                .totalPages(pagination.getTotalPages())
                .firstVisiblePage(firstPage)
                .lastVisiblePage(lastPage)
                .isFirstPage(pagination.getNumber() == 0)
                .isLastPage(pagination.getNumber() == pagination.getTotalPages() - 1)
                .build();
    }

    @Override
    public CategoryByPurpose findById(Long id) {
        return categoryByPurposeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Category by id not found"));
    }

    @Override
    public void create(CategoryByPurposeCreateDto categoryCreateDto) {

        if (categoryByPurposeRepository.existsByName(categoryCreateDto.getName())) {
            throw new ServiceException("Category with such name already exists");
        }

        CategoryByPurpose categoryByPurpose = CategoryByPurpose.builder()
                .name(categoryCreateDto.getName())
                .build();

        categoryByPurposeRepository.save(categoryByPurpose);
    }

    @Override
    public Long update(CategoryByPurposeUpdateDto categoryByPurposeUpdateDto) {

        CategoryByPurpose category = categoryByPurposeRepository
                .findById(categoryByPurposeUpdateDto.getId())
                .orElseThrow(() -> new ServiceException("Category by id not found"));

        if (categoryByPurposeRepository.existsByName(categoryByPurposeUpdateDto.getName())) {
            throw new ServiceException("Category with such name already exists");
        }

        CategoryByPurpose updatedCategory = category.toBuilder()
                .name(categoryByPurposeUpdateDto.getName())
                .build();

        categoryByPurposeRepository.save(updatedCategory);

        return updatedCategory.getId();
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
