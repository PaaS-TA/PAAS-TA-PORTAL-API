package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.client.v2.buildpacks.ListBuildpacksRequest;
import org.cloudfoundry.client.v2.buildpacks.ListBuildpacksResponse;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackRequest;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;

/**
 * 빌드팩 컨트롤러 - 빌드팩 정보를 조회, 수정한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@EnableAsync
@org.springframework.stereotype.Service
public class BuildPackService extends Common {

    /**
     * 빌드팩 리스트 조회
     *
     * @return the boolean
     * @throws Exception the exception
     */
    //@HystrixCommand(commandKey = "getBuildPacks")
    public Map<String, Object> getBuildPacks() throws Exception {
        ListBuildpacksResponse listBuildpacksResponse =
        Common.cloudFoundryClient(connectionContext(), tokenProvider())
                .buildpacks()
                .list(ListBuildpacksRequest.builder().build())
                .block();

        return objectMapper.convertValue(listBuildpacksResponse, Map.class);
    }

    /**
     * 빌드팩 정보 수정
     *
     * @param buildPack the buildPack
     * @return the boolean
     * @throws Exception the exception
     */
    //@HystrixCommand(commandKey = "updateBuildPack")
    public boolean updateBuildPack(BuildPack buildPack) throws Exception {

        Common.cloudFoundryClient(connectionContext(), tokenProvider())
                .buildpacks()
                .update(UpdateBuildpackRequest.builder()
                        .buildpackId(buildPack.getGuid().toString())
                        .position(buildPack.getPosition())
                        .enabled(buildPack.getEnable())
                        .locked(buildPack.getLock())
                        .build())
                .block();

        return true;
    }

}
