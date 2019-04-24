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


    public GetDropletResponse getDroplet(String dropletId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.droplets().get(GetDropletRequest.builder().dropletId(dropletId).build()).block();
    }

    public ListDropletsResponse listDroplet(Droplet droplet, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.droplets().list(ListDropletsRequest.builder().organizationId(droplet.getOrgId()).spaceId(droplet.getSpaceId()).applicationId(droplet.getAppId()).build()).block();
    }

    public CopyDropletResponse copyDroplet(Droplet droplet, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.droplets().copy(CopyDropletRequest.builder().relationships(DropletRelationships.builder().application(ToOneRelationship.builder().data(Relationship.builder().id(droplet.getAppId()).build()).build()).build())
                .sourceDropletId(droplet.getGuid()).build()).block();
    }

    public String deleteDroplet(Droplet droplet, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.droplets().delete(DeleteDropletRequest.builder().dropletId(droplet.getGuid()).build()).block();
    }






}
