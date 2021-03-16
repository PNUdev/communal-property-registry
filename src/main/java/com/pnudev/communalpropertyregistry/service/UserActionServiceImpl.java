package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.UserAction;
import com.pnudev.communalpropertyregistry.dto.UserActionPairDto;
import com.pnudev.communalpropertyregistry.repository.UserActionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static com.pnudev.communalpropertyregistry.service.processor.UserActionProcessor.USER_ACTION_QUEUE_DESTINATION;
import static java.util.Objects.isNull;

@Slf4j
@Service
public class UserActionServiceImpl implements UserActionService {

    private JmsTemplate jmsTemplate;
    private UserActionRepository userActionRepository;

    @Autowired
    public UserActionServiceImpl(JmsTemplate jmsTemplate, UserActionRepository userActionRepository) {
        this.jmsTemplate = jmsTemplate;
        this.userActionRepository = userActionRepository;
    }

    @Override
    public void saveUserAction(HttpServletRequest httpServletRequest) {

        UserAction userAction = prepareUserAction(httpServletRequest);

        jmsTemplate.convertAndSend(USER_ACTION_QUEUE_DESTINATION, userAction);

        log.info("User action: [{}] sent to queue!", userAction);
    }

    @Override
    public Page<UserActionPairDto> findAllUserActionPair(Pageable pageable) {

        return new PageImpl<>(
                userActionRepository.getCountPairsByIpAddresses(pageable.getPageSize(), pageable.getOffset()),
                pageable, userActionRepository.countAllIpAddresses()
        );
    }

    @Override
    public Page<UserAction> findAllByIpAddress(String ipAddress, Pageable pageable) {

        return new PageImpl<>(
                userActionRepository.findUserActionsByIpAddressOrderByTimeDesc(ipAddress, pageable),
                pageable, userActionRepository.countByIpAddress(ipAddress)
        );
    }

    private UserAction prepareUserAction(HttpServletRequest servletRequest) {

        final String httpMethod = servletRequest.getMethod();
        final String ipAddress = getIpAddress(servletRequest);
        final String url = getUrl(servletRequest);
        final String referrerUrl = servletRequest.getHeader("referer");
        final LocalDateTime dateTime = LocalDateTime.now();


        return UserAction.builder()
                .httpMethod(httpMethod)
                .ipAddress(ipAddress)
                .url(url)
                .referrerUrl(isNull(referrerUrl) ? " " : referrerUrl)
                .time(dateTime)
                .build();

    }

    private String getIpAddress(HttpServletRequest servletRequest) {

        final String ipAddress = servletRequest.getHeader("X-Forward-For");

        return ipAddress != null ? ipAddress : servletRequest.getRemoteAddr();
    }

    private String getUrl(HttpServletRequest request) {

        StringBuilder uri = new StringBuilder();
        uri.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(
                        ("http".equals(request.getScheme()) && request.getServerPort() == 80
                                || "https".equals(request.getScheme()) && request.getServerPort() == 443
                                ? "" : ":" + request.getServerPort()
                        )
                )
                .append(request.getRequestURI())
                .append((request.getQueryString() != null ? "?" + request.getQueryString() : ""));

        return uri.toString();
    }
}
