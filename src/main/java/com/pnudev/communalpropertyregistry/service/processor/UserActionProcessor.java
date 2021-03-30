package com.pnudev.communalpropertyregistry.service.processor;

import com.pnudev.communalpropertyregistry.domain.UserAction;
import com.pnudev.communalpropertyregistry.repository.UserActionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserActionProcessor {

    public static final String USER_ACTION_QUEUE_DESTINATION = "userActions";

    private final UserActionRepository userActionRepository;

    @Autowired
    public UserActionProcessor(UserActionRepository userActionRepository) {
        this.userActionRepository = userActionRepository;
    }

    @JmsListener(destination = USER_ACTION_QUEUE_DESTINATION, containerFactory = "userActionListenerFactory")
    public void processUserAction(UserAction userAction) {

        log.info("Received user action: [{}] for processing!", userAction);

        userActionRepository.save(userAction);

    }
}
