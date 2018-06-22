package org.openpaas.paasta.portal.api.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudServiceInstance;
import org.cloudfoundry.client.v2.servicebrokers.CreateServiceBrokerResponse;
import org.cloudfoundry.client.v2.servicebrokers.GetServiceBrokerResponse;
import org.cloudfoundry.client.v2.servicebrokers.ListServiceBrokersResponse;
import org.cloudfoundry.client.v2.servicebrokers.UpdateServiceBrokerResponse;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.userprovidedserviceinstances.GetUserProvidedServiceInstanceResponse;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Service;
import org.openpaas.paasta.portal.api.model.ServiceBroker;
import org.openpaas.paasta.portal.api.service.AppService;
import org.openpaas.paasta.portal.api.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 서비스 컨트롤 - 서비스 목록 , 서비스 상세 정보, 서비스 인스턴스 추가, 서비스 인스턴스 수정, 서비스 인스턴스 삭제 등 서비스 인스턴스 관리를  제공한다.
 * @version 2.0
 * @since 2018.2.20 최초작성
 */
@RestController
@Transactional
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private AppService appService;

    @Autowired
    private ServiceService serviceService;


    /**
     * 서비스 인스턴스를 조회한다.
     *
     * @param service the service
     * @param request the request
     * @return CloudServiceInstance cloudServiceInstance
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/service/getServiceInstance"}, method = RequestMethod.POST)
    public CloudServiceInstance getServiceInstance(@RequestBody Service service, HttpServletRequest request) throws Exception {

        LOGGER.info("getServiceInstance Start : " + service.getGuid());

        //token setting
        CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY), service.getOrgName(), service.getSpaceName());

        //service call
        CloudServiceInstance cloudServiceInstance = serviceService.getServiceInstance(service, client);

        LOGGER.info("getServiceInstance End ");

        return cloudServiceInstance;
    }


    /**
     * 서비스 인스턴스 이름을 변경한다.
     *
     * @param service the service
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "renameInstance")
    @RequestMapping(value = {Constants.V2_URL + "/service/{guid}/rename"}, method = RequestMethod.PUT)
    public Map renameInstance(@PathVariable String guid, @RequestBody Service service, HttpServletRequest request) throws Exception {
        LOGGER.info("Rename InstanceService Start : " + guid + " : " + service.getName() + " : " + service.getNewName());
        //service call
        Map result = serviceService.renameInstance(service, guid);
        LOGGER.info("Rename InstanceService End ");
        return result;
    }


    /**
     * 서비스 인스턴스를 삭제한다.
     *
     * @return boolean boolean
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "deleteInstance")
    @RequestMapping(value = {Constants.V2_URL + "/service/{guid}"}, method = RequestMethod.DELETE)
    public Map deleteInstance(@PathVariable String guid) throws Exception {
        LOGGER.info("delete InstanceService Start : " + guid);
        //service call
        Map result = serviceService.deleteInstance(guid);
        LOGGER.info("delete InstanceService End ");
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
    @HystrixCommand(fallbackMethod = "getUserProvided")
    @RequestMapping(value = {Constants.V2_URL + "/service/userprovidedserviceinstances/{guid}"}, method = RequestMethod.GET)
    public GetUserProvidedServiceInstanceResponse getUserProvided(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @PathVariable String guid) throws Exception {
        LOGGER.info("getUserProvidedService Start");
        return serviceService.getUserProvided(token, guid);
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
    @HystrixCommand(fallbackMethod = "createUserProvided")
    @RequestMapping(value = {Constants.V2_URL + "/service/userprovidedserviceinstances"}, method = RequestMethod.POST)
    public Map createUserProvided(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Service service) throws Exception {

        LOGGER.info("createUserProvided Start");
        Map result = serviceService.createUserProvided(token, service);
        LOGGER.info("createUserProvided End");
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
    @HystrixCommand(fallbackMethod = "updateUserProvided")
    @RequestMapping(value = {Constants.V2_URL + "/service/userprovidedserviceinstances/{guid}"}, method = RequestMethod.PUT)
    public Map updateUserProvided(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @RequestBody Service service) throws Exception {

        LOGGER.info("updateUserProvidedService Start");
        Map result = serviceService.updateUserProvided(guid, token, service);
        LOGGER.info("updateUserProvidedService End");
        return result;
    }

    /**
     * 서비스 브로커 리스트를 조회한다.
     *
     * @param request the request
     * @return CloudServiceInstance cloudServiceInstance
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "getServiceBrokers")
    @GetMapping(value = {Constants.V2_URL + "/servicebrokers"})
    public ListServiceBrokersResponse getServiceBrokers(HttpServletRequest request) throws Exception {
        LOGGER.info("getServiceBrokers Start:");
        return serviceService.getServiceBrokers(request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 서비스 브로커 상세내용을 조회한다.
     *
     * @param serviceBroker the serviceBroker
     * @param request       the request
     * @return CloudServiceInstance cloudServiceInstance
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "getServiceBroker")
    @GetMapping(value = {Constants.V2_URL + "/servicebrokers/{guid}"})
    public GetServiceBrokerResponse getServiceBroker(@ModelAttribute ServiceBroker serviceBroker, @PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getServiceBroker Start : " + serviceBroker.getGuid());
        return serviceService.getServiceBroker(serviceBroker, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }


    /**
     * 서비스 브로커를 등록한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param request       the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "createServiceBroker")
    @PostMapping(value = {Constants.V2_URL + "/servicebrokers"})
    public CreateServiceBrokerResponse createServiceBroker(@RequestBody ServiceBroker serviceBroker, HttpServletRequest request) throws Exception {

        LOGGER.info("createServiceBroker Start : " + serviceBroker.getName());
        return serviceService.createServiceBroker(serviceBroker, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }


    /**
     * 서비스 브로커를 수정한다. / 서비스 브로커 이름을 변경한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param request       the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "updateServiceBroker")
    @PutMapping(value = {Constants.V2_URL + "/servicebrokers/{guid}"})
    public UpdateServiceBrokerResponse updateServiceBroker(@RequestBody ServiceBroker serviceBroker, @PathVariable String guid, HttpServletRequest request) throws Exception {

        LOGGER.info("updateServiceBroker Start : " + serviceBroker.getName());
        serviceBroker.setGuid(UUID.fromString(guid));
        return serviceService.updateServiceBroker(serviceBroker, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }


    /**
     * 서비스 브로커를 삭제한다.
     *
     * @param guid    the cloudServiceBroker
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "deleteServiceBroker")
    @DeleteMapping(value = {Constants.V2_URL + "/servicebrokers/{guid}"})
    public Map<String, Object> deleteServiceBroker(@PathVariable String guid, HttpServletRequest request) throws Exception {

        LOGGER.info("deleteServiceBroker Start : " + guid);
        serviceService.deleteServiceBroker(guid, request.getHeader(AUTHORIZATION_HEADER_KEY));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        return resultMap;
    }



    @HystrixCommand(fallbackMethod = "getServicesInstances")
    @RequestMapping(value = {Constants.V2_URL + "/service-instances/space/{guid}"}, method = RequestMethod.GET)
    public ListServiceInstancesResponse getServicesInstances(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("getServicesInstances Start ");

        ListServiceInstancesResponse respServicesInstances = serviceService.getServicesInstances(guid, this.getToken());

        LOGGER.info("getServicesInstances End ");

        return respServicesInstances;
    }


}
