package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import org.springframework.data.repository.CrudRepository;

public interface AttachmentRepository extends CrudRepository<Attachment, Long> {
}
