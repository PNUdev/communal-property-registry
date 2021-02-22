package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.UserAction;
import com.pnudev.communalpropertyregistry.dto.IpAddressAndCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

public interface UserActionService {

    void saveUserAction(HttpServletRequest httpServletRequest);

    Page<IpAddressAndCountDto> countAllByIpAddresses(Pageable pageable);

    Page<UserAction> findAllByIpAddress(String ipAddress, Pageable pageable);
}
