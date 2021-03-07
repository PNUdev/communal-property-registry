package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryByPurposeRepository extends PagingAndSortingRepository<CategoryByPurpose, Long> {

    Boolean existsByName(String name);

}
