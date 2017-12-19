package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
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
     * @param buildPack the buildPack
     * @param client    the client
     * @return the boolean
     * @throws Exception the exception
     */
    public Map<String, Object> getBuildPacks(BuildPack buildPack, CustomCloudFoundryClient client) throws Exception {

        return client.getBuildPacks();

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

        client.updateBuildPack(buildPack.getGuid(), buildPack.getPosition(), buildPack.getEnable(), buildPack.getLock());

        return true;

    }


}
