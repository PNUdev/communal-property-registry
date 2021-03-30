package com.pnudev.communalpropertyregistry.scheduled;

import com.pnudev.communalpropertyregistry.service.UserActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClearUserActionTableJob {

    private final UserActionService userActionService;

    @Autowired
    public ClearUserActionTableJob(UserActionService userActionService) {
        this.userActionService = userActionService;
    }

    // runs every day at 6 a.m
    @Scheduled(cron = "0 0 6 * * ?", zone = "Europe/Kiev")
    public void clearOldLoginAttemptRecords() {

        log.info("Removing deprecated user action records job started!");

        userActionService.deleteDeprecatedUserActions();

        log.info("Removing deprecated user action records job successfully finished!");

    }

}
