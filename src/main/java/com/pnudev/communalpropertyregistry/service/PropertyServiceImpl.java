package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.exception.ServiceException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final Environment environment;

    public PropertyServiceImpl(Environment environment) {
        this.environment = environment;
    }

    private Property.PropertyLocation convertAddressToPropertyLocation(String address) {

        final String url = String.format("https://api.tomtom.com/search/2/geocode/%s.json?" +
                        "storeResult=false&limit=1&lat=48.610742062164974f&lon=24.975710390429917&radius=30000&language=uk-UA&key=%s",
                address, environment.getProperty("application.tomtom.api.key")
        );

        String response = new RestTemplate().getForObject(url, String.class);

        Pattern pattern = Pattern.compile(".*position\":\\{\"lat\":(?<lat>[\\d\\.]+),\"lon\":(?<lon>[\\d\\.]+).*");

        Matcher matcher = pattern.matcher(response);

        if (matcher.matches()) {

            return Property.PropertyLocation.builder()
                    .lat(Double.parseDouble(matcher.group("lat")))
                    .lon(Double.parseDouble(matcher.group("lon")))
                    .build();
        }
        
        throw new ServiceException("Illegal address!");
    }

}
