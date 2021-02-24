package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<Property, Long> {

    Long countPropertiesByCategoryByPurposeIdEqualsAndPropertyStatusEquals(Long categoryByPurposeId,
                                                                           Property.PropertyStatus propertyStatus);

}
