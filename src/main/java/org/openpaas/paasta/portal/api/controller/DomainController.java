package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.cloudfoundry.client.v2.PaginatedResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.service.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
public class DomainController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainController.class);
    private final String V2_URL = "/v2";

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
    @GetMapping( V2_URL + "/domains/{status}" )
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
    @PostMapping( V2_URL + "/domains" )
    public boolean addDomain ( @RequestHeader( AUTHORIZATION_HEADER_KEY ) String token,
                               @RequestBody Map<String, String> body ) throws Exception {
        final String domainName = body.get( "domainName" );
        final String orgId = body.get( "orgId" );
        if ( body.containsKey( "isShared" ) )
            return domainService.addDomain(
                token, domainName, orgId, Boolean.valueOf( body.get( "isShared" ) ) );
        else
            return domainService.addDomain( token, domainName, orgId );
    }

    /**
     * Delete domain boolean.
     *
     * @param token the token
     * @param domainName the domain name
     * @return the boolean
     * @throws Exception the exception
     */
    @DeleteMapping(V2_URL+"/domains")
    public boolean deleteDomain(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token,
                                @RequestParam String domainName) throws Exception {

        return domainService.deleteDomain(token, domainName);
    }


}
