package com.pnudev.communalpropertyregistry.util;

import com.pnudev.communalpropertyregistry.dto.AddressDto;
import com.pnudev.communalpropertyregistry.exception.PropertyAdminException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Profile("default")
@Slf4j
@Component
public class TomTomAddressResolverClient implements AddressResolverClient {

    private static final Pattern RESPONSE_PARSE_PATTERN = Pattern.compile("freeformAddress\":\"(?<freeformAddress>[^\"]+).*?position\":\\{\"lat\":(?<lat>[\\d.]+),\"lon\":(?<lon>[\\d.]+)");

    private static final String TOM_TOM_URL = "https://api.tomtom.com/search/2/geocode/%s.json?storeResult=false&limit=20&lat=48.610742062164974f&lon=24.975710390429917&radius=30000&language=uk-UA&key=%s";

    @Value("${application.tomtom.api.key}")
    private String tomtomApiKey;

    @Override
    public List<AddressDto> getAddressesDto(String address) {
        final String URL = String.format(TOM_TOM_URL, address, tomtomApiKey);

        try {
            String responseFromTomTom = new RestTemplate().getForObject(URL, String.class);

            log.info("The address : [{}] was successfully found!", address);

            Matcher matcher = RESPONSE_PARSE_PATTERN.matcher(responseFromTomTom);

            List<AddressDto> addresses = new ArrayList<>();

            while (matcher.find()) {
                addresses.add(AddressDto.builder()
                        .address(matcher.group("freeformAddress"))
                        .lat(Double.parseDouble(matcher.group("lat")))
                        .lon(Double.parseDouble(matcher.group("lon")))
                        .build()
                );
            }

            if (addresses.isEmpty()) {
                throw new PropertyAdminException("Вказанa адресa є невірною!");
            }

            log.info("Found [{}] addresses by input data: [{}]!", addresses.size(), address);

            return addresses;

        } catch (RestClientException e) {
            log.error("RestClientException was caught, failed to get response in method 'getAddresses'!");
            throw new PropertyAdminException("Таку адресу неможливо обробити, спробуйте ввести іншу!");
        }
    }

}
