package com.pnudev.communalpropertyregistry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentCategoryDto {

    private String name;

    private boolean isPubliclyViewable;

}
