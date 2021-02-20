package com.pnudev.communalpropertyregistry.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    private Long id;

    private String note;

    private String link;

    private Long attachmentCategoryId;

    private Long propertyId;

    private boolean isPubliclyViewable;

}
