package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v3.droplets.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Droplet;
import org.springframework.stereotype.Service;

@Service
public class DropletServiceV3 extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(DropletServiceV3.class);

    /**
     * droplet 정보를 가져온다.
     *
     * @param dropletId    the droplet id
     * @param  token    user token
     * @return GetDropletResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public GetDropletResponse getDroplet(String dropletId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.droplets().get(GetDropletRequest.builder().dropletId(dropletId).build()).block();
    }

    /**
     * droplet 리스트를 가져온다.
     *
     * @param orgId    the organization id
     * @param spaceId    the space id
     * @param dropletId    the droplet id
     * @param  token    user token
     * @return ListDropletsResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public ListDropletsResponse listDroplet(String orgId, String spaceId, String dropletId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.droplets().list(ListDropletsRequest.builder().organizationId(orgId).spaceId(spaceId).applicationId(dropletId).build()).block();
    }

    /**
     * droplet을 복사한다.
     *
     * @param droplet    the Droplet
     * @param  token    user token
     * @return CopyDropletResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public CopyDropletResponse copyDroplet(Droplet droplet, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.droplets().copy(CopyDropletRequest.builder().relationships(DropletRelationships.builder().application(ToOneRelationship.builder().data(Relationship.builder().id(droplet.getAppId()).build()).build()).build())
                .sourceDropletId(droplet.getGuid()).build()).block();
    }

    /**
     * droplet을 복사한다.
     *
     * @param dropletId    the Droplet Id
     * @param  token    user token
     * @return String
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public String deleteDroplet(String dropletId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.droplets().delete(DeleteDropletRequest.builder().dropletId(dropletId).build()).block();
    }






}
