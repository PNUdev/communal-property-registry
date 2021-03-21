package com.pnudev.communalpropertyregistry.dto.form;

import com.pnudev.communalpropertyregistry.domain.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyAdminFormDto {

    private String imageUrl;

    private String address;

    private String name;

    private Long categoryByPurposeId;

    private Property.PropertyStatus propertyStatus;

    private Double area;

    private Double areaTransferred;

    private String balanceHolder;

    private String owner;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leaseAgreementEndDate;

    private Double amountOfRent;

    private boolean isAreaTransferredPubliclyViewable;

    private boolean isBalanceHolderPubliclyViewable;

    private boolean isOwnerPubliclyViewable;

    private boolean isLeaseAgreementEndDatePubliclyViewable;

    private boolean isAmountOfRentPubliclyViewable;

}
