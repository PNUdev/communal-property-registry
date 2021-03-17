package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationDto;
import com.pnudev.communalpropertyregistry.exception.PropertyAdminException;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import com.querydsl.core.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;

@Component
public class PropertyMapper {

    private final CategoryByPurposeRepository categoryByPurposeRepository;

    @Autowired
    public PropertyMapper(CategoryByPurposeRepository categoryByPurposeRepository) {
        this.categoryByPurposeRepository = categoryByPurposeRepository;
    }

    public PropertyLocationDto mapToPropertyLocationDto(Tuple tuple) {

        return PropertyLocationDto.builder()
                .propertyId(tuple.get(property.id))
                .propertyStatus(Property.PropertyStatus.valueOf(tuple.get(property.propertyStatus)))
                .lat(tuple.get(property.lat))
                .lon(tuple.get(property.lon))
                .build();
    }

    public List<Property> mapToProperties(List<Tuple> properties) {
        return properties.stream().map(this::mapToProperty).collect(Collectors.toList());
    }

    public Property mapToProperty(Tuple tuple) {

        Property.PropertyLocation propertyLocation = Property.PropertyLocation.builder()
                .lat(tuple.get(property.lat))
                .lon(tuple.get(property.lon))
                .build();

        return Property.builder()
                .id(tuple.get(property.id))
                .imageUrl(tuple.get(property.imageUrl))
                .address(tuple.get(property.address))
                .propertyLocation(propertyLocation)
                .name(tuple.get(property.name))
                .categoryByPurposeId(tuple.get(property.categoryByPurposeId))
                .propertyStatus(Property.PropertyStatus.valueOf(tuple.get(property.propertyStatus)))
                .area(tuple.get(property.area))
                .areaTransferred(tuple.get(property.areaTransferred))
                .balanceHolder(tuple.get(property.balanceHolder))
                .owner(tuple.get(property.owner))
                .leaseAgreementEndDate(tuple.get(property.leaseAgreementEndDate).toLocalDate())
                .amountOfRent(tuple.get(property.amountOfRent))
                .isAreaTransferredPubliclyViewable(tuple.get(property.isAreaTransferredPubliclyViewable))
                .isBalanceHolderPubliclyViewable(tuple.get(property.isBalanceHolderPubliclyViewable))
                .isOwnerPubliclyViewable(tuple.get(property.isOwnerPubliclyViewable))
                .isLeaseAgreementEndDatePubliclyViewable(tuple.get(property.isLeaseAgreementEndDatePubliclyViewable))
                .isAmountOfRentPubliclyViewable(tuple.get(property.isAmountOfRentPubliclyViewable))
                .build();
    }

    public List<PropertyAdminDto> mapToPropertiesAdminDto(List<Property> properties) {

        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeRepository.findAll();

        return properties.stream()
                .map(property -> mapToPropertyAdminDto(property, categoriesByPurpose))
                .collect(Collectors.toList());
    }

    public PropertyAdminDto mapToPropertyAdminDto(Property property) {

        Optional<CategoryByPurpose> categoryByPurpose = categoryByPurposeRepository.findById(property.getCategoryByPurposeId());

        return this.mapToPropertyAdminDto(property, Collections.singletonList(categoryByPurpose.orElseThrow(
                () -> new PropertyAdminException("Категорії за призначенням не знайдено!"))));
    }

    private PropertyAdminDto mapToPropertyAdminDto(Property property, List<CategoryByPurpose> categoriesByPurpose) {

        return PropertyAdminDto.builder()
                .id(property.getId())
                .imageUrl(property.getImageUrl())
                .address(property.getAddress())
                .name(property.getName())
                .categoryByPurposeName(findById(categoriesByPurpose, property.getCategoryByPurposeId()).getName())
                .propertyStatus(property.getPropertyStatus())
                .area(property.getArea())
                .areaTransferred(property.getAreaTransferred())
                .balanceHolder(property.getBalanceHolder())
                .owner(property.getOwner())
                .leaseAgreementEndDate(property.getLeaseAgreementEndDate())
                .amountOfRent(property.getAmountOfRent())
                .isAreaTransferredPubliclyViewable(property.isAreaTransferredPubliclyViewable())
                .isBalanceHolderPubliclyViewable(property.isBalanceHolderPubliclyViewable())
                .isOwnerPubliclyViewable(property.isOwnerPubliclyViewable())
                .isLeaseAgreementEndDatePubliclyViewable(property.isLeaseAgreementEndDatePubliclyViewable())
                .isAmountOfRentPubliclyViewable(property.isAmountOfRentPubliclyViewable())
                .build();
    }

    private CategoryByPurpose findById(List<CategoryByPurpose> categoriesByPurposes, Long id) {

        Optional<CategoryByPurpose> categoryByPurpose = categoriesByPurposes.stream()
                .filter(c -> c.getId().equals(id)).findFirst();

        return categoryByPurpose.orElseThrow(
                () -> new PropertyAdminException("Категорію за призначенням не знайдено!"));
    }

}
