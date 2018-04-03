package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudServiceBroker;
import org.cloudfoundry.client.lib.domain.CloudServiceInstance;
import org.cloudfoundry.client.v2.servicebrokers.GetServiceBrokerResponse;
import org.cloudfoundry.client.v2.servicebrokers.ServiceBrokerResource;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.model.App;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 서비스 컨트롤 - 서비스 목록 , 서비스 상세 정보, 서비스 인스턴스 추가, 서비스 인스턴스 수정, 서비스 인스턴스 삭제 등 서비스 인스턴스 관리를  제공한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
@Transactional
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);
    private final String V2_URL = "/v2";

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

        LOGGER.info("getServiceInstance Start : " + service.getGuid() );

        //token setting
        CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY),service.getOrgName(),service.getSpaceName());

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
    @RequestMapping(value = {"/service/renameInstanceService"}, method = RequestMethod.POST)
    public boolean renameInstanceService(@RequestBody Service service, HttpServletRequest request) throws Exception {

        LOGGER.info("renameInstanceService Start : " + service.getGuid() );

        //token setting
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY),service.getOrgName(),service.getSpaceName());

        //service call
        serviceService.renameInstanceService(service, client);

        LOGGER.info("renameInstanceService End ");

        return true;
    }


    /**
     * 서비스 인스턴스를 삭제한다.
     *
     * @param service the service
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/service/deleteInstanceService"}, method = RequestMethod.POST)
    public boolean deleteInstanceService(@RequestBody Service service, HttpServletRequest request) throws Exception {

        LOGGER.info("deleteInstanceService Start : " + service.getGuid() );

        //token setting
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY),service.getOrgName(),service.getSpaceName());

        //service call
        serviceService.deleteInstanceService(service, client);

        LOGGER.info("deleteInstanceService End ");

        return true;
    }


    /**
     * 서비스 인스턴스를 삭제한다.
     *
     * @param service the service
     * @param request the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/service/deleteInstanceServiceForBoundApp"}, method = RequestMethod.POST)
    public boolean deleteInstanceServiceForBoundApp(@RequestBody Service service, HttpServletRequest request) throws Exception {
        CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY),service.getOrgName(),service.getSpaceName());
        CustomCloudFoundryClient customClient = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY),service.getOrgName(),service.getSpaceName());

        // UNBIND SERVICE
        //CISS appService.unbindService(new App(){{setName(service.getName()); setServiceName(service.getServiceName());}}, client);

        // DELETE SERVICE INSTANCE
        serviceService.deleteInstanceService(service, customClient);

        return true;
    }

    /**
     * 유저프로바이드 서비스 인스턴스를 조회한다.
     *
     * @param token the token
     * @param body  the body
     * @return Map userProvidedServiceInstance
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.5.20 최초작성
     */
    @RequestMapping(value = {"/service/getUserProvidedService"}, method = RequestMethod.POST)
    public Map<String, Object> getUserProvided(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,
                                      @RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("getUserProvidedService Start");

        Map<String, Object> userProvidedServiceInstance = serviceService.getUserProvided(token, body);

        LOGGER.info("getUserProvidedService End");
        return userProvidedServiceInstance;
    }

    /**
     * 유저프로바이드 서비스 인스턴스를 생성한다.
     *
     * @param token the token
     * @param body  the body
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.5.20 최초작성
     */
    @RequestMapping(value = {"/service/createUserProvidedService"}, method = RequestMethod.POST)
    public boolean createUserProvided(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,
                               @RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("createUserProvided Start");

        serviceService.createUserProvided(token, body);

        LOGGER.info("createUserProvided End");
        return true;
    }

    /**
     * 유저프로바이드 서비스 인스턴스를 수정한다.
     *
     * @param token the token
     * @param body  the body
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.5.20 최초작성
     */
    @RequestMapping(value = {"/service/updateUserProvidedService"}, method = RequestMethod.POST)
    public boolean updateUserProvided(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,
                               @RequestBody Map<String, String> body) throws Exception {

        LOGGER.info("updateUserProvidedService Start");

        serviceService.updateUserProvided(token, body);

        LOGGER.info("updateUserProvidedService End");
        return true;
    }


    /**
     * 서비스 브로커 리스트를 조회한다.
     *
     * @param serviceBroker the serviceBroker
     * @param request       the request
     * @return CloudServiceInstance cloudServiceInstance
     * @throws Exception the exception
     */
    @GetMapping(value = {V2_URL + "/servicebrokers"})
    public Map<String, Object> getServiceBrokers(@ModelAttribute ServiceBroker serviceBroker, HttpServletRequest request) throws Exception {

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));
        Map<String, Object> resultMap = new HashMap<>();

        // 서비스 리스트 조회
        LOGGER.info("getServiceBrokers Start.");
        List<ServiceBrokerResource> list = serviceService.getServiceBrokers(serviceBroker);
        resultMap.put("list", list);
        LOGGER.info("getServiceBrokers End ");

        return resultMap;
    }

    /**
     * 서비스 브로커 상세내용을 조회한다.
     *
     * @param serviceBroker the serviceBroker
     * @param request       the request
     * @return CloudServiceInstance cloudServiceInstance
     * @throws Exception the exception
     */
    @GetMapping(value = {V2_URL + "/servicebrokers/{guid}" })
    public Map<String, Object> getServiceBroker(@ModelAttribute ServiceBroker serviceBroker, @PathVariable String guid  ,HttpServletRequest request) throws Exception {

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));
        Map<String, Object> resultMap = new HashMap<>();

        // 서비스 항목 조회
        LOGGER.info("getServiceBroker Start : " + serviceBroker.getGuid());
        serviceBroker.setGuid(UUID.fromString(guid));
        GetServiceBrokerResponse servicebroker = serviceService.getServiceBroker(serviceBroker);
        resultMap.put("servicebroker", servicebroker);
        LOGGER.info("getServiceBroker End ");

        return resultMap;
    }


    /**
     * 서비스 브로커를 등록한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param request       the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @PostMapping(value = {V2_URL + "/servicebrokers"})
    public Map<String, Object>  createServiceBroker(@RequestBody ServiceBroker serviceBroker, HttpServletRequest request) throws Exception {

        LOGGER.info("createServiceBroker Start : " + serviceBroker.getName() );

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        serviceService.createServiceBroker(serviceBroker);

        LOGGER.info("createServiceBroker End ");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        return resultMap;

    }


    /**
     * 서비스 브로커를 수정한다. / 서비스 브로커 이름을 변경한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param request       the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @PutMapping(value = {V2_URL + "/servicebrokers/{guid}"})
    public Map<String, Object>  updateServiceBroker(@RequestBody ServiceBroker serviceBroker, @PathVariable String guid ,HttpServletRequest request) throws Exception {

        LOGGER.info("updateServiceBroker Start : " + serviceBroker.getName() );

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        //serviceService.updateServiceBroker(serviceBroker, client);

        serviceBroker.setGuid(UUID.fromString(guid));
        serviceService.updateServiceBroker(serviceBroker);

        LOGGER.info("updateServiceBroker End ");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        return resultMap;

    }


    /**
     * 서비스 브로커를 삭제한다.
     *
     * @param guid the cloudServiceBroker
     * @param request       the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @DeleteMapping(value = {V2_URL + "/servicebrokers/{guid}"})
    public Map<String, Object>  deleteServiceBroker(@PathVariable String guid, HttpServletRequest request) throws Exception {

        LOGGER.info("deleteServiceBroker Start : " + guid );

        //token setting
        //CloudFoundryClient client = getCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        serviceService.deleteServiceBroker(guid);

        LOGGER.info("deleteServiceBroker End ");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        return resultMap;
    }

    /**
     * 서비스 이미지를 조회한다.
     *
     * @param service the service
     * @return the menu list
     */
    @RequestMapping(value = {"/service/getServiceImageUrl"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getServiceImageUrl(@RequestBody Service service) {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("serviceImageUrl", serviceService.getServiceImageUrl(service.getServiceName()));

        return resultMap;
    }


}
