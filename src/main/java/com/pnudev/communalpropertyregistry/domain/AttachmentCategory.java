package com.pnudev.communalpropertyregistry.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentCategory {

    @Id
    private Long id;

    private String name;

    private boolean isPubliclyViewable;

}
