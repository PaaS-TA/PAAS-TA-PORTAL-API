package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.v2.servicebrokers.CreateServiceBrokerResponse;
import org.cloudfoundry.client.v2.servicebrokers.GetServiceBrokerResponse;
import org.cloudfoundry.client.v2.servicebrokers.ListServiceBrokersResponse;
import org.cloudfoundry.client.v2.servicebrokers.UpdateServiceBrokerResponse;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.serviceplans.DeleteServicePlanResponse;
import org.cloudfoundry.client.v2.serviceplans.GetServicePlanResponse;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansResponse;
import org.cloudfoundry.client.v2.serviceplans.UpdateServicePlanResponse;
import org.cloudfoundry.client.v2.serviceplanvisibilities.CreateServicePlanVisibilityResponse;
import org.cloudfoundry.client.v2.serviceplanvisibilities.DeleteServicePlanVisibilityResponse;
import org.cloudfoundry.client.v2.serviceplanvisibilities.ListServicePlanVisibilitiesResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Service;
import org.openpaas.paasta.portal.api.model.ServiceBroker;
import org.openpaas.paasta.portal.api.service.ServiceServiceV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 서비스 컨트롤 - 서비스 목록 , 서비스 상세 정보, 서비스 인스턴스 추가, 서비스 인스턴스 수정, 서비스 인스턴스 삭제 등 서비스 인스턴스 관리를  제공한다.
 *
 * @version 2.0
 * @since 2018.2.20 최초작성
 */
