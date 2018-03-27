package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.buildpacks.ListBuildpacksRequest;
import org.cloudfoundry.client.v2.buildpacks.ListBuildpacksResponse;
import org.cloudfoundry.client.v2.buildpacks.UpdateBuildpackRequest;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.openpaas.paasta.portal.api.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    /**
     * 빌드팩 리스트 조회
     *
     * @param buildPack the buildPack
     * @param reqToken    the reqToken
     * @return the boolean
     * @throws Exception the exception
     */
    public Map<String, Object> getBuildPacks(BuildPack buildPack, String reqToken) throws Exception {
        // Old CF API Version
        //return client.getBuildPacks();

        ListBuildpacksResponse listBuildpacksResponse =
        Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                .buildpacks()
                .list(ListBuildpacksRequest.builder().build())
                .block();

        return ConvertUtil.getObjectMapper().convertValue(listBuildpacksResponse, Map.class);
    }

    /**
     * 빌드팩 정보 수정
     *
     * @param buildPack the buildPack
     * @param client    the client
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean updateBuildPack(BuildPack buildPack, CustomCloudFoundryClient client) throws Exception {

        //client.updateBuildPack(buildPack.getGuid(), buildPack.getPosition(), buildPack.getEnable(), buildPack.getLock());

        //UpdateBuildpackResponse UpdateBuildpackResponse =
        Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                .buildpacks()
                .update(UpdateBuildpackRequest.builder()
                        .buildpackId(buildPack.getGuid().toString())
                        .position(buildPack.getPosition())
                        .enabled(buildPack.getEnable())
                        .locked(buildPack.getLock())
                        .build())
                .block();

//        Map<String, Object> resultMap =
//                ConvertUtil.getObjectMapper().convertValue(UpdateBuildpackResponse, Map.class);
        //LOGGER.info("::::::INININININ:::::"+resultMap.toString());

        return true;

    }

    /**
     * 빌드팩 정보 수정
     *
     * @param buildPack the buildPack
     * @param client    the client
     * @return the boolean
     * @throws Exception the exception
     */
    public boolean updateBuildPack(BuildPack buildPack, String guid) throws Exception {

        //client.updateBuildPack(buildPack.getGuid(), buildPack.getPosition(), buildPack.getEnable(), buildPack.getLock());

        //UpdateBuildpackResponse UpdateBuildpackResponse =
        Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                .buildpacks()
                .update(UpdateBuildpackRequest.builder()
                        .buildpackId(guid)
                        .position(buildPack.getPosition())
                        .enabled(buildPack.getEnable())
                        .locked(buildPack.getLock())
                        .build())
                .block();

//        Map<String, Object> resultMap =
//                ConvertUtil.getObjectMapper().convertValue(UpdateBuildpackResponse, Map.class);
        //LOGGER.info("::::::INININININ:::::"+resultMap.toString());

        return true;

    }

}
