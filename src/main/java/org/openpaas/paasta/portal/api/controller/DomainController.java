package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.service.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 도메인 컨트롤러 - 도메인 정보를 조회, 수정, 삭제한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.19 최초작성
 */
@RestController
@Transactional
@RequestMapping(value = {"/domain"})
public class DomainController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainController.class);

    @Autowired
    private DomainService domainService;


    /**
     * Gets domains.
     *
     * @param token  the token
     * @param status the status
     * @return the domains
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getDomains/{status}"}, method = RequestMethod.POST)
    public List getDomains(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,
                           @PathVariable String status) throws Exception {
        
        List<CloudDomain> domains = domainService.getDomains(token, status);

        return domains;
    }

    /**
     * Add domain boolean.
     *
     * @param token the token
     * @param body  the body
     * @return the boolean
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/addDomain"}, method = RequestMethod.POST)
    public boolean addDomain(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,
                             @RequestBody Map<String, String> body) throws Exception {

        domainService.addDomain(token, body.get("orgName"), body.get("spaceName"), body.get("domainName"));

        return true;
    }

    /**
     * Delete domain boolean.
     *
     * @param token the token
     * @param body  the body
     * @return the boolean
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteDomain"}, method = RequestMethod.POST)
    public boolean deleteDomain(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,
                                @RequestBody Map<String, String> body) throws Exception {

        domainService.deleteDomain(token, body.get("orgName"), body.get("spaceName"), body.get("domainName"));

        return true;
    }


}
