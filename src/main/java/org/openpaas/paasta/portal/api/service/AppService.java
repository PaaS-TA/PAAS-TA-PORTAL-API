package org.openpaas.paasta.portal.api.service;


import com.corundumstudio.socketio.SocketIOClient;
import org.cloudfoundry.client.lib.org.codehaus.jackson.map.ObjectMapper;
import org.cloudfoundry.client.lib.org.codehaus.jackson.type.TypeReference;
import org.cloudfoundry.client.v2.applications.*;
import org.cloudfoundry.client.v2.events.ListEventsRequest;
import org.cloudfoundry.client.v2.events.ListEventsResponse;
import org.cloudfoundry.client.v2.routemappings.CreateRouteMappingRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteResponse;
import org.cloudfoundry.client.v2.routes.DeleteRouteRequest;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.DeleteServiceBindingRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstanceServiceBindingsRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstanceServiceBindingsResponse;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.LogMessage;
import org.cloudfoundry.doppler.RecentLogsRequest;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.applications.DeleteApplicationRequest;
import org.cloudfoundry.operations.applications.*;
import org.cloudfoundry.operations.applications.RestageApplicationRequest;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.openpaas.paasta.portal.api.mapper.cc.AppCcMapper;
//import org.openpaas.paasta.portal.api.mapper.portal.AppMapper;

/**
 * 앱 서비스 - 애플리케이션 정보 조회, 구동, 정지 등의 API 를 호출 하는 서비스이다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Service
public class AppService extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

//    @Autowired
//    private AppCcMapper appCcMapper;

//    @Autowired
//    private AppMapper appMapper;

    @Autowired
    private AppAutoScaleModalService appAutoScaleModalService;

    @Autowired
    ReactorCloudFoundryClient reactorCloudFoundryClient;

    //ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));
    //DefaultCloudFoundryOperations cloudFoundryOperations  = cloudFoundryOperations(connectionContext(),tokenProvider(token));
    //DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());

    public SummaryApplicationResponse getAppSummary(String guid, String token) throws IOException{

        //SummaryApplicationResponse summaryApplicationResponse = cloudFoundryClient.applicationsV2().summary(SummaryApplicationRequest.builder().applicationId(app.getGuid().toString()).build()).block();
        // 로그 추가(이후 .log() 제거)
        SummaryApplicationResponse summaryApplicationResponse = Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .applicationsV2()
                .summary(SummaryApplicationRequest.builder()
                        .applicationId(guid)
                        .build())
                .log()
                .block();

        return summaryApplicationResponse;
    }


    /**
     * 앱 실시간 상태를 조회한다.
     *
     * @param guid   the app guid
     * @param token the client
     * @return the app stats
     */
    public ApplicationStatisticsResponse getAppStats(String guid, String token) {
        ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));
        //ApplicationStatisticsResponse applicationStatisticsResponse = cloudFoundryClient.applicationsV2().statistics(ApplicationStatisticsRequest.builder().applicationId(app.getGuid().toString()).build()).block();

        ApplicationStatisticsResponse applicationStatisticsResponse =
                cloudFoundryClient.applicationsV2()
                .statistics(ApplicationStatisticsRequest.builder()
                        .applicationId(guid)
                        .build()).block();

        return applicationStatisticsResponse;
    }


    /**
     * 앱을 변경한다.
     *
     * @param app   the app
     * @param token the client
     * @throws Exception the exception
     */
    public void renameApp(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().rename(RenameApplicationRequest.builder().name(app.getName()).newName(app.getNewName()).build());
    }


    /**
     * 앱을 실행한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void startApp(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().start(StartApplicationRequest.builder().name(app.getName()).build());
    }


    /**
     * 앱을 중지한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void stopApp(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().stop(StopApplicationRequest.builder().name(app.getName()).build());
    }


    /**
     * 앱을 삭제한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void deleteApp(App app, String token) throws Exception {

        //앱 삭제
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().delete(DeleteApplicationRequest.builder().name(app.getName()).build());
        //AutoScale 설정 삭제
        try {
            HashMap map = new HashMap();
            map.put("guid", String.valueOf(app.getGuid()));
            if (null != appAutoScaleModalService.getAppAutoScaleInfo(map).get("list")) {
                appAutoScaleModalService.deleteAppAutoScale(String.valueOf(app.getGuid()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 앱을 리스테이징한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void restageApp(App app, String token) throws Exception {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token),app.getOrgName(),app.getSpaceName());
        cloudFoundryOperations.applications().restage(RestageApplicationRequest.builder().name(app.getName()).build());
    }

    /**
     * 앱 인스턴스를 변경한다.
     *
     * @param app    the app
     * @param token the client
     * @throws Exception the exception
     */
    public void updateApp(App app, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));
        if (app.getInstances() > 0) {
            cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).instances(app.getInstances()).build()).block();
        }
        if (app.getMemory() > 0) {
            cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).memory(app.getMemory()).build()).block();
        }
        if (app.getDiskQuota() > 0) {
            cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).diskQuota(app.getDiskQuota()).build()).block();
        }
        if (app.getName() != null && !app.getName().equals("")) {
            cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).name(app.getName()).build()).block();
        }
        if (app.getEnvironment() != null && app.getEnvironment().size() > 0) {
            cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).environmentJsons(app.getEnvironment()).build()).block();
        }
    }

    /**
     * 앱-서비스를 바인드한다.
     *
     * @param body
     * @param token the client
     * @throws Exception the exception
     */
    public void bindService(Map body, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> parameterMap = mapper.readValue(body.get("parameter").toString(), new TypeReference<Map<String, Object>>() {
        });

        cloudFoundryClient.serviceBindingsV2()
                .create(CreateServiceBindingRequest.builder()
                        .applicationId(body.get("applicationId").toString())
                        .serviceInstanceId(body.get("serviceInstanceId").toString())
                        .parameters(parameterMap)
                        .build()
                ).block();

//        DefaultCloudFoundryOperations cloudFoundryOperations  = cloudFoundryOperations(connectionContext(),tokenProvider(token),app.getOrgName(),app.getSpaceName());
//        cloudFoundryOperations.services().bind(BindServiceInstanceRequest.builder().applicationName(app.getName()).serviceInstanceName(app.getServiceName()).build());
    }


    /**
     * 앱-서비스를 언바인드한다.
     *
     * @param serviceInstanceId
     * @param applicationId
     * @param token the client
     * @throws Exception the exception
     */
    public void unbindService(String serviceInstanceId, String applicationId, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));

        ListServiceInstanceServiceBindingsResponse listServiceInstanceServiceBindingsResponse =
            cloudFoundryClient.serviceInstances()
                    .listServiceBindings(ListServiceInstanceServiceBindingsRequest.builder()
                            .applicationId(applicationId)
                            .serviceInstanceId(serviceInstanceId)
                            .build()
                    ).block();

        String instancesServiceBindingGuid = listServiceInstanceServiceBindingsResponse.getResources().get(0).getMetadata().getId();

        cloudFoundryClient.serviceBindingsV2()
                .delete(DeleteServiceBindingRequest.builder()
                        .serviceBindingId(instancesServiceBindingGuid)
                        .build()
                ).block();

