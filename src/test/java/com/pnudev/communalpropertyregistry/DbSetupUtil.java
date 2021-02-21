package com.pnudev.communalpropertyregistry;

import com.pnudev.communalpropertyregistry.domain.Attachment;
import com.pnudev.communalpropertyregistry.domain.AttachmentCategory;
import com.pnudev.communalpropertyregistry.domain.CategoryByPurpose;
import com.pnudev.communalpropertyregistry.domain.Property;
import com.pnudev.communalpropertyregistry.domain.UserAction;
import com.pnudev.communalpropertyregistry.repository.AttachmentCategoryRepository;
import com.pnudev.communalpropertyregistry.repository.AttachmentRepository;
import com.pnudev.communalpropertyregistry.repository.CategoryByPurposeRepository;
import com.pnudev.communalpropertyregistry.repository.PropertyRepository;
import com.pnudev.communalpropertyregistry.repository.UserActionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
class DbSetupUtil {

    @Autowired
    private AttachmentCategoryRepository attachmentCategoryRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private CategoryByPurposeRepository categoryByPurposeRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserActionRepository userActionRepository;

    @Test
    void setupDb() {

        CategoryByPurpose categoryByPurpose = CategoryByPurpose.builder()
                .name("name")
                .build();

        categoryByPurposeRepository.save(categoryByPurpose);

        Property property = Property.builder()
                .imageUrl("imageUrl")
                .address("address")
                .propertyLocation(Property.PropertyLocation.builder()
                        .lat(33.30)
                        .lon(33.30)
                        .build())
                .name("name")
                .categoryByPurposeId(categoryByPurpose.getId())
                .propertyStatus(Property.PropertyStatus.PRIVATIZED)
                .area(3333.0)
                .areaTransferred(3333.0)
                .balanceHolder("balanceHolder")
                .owner("owner")
                .leaseAgreementEndDate(LocalDate.now())
                .amountOfRent(3333.0)
                .isAreaTransferredPubliclyViewable(true)
                .isBalanceHolderPubliclyViewable(false)
                .isOwnerPubliclyViewable(true)
                .isLeaseAgreementEndDatePubliclyViewable(false)
                .isAmountOfRentPubliclyViewable(true)
                .build();

        propertyRepository.save(property);


        AttachmentCategory attachmentCategory = AttachmentCategory.builder()
                .name("name")
                .isPubliclyViewable(true)
                .build();

        attachmentCategoryRepository.save(attachmentCategory);

        Attachment attachment = Attachment.builder()
                .note("note")
                .link("link")
                .attachmentCategoryId(attachmentCategory.getId())
                .propertyId(property.getId())
                .isPubliclyViewable(true)
                .build();

        attachmentRepository.save(attachment);

        UserAction userAction = UserAction.builder()
                .ipAddress("ipAddress")
                .httpMethod("httpMethod")
                .url("url")
                .referrerUrl("referrerUrl")
                .time(LocalDateTime.now())
                .build();

        userActionRepository.save(userAction);

    }

}
