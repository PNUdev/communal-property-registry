package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends CrudRepository<Attachment, Long> {

    List<Attachment> findAttachmentsByPropertyId(Long propertyId);

    List<Attachment> findAttachmentsByPropertyIdIn(List<Long> propertyIds);

    List<Attachment> findAllByPropertyId(Long id);

    Optional<Attachment> findByIdAndPropertyId(Long attachmentId, Long propertyId);

    @Modifying
    @Query("DELETE FROM attachment WHERE attachment.property_id = :propertyId")
    void deleteAllByPropertyId(@Param("propertyId") Long propertyId);

    boolean existsByAttachmentCategoryIdAndPropertyIdAndIdNot(Long attachmentCategoryId, Long propertyId, Long AttachmentId);

    boolean existsByAttachmentCategoryIdAndPropertyId(Long attachmentCategoryId, Long propertyId);

    boolean existsByAttachmentCategoryId(Long attachmentCategoryId);

}