//        DefaultCloudFoundryOperations cloudFoundryOperations  = cloudFoundryOperations(connectionContext(),tokenProvider(token),app.getOrgName(),app.getSpaceName());
//        cloudFoundryOperations.services().unbind(UnbindServiceInstanceRequest.builder().applicationName(app.getName()).serviceInstanceName(app.getServiceName()).build());
    }

    /**
     * 앱 이벤트를 조회한다.
     *
     * @param guid
     * @param token the client
     * @return the app events
     * @throws Exception the exception
     */
    public ListEventsResponse getAppEvents(String guid, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient  = Common.cloudFoundryClient(connectionContext(), tokenProvider(token));

        ListEventsRequest.Builder requestBuilder = ListEventsRequest.builder().actee(guid);
        ListEventsResponse listEventsResponse = cloudFoundryClient.events().list(requestBuilder.build()).block();

        return listEventsResponse;
    }

    /**
     * 앱 환경변수를 조회한다.
     *
     * @param guid
     * @param token the token
     * @return the application env
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.6.29 최초작성
     */
    public ApplicationEnvironmentResponse getApplicationEnv(String guid, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient  = Common.cloudFoundryClient(connectionContext(), tokenProvider(token));

        ApplicationEnvironmentResponse applicationEnvironmentResponse =
                cloudFoundryClient.applicationsV2()
                        .environment(ApplicationEnvironmentRequest.builder()
                                .applicationId(guid)
                                .build()
                        ).block();

        return applicationEnvironmentResponse;
    }

    /**
     * 앱 환경변수 중 사용자 정의 환경변수를 추가,수정한다.
     *
     * @param app   the app
     * @param token the token
     * @return the boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.6.30 최초작성
     */
    public boolean updateApplicationEnv(App app, String token) throws Exception {
//        String orgName = app.getOrgName();
//        String spaceName = app.getSpaceName();
//        String appName = app.getName();
//        Map<String, String> appEnvironment = app.getEnvironment();
//
//        if (!stringNullCheck(orgName, spaceName, appName) || appEnvironment == null) {
//            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
//        }
//
//        CloudFoundryClient client = getCloudFoundryClient(token, orgName, spaceName);
//
//        client.updateApplicationEnv(appName, appEnvironment);

        return true;

    }


    /**
     * 앱 라우트를 조회한다.
     *
     * @param domainName,client
     * @return map
     * @author 김도준
     * @version 1.0
     * @since 2016.7.5 최초작성
     */
