package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.domains.ListDomainsRequest;
import org.cloudfoundry.client.v2.domains.ListDomainsResponse;
import org.cloudfoundry.client.v2.privatedomains.ListPrivateDomainsRequest;
import org.cloudfoundry.client.v2.privatedomains.ListPrivateDomainsResponse;
import org.cloudfoundry.client.v2.shareddomains.ListSharedDomainsRequest;
import org.cloudfoundry.client.v2.shareddomains.ListSharedDomainsResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * 도메인 컨트롤러 - 도메인 정보를 조회, 수정, 삭제한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.19 최초작성
 */
@Service
public class DomainService extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainService.class);


    /**
     * 도메인 가져오기 - status 값을 받아 private, shared 중 선택하여 가져오거나 모두 가져올수 있음
     *
     * @param token  the token
     * @param status the status
     * @return domains
     * @throws Exception the exception
     * @author kimdojun
     * @since 2016.7.25 최초작성
     */
    public Map<String, Object> getDomains(String token, String status) throws Exception {

        LOGGER.info("Start getDomains service. status : "+status);
        ObjectMapper objectMapper = new ObjectMapper();
        if(!stringNullCheck(status)){
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Invalid status");
        } else {
            switch (status){
                case "all":
                    ListDomainsResponse listDomainsResponse =
                            Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                                    .domains().list(ListDomainsRequest.builder().build()).block();
                    return objectMapper.convertValue(listDomainsResponse, Map.class);
                case "private":
                    ListPrivateDomainsResponse ListPrivateDomainResponse =
                            Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                                    .privateDomains().list(ListPrivateDomainsRequest.builder().build()).block();
                    return objectMapper.convertValue(ListPrivateDomainResponse, Map.class);
                case "shared":
                    ListSharedDomainsResponse listSharedDomainsResponse =
                            Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                                    .sharedDomains().list(ListSharedDomainsRequest.builder().build()).block();
                    return objectMapper.convertValue(listSharedDomainsResponse, Map.class);
                default:
                    throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Invalid status");
            }
        }



    }

    /**
     * 도메인 추가
     *
     * @param token      the token
     * @param orgName    the org name
     * @param spaceName  the space name
     * @param domainName the domain name
     * @return Boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @since 2016.7.25 최초작성
     */
    public boolean addDomain(String token, String orgName, String spaceName, String domainName) throws Exception {
        LOGGER.info("Start addDomain service. domainName : "+domainName);

        if (!stringNullCheck(orgName,spaceName,domainName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CloudFoundryClient client = getCloudFoundryClient(token,orgName,spaceName);


        if(client.getDomains().stream().anyMatch(domain -> domain.getName().equals(domainName))){
            throw new CloudFoundryException(HttpStatus.CONFLICT,"Conflict","Domain name already exist.");
        }

        client.addDomain(domainName);

        LOGGER.info("End addDomain service. domainName : "+domainName);
        return true;
    }


    /**
     * 도메인 삭제
     *
     * @param token      the token
     * @param orgName    the org name
     * @param spaceName  the space name
     * @param domainName the domain name
     * @return Boolean boolean
     * @throws Exception the exception
     * @author kimdojun
     * @since 2016.7.25 최초작성
     */
    public boolean deleteDomain(String token, String orgName, String spaceName, String domainName) throws Exception {
        LOGGER.info("Start deleteDomain service. domainName : "+domainName);

        if (!stringNullCheck(orgName,spaceName,domainName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST,"Bad Request","Required request body content is missing");
        }

        CloudFoundryClient client = getCloudFoundryClient(token,orgName,spaceName);
        client.deleteDomain(domainName);

        LOGGER.info("End deleteDomain service. domainName : "+domainName);
        return true;
    }

}
