package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryByPurposeRepository extends CrudRepository<CategoryByPurpose, Long> {

    @Override
    List<CategoryByPurpose> findAll();

}
