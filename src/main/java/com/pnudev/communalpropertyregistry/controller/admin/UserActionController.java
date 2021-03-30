package com.pnudev.communalpropertyregistry.controller.admin;

import com.pnudev.communalpropertyregistry.domain.UserAction;
import com.pnudev.communalpropertyregistry.dto.UserActionPairDto;
import com.pnudev.communalpropertyregistry.service.UserActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
@RequestMapping("admin/user-actions")
public class UserActionController {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

    private UserActionService userActionService;

    @Autowired
    public UserActionController(UserActionService userActionService) {
        this.userActionService = userActionService;
    }

    @GetMapping
    public String getAllUserActionPair(
            @PageableDefault(size = 14) Pageable pageable, Model model) {

        Page<UserActionPairDto> ipAddressAndCountDtoList = userActionService.findAllUserActionPair(pageable);

        log.info("Analytics pairs successfully gathered!");

        model.addAttribute("ipAddressAndCountDtoList", ipAddressAndCountDtoList);

        return "admin/analytics/user-analytics";
    }

    @GetMapping("/partial")
    public String getAllByIpAddress(
            @PageableDefault(size = 6) Pageable pageable,
            @RequestParam String ipAddress, Model model) {

        Page<UserAction> partialUserActionsByIpAddress = userActionService.findAllByIpAddress(ipAddress, pageable);

        log.info("Analytics about user actions by a specified ipAddress successfully gathered!");

        model.addAttribute("partialUserActionsByIpAddress", partialUserActionsByIpAddress);
        model.addAttribute("formatter", DATE_TIME_FORMATTER);

        return "admin/analytics/partial-user-actions";
    }

    @GetMapping("/download/txt")
    public void downloadReportTxt(HttpServletResponse response) {

        userActionService.downloadReport(response);

        log.info("Txt report successfully downloaded");
    }
}
