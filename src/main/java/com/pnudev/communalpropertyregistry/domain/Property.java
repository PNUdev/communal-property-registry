package com.pnudev.communalpropertyregistry.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import java.time.LocalDate;
import java.util.Optional;

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
    @Getter
    public enum PropertyStatus {

        NON_RENT("Нерендовано"),
        RENT("Орендовано"),
        FIRST_OR_SECOND_TYPE_LIST("I-II типу"),
        PRIVATIZED("Приватизовано"),
        USED_BY_CITY_COUNCIL("Вик. міськвладою");

        private final String ukrainianRepresentation;

        public static Optional<PropertyStatus> fromName(String name) {
            try {

                Property.PropertyStatus status = Property.PropertyStatus.valueOf(name.toUpperCase());
                return Optional.of(status);

            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }
    }
}
