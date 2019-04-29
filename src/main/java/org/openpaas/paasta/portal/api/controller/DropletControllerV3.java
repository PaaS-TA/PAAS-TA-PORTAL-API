package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v3.droplets.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Droplet;
import org.openpaas.paasta.portal.api.service.DropletServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DropletControllerV3 extends Common {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////                            * CLOUD FOUNDRY CLIENT API VERSION 3                                      //////
    //////             Document : https://v3-apidocs.cloudfoundry.org/version/3.69.0/#droplets                  //////
    //////                                     Not-implemented                                                  //////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    DropletServiceV3 dropletServiceV3;


    /**
     * droplet 정보를 가져온다.
     *
     * @param dropletId the droplet id
     * @param token     user token
     * @return GetDropletResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/droplets/{dropletId}")
    public GetDropletResponse getDroplet(@PathVariable String dropletId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return dropletServiceV3.getDroplet(dropletId, token);
    }

    /**
     * droplet 리스트를 가져온다.
     *
     * @param orgId     the organization id
     * @param spaceId   the space id
     * @param dropletId the droplet id
     * @param token     user token
     * @return ListDropletsResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL + "/droplets/{orgId}/{spaceId}/{dropletId}")
    public ListDropletsResponse listDroplet(@PathVariable String orgId, @PathVariable String spaceId, @PathVariable String dropletId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return dropletServiceV3.listDroplet(orgId, spaceId, dropletId, token);
    }

    /**
     * droplet을 복사한다.
     *
     * @param droplet the Droplet
     * @param token   user token
     * @return CopyDropletResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL + "/droplets")
    public CopyDropletResponse copyDroplet(@RequestBody Droplet droplet, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return dropletServiceV3.copyDroplet(droplet, token);
    }

    /**
     * droplet을 복사한다.
     *
     * @param dropletId the Droplet Id
     * @param token     user token
     * @return String
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V3_URL + "/droplets/{dropletId}")
    public String deleteDroplet(@PathVariable String dropletId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        return dropletServiceV3.deleteDroplet(dropletId, token);
    }

}
