package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v3.applications.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.AppServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class AppControllerV3 extends Common {

    @Autowired
    private AppServiceV3 appServiceV3;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV3.class);


    /**
     * 앱 요약 정보를 조회한다.
     *
     * @param guid
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @RequestMapping(value = {Constants.V3_URL + "/apps/{guid}/summary"}, method = RequestMethod.GET)
    public void getAppSummary(@PathVariable String guid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("getAppSummary Start : " + guid);

        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        GetApplicationResponse getApplicationResponse = reactorCloudFoundryClient.applicationsV3().get(org.cloudfoundry.client.v3.applications.GetApplicationRequest.builder().applicationId(guid).build()).block();
        GetApplicationCurrentDropletResponse getApplicationCurrentDropletResponse = reactorCloudFoundryClient.applicationsV3().getCurrentDroplet(GetApplicationCurrentDropletRequest.builder().applicationId(guid).build()).block();
        GetApplicationProcessResponse getApplicationProcessResponse = reactorCloudFoundryClient.applicationsV3().getProcess(GetApplicationProcessRequest.builder().applicationId(guid).build()).block();
        GetApplicationProcessStatisticsResponse processStatisticsResponse = reactorCloudFoundryClient.applicationsV3().getProcessStatistics(GetApplicationProcessStatisticsRequest.builder().applicationId(guid).build()).block();

        


    }


}
