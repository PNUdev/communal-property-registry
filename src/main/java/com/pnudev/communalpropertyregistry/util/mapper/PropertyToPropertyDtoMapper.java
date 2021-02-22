package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyDto;
import com.pnudev.communalpropertyregistry.repository.AttachmentCategoryRepository;
import com.pnudev.communalpropertyregistry.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PropertyToPropertyDtoMapper {

    // TODO: 22.02.21 Replace with services
    private final AttachmentRepository attachmentRepository;
    private final AttachmentCategoryRepository attachmentCategoryRepository;

    @Autowired
    public PropertyToPropertyDtoMapper(AttachmentRepository attachmentRepository,
                                       AttachmentCategoryRepository attachmentCategoryRepository) {

        this.attachmentRepository = attachmentRepository;
        this.attachmentCategoryRepository = attachmentCategoryRepository;
    }

    public PropertyDto map(Property property) {

        List<AttachmentCategory> categories = (List<AttachmentCategory>)
                attachmentCategoryRepository.findAll();

        List<Attachment> attachments = attachmentRepository
                .findAttachmentsByPropertyIdEquals(property.getId());

        return PropertyDto.builder()
                .categorizedAttachments(
                        categories.stream()
                                .filter(AttachmentCategory::isPubliclyViewable)
                                .map((c) ->
                                        new PropertyDto.CategorizedAttachment(
                                                c, getAttachmentWithCategoryId(attachments, c.getId())
                                        )
                                )
                                .collect(Collectors.toList()))
                .property(property)
                .build();
    }

    public List<PropertyDto> map(List<Property> properties) {

        return properties.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private Attachment getAttachmentWithCategoryId(List<Attachment> attachments, Long id) {

        return attachments.stream()
                .filter((a) -> a.getAttachmentCategoryId().equals(id) && a.isPubliclyViewable())
                .findFirst()
                .orElse(null);
    }
}
