package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.dto.AddressDto;
import com.pnudev.communalpropertyregistry.dto.PropertyAdminDto;
import com.pnudev.communalpropertyregistry.dto.form.PropertyAdminFormDto;
import com.pnudev.communalpropertyregistry.exception.PropertyAdminException;
import com.pnudev.communalpropertyregistry.repository.AttachmentRepository;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import com.pnudev.communalpropertyregistry.repository.dsl.PropertyDslRepository;
import com.pnudev.communalpropertyregistry.util.mapper.PropertyMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.pnudev.communalpropertyregistry.domain.QProperty.property;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class PropertyAdminServiceImpl implements PropertyAdminService {

    private final PropertyDslRepository propertyDslRepository;

    private final PropertyRepository propertyRepository;

    private final PropertyMapper propertyMapper;

    private final AttachmentRepository attachmentRepository;

    @Autowired
    public PropertyAdminServiceImpl(PropertyDslRepository propertyDslRepository,
                                    PropertyRepository propertyRepository,
                                    PropertyMapper propertyMapper,
                                    AttachmentRepository attachmentRepository) {

        this.propertyDslRepository = propertyDslRepository;
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.attachmentRepository = attachmentRepository;
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
                    .eq(Property.PropertyStatus.valueOf(propertyStatus).name()));
        }

        Page<Property> propertiesPage = propertyDslRepository.findAll(pageable, predicates.toArray(Predicate[]::new));

        return new PageImpl<>(propertyMapper.mapToPropertiesAdminDto(propertiesPage.getContent()),
                pageable, propertiesPage.getTotalElements());
    }

    @Override
    public PropertyAdminDto findById(Long id) {

        Property property = propertyRepository.findById(id).orElseThrow(
                () -> new PropertyAdminException("Майно не знайдено!"));

        return propertyMapper.mapToPropertyAdminDto(property);
    }

    @Override
    public void updateById(Long id, PropertyAdminFormDto propertyAdminFormDto, AddressDto address) {

        Property propertyFromDb = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyAdminException("Майно не існує!"));

        Property.PropertyLocation propertyLocation = Property.PropertyLocation.builder()
                .lat(address.getLat())
                .lon(address.getLon())
                .build();

        Property property = propertyFromDb.toBuilder()
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
    public void create(PropertyAdminFormDto propertyAdminFormDto, AddressDto address) {

        Property.PropertyLocation propertyLocation = Property.PropertyLocation.builder()
                .lat(address.getLat())
                .lon(address.getLon())
                .build();

        Property property = Property.builder()
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

    @Transactional
    @Override
    public void deleteById(Long id) {

        attachmentRepository.deleteAllByPropertyId(id);

        propertyRepository.deleteById(id);
    }

}
