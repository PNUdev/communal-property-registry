package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.dto.CategoryByPurposeResponseDto;
import com.pnudev.communalpropertyregistry.dto.form.CategoryByPurposeFormDto;
import com.pnudev.communalpropertyregistry.exception.CategoryDuplicationException;
import com.pnudev.communalpropertyregistry.exception.CategoryExistsException;
import com.pnudev.communalpropertyregistry.exception.ServiceAdminException;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
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
    public List<CategoryByPurpose> findAll() {
        return categoryByPurposeRepository.findAll();
    }

    @Override
    public CategoryByPurpose findById(Long id) {
        return categoryByPurposeRepository.findById(id)
                .orElseThrow(() -> new ServiceAdminException("Категорію не знайдено!"));
    }

    @Override
    public void create(CategoryByPurposeFormDto categoryByPurposeDto) {

        validateCategoryName(categoryByPurposeDto.getName());

        CategoryByPurpose categoryByPurpose = CategoryByPurpose.builder()
                .name(categoryByPurposeDto.getName())
                .build();

        categoryByPurposeRepository.save(categoryByPurpose);

        log.info("Category {}({}) was created", categoryByPurpose.getName(), categoryByPurpose.getId());
    }

    @Override
    public void updateById(CategoryByPurposeFormDto categoryByPurposeDto, Long categoryId) {

        validateCategoryName(categoryByPurposeDto.getName());

        CategoryByPurpose category = findById(categoryId);

        CategoryByPurpose updatedCategory = category.toBuilder()
                .name(categoryByPurposeDto.getName())
                .build();

        categoryByPurposeRepository.save(updatedCategory);

        log.info("Category {}({}) was updated", category.getName(), category.getId());
    }

    @Override
    public void deleteById(Long id) {

        if (propertyRepository.existsByCategoryByPurposeId(id)) {
            log.error("Category is used by property. Can't delete category");
            throw new CategoryExistsException("Дію неможливо виконати, оскільки дана категорія активно використовується!");
        }

        categoryByPurposeRepository.deleteById(id);
        log.info("Category with id {} was deleted", id);
    }

    private void validateCategoryName(String name) {

        if (categoryByPurposeRepository.existsByName(name)) {
            log.error("Category with name {} already exists", name);
            throw new CategoryDuplicationException("Категорія з даною назвою уже існує!");
        }

    }

}
