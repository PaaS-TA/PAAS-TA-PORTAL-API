package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v3.BuildpackData;
import org.cloudfoundry.client.v3.Lifecycle;
import org.cloudfoundry.client.v3.LifecycleType;
import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.builds.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.stereotype.Service;

@Service
public class BuildServiceV3 extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildServiceV3.class);


    /**
     * build 정보를 가져온다.
     *
     * @param buildId    the build id
     * @param  token    user token
     * @return GetBuildResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public GetBuildResponse getBuilds(String buildId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.builds().get(GetBuildRequest.builder().buildId(buildId).build()).block();
    }

    /**
     * build 리스트를 가져온다.
     *
     * @param  token    user token
     * @return ListBuildsResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public ListBuildsResponse listBuilds(String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient();
        ListBuildsResponse listBuildsResponse = reactorCloudFoundryClient.builds().list(ListBuildsRequest.builder().build()).block();
        int i;
        for(i = 1 ; listBuildsResponse.getPagination().getTotalPages().intValue() > i ; i++){
            listBuildsResponse.getResources().addAll(reactorCloudFoundryClient.builds().list(ListBuildsRequest.builder().page(i+1).build()).block().getResources());
        }
        return listBuildsResponse;
    }

    /**
     * build를 생성한다.
     *
     * @param packageId    the package id
     * @param buildPack    the buildpack name
     * @param Stack    the stack name
     * @param  token    user token
     * @return CreateBuildResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public CreateBuildResponse createBuilds(String packageId, String buildPack, String Stack, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.builds().create(CreateBuildRequest.builder()
                .getPackage(Relationship.builder().id(packageId).build())
                .lifecycle(Lifecycle.builder().data(BuildpackData.builder().buildpack(buildPack).stack(Stack).build()).type(LifecycleType.BUILDPACK).build()).build()).block();
    }


}
