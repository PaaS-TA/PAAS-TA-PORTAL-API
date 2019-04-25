package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v3.BuildpackData;
import org.cloudfoundry.client.v3.Lifecycle;
import org.cloudfoundry.client.v3.LifecycleType;
import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.builds.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Builds;
import org.openpaas.paasta.portal.api.service.BuildServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BuildControllerV3 extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildControllerV3.class);

    @Autowired
    BuildServiceV3 buildServiceV3;

    /**
     * build 정보를 가져온다.
     *
     * @param buildId    the build id
     * @param  token    user token
     * @return GetBuildResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/builds/{buildId}")
    public GetBuildResponse getBuilds(@PathVariable String buildId, @RequestHeader(AUTHORIZATION_HEADER_KEY)String token){

        return buildServiceV3.getBuilds(buildId, token);
    }

    /**
     * build 리스트를 가져온다.
     *
     * @param  token    user token
     * @return ListBuildsResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @GetMapping(Constants.V3_URL+"/builds")
    public ListBuildsResponse listBuilds(@RequestHeader(AUTHORIZATION_HEADER_KEY)String token){
        return buildServiceV3.listBuilds(token);
    }

    /**
     * build를 생성한다.
     *
     * @param builds    the Builds
     * @param  token    user token
     * @return CreateBuildResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    @PostMapping(Constants.V3_URL+"/builds")
    public CreateBuildResponse createBuilds(@RequestBody Builds builds, @RequestHeader(AUTHORIZATION_HEADER_KEY)String token){
        return buildServiceV3.createBuilds(builds.getPackageId(), builds.getBuildPack(), builds.getStack(), token);
    }
}
