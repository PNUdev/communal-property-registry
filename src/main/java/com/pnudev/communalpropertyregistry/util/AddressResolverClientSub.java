package com.pnudev.communalpropertyregistry.util;

import com.pnudev.communalpropertyregistry.dto.AddressDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile({"default", "test"})
@Component
public class AddressResolverClientSub implements AddressResolverClient {

    @Override
    public List<AddressDto> getAddressesDto(String address) {
        return null;
    }

}
