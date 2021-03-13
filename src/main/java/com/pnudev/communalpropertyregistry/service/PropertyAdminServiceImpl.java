package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.AddressDto;
import com.pnudev.communalpropertyregistry.dto.AddressResponseDto;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.PropertyAdminFormDto;
import com.pnudev.communalpropertyregistry.exception.IllegalAddressAdminException;
import com.pnudev.communalpropertyregistry.exception.ServiceAdminException;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import com.pnudev.communalpropertyregistry.repository.dsl.PropertyDslRepository;
import com.pnudev.communalpropertyregistry.util.mapper.PropertyMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class PropertyAdminServiceImpl implements PropertyAdminService {

    private static final Pattern pattern = Pattern.compile("freeformAddress\":\"(?<freeformAddress>[^\"]+).*?position\":\\{\"lat\":(?<lat>[\\d.]+),\"lon\":(?<lon>[\\d.]+)");

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
    public Page<PropertyAdminDto> findAll(@Nullable String nameOrAddress,
                                          @Nullable Long categoryByPurposeId,
                                          @Nullable String propertyStatus,
                                          Pageable pageable) {

        List<Predicate> predicates = new ArrayList<>();

        if (nonNull(nameOrAddress)) {
            predicates.add(property.name.contains(nameOrAddress)
                    .or(property.address.contains(nameOrAddress)));
        }

        if (nonNull(categoryByPurposeId)) {
            predicates.add(property.categoryByPurposeId.eq(categoryByPurposeId));
        }

        if (nonNull(propertyStatus)) {
            predicates.add(property.propertyStatus
                    .eq(String.valueOf(Property.PropertyStatus.valueOf(propertyStatus))));
        }

        Page<Property> propertiesPage = propertyDslRepository.findAll(pageable, predicates.toArray(Predicate[]::new));

        return new PageImpl<>(propertyMapper.mapToPropertiesAdminDto(propertiesPage.getContent()),
                pageable, propertiesPage.getTotalElements());
    }

    @Override
    public PropertyAdminDto findById(Long id) {

        Property property = propertyRepository.findById(id).orElseThrow(
                () -> new ServiceAdminException("Майно не знайдено!"));

        return propertyMapper.mapToPropertyAdminDto(property);
    }

    @Override
    public void save(PropertyAdminFormDto propertyAdminFormDto, AddressDto address) {

        Property.PropertyLocation propertyLocation = Property.PropertyLocation.builder()
                .lat(address.getLat())
                .lon(address.getLon())
                .build();

        Property property = Property.builder()
                .id(propertyAdminFormDto.getId())
                .imageUrl(propertyAdminFormDto.getImageUrl())
                .address(propertyAdminFormDto.getAddress())
                .propertyLocation(propertyLocation)
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

    @Override
    public void delete(Long id) {
        propertyRepository.deleteById(id);
    }

    @Override
    public AddressResponseDto getAddresses(String address) {

        log.info("Method 'getAddresses' started work!");

        final String URL = String.format("https://api.tomtom.com/search/2/geocode/%s.json?" +
                        "storeResult=false&limit=20&lat=48.610742062164974f&lon=24.975710390429917&radius=30000&language=uk-UA&key=%s",
                address, environment.getProperty("application.tomtom.api.key")
        );

        String response = null;
        Matcher matcher = null;

        try {
            response = new RestTemplate().getForObject(URL, String.class);
        } catch (RestClientException e) {
            log.error("RestClientException was caught, failed to get response in method 'getAddresses'!");
            throw new IllegalAddressAdminException("Таку адресу неможливо обробити, спробуйте ввести іншу!");
        }

        if (nonNull(response)) {
            matcher = pattern.matcher(response);
        }

        List<AddressDto> addresses = new ArrayList<>();

        if (nonNull(matcher)) {
            while (matcher.find()) {
                addresses.add(AddressDto.builder()
                        .address(matcher.group("freeformAddress"))
                        .lat(Double.parseDouble(matcher.group("lat")))
                        .lon(Double.parseDouble(matcher.group("lon")))
                        .build()
                );
            }
        }

        if (addresses.isEmpty()) {
            throw new IllegalAddressAdminException("Невірно вказаний адрес!");
        }

        log.info("Method 'getAddresses' is going to finish work successfully!");

        return new AddressResponseDto(addresses);
    }

}
