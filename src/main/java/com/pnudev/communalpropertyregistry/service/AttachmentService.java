package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Attachment;

import java.util.Collection;
import java.util.List;

public interface AttachmentService {

    List<Attachment> findByPropertyId(Long id);

    List<Attachment> findByPropertyIdIn(List<Long> propertyIds);

}