@RestController
public class CommonControllerV3 extends Common {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonControllerV3.class);


    @Autowired
    private ServiceServiceV2 serviceServiceV2;

    /**
     * 서비스 인스턴스 이름을 변경한다.
     *
     * @param service the service
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V3_URL + "/service/{guid}/rename"}, method = RequestMethod.PUT)
    public Map renameInstance(@PathVariable String guid, @RequestBody Service service, HttpServletRequest request) throws Exception {
        //LOGGER.info("Rename InstanceService Start : " + guid + " : " + service.getName() + " : " + service.getNewName());
        //service call
        Map result = serviceServiceV2.renameInstance(service, guid);
        //LOGGER.info("Rename InstanceService End ");
        return result;
    }

    /**
     * 서비스 인스턴스를 삭제한다.
     *
     * @return boolean boolean
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V3_URL + "/service/{guid}"}, method = RequestMethod.DELETE)
    public Map deleteInstance(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        //LOGGER.info("delete InstanceService Start : " + guid);
        //service call
        Map result = serviceServiceV2.deleteInstance(guid, token);
        //LOGGER.info("delete InstanceService End ");
        return result;
    }

    /**
     * 유저프로바이드 서비스 인스턴스를 조회한다.
     *
     * @param token the token\
     * @return Map userProvidedServiceInstance
     * @throws Exception the exception
     * @author CSJ
     * @version 2.0
     * @since 2018.5.24 최초작성
     */
    @RequestMapping(value = {Constants.V3_URL + "/service/userprovidedserviceinstances/{guid}"}, method = RequestMethod.GET)
    public GetUserProvidedServiceInstanceResponse getUserProvided(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @PathVariable String guid) throws Exception {
        //LOGGER.info("getUserProvidedService Start");
        return serviceServiceV2.getUserProvided(token, guid);
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
    @RequestMapping(value = {Constants.V3_URL + "/service/userprovidedserviceinstances"}, method = RequestMethod.POST)
    public Map createUserProvided(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Service service) throws Exception {

        //LOGGER.info("createUserProvided Start");
        Map result = serviceServiceV2.createUserProvided(token, service);
        //LOGGER.info("createUserProvided End");
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
    @RequestMapping(value = {Constants.V3_URL + "/service/userprovidedserviceinstances/{guid}"}, method = RequestMethod.PUT)
    public Map updateUserProvided(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Service service) throws Exception {

        //LOGGER.info("updateUserProvidedService Start");
        Map result = serviceServiceV2.updateUserProvided(guid, token, service);
        //LOGGER.info("updateUserProvidedService End");
        return result;
    }

    /**
     * 서비스 브로커 리스트를 조회한다.
     *
     * @param request the request
     * @return CloudServiceInstance cloudServiceInstance
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V3_URL + "/servicebrokers"})
    public ListServiceBrokersResponse getServiceBrokers(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, HttpServletRequest request) throws Exception {
        //LOGGER.info("getServiceBrokers Start:");
        return serviceServiceV2.getServiceBrokers(token);
    }

    /**
     * 해당 서비스 브로커 상세내용을 조회한다.
     *
     * @param serviceBroker the serviceBroker
     * @param request       the request
     * @return CloudServiceInstance cloudServiceInstance
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V3_URL + "/servicebrokers/{guid}"})
    public GetServiceBrokerResponse getServiceBroker(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @ModelAttribute ServiceBroker serviceBroker, @PathVariable String guid, HttpServletRequest request) throws Exception {
        //LOGGER.info("getServiceBroker Start : " + serviceBroker.getGuid());
        return serviceServiceV2.getServiceBroker(serviceBroker, token);
    }

    /**
     * 서비스 브로커를 등록한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param request       the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @PostMapping(value = {Constants.V3_URL + "/servicebrokers"})
    public CreateServiceBrokerResponse createServiceBroker(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody ServiceBroker serviceBroker, HttpServletRequest request) throws Exception {

        //LOGGER.info("createServiceBroker Start : " + serviceBroker.getName());
        return serviceServiceV2.createServiceBroker(serviceBroker, token);
    }

    /**
     * 서비스 브로커를 수정한다. / 서비스 브로커 이름을 변경한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param request       the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @PutMapping(value = {Constants.V3_URL + "/servicebrokers/{guid}"})
    public UpdateServiceBrokerResponse updateServiceBroker(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody ServiceBroker serviceBroker, @PathVariable String guid, HttpServletRequest request) throws Exception {

        //LOGGER.info("updateServiceBroker Start : " + serviceBroker.getName());
        serviceBroker.setGuid(UUID.fromString(guid));
        return serviceServiceV2.updateServiceBroker(serviceBroker, token);
    }

    /**
     * 서비스 브로커를 삭제한다.
     *
     * @param guid    the cloudServiceBroker
     * @return boolean boolean
     * @throws Exception the exception
     */
    @DeleteMapping(value = {Constants.V3_URL + "/servicebrokers/{guid}"})
    public Map<String, Object> deleteServiceBroker(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @PathVariable String guid, @RequestParam boolean purge) throws Exception {

        //LOGGER.info("deleteServiceBroker Start : " + guid);
        //LOGGER.info("requestParam: " + purge);
        serviceServiceV2.deleteServiceBroker(guid, token, purge);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        return resultMap;
    }

    @RequestMapping(value = {Constants.V3_URL + "/service-instances/space/{guid}"}, method = RequestMethod.GET)
    public ListServiceInstancesResponse getServicesInstances(@PathVariable String guid, HttpServletRequest request) throws Exception {
        //LOGGER.info("getServicesInstances Start ");

        ListServiceInstancesResponse respServicesInstances = serviceServiceV2.getServicesInstances(guid);

        //LOGGER.info("getServicesInstances End ");

        return respServicesInstances;
    }

    /**
     * 서비스 제어 리스트를 조회한다.
     *
     * @param request the request
     * @return CloudServiceInstance cloudServiceInstance
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V3_URL + "/serviceplans"})
    public ListServicePlansResponse getServicePlans(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, HttpServletRequest request) throws Exception {
        //LOGGER.info("getServicePlans Start:");
        return serviceServiceV2.getServicePlans(token);
    }

    /**
     * 해당 서비스 제어 상세내용을 조회한다.
     *
     * @param serviceBroker the serviceBroker
     * @param request       the request
     * @return CloudServiceInstance cloudServiceInstance
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V3_URL + "/serviceplans/{guid}"})
    public GetServicePlanResponse getServicePlan(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @ModelAttribute ServiceBroker serviceBroker, @PathVariable String guid, HttpServletRequest request) throws Exception {
        //LOGGER.info("getServicePlan Start : " + serviceBroker.getGuid());
        return serviceServiceV2.getServicePlan(serviceBroker, token);
    }

    /**
     * 서비스 활성화 접근허용한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param request       the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @PutMapping(value = {Constants.V3_URL + "/serviceplans/{guid}"})
    public UpdateServicePlanResponse updateServicePlan(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody ServiceBroker serviceBroker, @PathVariable String guid, HttpServletRequest request) throws Exception {
        //LOGGER.info("updateServicePlan Start : " + serviceBroker.getGuid() + "   " + serviceBroker.getPubliclyVisible() + "   " + serviceBroker.getServiceName());
        return serviceServiceV2.updateServicePlan(serviceBroker, guid, token);
    }

    /**
     * 서비스 플랜 삭제
     *
     * @param serviceBroker the cloudServiceBroker
     * @param request       the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @DeleteMapping(value = {Constants.V3_URL + "/serviceplans/{guid}"})
    public DeleteServicePlanResponse deleteServicePlan(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody ServiceBroker serviceBroker, @PathVariable String guid, HttpServletRequest request) throws Exception {
        //LOGGER.info("deleteServicePlan Start : " + serviceBroker.getGuid() + "   " + serviceBroker.getPubliclyVisible() + "   " + serviceBroker.getServiceName());
        return serviceServiceV2.deleteServicePlan(serviceBroker, guid, token);
    }

    /**
     * 서비스 Plan에 Access 등록 되어있는 조직을 조회한다.
     *
     * @param serviceplanId the serviceplan id
     * @return boolean boolean
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V3_URL + "/serviceplans/{serviceplanId}/visibilites"})
    public ListServicePlanVisibilitiesResponse getServicePlanVisibilites(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @PathVariable String serviceplanId) throws Exception {
        return serviceServiceV2.getServicePlanVisibilites(serviceplanId);
    }

    /**
     * 서비스 Plan에 Access 등록 되어있는 조직을 추가한다.
     *
     * @param bodyMap the map
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @PutMapping(value = {Constants.V3_URL + "/serviceplanvisibilities/{guid}"})
    public CreateServicePlanVisibilityResponse updateServicePlanVisibility(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Map<String, Object> bodyMap, @PathVariable String guid, HttpServletRequest request) throws Exception {
        //LOGGER.info("serviceplanvisibilities Start : " + guid + "   " + bodyMap.get("servicePlanGuid").toString() + "   " + bodyMap.get("orgGuid").toString());
        return serviceServiceV2.updateServicePlanVisibility(bodyMap, guid, token);
    }

    /**
     * 서비스 Plan에 Access 등록 되어있는 조직을 삭제한다.
     *
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @DeleteMapping(value = {Constants.V3_URL + "/serviceplanvisibilities/{guid}"})
    public DeleteServicePlanVisibilityResponse deleteServicePlanVisibility(@PathVariable String guid, HttpServletRequest request, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return serviceServiceV2.deleteServicePlanVisibility(guid, token);
    }

    /**
     * 서비스 Plan에 Access 등록 되어있는 조직을 모두 삭제한다.
     *
     * @param token token
     * @return boolean boolean
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V3_URL + "/serviceplanvisibilities/all/{guid}")
    public Map allDeleteServicePlanVisibility(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return serviceServiceV2.allDeleteServicePlanVisibility(guid);
    }

}

