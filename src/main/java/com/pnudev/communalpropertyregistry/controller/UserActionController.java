package com.pnudev.communalpropertyregistry.controller;

import com.pnudev.communalpropertyregistry.domain.UserAction;
import com.pnudev.communalpropertyregistry.dto.IpAddressAndCountDto;
import com.pnudev.communalpropertyregistry.service.UserActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Slf4j
@Controller
@RequestMapping("admin/user-actions")
public class UserActionController {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")).toFormatter();

    private UserActionService userActionService;

    @Autowired
    public UserActionController(UserActionService userActionService) {
        this.userActionService = userActionService;
    }

    @GetMapping
    public String getAnalyticPairs(
            @PageableDefault(size = 14, sort = "ipAddress", direction = Sort.Direction.ASC) Pageable pageable, Model model){

        Page<IpAddressAndCountDto> ipAddressAndCountDtoList = userActionService.countAllByIpAddresses(pageable);

        log.info("Analytics pairs successfully gathered!");

        model.addAttribute("ipAddressAndCountDtoList", ipAddressAndCountDtoList);

        return "admin/analytics/user-analytics";
    }

    @GetMapping("/partial")
    public String getByIpAddress(
            @PageableDefault(size = 6, sort = "time", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String ipAddress, Model model){

        Page<UserAction> partialUserActionsByIpAddress = userActionService.findAllByIpAddress(ipAddress, pageable);

        log.info("Analytics about user actions successfully gathered!");

        model.addAttribute("partialUserActionsByIpAddress", partialUserActionsByIpAddress);
        model.addAttribute("formatter", DATE_TIME_FORMATTER);

        return "admin/analytics/partial-user-actions";
    }
}
