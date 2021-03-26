package com.pnudev.communalpropertyregistry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentAdminDto {

    private Long id;

    private String note;

    private String link;

    private String attachmentCategoryName;

    private boolean isAttachmentCategoryPubliclyViewable;

    private boolean isPubliclyViewable;

}
