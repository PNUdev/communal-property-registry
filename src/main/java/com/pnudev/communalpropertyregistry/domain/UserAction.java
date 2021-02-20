package com.pnudev.communalpropertyregistry.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAction {

    @Id
    private Long id;

    private String ipAddress;

    private String httpMethod;

    private String url;

    private String referrerUrl;

    private LocalDateTime time;

}
