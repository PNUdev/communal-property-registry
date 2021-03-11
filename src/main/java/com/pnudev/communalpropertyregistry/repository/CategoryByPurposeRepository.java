package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryByPurposeRepository extends CrudRepository<CategoryByPurpose, Long> {

    List<CategoryByPurpose> findAll();

    CategoryByPurpose findByName(String name);

}
