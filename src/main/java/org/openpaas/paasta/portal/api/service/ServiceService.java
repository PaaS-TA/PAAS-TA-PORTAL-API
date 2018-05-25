package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudServiceInstance;
import org.cloudfoundry.client.v2.servicebrokers.*;
import org.cloudfoundry.client.v2.serviceinstances.DeleteServiceInstanceRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.serviceinstances.UpdateServiceInstanceRequest;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Service;
import org.openpaas.paasta.portal.api.model.ServiceBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

//import org.openpaas.paasta.portal.api.mapper.portal.ServiceMapper;

/**
 * 서비스 컨트롤 - 서비스 목록 , 서비스 상세 정보, 서비스 인스턴스 추가, 서비스 인스턴스 수정, 서비스 인스턴스 삭제 등 서비스 인스턴스 관리를  제공한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@EnableAsync
@org.springframework.stereotype.Service
public class ServiceService extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceService.class);

//    @Autowired
//    private ServiceMapper serviceMapper;


    /**
     * 서비스 인스턴스를 조회한다.
     *
     * @param service the service
     * @param client  the client
     * @return the service instance
     * @throws Exception the exception
     */
    public CloudServiceInstance getServiceInstance(org.openpaas.paasta.portal.api.model.Service service, CloudFoundryClient client) throws Exception {

        CloudServiceInstance cloudServiceInstance = client.getServiceInstance(service.getName());

        return cloudServiceInstance;

    }

    /**
     * 서비스 인스턴스 이름을 변경한다.
     *
     * @param service the service
     * @return the boolean
     * @throws Exception the exception
     */
    public Map renameInstance(Service service, String guid) throws Exception {
        HashMap result = new HashMap();
//        guid.renameInstanceService(service.getGuid(), service.getNewName());
        try{
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(adminUserName, adminPassword));
            //cloudFoundryClient.applicationsV2().update(UpdateApplicationRequest.builder().applicationId(guid).name(service.getNewName()).build()).block();
            cloudFoundryClient.serviceInstances().update(UpdateServiceInstanceRequest.builder().serviceInstanceId(guid).name(service.getNewName()).build()).block();
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e){
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
    public Map deleteInstance(String guid) throws Exception {
        HashMap result = new HashMap();
//        client.deleteInstanceService(service.getGuid());
        try{
            ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(adminUserName, adminPassword));
            cloudFoundryClient.serviceInstances().delete(DeleteServiceInstanceRequest.builder().serviceInstanceId(guid).build()).block();
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        }catch (Exception e){
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 유저 프로바이드 서비스를 조회한다.
     *
     * @param token the token
     * @param body  the body
     * @return boolean user provided
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.8.4 최초작성
     */
    public Map<String, Object> getUserProvided(String token, Map<String, String> body) throws Exception {

//        String orgName = body.get("orgName");
//        String spaceName = body.get("spaceName");
//        String serviceInstanceName = body.get("serviceInstanceName");
//
//        if (!stringNullCheck(orgName, spaceName, serviceInstanceName)) {
//            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
//        }
//
//        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);
//
//        Map<String, Object> userProvidedServiceInstance = client.getUserProvidedServiceInstance(orgName, spaceName, serviceInstanceName);
//        LOGGER.info(userProvidedServiceInstance.toString());
        return null;
    }

    /**
     * 유저 프로바이드 서비스를 생성한다. (유저가 SpaceDeveloper role을 갖고 있을때만 가능)
     *
     * @param token the token
     * @param body  the body
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.8.4 최초작성
     */
    public boolean createUserProvided(String token, Map<String, String> body) throws Exception {

        String orgName = body.get("orgName");
        String spaceName = body.get("spaceName");
        String serviceInstanceName = body.get("serviceInstanceName");
        String credentialsStr = body.get("credentials");
        String syslogDrainUrl = body.get("syslogDrainUrl"); // null 또는 빈값 허용
//
//        if (!stringNullCheck(orgName, spaceName, serviceInstanceName, credentialsStr)) {
//            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object>  credentials =  mapper.readValue(credentialsStr, new TypeReference<Map<String, Object>>(){});
//
//        CloudService service = new CloudService();
//        service.setName(serviceInstanceName);
//
//        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);
//
//        client.createUserProvidedService(service, credentials, syslogDrainUrl);
        return true;
    }

    /**
     * 유저 프로바이드 서비스를 생성한다. (유저가 SpaceDeveloper role을 갖고 있을때만 가능)
     *
     * @param token the token
     * @param body  the body
     * @return boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @version 1.0
     * @since 2016.8.4 최초작성
     */
//    public boolean updateUserProvided(String token, Map<String, String> body) throws Exception {
//
//        String orgName = body.get("orgName");
//        String spaceName = body.get("spaceName");
//        String serviceInstanceName = body.get("serviceInstanceName");
//        String newServiceInstanceName = body.get("newServiceInstanceName"); // null 또는 빈값 허용
//        String credentialsStr = body.get("credentials");
//        String syslogDrainUrl = body.get("syslogDrainUrl"); // null 또는 빈값 허용
//
//        if (!stringNullCheck(orgName, spaceName, serviceInstanceName,newServiceInstanceName, credentialsStr)) {
//            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object>  credentials =  mapper.readValue(credentialsStr, new TypeReference<Map<String, Object>>(){});
//
//        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);
//
//        client.updateUserProvidedService(orgName, spaceName, serviceInstanceName, newServiceInstanceName, credentials, syslogDrainUrl);
//        return true;
//    }


    /**
     * 서비스 브로커 리스트를 조회한다.
     *
     * @param token token
     * @return the boolean
     * @throws Exception the exception
     */
    public ListServiceBrokersResponse getServiceBrokers(String token) throws Exception {
        return  Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .serviceBrokers()
                .list(ListServiceBrokersRequest.builder().build())
                .log()
                .block();
    }


    /**
     * 서비스 브로커를 조회한다.
     *
     * @param serviceBroker the serviceBroker
     * @return the boolean
     * @throws Exception the exception
     */
    public GetServiceBrokerResponse getServiceBroker(ServiceBroker serviceBroker, String token) throws Exception {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .serviceBrokers()
                .get(GetServiceBrokerRequest.builder()
                        .serviceBrokerId(serviceBroker.getGuid().toString())
                        .build()
                ).log()
                .block();
    }

    /**
     * 서비스 브로커를 생성한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @return the boolean
     * @throws Exception the exception
     */
    public CreateServiceBrokerResponse createServiceBroker(ServiceBroker serviceBroker, String token) throws Exception {
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .serviceBrokers()
                .create(CreateServiceBrokerRequest.builder()
                        .name(serviceBroker.getName())
                        .brokerUrl(serviceBroker.getUrl())
                        .authenticationUsername(serviceBroker.getUsername())
                        .authenticationPassword(serviceBroker.getPassword())
                        .build()
                ).block();
    }

    /**
     * 서비스 브로커를 수정한다.
     *
     * @param serviceBroker the cloudServiceBroker
//     * @param client        the client
     * @return the boolean
     * @throws Exception the exception
     */
    public UpdateServiceBrokerResponse updateServiceBroker(ServiceBroker serviceBroker, String token) throws Exception {
        /* 서비스 이름만 변경을 위한 분기 처리 URL 생성할것
           T0-Be : portal-user 에서 서비스 이름만 변경하는 기능 있는지 확인후 URL 분리 혹은 현 메소드에 분기 추가 처리
           //if(serviceBroker.getUsername().isEmpty() && serviceBroker.getUrl().isEmpty()){
           // 사용자 화면 개발 완료시 삭제 처리 필요
        */
            return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                    .serviceBrokers()
                    .update(UpdateServiceBrokerRequest.builder()
                            .serviceBrokerId(serviceBroker.getGuid().toString())
                            .name(serviceBroker.getName())
                            .authenticationUsername(serviceBroker.getUsername())
                            .authenticationPassword(serviceBroker.getPassword())
                            .brokerUrl(serviceBroker.getUrl())
                            .build()
                    ).block();
    }

    /**
     * 서비스 브로커 이름을 변경한다.
     *
     * @param serviceBroker the serviceBroker
     * @return the boolean
     * @throws Exception the exception
     */
    public UpdateServiceBrokerResponse renameServiceBroker(ServiceBroker serviceBroker, String token) throws Exception {
        // 서비스 브로커 이름변경 메소드를 CF Method 를 따로 제공하지 않으므로 update Method 를 사용하여 수정한다. 사용시 컨트롤단 생성 필요
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .serviceBrokers()
                .update(UpdateServiceBrokerRequest.builder()
                        .serviceBrokerId(serviceBroker.getGuid().toString())
                        .name(serviceBroker.getName())
                        .build()
                ).block();
    }

    /**
     * 서비스 브로커를 삭제한다.
     *
     * @param guid the cloudServiceBroker
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean deleteServiceBroker(String guid, String token) throws Exception {

        Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .serviceBrokers()
                .delete(DeleteServiceBrokerRequest.builder()
                        .serviceBrokerId(guid)
                        .build()
                )
                .block();
        return true;
    }

    /**
     * 서비스 이미지를 가져온다.
     *
     * @param serviceName the serviceName
     * @return the boolean
     * @throws Exception the exception
     */
    public String getServiceImageUrl(String serviceName) {

//        String appImageUrl = serviceMapper.getServiceImageUrl(serviceName);
        String appImageUrl = "";
        return appImageUrl;

    }

    public ListServiceInstancesResponse getServicesInstances(String guid, String token) {
        ReactorCloudFoundryClient cloudFoundryClient  = cloudFoundryClient(connectionContext(),tokenProvider(token));

        ListServiceInstancesResponse listServicesInstancesResponse =
                cloudFoundryClient.serviceInstances().list(ListServiceInstancesRequest.builder().spaceId(guid).build()
                ).block();

        return listServicesInstancesResponse;
    }


}
