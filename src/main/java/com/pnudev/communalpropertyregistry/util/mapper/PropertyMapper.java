package com.pnudev.communalpropertyregistry.util.mapper;

<<<<<<< HEAD
import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.PropertyLocationDto;
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

    public List<PropertyAdminDto> mapToPropertyAdminDto(List<Property> properties) {

        List<CategoryByPurpose> categoriesByPurpose = categoryByPurposeRepository.findAll();

        return properties.stream()
                .map(property -> mapToPropertyAdminDto(property, categoriesByPurpose))
                .collect(Collectors.toList());
    }

    public PropertyAdminDto mapToPropertyAdminDto(Property property) {

        Optional<CategoryByPurpose> categoryByPurpose = categoryByPurposeRepository.findById(property.getCategoryByPurposeId());

        return this.mapToPropertyAdminDto(property, Collections.singletonList(categoryByPurpose.orElseThrow(
                () -> new RuntimeException("Не знайдено категорії за призначенням!"))));
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
                .filter(c -> c.getId().equals(id)).findAny();

        return categoryByPurpose.orElseThrow(() -> new RuntimeException("Не знайдено категорії за призначенням!"));
    }

    public PropertyLocationDto mapToPropertyLocationDto(Tuple tuple) {

        return PropertyLocationDto.builder()
                .propertyId(tuple.get(property.id))
                .propertyStatus(Property.PropertyStatus.valueOf(tuple.get(property.propertyStatus)))
                .lat(tuple.get(property.lat))
                .lon(tuple.get(property.lon))
                .build();
    }

}