/*
    public List<CloudRoute> getRoute(String domainName, CloudFoundryClient client) throws Exception {

        List<CloudRoute> routes = client.getRoutes(domainName);

        return routes;
    }
*/


    /**
     * 라우트 추가 및 라우트와 앱을 연결한다. (앱에 URI를 추가함)
     *
     * @param body
     * @param token the token
     * @return the boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    public boolean addApplicationRoute(Map body, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient  = Common.cloudFoundryClient(connectionContext(), tokenProvider(token));
//        ReactorDopplerClient reactorDopplerClient  = Common.dopplerClient(connectionContext(), tokenProvider(token));


        CreateRouteResponse createRouteResponse =
                cloudFoundryClient.routes()
                        .create(CreateRouteRequest.builder()
                                .host(body.get("host").toString())
                                .domainId(body.get("domainId").toString())
                                .spaceId(body.get("spaceId").toString())
                                .build()
                        ).block();

        cloudFoundryClient.routeMappings()
                .create(CreateRouteMappingRequest.builder()
                        .applicationId(body.get("applicationId").toString())
                        .routeId(createRouteResponse.getMetadata().getId())
                        .build()
                ).block();


//        cloudFoundryClient.routes()
//                .associateApplication(AssociateRouteApplicationRequest.builder()
//                        .applicationId("")
//                        .routeId("")
//                        .build()
//                ).block();

//                AssociateApplicationRouteResponse associateApplicationRouteResponse =
//                cloudFoundryClient.applicationsV2()
//                        .associateRoute(AssociateApplicationRouteRequest.builder()
//                                .applicationId("")
//                                .routeId("")
//                                .build()
//                        ).block();


//        String orgName = app.getOrgName();
//        String spaceName = app.getSpaceName();
//        String appName = app.getName();
//        String host = app.getHost();
//        String domainName = app.getDomainName();
//
//        if (!stringNullCheck(orgName, spaceName, appName, host, domainName)) {
//            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
//        }
//
//        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);
//
//        client.bindRoute(host, domainName, appName);

        return true;
    }

    /**
     * 앱 라우트를 해제한다.
     *
     * @param guid
     * @param route_guid
     * @param token the token
     * @return the boolean
     * @throws Exception the exception
     * @author 김도준
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    public boolean removeApplicationRoute(String guid, String route_guid, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient  = Common.cloudFoundryClient(connectionContext(), tokenProvider(token));

        cloudFoundryClient.applicationsV2()
                .removeRoute(
                        RemoveApplicationRouteRequest.builder()
                        .applicationId(guid)
                        .routeId(route_guid)
                        .build()
                ).block();

        cloudFoundryClient.routes()
                .delete(DeleteRouteRequest.builder()
                        .routeId(route_guid)
                        .build()
                ).block();


//        cloudFoundryClient.routeMappings()
//                .delete(DeleteRouteMappingRequest.builder()
//                        .routeMappingId(body.get("routeMappingId").toString())
//                        .build()
//                ).block();
//
//        cloudFoundryClient.routes()
//                .delete(DeleteRouteRequest.builder()
//                        .routeId(body.get("routeId").toString())
//                        .build()
//                ).block();


//        String orgName = app.getOrgName();
//        String spaceName = app.getSpaceName();
//        String appName = app.getName();
//        String host = app.getHost();
//        String domainName = app.getDomainName();
//
//        if (!stringNullCheck(orgName, spaceName, appName, host, domainName)) {
//            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
//        }
//
//        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);
//
//        client.unbindRoute(host, domainName, appName);
//        client.deleteRoute(host, domainName);

        return true;
    }

    /**
     * 라우트를 삭제한다.
     *
     * @param orgName   the org name
     * @param spaceName the space name
     * @param urls      the urls
     * @param token     the token
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean deleteRoute(String orgName, String spaceName, List<String> urls, String token) throws Exception {
        return true;
    }

    /**
     * 인덱스로 앱 인스턴스를 종료한다.
     *
     * @param guid
     * @param index
     * @param token
     * @return the map
     * @throws Exception the exception
     */
    public void terminateInstance(String guid, String index, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));

        TerminateApplicationInstanceRequest.Builder requestBuilder = TerminateApplicationInstanceRequest.builder();
        requestBuilder.applicationId(guid);
        requestBuilder.index(index);
        cloudFoundryClient.applicationsV2().terminateInstance(requestBuilder.build()).block();

