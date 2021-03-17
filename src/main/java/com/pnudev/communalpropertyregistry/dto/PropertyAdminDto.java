package com.pnudev.communalpropertyregistry.dto;

import com.pnudev.communalpropertyregistry.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyAdminDto {

    private Long id;

    private String imageUrl;

    private String address;

    private String name;

    private String categoryByPurposeName;

    private Property.PropertyStatus propertyStatus;

    private Double area;

    private Double areaTransferred;

    private String balanceHolder;

    private String owner;

    private LocalDate leaseAgreementEndDate;

    private Double amountOfRent;

    private boolean isAreaTransferredPubliclyViewable;

    private boolean isBalanceHolderPubliclyViewable;

    private boolean isOwnerPubliclyViewable;

    private boolean isLeaseAgreementEndDatePubliclyViewable;

    private boolean isAmountOfRentPubliclyViewable;

}
