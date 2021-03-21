package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttachmentCategoryRepository extends CrudRepository<AttachmentCategory, Long> {

    List<AttachmentCategory> findAll();

}
