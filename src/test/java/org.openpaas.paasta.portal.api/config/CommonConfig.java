package org.openpaas.paasta.portal.api.config;

import org.cloudfoundry.client.v2.applications.SummaryApplicationResponse;
import org.cloudfoundry.reactor.TokenProvider;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.TailSocket;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.service.AppService;
import org.openpaas.paasta.portal.api.service.CommonService;
import org.openpaas.paasta.portal.api.service.MonitoringRestTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

public class CommonConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(org.openpaas.paasta.portal.api.Service.AppTest.class);

    public static final String AUTHORIZATION_HEADER_KEY = "cf-Authorization";


    @Value("${spring.security.username}")
    String username;

    @Value("${spring.security.password}")
    String password;

    @Value("${cloudfoundry.cc.api.url}")
    public String apiTarget;

    @Value("${cloudfoundry.cc.api.uaaUrl}")
    public String uaaTarget;

    @Value("${cloudfoundry.cc.api.sslSkipValidation}")
    public boolean cfskipSSLValidation;

    @Value("${cloudfoundry.user.admin.username}")
    public String adminUserName;

    @Value("${cloudfoundry.user.admin.password}")
    public String adminPassword;

    @Value("${cloudfoundry.user.uaaClient.clientId}")
    public String uaaClientId;

    @Value("${cloudfoundry.user.uaaClient.clientSecret}")
    public String uaaClientSecret;

    @Value("${cloudfoundry.user.uaaClient.adminClientId}")
    public String uaaAdminClientId;

    @Value("${cloudfoundry.user.uaaClient.adminClientSecret}")
    public String uaaAdminClientSecret;

    @Value("${cloudfoundry.user.uaaClient.loginClientId}")
    public String uaaLoginClientId;

    @Value("${cloudfoundry.user.uaaClient.loginClientSecret}")
    public String uaaLoginClientSecret;

    @Value("${cloudfoundry.user.uaaClient.skipSSLValidation}")
    public boolean skipSSLValidation;

    @Value("${monitoring.api.url}")
    public String monitoringApiTarget;

    @Value("${paasta.portal.api.authorization.base64}")
    private String base64Authorization;

    @Value("${paasta.portal.api.zuulUrl.cfapi}")
    private String cfApiUrl;

    @Value("${paasta.portal.api.zuulUrl.commonapi}")
    private String commonApiUrl;

    @Value("${paasta.portal.api.zuulUrl.storageapi}")
    private String storageApiUrl;

    @Value("${paasta.portal.storageapi.type}")
    private String storageApiType;

    @Value("${tailsocket.port}")
    public Integer tailPort;

    @InjectMocks
    private AppService appService;

    @Mock
    private Common common;

    @Mock
    private CommonService commonService;

    @Mock
    private MonitoringRestTemplateService monitoringRestTemplateService;

    @Mock
    private TailSocket tailSocket;

    private TokenProvider tokenProvider;



    final static String TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImtleS0xIiwidHlwIjoiSldUIn0.eyJqdGkiOiIxZTFkOWJjNTBlN2U0ZDZlODE4MjU0MDZmODEwN2U0OCIsInN1YiI6ImUwNjFkOTk3LTZmOWQtNGE0My05ZmI2LTg4ZDU3OTlmZjllMCIsInNjb3BlIjpbImNsb3VkX2NvbnRyb2xsZXIucmVhZCIsImNsb3VkX2NvbnRyb2xsZXIud3JpdGUiLCJjbG91ZF9jb250cm9sbGVyX3NlcnZpY2VfcGVybWlzc2lvbnMucmVhZCIsIm9wZW5pZCJdLCJjbGllbnRfaWQiOiJsb2NhbHBvcnRhbGNsaWVudCIsImNpZCI6ImxvY2FscG9ydGFsY2xpZW50IiwiYXpwIjoibG9jYWxwb3J0YWxjbGllbnQiLCJncmFudF90eXBlIjoiYXV0aG9yaXphdGlvbl9jb2RlIiwidXNlcl9pZCI6ImUwNjFkOTk3LTZmOWQtNGE0My05ZmI2LTg4ZDU3OTlmZjllMCIsIm9yaWdpbiI6InVhYSIsInVzZXJfbmFtZSI6InN3bW9vbiIsImVtYWlsIjoic3dtb29uIiwiYXV0aF90aW1lIjoxNTI3NTYxNjM3LCJyZXZfc2lnIjoiNTJlZDFjMjAiLCJpYXQiOjE1Mjc1NjE2NDUsImV4cCI6MTUyNzYwNDg0NSwiaXNzIjoiaHR0cHM6Ly91YWEuMTE1LjY4LjQ2LjE4Ny54aXAuaW8vb2F1dGgvdG9rZW4iLCJ6aWQiOiJ1YWEiLCJhdWQiOlsiY2xvdWRfY29udHJvbGxlciIsImxvY2FscG9ydGFsY2xpZW50Iiwib3BlbmlkIiwiY2xvdWRfY29udHJvbGxlcl9zZXJ2aWNlX3Blcm1pc3Npb25zIl19.j02QrO7X9G4zwo7RbUn_VCxNnQo9AfjexU67ZUo22wlQcXE_1HdqCosm9AgTfbgN3FZfbkn7EbC529Um9ZIgwfnEu2MR_xTO7vRS22m--W8o6gWluVvge6wuhu-4LlPaUYXgovTwEYrqBCo1nsDBD7nbXfft4I1EBw_-FpPbg1bBLZa09qv3i6An0xrjXsw1dCSR138xctx6m0dptoo_BL0MLQ7Nsj5KW2nx8Q9AFAEf-ZPGgPyxU3R9L_hEFsF5SUEHF6jtSmNy_WKvNvuX5zldXfxY8w77nJgvFxJXh-p7y15CszA-Z4n65rUxTS1jhkaURKM9pJYT76j4eRh82RrK6j9cxj5nqjPiIe1ArPRiTWkUv6GB80vD3nxeKSdVeynkP6_ZtJZgChxzeoEgIpoDwBUii09sDRilpAzS3H3-L--aOBL16QwEk64sLlmosMeNyl6XfdyC3RbVykJKIyd0RnHoYmx36YSsxCUctsQTYmVFXWdL9SeMZoxs0Zu91-V-CUVL5XGGO3JOi5NTnSWbY4PWR59X0AJI05qcNf65DOmn7eoCUevRcgQ3u6_p1Ae-mGppf7FxDO9QCBavDj5nCGCrptEn1Ek77DWurN7o8VEZnsmVWlRQTRE96Owc1DyZIezOrHCZu12Gw9dB5oPoCPpJ8IV-o0v7VFqKSLs";
    final static String GUID = "42a50bfe-f186-48dc-8e5c-ad2a73bc4a8a";
    final static String APPNAME = "test-app";

    SummaryApplicationResponse summaryApplicationResponse;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

        LOGGER.info("##############################################################");
        LOGGER.info("##############################################################");
        LOGGER.info("AppService Test Start");
        LOGGER.info("##############################################################");
        LOGGER.info("##############################################################");


