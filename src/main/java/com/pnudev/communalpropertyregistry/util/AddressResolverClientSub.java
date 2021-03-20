package com.pnudev.communalpropertyregistry.util;

import com.pnudev.communalpropertyregistry.dto.AddressDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Profile("local")
@Component
public class AddressResolverClientSub implements AddressResolverClient {

    @Override
    public List<AddressDto> getAddressesDto(String address) {
        return Collections.singletonList(
                AddressDto.builder()
                        .address("default")
                        .lat(6.)
                        .lon(.9)
                        .build()
        );
    }

}
