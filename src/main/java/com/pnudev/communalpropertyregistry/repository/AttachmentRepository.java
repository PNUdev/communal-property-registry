package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttachmentRepository extends CrudRepository<Attachment, Long> {

    List<Attachment> findAttachmentsByPropertyId(Long propertyId);

    List<Attachment> findAttachmentsByPropertyIdIn(List<Long> propertyIds);

    List<Attachment> findAllByPropertyId(Long id);

    void deleteAllByPropertyId(Long propertyId);

    boolean existsByAttachmentCategoryIdAndPropertyIdAndIdNot(Long attachmentCategoryId, Long propertyId, Long AttachmentId);

    boolean existsByAttachmentCategoryIdAndPropertyId(Long attachmentCategoryId, Long propertyId);

    boolean existsByAttachmentCategoryId(Long attachmentCategoryId);

}
