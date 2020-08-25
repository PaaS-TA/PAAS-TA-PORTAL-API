package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.client.v2.buildpacks.ListBuildpacksRequest;
import org.cloudfoundry.client.v2.buildpacks.ListBuildpacksResponse;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackRequest;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Map;

@EnableAsync
@Service
public class BuildPackServiceV2 extends Common{


    /**
     * 빌드팩 리스트 조회
     *
     * @return the boolean
     * @throws Exception the exception
     */
    public Map<String, Object> getBuildPacks() throws Exception {
        ListBuildpacksResponse listBuildpacksResponse =
                cloudFoundryClient(connectionContext(), tokenProvider())
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
    public boolean updateBuildPack(BuildPack buildPack) throws Exception {

        cloudFoundryClient(connectionContext(), tokenProvider())
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
