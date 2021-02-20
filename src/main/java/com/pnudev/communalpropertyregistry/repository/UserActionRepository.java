package com.pnudev.communalpropertyregistry.repository;

import com.pnudev.communalpropertyregistry.domain.UserAction;
import org.springframework.data.repository.CrudRepository;

public interface UserActionRepository extends CrudRepository<UserAction, Long> {
}
