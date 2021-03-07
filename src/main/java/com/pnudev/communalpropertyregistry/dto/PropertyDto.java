package com.pnudev.communalpropertyregistry.dto;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.domain.Property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDto {

    private Property property;

    private List<CategorizedAttachment> categorizedAttachments;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategorizedAttachment {

        private AttachmentCategory attachmentCategory;

        private Attachment attachment;
    }
}
