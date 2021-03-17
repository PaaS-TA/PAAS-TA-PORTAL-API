package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.v2.PaginatedResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.DomainServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by cheolhan on 2018-03-26.
 */
@RestController
public class DomainControllerV3 extends Common {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainControllerV3.class);

    @Autowired
    private DomainServiceV3 domainServiceV3;

    /**
     * Gets domains for all.
     *
     * @param token the token
     * @return the domains
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/domains")
    public PaginatedResponse getDomains(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return domainServiceV3.getDomains("all");
    }

    /**
     * Gets domains with status.
     *
     * @param token  the token
     * @param status the status
     * @return the domains
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/domains/{status}")
    public PaginatedResponse getDomains(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @PathVariable String status) throws Exception {
        return domainServiceV3.getDomains(status.toLowerCase());
    }


    /**
     * Gets domains for all.
     *
     * @param token the token
     * @return the domains
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/domains-admin")
    public PaginatedResponse getDomainsAdmin(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return domainServiceV3.getDomains("all");
    }

    /**
     * Gets domains with status.
     *
     * @param token  the token
     * @param status the status
     * @return the domains
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/domains-admin/{status}")
    public PaginatedResponse getDomainsAdmin(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @PathVariable String status) throws Exception {
        return domainServiceV3.getDomains(status.toLowerCase());
    }


    /**
     * Gets domains with guid.
     *
     * @param token the token
     * @param guid  the Org guid
     * @return the domains
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/{guid}/domains")
    public PaginatedResponse getOrgPrivateDomain(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token, @PathVariable String guid) throws Exception {
        return domainServiceV3.getOrgPrivateDomain(guid);
    }

    /**
     * Add domain boolean.
     *
     * @param token the token
     * @param body  the body
     * @return the boolean
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL + "/domains")
    public Map addDomain(@RequestBody Map<String, String> body, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        //LOGGER.info("addDomain Start ");
        Map resultMap = null;

        if (body.containsKey("isShared")) {
            boolean isShared = Boolean.valueOf(body.get("isShared"));
            resultMap = domainServiceV3.addDomain(token, body.get("domainName").toString(), body.get("orgId").toString(), isShared);
        } else {
            resultMap = domainServiceV3.addDomain(token, body.get("domainName").toString(), body.get("orgId").toString());
        }

        //LOGGER.info("addDomain End ");
        return resultMap;
    }

    /**
     * Delete domain boolean.
     *
     * @param token      the token
     * @param domainName the domain name
     * @return the boolean
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V3_URL + "/domains/{guid}")
    public Map deleteDomain(@PathVariable String guid, @RequestParam String domainName, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        //LOGGER.info("deleteDomain Start ");

        Map resultMap = domainServiceV3.deleteDomain(guid, domainName);

        //LOGGER.info("deleteDomain End ");
        return resultMap;
    }

    /**
     * Delete domain boolean.
     *
     * @param token      the token
     * @param domainName the domain name
     * @return the boolean
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V3_URL + "/domains-admin/{guid}")
    public Map deleteDomainAdmin(@PathVariable String guid, @RequestParam String domainName, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        //LOGGER.info("deleteDomain Start ");

        Map resultMap = domainServiceV3.deleteDomain(guid, domainName);

        //LOGGER.info("deleteDomain End ");
        return resultMap;
    }


}
