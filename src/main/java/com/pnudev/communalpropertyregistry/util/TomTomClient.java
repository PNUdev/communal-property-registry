package com.pnudev.communalpropertyregistry.util;

import com.pnudev.communalpropertyregistry.exception.ServiceAdminException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class TomTomClient {

    private static final String TOM_TOM_URL = "https://api.tomtom.com/search/2/geocode/%s.json?storeResult=false&limit=20&lat=48.610742062164974f&lon=24.975710390429917&radius=30000&language=uk-UA&key=%s";

    @Value("${application.tomtom.api.key}")
    private String tomtomApiKey;

    public String getResponse(String address) {
        final String URL = String.format(TOM_TOM_URL, address, tomtomApiKey);

        try {
            return new RestTemplate().getForObject(URL, String.class);

        } catch (RestClientException e) {
            log.error("RestClientException was caught, failed to get response in method 'getAddresses'!");
            throw new ServiceAdminException("Таку адресу неможливо обробити, спробуйте ввести іншу!");
        }
    }

}
