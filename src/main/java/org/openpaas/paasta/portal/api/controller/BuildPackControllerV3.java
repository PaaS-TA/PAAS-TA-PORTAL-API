package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.openpaas.paasta.portal.api.service.BuildPackServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by swmoon on 2017-12-19.
 */
@RestController
public class BuildPackControllerV3 extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildPackControllerV3.class);

    @Autowired
    private BuildPackServiceV3 buildPackServiceV3;


    /**
     * 빌드팩 리스트 가져오기
     *
     * @param request   the request
     * @return boolean boolean
     * @throws Exception the exception
     * 권한 : 관리자
     */
    @GetMapping(value = {Constants.V3_URL + "/buildpacks"})
    public Map<String, Object> getBuildPacks(HttpServletRequest request) throws Exception {

        LOGGER.info("getBuildPacks Start");
        Map<String, Object> buildPacks = buildPackServiceV3.getBuildPacks();
        LOGGER.info("getBuildPacks End ");

        return buildPacks;

    }


    /**
     * 빌드팩 정보 수정
     *
     * @param buildPack the buildPack
     * @param request   the request
     * @return boolean boolean
     * @throws Exception the exception
     * 권한 :  관리자
     */
    @PutMapping(value = {Constants.V3_URL + "/buildpacks/{guid}"})
    public Map<String, Object> updateBuildPack(@RequestBody BuildPack buildPack,@PathVariable String guid,HttpServletRequest request) throws Exception {

        LOGGER.info("updateBuildPack Start : " + guid);
        Map<String, Object> resultMap = new HashMap<>();
        buildPack.setGuid(UUID.fromString(guid));
        buildPackServiceV3.updateBuildPack(buildPack);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        LOGGER.info("updateBuildPack End ");

        return resultMap;
    }
}
