package com.pnudev.communalpropertyregistry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyStatisticsDto {

    private String category;

    private Long totalNumber;

    private Long numberOfNonRented;

    private Long numberOfRented;

    private Long numberOfPrivatized;

    private Long numberOfFirstOrSecondType;

    private Long numberOfUsedByCityCouncil;

}
