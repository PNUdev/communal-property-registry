package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface AttachmentRepository extends CrudRepository<Attachment, Long> {

    List<Attachment> findAttachmentsByPropertyId(Long propertyId);

    List<Attachment> findAttachmentsByPropertyIdIn(Collection<Long> propertyId);
}