//        CustomCloudFoundryClient customCloudFoundryClient = getCustomCloudFoundryClient(req.getHeader(AUTHORIZATION_HEADER_KEY), param.getOrgName(), param.getSpaceName());
//        customCloudFoundryClient.terminateAppInstanceByIndex(param.getGuid(), param.getAppInstanceIndex(), param.getOrgName(), param.getSpaceName());
    }


    /**
     * 앱 이미지 URL를 조회한다.
     *
     * @param app the app
     * @return the app image url
     */
    public String getAppImageUrl(App app) {

//        String buildPack = appCcMapper.getAppBuildPack(String.valueOf(app.getGuid()));
        String buildPack = "";
//        String appImageUrl = appMapper.getAppImageUrl(buildPack);
        String appImageUrl = "";
        return appImageUrl;

    }


//    public List<ApplicationSummary> getAppSummery(DefaultCloudFoundryOperations cloudFoundryOperations) {
//        return cloudFoundryOperations.applications().list().collectList().block();
//    }
//
//    public ApplicationDetail getAppDetail(DefaultCloudFoundryOperations cloudFoundryOperations, String appName) {
//        return cloudFoundryOperations.applications().get(GetApplicationRequest.builder().name(appName).build()).block();
//    }
//
//    public Flux<Envelope> getRecentLog(ReactorDopplerClient reactorDopplerClient, String appId) {
//
//        RecentLogsRequest recentLogsRequest = RecentLogsRequest.builder().applicationId(appId).build();
//
//        Flux<Envelope> getRecentLog = reactorDopplerClient.recentLogs(recentLogsRequest);
//
//        return getRecentLog;
//    }

    public List<Envelope> getRecentLog(String guid, String token) {
        TokenProvider tokenProvider = tokenProvider(token);
        ReactorDopplerClient reactorDopplerClient  = Common.dopplerClient(connectionContext(), tokenProvider);

        RecentLogsRequest.Builder requestBuilder = RecentLogsRequest.builder();
        requestBuilder.applicationId(guid);

        List<Envelope> getRecentLog = reactorDopplerClient.recentLogs(requestBuilder.build()).collectList().block();
        return getRecentLog;
    }

    public List<LogMessage> getTailLog(String guid, String token) {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token), "demo.org", "dev");
//        CloudFoundryOperations cloudFoundryOperations  = cloudFoundryOperations(connectionContext(),tokenProvider(token));

//        List<LogMessage> getTailLog = cloudFoundryOperations.applications()
//                .logs(LogsRequest.builder()
//                        .name("github-test-app2")
//                        .build()
//                ).collectList().block();

        cloudFoundryOperations.applications()
                .logs(LogsRequest.builder()
                        .name("github-test-app2")
                        .build()
                ).subscribe((msg) -> {
                    printLog(msg);
                },
                (error) -> {
                    error.printStackTrace();
                }
        );

        return null;
    }
    private void printLog(LogMessage msg) {
        LOGGER.info(" ["+msg.getSourceType()+"/"+msg.getSourceInstance()+"] ["+msg.getMessageType()+msg.getMessageType()+"] "+msg.getMessage());
//        System.out.println(
//                new StringBuffer()
//                        .append(new Timestamp(msg.getTimestamp()/1000000).toLocalDateTime())
//                        .append(" [")
//                        .append(msg.getSourceType())
//                        .append("/")
//                        .append(msg.getSourceInstance())
//                        .append("] [")
//                        .append(msg.getMessageType())
//                        .append("] ")
//                        .append(msg.getMessage())
//        );
    }

    public SocketIOClient socketTailLogs(SocketIOClient client, String appName, String orgName, String spaceName, String token) {
        DefaultCloudFoundryOperations cloudFoundryOperations = cloudFoundryOperations(connectionContext(), tokenProvider(token), orgName, spaceName);

        cloudFoundryOperations.applications()
                .logs(LogsRequest.builder()
                        .name(appName)
                        .build()
                ).subscribe((msg) -> {
                    printLog(msg);
                    client.sendEvent("message", " ["+msg.getSourceType()+"/"+msg.getSourceInstance()+"] ["+msg.getMessageType()+msg.getMessageType()+"] "+msg.getMessage());
                },
                (error) -> {
                    error.printStackTrace();
                }
        );
        return client;
    }

}
