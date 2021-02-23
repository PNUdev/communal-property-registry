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

    private int totalNumber;

    private int numberOfNonRented;

    private int numberOfRented;

    private int numberOfPrivatized;

    private int numberOfListed;

    private int numberOfUsedByCityCouncil;

}
