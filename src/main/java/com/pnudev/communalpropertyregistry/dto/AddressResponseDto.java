package com.pnudev.communalpropertyregistry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {

    private List<AddressDto> addresses;

}