//        ReflectionTestUtils.setField(common, "username", username);
//        ReflectionTestUtils.setField(common, "password", password);

        ReflectionTestUtils.setField(common, "apiTarget", apiTarget);
        ReflectionTestUtils.setField(common, "uaaTarget", uaaTarget);
        ReflectionTestUtils.setField(common, "adminUserName", adminUserName);
        ReflectionTestUtils.setField(common, "adminPassword", adminPassword);
        ReflectionTestUtils.setField(common, "uaaClientId", uaaClientId);
        ReflectionTestUtils.setField(common, "uaaClientSecret", uaaClientSecret);
        ReflectionTestUtils.setField(common, "uaaAdminClientId", uaaAdminClientId);
        ReflectionTestUtils.setField(common, "uaaAdminClientSecret", uaaAdminClientSecret);
        ReflectionTestUtils.setField(common, "uaaLoginClientId", uaaLoginClientId);
        ReflectionTestUtils.setField(common, "uaaLoginClientSecret", uaaLoginClientSecret);
        ReflectionTestUtils.setField(common, "skipSSLValidation", skipSSLValidation);
        ReflectionTestUtils.setField(common, "monitoringApiTarget", monitoringApiTarget);
        ReflectionTestUtils.setField(common, "cfskipSSLValidation", cfskipSSLValidation);


        ReflectionTestUtils.setField(commonService, "base64Authorization", base64Authorization);
        ReflectionTestUtils.setField(commonService, "cfApiUrl", cfApiUrl);
        ReflectionTestUtils.setField(commonService, "commonApiUrl", commonApiUrl);
        ReflectionTestUtils.setField(commonService, "storageApiUrl", storageApiUrl);
        ReflectionTestUtils.setField(commonService, "storageApiType", storageApiType);

        ReflectionTestUtils.setField(tailSocket, "tailPort", tailPort);


        tokenProvider = new TokenGrantTokenProvider(TOKEN);
        summaryApplicationResponse = SummaryApplicationResponse.builder().id(GUID).name(APPNAME).build();
    }

}


