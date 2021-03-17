package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.client.lib.org.codehaus.jackson.map.ObjectMapper;
import org.cloudfoundry.client.lib.org.codehaus.jackson.type.TypeReference;
import org.cloudfoundry.client.v2.OrderDirection;
import org.cloudfoundry.client.v2.applications.*;
import org.cloudfoundry.client.v2.events.ListEventsRequest;
import org.cloudfoundry.client.v2.events.ListEventsResponse;
import org.cloudfoundry.client.v2.routemappings.CreateRouteMappingRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteRequest;
import org.cloudfoundry.client.v2.routes.CreateRouteResponse;
import org.cloudfoundry.client.v2.routes.DeleteRouteRequest;
import org.cloudfoundry.client.v2.routes.Route;
import org.cloudfoundry.client.v2.servicebindings.CreateServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.DeleteServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.DeleteServiceBindingResponse;
import org.cloudfoundry.client.v2.servicebindings.ServiceBindingResource;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstanceServiceBindingsRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstanceServiceBindingsResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.*;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.LogMessage;
import org.cloudfoundry.doppler.RecentLogsRequest;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.applications.LogsRequest;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppServiceV2 extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceV2.class);


    public SummaryApplicationResponse getAppSummary(String guid, String token) {
        SummaryApplicationResponse summaryApplicationResponse = cloudFoundryClient(tokenProvider(token)).applicationsV2().summary(SummaryApplicationRequest.builder().applicationId(guid).build()).block();
        return summaryApplicationResponse;
    }


    /**
     * 앱 실시간 상태를 조회한다.
     *
     * @param guid  the app guid
     * @param token the client
     * @return the app stats
     */
    public ApplicationStatisticsResponse getAppStats(String guid, String token) {
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

        ApplicationStatisticsResponse applicationStatisticsResponse = cloudFoundryClient.applicationsV2().statistics(ApplicationStatisticsRequest.builder().applicationId(guid).build()).block();

        return applicationStatisticsResponse;
    }

    /**
     * 앱을 변경한다.
     *
     * @param app   the app
     * @param token the client
     * @throws Exception the exception
     */
    public Map renameApp(App app, String token) {
        HashMap result = new HashMap();
        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            UpdateApplicationResponse response = cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).name(app.getNewName()).build()).block();

            //LOGGER.info("Update app response :", response);

            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }

        return result;

    }


    /**
     * 앱을 실행한다.
     *
     * @param app   the app
     * @param token the client
     * @throws Exception the exception
     */
    public Map startApp(App app, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

            cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).state("STARTED").build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e.getMessage());
        }

        return resultMap;
    }


    /**
     * 앱을 중지한다.
     *
     * @param app   the app
     * @param token the client
     * @throws Exception the exception
     */
    public Map stopApp(App app, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

            cloudFoundryClient.applicationsV3().stop(org.cloudfoundry.client.v3.applications.StopApplicationRequest.builder().applicationId(app.getGuid().toString()).build()).block();
            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }


    /**
     * 앱을 삭제한다.
     *
     * @param guid the app
     * @throws Exception the exception
     */
    public Map deleteApp(String guid) {
        HashMap result = new HashMap();
        try {
            //앱 삭제
            ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient();
            try {
                ListApplicationServiceBindingsResponse listApplicationServiceBindingsResponse = reactorCloudFoundryClient.applicationsV2().listServiceBindings(ListApplicationServiceBindingsRequest.builder().applicationId(guid).build()).block();
                for (ServiceBindingResource resource : listApplicationServiceBindingsResponse.getResources()) {
                    reactorCloudFoundryClient.serviceBindingsV2().delete(DeleteServiceBindingRequest.builder().serviceBindingId(resource.getMetadata().getId()).build()).block();
                }
            } catch (Exception e) {

            }
            List<Route> routes = reactorCloudFoundryClient.applicationsV2().summary(SummaryApplicationRequest.builder().applicationId(guid).build()).block().getRoutes();
            for (Route route : routes) {
                reactorCloudFoundryClient.routes().delete(DeleteRouteRequest.builder().routeId(route.getId()).build()).block();
            }
            reactorCloudFoundryClient.applicationsV2().delete(DeleteApplicationRequest.builder().applicationId(guid).build()).block();
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }


    /**
     * 앱을 리스테이징한다.
     *
     * @param app   the app
     * @param token the client
     * @the exception
     */
    public Map restageApp(App app, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

            cloudFoundryClient.applicationsV2().restage(RestageApplicationRequest.builder().applicationId(app.getGuid().toString()).build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    /**
     * 앱 인스턴스를 변경한다.
     *
     * @param app   the app
     * @param token the client
     * @the exception
     */
    public Map updateApp(App app, String token) {
        Map resultMap = new HashMap();
        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
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
            } else if (app.getEnvironment() != null && app.getEnvironment().size() == 0) {
                cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(app.getGuid().toString()).environmentJsons(new HashMap<>()).build()).block();
            }

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    /**
     * 앱-서비스를 바인드한다.
     *
     * @param body
     * @param token the client
     * @the exception
     */
    public Map bindService(Map body, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> parameterMap = mapper.readValue(body.get("parameter").toString(), new TypeReference<Map<String, Object>>() {
            });

            cloudFoundryClient.serviceBindingsV2().create(CreateServiceBindingRequest.builder().applicationId(body.get("applicationId").toString()).serviceInstanceId(body.get("serviceInstanceId").toString()).parameters(parameterMap).build()).block();

            resultMap.put("result", true);

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }


    /**
     * 앱-서비스를 언바인드한다.
     *
     * @param serviceInstanceId
     * @param applicationId
     * @param token             the client
     * @the exception
     */
    public Map unbindService(String serviceInstanceId, String applicationId, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

            ListServiceInstanceServiceBindingsResponse listServiceInstanceServiceBindingsResponse = cloudFoundryClient.serviceInstances().listServiceBindings(ListServiceInstanceServiceBindingsRequest.builder().applicationId(applicationId).serviceInstanceId(serviceInstanceId).build()).block();
            String instancesServiceBindingGuid = listServiceInstanceServiceBindingsResponse.getResources().get(0).getMetadata().getId();

            DeleteServiceBindingResponse deleteServiceBindingResponse = cloudFoundryClient.serviceBindingsV2().delete(DeleteServiceBindingRequest.builder().serviceBindingId(instancesServiceBindingGuid).build()).block();

            resultMap.put("result", true);

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    /**
     * 앱-유저 프로바이드 서비스를 언바인드한다.
     *
     * @param serviceInstanceId
     * @param applicationId
     * @param token             the client
     * @the exception
     */
    public Map unbindUserProvideService(String serviceInstanceId, String applicationId, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            ListUserProvidedServiceInstanceServiceBindingsResponse listUserProvidedServiceInstanceServiceBindingsResponse = cloudFoundryClient.userProvidedServiceInstances().listServiceBindings(ListUserProvidedServiceInstanceServiceBindingsRequest.builder().applicationId(applicationId).userProvidedServiceInstanceId(serviceInstanceId).build()).block();
            String instancesUserProvidedServiceBindingGuid = listUserProvidedServiceInstanceServiceBindingsResponse.getResources().get(0).getMetadata().getId();
            DeleteServiceBindingResponse deleteServiceBindingResponse = cloudFoundryClient.serviceBindingsV2().delete(DeleteServiceBindingRequest.builder().serviceBindingId(instancesUserProvidedServiceBindingGuid).build()).block();
            resultMap.put("result", true);

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }


    /**
     * 앱 이벤트를 조회한다.
     *
     * @param guid
     * @param token the client
     * @return the app events
     * @the exception
     */
    public ListEventsResponse getAppEvents(String guid, String token) {
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

        ListEventsRequest.Builder requestBuilder = ListEventsRequest.builder().actee(guid).resultsPerPage(100).orderDirection(OrderDirection.DESCENDING);
        ListEventsResponse listEventsResponse = cloudFoundryClient.events().list(requestBuilder.build()).block();

        return listEventsResponse;
    }

    /**
     * 앱 환경변수를 조회한다.
     *
     * @param guid
     * @param token the token
     * @return the application env
     * @the exception
     * @version 1.0
     * @since 2016.6.29 최초작성
     */
    public ApplicationEnvironmentResponse getApplicationEnv(String guid, String token) {
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

        ApplicationEnvironmentResponse applicationEnvironmentResponse = cloudFoundryClient.applicationsV2().environment(ApplicationEnvironmentRequest.builder().applicationId(guid).build()).block();

        return applicationEnvironmentResponse;
    }

    /**
     * 라우트 추가 및 라우트와 앱을 연결한다. (앱에 URI를 추가함)
     *
     * @param body
     * @param token the token
     * @return the boolean
     * @the exception
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    public Map addApplicationRoute(Map body, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

            CreateRouteResponse createRouteResponse = cloudFoundryClient.routes().create(CreateRouteRequest.builder().host(body.get("host").toString()).domainId(body.get("domainId").toString()).spaceId(body.get("spaceId").toString()).build()).block();

            cloudFoundryClient.routeMappings().create(CreateRouteMappingRequest.builder().applicationId(body.get("applicationId").toString()).routeId(createRouteResponse.getMetadata().getId()).build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    /**
     * 앱 라우트를 해제한다.
     *
     * @param guid
     * @param route_guid
     * @param token      the token
     * @return the boolean
     * @the exception
     * @version 1.0
     * @since 2016.7.6 최초작성
     */
    public Map removeApplicationRoute(String guid, String route_guid, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

            cloudFoundryClient.applicationsV2().removeRoute(RemoveApplicationRouteRequest.builder().applicationId(guid).routeId(route_guid).build()).block();

            cloudFoundryClient.routes().delete(DeleteRouteRequest.builder().routeId(route_guid).build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    /**
     * 인덱스로 앱 인스턴스를 종료한다.
     *
     * @param guid
     * @param index
     * @param token
     * @return the map
     * @the exception
     */
    public Map terminateInstance(String guid, String index, String token) {
        Map resultMap = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

            TerminateApplicationInstanceRequest.Builder requestBuilder = TerminateApplicationInstanceRequest.builder();
            requestBuilder.applicationId(guid);
            requestBuilder.index(index);
            cloudFoundryClient.applicationsV2().terminateInstance(requestBuilder.build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    public List<Envelope> getRecentLog(String guid, String token) {
        TokenProvider tokenProvider = tokenProvider(token);
        ReactorDopplerClient reactorDopplerClient = dopplerClient(connectionContext(), tokenProvider);

        RecentLogsRequest.Builder requestBuilder = RecentLogsRequest.builder();
        requestBuilder.applicationId(guid);

        List<Envelope> getRecentLog = reactorDopplerClient.recentLogs(requestBuilder.build()).collectList().block();
        return getRecentLog;
    }


    public Map userProvideCredentials(String guid, String token) {
        Map resultMap = new HashMap();
        ArrayList resultlist = new ArrayList();
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

        GetUserProvidedServiceInstanceResponse getUserProvidedServiceInstanceResponse = cloudFoundryClient.userProvidedServiceInstances().get(GetUserProvidedServiceInstanceRequest.builder().userProvidedServiceInstanceId(guid).build()).block();
        String str = getUserProvidedServiceInstanceResponse.getEntity().getCredentials().toString();
        str = str.replace("{", "");
        str = str.replace("}", "");
        str = str.replace(" ", "");
        String[] str2 = str.split(",");
        for (String strs : str2) {
            Map listStr = new HashMap();
            String[] str3 = strs.split("=");
            listStr.put("key", str3[0]);
            listStr.put("value", str3[1]);
            resultlist.add(listStr);
        }
        resultMap.put("List", resultlist);
        return resultMap;
    }

}
