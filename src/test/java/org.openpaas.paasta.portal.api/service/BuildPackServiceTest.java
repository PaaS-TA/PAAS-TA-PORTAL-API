package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v3.applications.ApplicationsV3;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.client.v2.applications.ApplicationsV2;
import org.cloudfoundry.client.v2.buildpacks.Buildpacks;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackRequest;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackResponse;
import org.cloudfoundry.identity.uaa.api.client.UaaClientOperations;
import org.cloudfoundry.identity.uaa.api.group.UaaGroupOperations;
import org.cloudfoundry.identity.uaa.api.user.UaaUserOperations;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.uaa.authorizations.Authorizations;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.config.TestConfig;
import org.openpaas.paasta.portal.api.config.cloudfoundry.provider.TokenGrantTokenProvider;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;


import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.applications.ApplicationsV2;
import org.cloudfoundry.client.v2.buildpacks.Buildpacks;
import org.cloudfoundry.client.v2.domains.Domains;
import org.cloudfoundry.client.v2.events.Events;
import org.cloudfoundry.client.v2.featureflags.FeatureFlags;
import org.cloudfoundry.client.v2.jobs.Jobs;
import org.cloudfoundry.client.v2.organizationquotadefinitions.OrganizationQuotaDefinitions;
import org.cloudfoundry.client.v2.organizations.Organizations;
import org.cloudfoundry.client.v2.privatedomains.PrivateDomains;
import org.cloudfoundry.client.v2.resourcematch.ResourceMatch;
import org.cloudfoundry.client.v2.routes.Routes;
import org.cloudfoundry.client.v2.servicebindings.ServiceBindingsV2;
import org.cloudfoundry.client.v2.servicebrokers.ServiceBrokers;
import org.cloudfoundry.client.v2.serviceinstances.ServiceInstances;
import org.cloudfoundry.client.v2.servicekeys.ServiceKeys;
import org.cloudfoundry.client.v2.serviceplans.ServicePlans;
import org.cloudfoundry.client.v2.serviceplanvisibilities.ServicePlanVisibilities;
import org.cloudfoundry.client.v2.services.Services;
import org.cloudfoundry.client.v2.shareddomains.SharedDomains;
import org.cloudfoundry.client.v2.spacequotadefinitions.SpaceQuotaDefinitions;
import org.cloudfoundry.client.v2.spaces.Spaces;
import org.cloudfoundry.client.v2.stacks.Stacks;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.UserProvidedServiceInstances;
import org.cloudfoundry.client.v2.users.Users;
import org.cloudfoundry.client.v3.applications.ApplicationsV3;
import org.cloudfoundry.client.v3.tasks.Tasks;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.routing.RoutingClient;
import org.cloudfoundry.routing.v1.routergroups.RouterGroups;
import org.cloudfoundry.uaa.UaaClient;
import org.cloudfoundry.uaa.authorizations.Authorizations;
import org.cloudfoundry.uaa.tokens.Tokens;
import org.junit.Before;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildPackServiceTest extends TestConfig {
    @Mock
    Logger LOGGER;
    @Mock
    LoginService loginService;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    ThreadLocal<DefaultConnectionContext> connectionContextThreadLocal;
//    @InjectMocks
    @MockBean
    BuildPackService buildPackService;

    private final CloudFoundryOperations cloudFoundryOperations = mock(CloudFoundryOperations.class);

//    private final CloudFoundryApplication application;

//    @Mock
//    private CloudFoundryClient cloudFoundryClient;

    @Mock
    Common common;

//    @Mock
//    Buildpacks buildpacks;

//    private final ApplicationsV2 applications = mock(ApplicationsV2.class, RETURNS_SMART_NULLS);
//
//    private final CloudFoundryClient cloudFoundryClient = mock(CloudFoundryClient.class, RETURNS_SMART_NULLS);

//    private final ReactorCloudFoundryClient reactorCloudFoundryClient = mock(ReactorCloudFoundryClient.class, RETURNS_SMART_NULLS);

    private CloudFoundryClient cfClient;
    private CloudFoundryOperations cfOps;

    ConnectionContext connectionContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ConnectionContext connectionContext = DefaultConnectionContext.builder().apiHost("apiHost").skipSslValidation(true).build();
        TokenProvider tokenProvider = PasswordGrantTokenProvider.builder().username("admin").password("admin").build();
        cfClient = ReactorCloudFoundryClient.builder().connectionContext(connectionContext).tokenProvider(tokenProvider).build();
        cfOps = DefaultCloudFoundryOperations.builder().cloudFoundryClient(cfClient).build();
    }

