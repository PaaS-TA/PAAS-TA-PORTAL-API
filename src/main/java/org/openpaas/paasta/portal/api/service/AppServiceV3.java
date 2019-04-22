package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.v3.applications.GetApplicationRequest;
import org.cloudfoundry.client.v3.applications.GetApplicationResponse;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AppServiceV3 extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceV3.class);


    public void getAppSummary(String guid, String token) {
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient();
        GetApplicationResponse getApplicationResponse =  cloudFoundryClient.applicationsV3().get(GetApplicationRequest.builder().applicationId(guid).build()).block();
        LOGGER.info(getApplicationResponse.getRelationships().toString());

    }


}
