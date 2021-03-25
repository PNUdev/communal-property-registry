package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttachmentCategoryRepository extends CrudRepository<AttachmentCategory, Long> {

    Page<AttachmentCategory> findAll(Pageable pageable);

    Boolean existsByNameAndIdNot(String name, Long id);

    Boolean existsByName(String name);

    List<AttachmentCategory> findAll();

}
