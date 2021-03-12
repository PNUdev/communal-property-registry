package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AttachmentCategoryRepository extends PagingAndSortingRepository<AttachmentCategory, Long> {

    List<AttachmentCategory> findAll();

    Boolean existsByName(String name);

}
