package com.pnudev.communalpropertyregistry.util;

import com.pnudev.communalpropertyregistry.dto.AddressDto;

import java.util.List;

public interface AddressResolverClient {

    List<AddressDto> getAddressesDto(String address);

}
