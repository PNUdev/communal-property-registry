package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.PropertyAdminFormDto;
import com.pnudev.communalpropertyregistry.exception.ServiceException;
import com.pnudev.communalpropertyregistry.repository.PropertyDslRepository;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import com.pnudev.communalpropertyregistry.util.mapper.PropertyAdminMapper;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class PropertyAdminServiceImpl implements PropertyAdminService {

    private static final Pattern pattern = Pattern.compile(".*position\":\\{\"lat\":(?<lat>[\\d\\.]+),\"lon\":(?<lon>[\\d\\.]+).*");

    private final Environment environment;

    private final PropertyDslRepository propertyDslRepository;

    private final PropertyRepository propertyRepository;

    private final PropertyMapper propertyMapper;

    @Autowired
    public PropertyAdminServiceImpl(Environment environment,
                                    PropertyDslRepository propertyDslRepository,
                                    PropertyRepository propertyRepository,
                                    PropertyMapper propertyMapper) {

        this.environment = environment;
        this.propertyDslRepository = propertyDslRepository;
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    @Override
    public Page<PropertyAdminDto> findAll(String nameOrAddress, Long categoryByPurposeId, String propertyStatus, Pageable pageable) {

        List<Predicate> predicates = new ArrayList<>();

        if (nonNull(nameOrAddress)) {
            predicates.add(property.name.contains(nameOrAddress).or(property.address.contains(nameOrAddress)));
        }

        if (nonNull(categoryByPurposeId)) {
            predicates.add(property.categoryByPurposeId.eq(categoryByPurposeId));
        }

        if (nonNull(propertyStatus)) {
            predicates.add(property.propertyStatus
                    .eq(String.valueOf(Property.PropertyStatus.valueOf(propertyStatus))));
        }

        Page<Property> propertiesPage = propertyDslRepository.findAll(pageable, predicates.toArray(Predicate[]::new));

        return new PageImpl<>(propertyMapper.mapToPropertyAdminDto(propertiesPage.getContent()),
                pageable, propertiesPage.getTotalElements());
    }

    @Override
    public PropertyAdminDto findById(Long id) {

        Property property = propertyRepository.findById(id).orElseThrow(
                () -> new ServiceException("Майно не знайдено!"));

        return propertyMapper.mapToPropertyAdminDto(property);
    }

    @Override
    public void save(PropertyAdminFormDto propertyAdminFormDto) {

        Property property = Property.builder()
                .id(propertyAdminFormDto.getId())
                .imageUrl(propertyAdminFormDto.getImageUrl())
                .address(propertyAdminFormDto.getAddress())
                .propertyLocation(convertAddressToPropertyLocation(propertyAdminFormDto.getAddress()))
                .name(propertyAdminFormDto.getName())
                .categoryByPurposeId(propertyAdminFormDto.getCategoryByPurposeId())
                .propertyStatus(propertyAdminFormDto.getPropertyStatus())
                .area(propertyAdminFormDto.getArea())
                .areaTransferred(propertyAdminFormDto.getAreaTransferred())
                .balanceHolder(propertyAdminFormDto.getBalanceHolder())
                .owner(propertyAdminFormDto.getOwner())
                .leaseAgreementEndDate(propertyAdminFormDto.getLeaseAgreementEndDate())
                .amountOfRent(propertyAdminFormDto.getAmountOfRent())
                .isAreaTransferredPubliclyViewable(propertyAdminFormDto.isAreaTransferredPubliclyViewable())
                .isBalanceHolderPubliclyViewable(propertyAdminFormDto.isBalanceHolderPubliclyViewable())
                .isOwnerPubliclyViewable(propertyAdminFormDto.isOwnerPubliclyViewable())
                .isLeaseAgreementEndDatePubliclyViewable(propertyAdminFormDto.isLeaseAgreementEndDatePubliclyViewable())
                .isAmountOfRentPubliclyViewable(propertyAdminFormDto.isAmountOfRentPubliclyViewable())
                .build();

        propertyRepository.save(property);
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
