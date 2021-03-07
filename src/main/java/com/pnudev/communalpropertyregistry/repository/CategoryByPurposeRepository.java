package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryByPurposeRepository extends CrudRepository<CategoryByPurpose, Long>,
                                                     PagingAndSortingRepository<CategoryByPurpose, Long> {

    Boolean existsByName(String name);

}
