package com.pnudev.communalpropertyregistry.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponseDto {

    private String categoryName;

    private String note;

    private String link;

}
