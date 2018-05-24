package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.cloudfoundry.client.v2.PaginatedResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 도메인 컨트롤러 - 도메인 정보를 조회, 수정, 삭제한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.19 최초작성
 */
@RestController
@Transactional
public class DomainController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainController.class);

    @Autowired
    private DomainService domainService;

    /**
     * Gets domains for all.
     *
     * @param token  the token
     * @return the domains
     * @throws Exception the exception
     */
    @GetMapping( Constants.V2_URL + "/domains" )
    public PaginatedResponse getDomains( @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return domainService.getDomains(token, "all");
    }

    /**
     * Gets domains with status.
     *
     * @param token  the token
     * @param status the status
     * @return the domains
     * @throws Exception the exception
     */
    @GetMapping( Constants.V2_URL + "/domains/{status}" )
    public PaginatedResponse getDomains( @RequestHeader(AUTHORIZATION_HEADER_KEY) String token,
                                         @PathVariable String status) throws Exception {
        return domainService.getDomains(token, status.toLowerCase());
    }

    /**
     * Add domain boolean.
     *
     * @param token the token
     * @param body  the body
     * @return the boolean
     * @throws Exception the exception
     */
    @PostMapping( Constants.V2_URL + "/domains" )
    public boolean addDomain ( @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token,
                               @RequestBody Map<String, String> body ) throws Exception {
        final String domainName = body.get( "domainName" );
        final String orgId = body.get( "orgId" );
        Objects.requireNonNull(domainName, "Domain name");
        Objects.requireNonNull(orgId, "Org Id");

        if ( body.containsKey( "isShared" ) ) {
            boolean isShared = Boolean.valueOf( body.get( "isShared" ) );
            return domainService.addDomain( token, domainName, orgId, isShared );
        } else {
            return domainService.addDomain( token, domainName, orgId );
        }
    }

    /**
     * Delete domain boolean.
     *
     * @param token the token
     * @param domainName the domain name
     * @return the boolean
     * @throws Exception the exception
     */
    @DeleteMapping( Constants.V2_URL + "/domains" )
    public boolean deleteDomain(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,
                                @RequestParam String orgId,
                                @RequestParam String domainName) throws Exception {
        return domainService.deleteDomain(token, orgId, domainName);
    }


}