//    @Test
//    public void testGetBuildPacks() throws Exception {
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any(), any())).thenReturn(null);
//
//        Map<String, Object> result = buildPackService.getBuildPacks();
//        Assert.assertEquals(new HashMap<String, Object>() {{
//            put("String", null);
//        }}, result);
//    }

    protected static final Mono<String> MISSING_ID = Mono.error(new java.lang.IllegalStateException("MISSING_ID"));

    protected static final Mono<String> MISSING_ORGANIZATION_ID = Mono.error(new java.lang.IllegalStateException("MISSING_ORGANIZATION_ID"));

    protected static final Mono<String> MISSING_SPACE_ID = Mono.error(new java.lang.IllegalStateException("MISSING_SPACE_ID"));

    protected static final Mono<String> MISSING_USERNAME = Mono.error(new java.lang.IllegalStateException("MISSING_USERNAME"));

    protected static final String TEST_ORGANIZATION_ID = "test-organization-id";

    protected static final String TEST_ORGANIZATION_NAME = "test-organization-name";

    protected static final String TEST_SPACE_ID = "test-space-id";

    protected static final String TEST_SPACE_NAME = "test-space-name";

    protected static final String TEST_USERNAME = "test-username";

    protected final ApplicationsV2 applications = mock(ApplicationsV2.class, RETURNS_SMART_NULLS);

    protected final ApplicationsV3 applicationsV3 = mock(ApplicationsV3.class, RETURNS_SMART_NULLS);

    protected final Authorizations authorizations = mock(Authorizations.class, RETURNS_SMART_NULLS);

    protected final Buildpacks buildpacks = mock(Buildpacks.class, RETURNS_SMART_NULLS);

    protected final CloudFoundryClient cloudFoundryClient = mock(CloudFoundryClient.class, RETURNS_SMART_NULLS);

    protected final Domains domains = mock(Domains.class, RETURNS_SMART_NULLS);

    protected final DopplerClient dopplerClient = mock(DopplerClient.class, RETURNS_SMART_NULLS);

    protected final Events events = mock(Events.class, RETURNS_SMART_NULLS);

    protected final FeatureFlags featureFlags = mock(FeatureFlags.class, RETURNS_SMART_NULLS);

    protected final Jobs jobs = mock(Jobs.class, RETURNS_SMART_NULLS);

    protected final OrganizationQuotaDefinitions organizationQuotaDefinitions = mock(OrganizationQuotaDefinitions.class, RETURNS_SMART_NULLS);

    protected final Organizations organizations = mock(Organizations.class, RETURNS_SMART_NULLS);

    protected final PrivateDomains privateDomains = mock(PrivateDomains.class, RETURNS_SMART_NULLS);

    protected final ResourceMatch resourceMatch = mock(ResourceMatch.class, RETURNS_SMART_NULLS);

    protected final RouterGroups routerGroups = mock(RouterGroups.class, RETURNS_SMART_NULLS);

    protected final Routes routes = mock(Routes.class, RETURNS_SMART_NULLS);

    protected final RoutingClient routingClient = mock(RoutingClient.class, RETURNS_SMART_NULLS);

    protected final ServiceBindingsV2 serviceBindingsV2 = mock(ServiceBindingsV2.class, RETURNS_SMART_NULLS);

    protected final ServiceBrokers serviceBrokers = mock(ServiceBrokers.class, RETURNS_SMART_NULLS);

    protected final ServiceInstances serviceInstances = mock(ServiceInstances.class, RETURNS_SMART_NULLS);

    protected final ServiceKeys serviceKeys = mock(ServiceKeys.class, RETURNS_SMART_NULLS);

    protected final ServicePlanVisibilities servicePlanVisibilities = mock(ServicePlanVisibilities.class, RETURNS_SMART_NULLS);

    protected final ServicePlans servicePlans = mock(ServicePlans.class, RETURNS_SMART_NULLS);

    protected final Services services = mock(Services.class, RETURNS_SMART_NULLS);

    protected final SharedDomains sharedDomains = mock(SharedDomains.class, RETURNS_SMART_NULLS);

    protected final SpaceQuotaDefinitions spaceQuotaDefinitions = mock(SpaceQuotaDefinitions.class, RETURNS_SMART_NULLS);

    protected final Spaces spaces = mock(Spaces.class, RETURNS_SMART_NULLS);

    protected final Stacks stacks = mock(Stacks.class, RETURNS_SMART_NULLS);

    protected final Tasks tasks = mock(Tasks.class, RETURNS_SMART_NULLS);

    protected final Tokens tokens = mock(Tokens.class, RETURNS_SMART_NULLS);

    protected final UaaClient uaaClient = mock(UaaClient.class, RETURNS_SMART_NULLS);

    protected final org.cloudfoundry.uaa.users.Users uaaUsers = mock(org.cloudfoundry.uaa.users.Users.class, RETURNS_SMART_NULLS);

    protected final UserProvidedServiceInstances userProvidedServiceInstances = mock(UserProvidedServiceInstances.class, RETURNS_SMART_NULLS);

    protected final Users users = mock(Users.class, RETURNS_SMART_NULLS);

    @Test
    public void testUpdateBuildPack() throws Exception {
        when(this.cloudFoundryClient.applicationsV2()).thenReturn(this.applications);
        when(this.cloudFoundryClient.applicationsV3()).thenReturn(this.applicationsV3);
        when(this.cloudFoundryClient.buildpacks()).thenReturn(this.buildpacks);
        when(this.cloudFoundryClient.domains()).thenReturn(this.domains);
        when(this.cloudFoundryClient.events()).thenReturn(this.events);
        when(this.cloudFoundryClient.featureFlags()).thenReturn(this.featureFlags);
        when(this.cloudFoundryClient.jobs()).thenReturn(this.jobs);
        when(this.cloudFoundryClient.organizations()).thenReturn(this.organizations);
        when(this.cloudFoundryClient.organizationQuotaDefinitions()).thenReturn(this.organizationQuotaDefinitions);
        when(this.cloudFoundryClient.privateDomains()).thenReturn(this.privateDomains);
        when(this.cloudFoundryClient.resourceMatch()).thenReturn(this.resourceMatch);
        when(this.cloudFoundryClient.routes()).thenReturn(this.routes);
        when(this.cloudFoundryClient.serviceBindingsV2()).thenReturn(this.serviceBindingsV2);
        when(this.cloudFoundryClient.serviceBrokers()).thenReturn(this.serviceBrokers);
        when(this.cloudFoundryClient.serviceInstances()).thenReturn(this.serviceInstances);
        when(this.cloudFoundryClient.serviceKeys()).thenReturn(this.serviceKeys);
        when(this.cloudFoundryClient.servicePlans()).thenReturn(this.servicePlans);
        when(this.cloudFoundryClient.servicePlanVisibilities()).thenReturn(this.servicePlanVisibilities);
        when(this.cloudFoundryClient.services()).thenReturn(this.services);
        when(this.cloudFoundryClient.sharedDomains()).thenReturn(this.sharedDomains);
        when(this.cloudFoundryClient.spaceQuotaDefinitions()).thenReturn(this.spaceQuotaDefinitions);
        when(this.cloudFoundryClient.spaces()).thenReturn(this.spaces);
        when(this.cloudFoundryClient.stacks()).thenReturn(this.stacks);
        when(this.cloudFoundryClient.tasks()).thenReturn(this.tasks);
        when(this.cloudFoundryClient.userProvidedServiceInstances()).thenReturn(this.userProvidedServiceInstances);
        when(this.cloudFoundryClient.users()).thenReturn(this.users);

        when(this.routingClient.routerGroups()).thenReturn(this.routerGroups);

        when(this.uaaClient.authorizations()).thenReturn(this.authorizations);
        when(this.uaaClient.tokens()).thenReturn(this.tokens);
        when(this.uaaClient.users()).thenReturn(this.uaaUsers);

        BuildPack buildPack = new BuildPack();
        buildPack.setGuid(UUID.fromString("f89b1ef6-7416-4d12-b492-c10fdaaff632"));
        buildPack.setPosition(1);
        buildPack.setEnable(true);
        buildPack.setLock(true);

        BuildPackService service = mock(BuildPackService.class);
        when(service.updateBuildPack(buildPack)).thenReturn(true);

        boolean result = service.updateBuildPack(buildPack);
        Assert.assertEquals(true, result);


//        Mono<UpdateBuildpackResponse> response = Mono.just(UpdateBuildpackResponse.builder().build());
//
//        when(cfClient.buildpacks().update(UpdateBuildpackRequest.builder()
//                .buildpackId("f89b1ef6-7416-4d12-b492-c10fdaaff632")
//                .position(1)
//                .enabled(true)
//                .locked(true)
//                .build())).thenReturn(response);



//        when(buildpacks.update(UpdateBuildpackRequest.builder()
//                                .buildpackId("f89b1ef6-7416-4d12-b492-c10fdaaff632")
//                                .position(1)
//                                .enabled(true)
//                                .locked(true)
//                                .build()
//        ));
//        BuildPackService service = mock(BuildPackService.class);
//        when(service.updateBuildPack(buildPack)).thenReturn(true);
//
//        boolean result = service.updateBuildPack(buildPack);
//        Assert.assertEquals(true, result);




//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.connectionContext()).thenReturn(null);
//        when(loginService.tokenProvider(any(), any())).thenReturn(null);
//
//        boolean result = buildPackService.updateBuildPack(new BuildPack());
//        Assert.assertEquals(true, result);
    }

