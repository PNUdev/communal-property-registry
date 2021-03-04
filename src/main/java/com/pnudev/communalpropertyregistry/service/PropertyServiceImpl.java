package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.exception.ServiceException;
import com.pnudev.communalpropertyregistry.repository.PropertyDslRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;
import static java.util.Objects.nonNull;

@Service
public class PropertyServiceImpl implements PropertyService {

    private static final Pattern pattern = Pattern.compile(".*position\":\\{\"lat\":(?<lat>[\\d\\.]+),\"lon\":(?<lon>[\\d\\.]+).*");

    private final Environment environment;

    private final PropertyDslRepository propertyDslRepository;

    public PropertyServiceImpl(Environment environment, PropertyDslRepository propertyDslRepository) {

        this.environment = environment;
        this.propertyDslRepository = propertyDslRepository;
    }

    @Override
    public Page<Property> findAll(String nameOrAddress, Long categoryByPurposeId, String propertyStatus, Pageable pageable) {

        List<Predicate> predicates = new ArrayList<>();

        if (nonNull(nameOrAddress)) {
            predicates.add(property.name.contains(nameOrAddress).or(property.address.contains(nameOrAddress)));
        }

        if (nonNull(categoryByPurposeId)) {
            predicates.add(property.categoryByPurposeId.eq(categoryByPurposeId));
        }

        if (nonNull(propertyStatus)) {
            predicates.add(property.propertyStatus
                    .contains(String.valueOf(Property.PropertyStatus.valueOf(propertyStatus.toUpperCase()))));
        }

        return propertyDslRepository.findAll(pageable, predicates.toArray(Predicate[]::new));
    }

    private Property.PropertyLocation convertAddressToPropertyLocation(String address) {

        final String url = String.format("https://api.tomtom.com/search/2/geocode/%s.json?" +
                        "storeResult=false&limit=1&lat=48.610742062164974f&lon=24.975710390429917&radius=30000&language=uk-UA&key=%s",
                address, environment.getProperty("application.tomtom.api.key")
        );

        String response = new RestTemplate().getForObject(url, String.class);

        Matcher matcher = pattern.matcher(response);

        if (!matcher.matches()) {
            throw new ServiceException("Illegal address!");
        }

        return Property.PropertyLocation.builder()
                .lat(Double.parseDouble(matcher.group("lat")))
                .lon(Double.parseDouble(matcher.group("lon")))
                .build();
    }

}
