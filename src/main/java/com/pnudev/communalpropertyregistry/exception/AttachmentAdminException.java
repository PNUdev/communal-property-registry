package com.pnudev.communalpropertyregistry.exception;

public class AttachmentAdminException extends RuntimeException {

    private final Long propertyId;

    public AttachmentAdminException(String message, Long propertyId) {
        super(message);
        this.propertyId = propertyId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

}