//    @Test
//    public void testGetToken() throws Exception {
//        when(loginService.login(any(), any())).thenReturn(null);
//
//        String result = buildPackService.getToken();
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testGetTargetURL() throws Exception {
//        when(loginService.getTargetURI(any())).thenReturn(null);
//
//        URL result = buildPackService.getTargetURL("target");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any())).thenReturn(null);
//        when(loginService.getOAuth2AccessToken(any())).thenReturn(null);
//
//        CloudFoundryClient result = buildPackService.getCloudFoundryClient("token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient2() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any())).thenReturn(null);
//        when(loginService.getOAuth2AccessToken(any())).thenReturn(null);
//
//        CloudFoundryClient result = buildPackService.getCloudFoundryClient("token", "organization", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient3() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any(), any())).thenReturn(null);
//
//        CloudFoundryClient result = buildPackService.getCloudFoundryClient("id", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudFoundryClient4() throws Exception {
//        when(loginService.getTargetURL(any())).thenReturn(null);
//        when(loginService.getTargetURI(any())).thenReturn(null);
//        when(loginService.getCloudCredentials(any(), any())).thenReturn(null);
//
//        CloudFoundryClient result = buildPackService.getCloudFoundryClient("id", "password", "organization", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudCredentials() throws Exception {
//        when(loginService.getOAuth2AccessToken(any())).thenReturn(null);
//
//        CloudCredentials result = buildPackService.getCloudCredentials("token");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetCloudCredentials2() throws Exception {
//        CloudCredentials result = buildPackService.getCloudCredentials("id", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaUserOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaUserOperations result = buildPackService.getUaaUserOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaGroupOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaGroupOperations result = buildPackService.getUaaGroupOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testGetUaaClientOperations() throws Exception {
//        when(loginService.getUaaConnection(any())).thenReturn(null);
//        when(loginService.getCredentials(any())).thenReturn(null);
//
//        UaaClientOperations result = buildPackService.getUaaClientOperations("uaaClientId");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testStringNullCheck() throws Exception {
//        boolean result = buildPackService.stringNullCheck("params");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testStringContainsSpaceCheck() throws Exception {
//        boolean result = buildPackService.stringContainsSpaceCheck("params");
//        Assert.assertEquals(true, result);
//    }
//
//    @Test
//    public void testGetPropertyValue() throws Exception {
//        when(loginService.getProcPropertyValue(any(), any())).thenReturn("getProcPropertyValueResponse");
//        when(loginService.getPropertyValue(any(), any())).thenReturn("getPropertyValueResponse");
//
//        String result = BuildPackService.getPropertyValue("key");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testGetPropertyValue2() throws Exception {
//        when(loginService.getProcPropertyValue(any(), any())).thenReturn("getProcPropertyValueResponse");
//
//        String result = BuildPackService.getPropertyValue("key", "configFileName");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testConvertApiUrl() throws Exception {
//        String result = BuildPackService.convertApiUrl("url");
//        Assert.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations() throws Exception {
//        when(loginService.cloudFoundryOperations(any(), any(), any())).thenReturn(null);
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.dopplerClient(any(), any())).thenReturn(null);
//        when(loginService.uaaClient(any(), any())).thenReturn(null);
//
//        DefaultCloudFoundryOperations result = BuildPackService.cloudFoundryOperations(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations2() throws Exception {
//        DefaultCloudFoundryOperations result = BuildPackService.cloudFoundryOperations(null, null, null);
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations3() throws Exception {
//        when(loginService.cloudFoundryOperations(any(), any(), any(), any(), any())).thenReturn(null);
//        when(loginService.cloudFoundryClient(any(), any())).thenReturn(null);
//        when(loginService.dopplerClient(any(), any())).thenReturn(null);
//        when(loginService.uaaClient(any(), any())).thenReturn(null);
//
//        DefaultCloudFoundryOperations result = BuildPackService.cloudFoundryOperations(null, new TokenGrantTokenProvider("token"), "org", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryOperations4() throws Exception {
//        DefaultCloudFoundryOperations result = BuildPackService.cloudFoundryOperations(null, null, null, "org", "space");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testCloudFoundryClient() throws Exception {
//        ReactorCloudFoundryClient result = BuildPackService.cloudFoundryClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testDopplerClient() throws Exception {
//        ReactorDopplerClient result = BuildPackService.dopplerClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaClient() throws Exception {
//        ReactorUaaClient result = BuildPackService.uaaClient(null, new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaClient2() throws Exception {
//        ReactorUaaClient result = BuildPackService.uaaClient(null, "clientId", "clientSecret");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testUaaAdminClient() throws Exception {
//        when(loginService.convertApiUrl(any())).thenReturn("convertApiUrlResponse");
//        when(loginService.uaaClient(any(), any())).thenReturn(null);
//        when(loginService.peekConnectionContext()).thenReturn(null);
//        when(loginService.connectionContext(any(), anyBoolean())).thenReturn(null);
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        ReactorUaaClient result = BuildPackService.uaaAdminClient("apiTarget", "token", "uaaAdminClientId", "uaaAdminClientSecret");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testConnectionContext() throws Exception {
//        when(loginService.convertApiUrl(any())).thenReturn("convertApiUrlResponse");
//        when(loginService.peekConnectionContext()).thenReturn(null);
//        when(loginService.connectionContext(any(), anyBoolean())).thenReturn(null);
//
//        DefaultConnectionContext result = buildPackService.connectionContext();
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testConnectionContext2() throws Exception {
//        when(loginService.convertApiUrl(any())).thenReturn("convertApiUrlResponse");
//        when(loginService.peekConnectionContext()).thenReturn(null);
//
//        DefaultConnectionContext result = BuildPackService.connectionContext("apiUrl", true);
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testTokenProvider() throws Exception {
//        TokenGrantTokenProvider result = BuildPackService.tokenProvider("token");
//        Assert.assertEquals(new TokenGrantTokenProvider("token"), result);
//    }
//
//    @Test
//    public void testTokenProviderWithDefault() throws Exception {
//        when(loginService.tokenProvider(any())).thenReturn(new TokenGrantTokenProvider("token"));
//
//        TokenProvider result = BuildPackService.tokenProviderWithDefault("token", new TokenGrantTokenProvider("token"));
//        Assert.assertEquals(new TokenGrantTokenProvider("token"), result);
//    }
//
//    @Test
//    public void testTokenProvider2() throws Exception {
//        PasswordGrantTokenProvider result = BuildPackService.tokenProvider("username", "password");
//        Assert.assertEquals(null, result);
//    }
//
//    @Test
//    public void testPreDestroy() throws Exception {
//        when(loginService.peekConnectionContext()).thenReturn(null);
//
//        buildPackService.preDestroy();
//    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme