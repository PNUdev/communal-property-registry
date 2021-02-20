package com.pnudev.communalpropertyregistry.dto;

import com.pnudev.communalpropertyregistry.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyLocationDto {

    private Long propertyId;

    private Property.PropertyStatus propertyStatus;

    private Double lat;

    private Double lon;

}
