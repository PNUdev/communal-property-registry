package com.pnudev.communalpropertyregistry.service;

import com.pnudev.communalpropertyregistry.domain.UserAction;
import com.pnudev.communalpropertyregistry.dto.UserActionPairDto;
import com.pnudev.communalpropertyregistry.exception.ServiceAdminException;
import com.pnudev.communalpropertyregistry.repository.UserActionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static com.pnudev.communalpropertyregistry.service.processor.UserActionProcessor.USER_ACTION_QUEUE_DESTINATION;
import static java.util.Objects.isNull;

@Slf4j
@Service
public class UserActionServiceImpl implements UserActionService {

    private final static String REPORT_FILE = "user-analytics.txt";
    private final static String REFERER_HEADER = "referer";
    private final static String FORWARD_FOR_HEADER = "X-Forward-For";
    private final static ZoneId ZONE_ID = ZoneId.of("Europe/Kiev");

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

    @Override
    public void downloadReport(HttpServletResponse response) {

        List<UserAction> userActions = userActionRepository.findAll();

        response.setContentType("application/txt");
        response.setHeader("Content-Disposition", "attachment; filename=" + REPORT_FILE);

        try {
            final byte[] bytes = userActions.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining("\n")).getBytes();

            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();

        } catch (RuntimeException | IOException e) {
            log.error("Error occurred during report generation! {}", e.getLocalizedMessage());
            throw new ServiceAdminException("Error occurred during report generation!");
        }
    }

    @Override
    public void deleteDeprecatedUserActions() {

        LocalDateTime weekTime = LocalDateTime.now().minusDays(7);
        userActionRepository.deleteAllByTimeBefore(weekTime);

    }

    private UserAction prepareUserAction(HttpServletRequest servletRequest) {

        final String httpMethod = servletRequest.getMethod();
        final String ipAddress = getIpAddress(servletRequest);
        final String url = getUrl(servletRequest);
        final String refererUrl = servletRequest.getHeader(REFERER_HEADER);
        final LocalDateTime dateTime = LocalDateTime.now(ZONE_ID);


        return UserAction.builder()
                .httpMethod(httpMethod)
                .ipAddress(ipAddress)
                .url(url)
                .referrerUrl(isNull(refererUrl) ? StringUtils.EMPTY : refererUrl)
                .time(dateTime)
                .build();

    }

    private String getIpAddress(HttpServletRequest servletRequest) {

        final String ipAddress = servletRequest.getHeader(FORWARD_FOR_HEADER);

        return !isNull(ipAddress) ? ipAddress : servletRequest.getRemoteAddr();
    }

    private String getUrl(HttpServletRequest request) {

        StringBuilder uri = new StringBuilder();
        uri.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(
                        ("http".equals(request.getScheme()) && request.getServerPort() == 80
                                || "https".equals(request.getScheme()) && request.getServerPort() == 443
                                ? StringUtils.EMPTY : ":" + request.getServerPort()
                        )
                )
                .append(request.getRequestURI())
                .append(!isNull(request.getQueryString()) ? "?" + request.getQueryString() : StringUtils.EMPTY);

        return uri.toString();
    }
}
