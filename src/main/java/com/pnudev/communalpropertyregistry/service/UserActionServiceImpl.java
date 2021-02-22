package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.UserAction;
import com.pnudev.communalpropertyregistry.dto.IpAddressAndCountDto;
import com.pnudev.communalpropertyregistry.repository.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserActionServiceImpl implements UserActionService {
    private final static String DESTINATION_USER_ANALYTICS_QUEUE = "userActions";
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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

        //jmsTemplate.convertAndSend(DESTINATION_USER_ANALYTICS_QUEUE, userAction);

        System.out.println(userAction);
        System.out.println("==========");
    }

    @Override
    public Page<IpAddressAndCountDto> countAllByIpAddresses(Pageable pageable) {
        return new PageImpl<>(
                userActionRepository.countByIpAddresses(pageable.getPageSize(), pageable.getOffset()),
                pageable, userActionRepository.countAllByIpAddress()
        );
    }

    @Override
    public Page<UserAction> findAllByIpAddress(String ipAddress, Pageable pageable) {
        return new PageImpl<>(
                userActionRepository.findAllByIpAddress(ipAddress, pageable.getPageSize(), pageable.getOffset()),
                pageable, userActionRepository.countByIpAddress(ipAddress)
        );
    }

    private UserAction prepareUserAction(HttpServletRequest servletRequest) {

        final String httpMethod = servletRequest.getMethod();
        final String ipAddress = getApAddress(servletRequest);
        final String url = getUrl(servletRequest);
        final String referrerUrl = servletRequest.getHeader("referer");
        final LocalDateTime dateTime = LocalDateTime.now();


        return UserAction.builder()
                .httpMethod(httpMethod)
                .ipAddress(ipAddress)
                .url(url)
                .referrerUrl(referrerUrl)
                .time(dateTime)
                .build();

    }

    private String getApAddress(HttpServletRequest servletRequest) {
        final String ipAddress = servletRequest.getHeader("X-Forward-For");
        return ipAddress != null ? ipAddress : servletRequest.getRemoteAddr();
    }

    private String getUrl(HttpServletRequest request) {
        final String uri = request.getScheme() + "://" +
                request.getServerName() +
                ("http".equals(request.getScheme()) && request.getServerPort() == 80
                        || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
                request.getRequestURI() +
                (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        return uri;
    }
}
