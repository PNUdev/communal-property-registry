package com.pnudev.communalpropertyregistry.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pnudev.communalpropertyregistry.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PropertyResponseDto {

    private Long id;

    private String imageUrl;

    private String address;

    private Property.PropertyLocation propertyLocation;

    private String name;

    private String categoryByPurposeName;

    private Property.PropertyStatus propertyStatus;

    private Double area;

    private Double areaTransferred;

    private String balanceHolder;

    private String owner;

    private LocalDate leaseAgreementEndDate;

    private Double amountOfRent;

    private List<AttachmentResponseDto> attachments;

}
