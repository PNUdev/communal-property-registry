package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.dto.AttachmentAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.AttachmentAdminFormDto;

import java.util.List;

public interface AttachmentAdminService {

    void create(Long propertyId, AttachmentAdminFormDto attachmentAdminFormDto);

    AttachmentAdminDto findById(Long attachmentId, Long propertyId);

    List<AttachmentAdminDto> findAllByPropertyId(Long id);

    void updateById(Long attachmentId, Long propertyId, AttachmentAdminFormDto attachmentAdminFormDto);

    void deleteById(Long propertyId, Long attachmentId);

}
