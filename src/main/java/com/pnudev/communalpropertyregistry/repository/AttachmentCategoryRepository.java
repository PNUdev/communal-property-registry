package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

public interface AttachmentCategoryRepository extends CrudRepository<AttachmentCategory, Long> {

    Page<AttachmentCategory> findAll(Pageable pageable);

    Boolean existsByName(String name);

}
