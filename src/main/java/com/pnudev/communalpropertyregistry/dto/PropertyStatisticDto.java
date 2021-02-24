package com.pnudev.communalpropertyregistry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyStatisticDto {

    private String category;

    private Long totalNumber;

    private Long numberOfNonRented;

    private Long numberOfRented;

    private Long numberOfPrivatized;

    private Long numberOfListed;

    private Long numberOfUsedByCityCouncil;

}
