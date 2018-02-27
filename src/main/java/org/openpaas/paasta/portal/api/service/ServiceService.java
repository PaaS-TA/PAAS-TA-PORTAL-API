package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudServiceBroker;
import org.cloudfoundry.client.lib.domain.CloudServiceInstance;
import org.cloudfoundry.client.lib.org.codehaus.jackson.map.ObjectMapper;
import org.cloudfoundry.client.lib.org.codehaus.jackson.type.TypeReference;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
//import org.openpaas.paasta.portal.api.mapper.portal.ServiceMapper;
import org.openpaas.paasta.portal.api.model.ServiceBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;
import java.util.Map;

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
     * @param client  the client
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean renameInstanceService(org.openpaas.paasta.portal.api.model.Service service, CustomCloudFoundryClient client) throws Exception {

        client.renameInstanceService(service.getGuid(), service.getNewName());

        return true;

    }

    /**
     * 서비스 인스턴스를 삭제한다.
     *
     * @param service the service
     * @param client  the client
     * @throws Exception the exception
     */
    public void deleteInstanceService(org.openpaas.paasta.portal.api.model.Service service,  CustomCloudFoundryClient client) throws Exception {

        client.deleteInstanceService(service.getGuid());

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

        String orgName = body.get("orgName");
        String spaceName = body.get("spaceName");
        String serviceInstanceName = body.get("serviceInstanceName");

        if (!stringNullCheck(orgName, spaceName, serviceInstanceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);

        Map<String, Object> userProvidedServiceInstance = client.getUserProvidedServiceInstance(orgName, spaceName, serviceInstanceName);
        LOGGER.info(userProvidedServiceInstance.toString());
        return userProvidedServiceInstance;
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

        if (!stringNullCheck(orgName, spaceName, serviceInstanceName, credentialsStr)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object>  credentials =  mapper.readValue(credentialsStr, new TypeReference<Map<String, Object>>(){});

        CloudService service = new CloudService();
        service.setName(serviceInstanceName);

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);

        client.createUserProvidedService(service, credentials, syslogDrainUrl);
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
    public boolean updateUserProvided(String token, Map<String, String> body) throws Exception {

        String orgName = body.get("orgName");
        String spaceName = body.get("spaceName");
        String serviceInstanceName = body.get("serviceInstanceName");
        String newServiceInstanceName = body.get("newServiceInstanceName"); // null 또는 빈값 허용
        String credentialsStr = body.get("credentials");
        String syslogDrainUrl = body.get("syslogDrainUrl"); // null 또는 빈값 허용

        if (!stringNullCheck(orgName, spaceName, serviceInstanceName,newServiceInstanceName, credentialsStr)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object>  credentials =  mapper.readValue(credentialsStr, new TypeReference<Map<String, Object>>(){});

        CustomCloudFoundryClient client = getCustomCloudFoundryClient(token, orgName, spaceName);

        client.updateUserProvidedService(orgName, spaceName, serviceInstanceName, newServiceInstanceName, credentials, syslogDrainUrl);
        return true;
    }


    /**
     * 서비스 브로커 리스트를 조회한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param client        the client
     * @return the boolean
     * @throws Exception the exception
     */
    public List<CloudServiceBroker> getServiceBrokers(ServiceBroker serviceBroker, CloudFoundryClient client) throws Exception {

        List<CloudServiceBroker> serviceBrokerList = client.getServiceBrokers();

        return serviceBrokerList;

    }


    /**
     * 서비스 브로커를 조회한다.
     *
     * @param serviceBroker the serviceBroker
     * @param client        the client
     * @return the boolean
     * @throws Exception the exception
     */
    public CloudServiceBroker getServiceBroker(ServiceBroker serviceBroker, CloudFoundryClient client) throws Exception {

        CloudServiceBroker cloudServiceBroker = client.getServiceBroker(serviceBroker.getName());

        return cloudServiceBroker;

    }

    /**
     * 서비스 브로커를 생성한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param client        the client
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean createServiceBroker(ServiceBroker serviceBroker, CloudFoundryClient client) throws Exception {

        CloudServiceBroker cloudServiceBroker = new CloudServiceBroker(serviceBroker.getUrl(), serviceBroker.getUsername(), serviceBroker.getPassword());
        cloudServiceBroker.setName(serviceBroker.getName());

        client.createServiceBroker(cloudServiceBroker);

        return true;

    }

    /**
     * 서비스 브로커를 수정한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param client        the client
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean updateServiceBroker(ServiceBroker serviceBroker, CloudFoundryClient client) throws Exception {

        CloudServiceBroker cloudServiceBroker = new CloudServiceBroker(serviceBroker.getUrl(), serviceBroker.getUsername(), serviceBroker.getPassword());
        cloudServiceBroker.setName(serviceBroker.getName());

        client.updateServiceBroker(cloudServiceBroker);

        return true;

    }

    /**
     * 서비스 브로커를 삭제한다.
     *
     * @param serviceBroker the cloudServiceBroker
     * @param client        the client
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean deleteServiceBroker(ServiceBroker serviceBroker, CloudFoundryClient client) throws Exception {

        client.deleteServiceBroker(serviceBroker.getName());

        return true;

    }


    /**
     * 서비스 브로커 이름을 변경한다.
     *
     * @param serviceBroker the serviceBroker
     * @param client        the client
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean renameServiceBroker(ServiceBroker serviceBroker, CustomCloudFoundryClient client) throws Exception {

        client.renameServiceBroker(serviceBroker.getName(), serviceBroker.getNewName());

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


}
