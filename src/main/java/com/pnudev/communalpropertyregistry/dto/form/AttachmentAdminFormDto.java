package com.pnudev.communalpropertyregistry.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentAdminFormDto {

    private String note;

    private String link;

    private Long attachmentCategoryId;

    private boolean isPubliclyViewable;

}
