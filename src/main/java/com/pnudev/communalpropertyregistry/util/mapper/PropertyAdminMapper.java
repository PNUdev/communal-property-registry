package com.pnudev.communalpropertyregistry.util.mapper;

import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PropertyAdminMapper {

    private final CategoryByPurposeRepository categoryByPurposeRepository;

    @Autowired
    public PropertyAdminMapper(CategoryByPurposeRepository categoryByPurposeRepository) {
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
                () -> new RuntimeException("Didn't find categoryByPurpose by id!"))));
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

        return categoryByPurpose.orElseThrow(() -> new RuntimeException("Didn't find categoryByPurpose by id!"));
    }

}
