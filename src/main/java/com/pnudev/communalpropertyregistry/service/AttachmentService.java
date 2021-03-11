package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Attachment;

import java.util.List;

public interface AttachmentService {

    List<Attachment> findByPropertyId(Long id);

}
