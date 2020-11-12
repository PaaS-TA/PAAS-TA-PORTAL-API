package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.client.v2.servicebindings.GetServiceBindingRequest;
import org.cloudfoundry.client.v2.servicebindings.GetServiceBindingResponse;
import org.cloudfoundry.client.v2.servicebrokers.*;
import org.cloudfoundry.client.v2.serviceinstances.DeleteServiceInstanceRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.serviceinstances.UpdateServiceInstanceRequest;
import org.cloudfoundry.client.v2.serviceplans.*;
import org.cloudfoundry.client.v2.serviceplanvisibilities.*;
import org.cloudfoundry.client.v2.services.DeleteServiceRequest;
import org.cloudfoundry.client.v2.services.GetServiceRequest;
import org.cloudfoundry.client.v2.services.ListServicesRequest;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Service;
import org.openpaas.paasta.portal.api.model.ServiceBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

@EnableAsync
@org.springframework.stereotype.Service
public class ServiceServiceV2 extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceServiceV2.class);


    /**
     * 서비스 인스턴스 이름을 변경한다.
     *
     * @param service the service
     * @return the boolean
     * @throws Exception the exception
     */
    public Map renameInstance(Service service, String guid) throws Exception {
        HashMap result = new HashMap();

        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient();
            cloudFoundryClient.serviceInstances().update(UpdateServiceInstanceRequest.builder().serviceInstanceId(guid).name(service.getNewName()).build()).block();
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
     * 서비스 인스턴스를 삭제한다.
     *
     * @param guid the service
     * @throws Exception the exception
     */
    public Map deleteInstance(String guid, String token) throws Exception {
        HashMap result = new HashMap();
        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            cloudFoundryClient.serviceInstances().delete(DeleteServiceInstanceRequest.builder().serviceInstanceId(guid).build()).block();
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e) {
            result.put("result", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 유저 프로바이드 서비스를 조회한다.
     *
     * @param token the token
     * @return boolean user provided
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.8.4 최초작성
     */
    public GetUserProvidedServiceInstanceResponse getUserProvided(String token, String userProvidedServiceInstanceId) throws Exception {
        try {
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));

            GetUserProvidedServiceInstanceResponse getUserProvidedServiceInstanceResponse = cloudFoundryClient.userProvidedServiceInstances().get(GetUserProvidedServiceInstanceRequest.builder().userProvidedServiceInstanceId(userProvidedServiceInstanceId).build()).block();

            LOGGER.info("Created ::: " + getUserProvidedServiceInstanceResponse.getEntity().getName());
            LOGGER.info("Credentials ::: " + getUserProvidedServiceInstanceResponse.getEntity().getCredentials().toString());
            LOGGER.info("SyslogDrainUrl ::: " + getUserProvidedServiceInstanceResponse.getEntity().getSyslogDrainUrl());

            return getUserProvidedServiceInstanceResponse;

        } catch (Exception e) {
            return null;
        }
    }


    /**
     * credentialsStr parsing => map
     *
     * @throws Exception the exception
     * @version 2.0
     * @since 2018.6.18 최초작성
     */

    private Map parsing(String str) {

        Map returnMap = new HashMap();
        if (str != null) {
            str = str.replace("{", "").replace("}", "");
            String datas[] = str.split(",");
            for (String data : datas) {
                String credentials[] = data.replace("\"", "").split(":");
                if (credentials.length > 0) {
                    String param = credentials[0];
                    String value = credentials[1];
                    returnMap.put(param, value);
                }
            }
        }
        return returnMap;
    }


    /**
     * 유저프로바이드 서비스 인스턴스를 생성한다.
     *
     * @param token   the token
     * @param service the body
     * @return boolean boolean
     * @throws Exception the exception
     * @author CSJ
     * @version 2.0
     * @since 2018.5.30 최초작성
     */
    public Map createUserProvided(String token, Service service) throws Exception {
        HashMap result = new HashMap();
        Map<String, Object> map = new HashMap<>();
        LOGGER.info(service.getRouteServiceUrl());
        try {
            //Todo credentialsStr parsing => map

            if (!service.getCredentials().equals("") && !service.getCredentials().equals("{}")) {
                map = parsing(service.getCredentials());
            }
            String orgName = service.getOrgName();//("orgName");
            String spaceName = service.getSpaceName();//("spaceName");
            String serviceInstanceName = service.getServiceInstanceName();//("serviceInstanceName");
            String syslogDrainUrl = service.getSyslogDrainUrl();//("syslogDrainUrl");
            String routeServiceUrl = service.getRouteServiceUrl();
            String tag = service.getTags().replaceAll(" ", "");
            String[] tags = tag.split(",");

            if (!stringNullCheck(orgName, spaceName, serviceInstanceName, service.getCredentials().toString())) {
            }

            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            CreateUserProvidedServiceInstanceResponse createUserProvidedServiceInstanceResponse = cloudFoundryClient.userProvidedServiceInstances().create(CreateUserProvidedServiceInstanceRequest.builder().name(serviceInstanceName).spaceId(String.valueOf(service.getSpaceGuid())).credentials(map).syslogDrainUrl(syslogDrainUrl).routeServiceUrl(routeServiceUrl).tags(tags).build()).block();


            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("Error :::: " + e.getMessage());
            result.put("result", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }


    /**
     * 유저프로바이드 서비스 인스턴스를 수정한다.
     *
     * @param token   the token
     * @param service the body
     * @return boolean boolean
     * @throws Exception the exception
     * @author CSJ
     * @version 2.0
     * @since 2018.5.30 최초작성
     */
    public Map updateUserProvided(String guid, String token, Service service) throws Exception {

        HashMap result = new HashMap();
        Map<String, Object> map = new HashMap<>();
        try {
            //Todo credentialsStr parsing => map
            if (!service.getCredentials().equals("") && !service.getCredentials().equals("{}")) {
                map = parsing(service.getCredentials());
            }
            String syslogDrainUrl = service.getSyslogDrainUrl();
            String tag = service.getTags().replaceAll(" ", "");
            String[] tags = tag.split(",");

            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
            UpdateUserProvidedServiceInstanceResponse updateUserProvidedServiceInstanceResponse = cloudFoundryClient.userProvidedServiceInstances().update(UpdateUserProvidedServiceInstanceRequest.builder().name(service.getServiceInstanceName()).userProvidedServiceInstanceId(guid).credentials(map).syslogDrainUrl(syslogDrainUrl).routeServiceUrl(service.getRouteServiceUrl()).tags(tags).build()).block();


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
     * 서비스 브로커 리스트를 조회한다.
     *
     * @param token token
     * @return the boolean
     * @throws Exception the exception
     */
    public ListServiceBrokersResponse getServiceBrokers(String token) throws Exception {
        return cloudFoundryClient().serviceBrokers().list(ListServiceBrokersRequest.builder().build()).block();
    }


    /**
     * 서비스 브로커를 조회한다.
     *
     * @param serviceBroker the serviceBroker
     * @return the boolean
     * @throws Exception the exception
     */
    public GetServiceBrokerResponse getServiceBroker(ServiceBroker serviceBroker, String token) throws Exception {
        return cloudFoundryClient().serviceBrokers().get(GetServiceBrokerRequest.builder().serviceBrokerId(serviceBroker.getGuid().toString()).build()).block();
    }

    /**
     * 서비스 브로커를 생성한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @return the boolean
     * @throws Exception the exception
     */
    public CreateServiceBrokerResponse createServiceBroker(ServiceBroker serviceBroker, String token) throws Exception {
        return cloudFoundryClient().serviceBrokers().create(CreateServiceBrokerRequest.builder().name(serviceBroker.getName()).brokerUrl(serviceBroker.getUrl()).authenticationUsername(serviceBroker.getUsername()).authenticationPassword(serviceBroker.getPassword()).build()).block();
    }

    /**
     * 서비스 브로커를 수정한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param token
     * @return the boolean
     * @throws Exception the exception
     */
    public UpdateServiceBrokerResponse updateServiceBroker(ServiceBroker serviceBroker, String token) throws Exception {

        return cloudFoundryClient().serviceBrokers().update(UpdateServiceBrokerRequest.builder().serviceBrokerId(serviceBroker.getGuid().toString()).name(serviceBroker.getName()).authenticationUsername(serviceBroker.getUsername()).authenticationPassword(serviceBroker.getPassword()).brokerUrl(serviceBroker.getUrl()).build()).block();
    }

    /**
     * 서비스 브로커를 삭제한다.
     *
     * @param guid the cloudServiceBroker
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean deleteServiceBroker(String guid, String token, boolean purge) throws Exception {
        if(purge){
            cloudFoundryClient().serviceBrokers().list(ListServiceBrokersRequest.builder().build()).block().getResources()
                    .forEach(resource -> {
                        if(resource.getMetadata().getId().equals(guid)){
                            cloudFoundryClient().services().list(ListServicesRequest.builder().serviceBrokerId(guid).build()).block().getResources()
                                    .forEach(service -> {
                                        cloudFoundryClient().services().delete(DeleteServiceRequest.builder().purge(true).serviceId(service.getMetadata().getId()).build()).block();
                                    });
                        }
                    });
        }

        cloudFoundryClient().serviceBrokers().delete(DeleteServiceBrokerRequest.builder().serviceBrokerId(guid).build()).block();

        return true;
    }


    public ListServiceInstancesResponse getServicesInstances(String guid) {
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient();

        ListServiceInstancesResponse listServicesInstancesResponse = cloudFoundryClient.serviceInstances().list(ListServiceInstancesRequest.builder().spaceId(guid).build()).block();

        return listServicesInstancesResponse;
    }

    public GetServiceBindingResponse getServiceBinding(String token, String serviceid) {
        return cloudFoundryClient().serviceBindingsV2().get(GetServiceBindingRequest.builder().serviceBindingId(serviceid).build()).block();
    }


    /**
     * 해당 서비스 브로커 상세내용을 조회한다.
     *
     * @param token token
     * @return the boolean
     * @throws Exception the exception
     */
    public ListServicePlansResponse getServicePlans(String token) throws Exception {
        return cloudFoundryClient().servicePlans().list(ListServicePlansRequest.builder().build()).block();
    }

    /**
     * 서비스  조회한다.
     *
     * @param serviceBroker the serviceBroker
     * @return the boolean
     * @throws Exception the exception
     */
    public GetServicePlanResponse getServicePlan(ServiceBroker serviceBroker, String token) throws Exception {
        return cloudFoundryClient().servicePlans().get(GetServicePlanRequest.builder().servicePlanId(serviceBroker.getGuid().toString()).build()).block();
    }

    /**
     * 서비스 제어 수정한다. / 서비스 활성화를 변경한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param token
     * @return the boolean
     * @throws Exception the exception
     */
    public UpdateServicePlanResponse updateServicePlan(ServiceBroker serviceBroker, String guid, String token) throws Exception {
        return cloudFoundryClient().servicePlans().update(UpdateServicePlanRequest.builder().servicePlanId(guid).publiclyVisible(serviceBroker.getPubliclyVisible()).build()).block();
    }


    /**
     * 서비스 플랜 삭제
     *
     * @param serviceBroker the cloudServiceBroker
     * @param token
     * @return the boolean
     * @throws Exception the exception
     */
    public DeleteServicePlanResponse deleteServicePlan(ServiceBroker serviceBroker, String guid, String token) throws Exception {
        return cloudFoundryClient().servicePlans().delete(DeleteServicePlanRequest.builder().servicePlanId(guid).async(serviceBroker.getPubliclyVisible()).build()).block();
    }

    /**
     * 서비스 Plan에 Access 등록 되어있는 조직을 조회한다.
     *
     * @param serviceplanId the serviceplan id
     * @return boolean boolean
     * @throws Exception the exception
     */
    public ListServicePlanVisibilitiesResponse getServicePlanVisibilites(String serviceplanId) throws Exception {
        return cloudFoundryClient().servicePlanVisibilities().list(ListServicePlanVisibilitiesRequest.builder().servicePlanId(serviceplanId).build()).block();
    }

    /**
     * 서비스 Plan에 Access 등록 되어있는 조직을 추가한다.
     *
     * @param bodyMap the map
     * @param token
     * @return the boolean
     * @throws Exception the exception
     */
    public CreateServicePlanVisibilityResponse updateServicePlanVisibility(Map<String, Object> bodyMap, String guid, String token) throws Exception {
        return cloudFoundryClient().servicePlanVisibilities().create(CreateServicePlanVisibilityRequest.builder().servicePlanId(bodyMap.get("servicePlanGuid").toString()).organizationId(bodyMap.get("orgGuid").toString()).build()).block();
    }

    /**
     * 서비스 Plan에 Access 등록 되어있는 조직을 삭제한다.
     *
     * @param token
     * @return the boolean
     * @throws Exception the exception
     */
    public DeleteServicePlanVisibilityResponse deleteServicePlanVisibility(String guid, String token) throws Exception {
        return cloudFoundryClient().servicePlanVisibilities().delete(DeleteServicePlanVisibilityRequest.builder().servicePlanVisibilityId(guid).async(false).build()).block();
    }

    /**
     * 서비스 Plan에 Access 등록 되어있는 조직을 삭제한다.
     *
     * @param guid serviceplan guid
     * @return the boolean
     * @throws Exception the exception
     */
    public Map allDeleteServicePlanVisibility(String guid) throws Exception {
        try {
            ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient();
            ListServicePlanVisibilitiesResponse listServicePlanVisibilitiesResponse = reactorCloudFoundryClient.servicePlanVisibilities().list(ListServicePlanVisibilitiesRequest.builder().servicePlanId(guid).build()).block();
            listServicePlanVisibilitiesResponse.getResources().forEach(resource -> {
                reactorCloudFoundryClient.servicePlanVisibilities().delete(DeleteServicePlanVisibilityRequest.builder().servicePlanVisibilityId(resource.getMetadata().getId()).async(false).build()).block();
            });
            return new HashMap<String, Object>() {{
                put("RESULT", "SUCCESS");
            }};
        } catch (Exception e) {
            return new HashMap<String, Object>() {{
                put("RESULT", "FALE");
            }};
        }
    }
}



