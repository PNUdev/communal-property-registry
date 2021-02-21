package com.pnudev.communalpropertyregistry.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    private Long id;

    private String imageUrl;

    private String address;

    @Embedded.Nullable
    private PropertyLocation propertyLocation;

    private String name;

    private Long categoryByPurposeId;

    private PropertyStatus propertyStatus;

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PropertyLocation {

        private Double lat;

        private Double lon;

    }

    @AllArgsConstructor
    public enum PropertyStatus {

        NON_RENT("Вільне"),
        RENT("Орендоване"),
        FIRST_OR_SECOND_TYPE_LIST("В переліку Першого або Другого типу"),
        PRIVATIZED("Приватизоване"),
        USED_BY_CITY_COUNCIL("У використанні органами міської ради");

        private String ukrainian_representation;
    }
}
