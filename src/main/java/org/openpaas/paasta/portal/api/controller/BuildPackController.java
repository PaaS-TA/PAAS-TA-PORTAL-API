package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.openpaas.paasta.portal.api.service.BuildPackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
public class BuildPackController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildPackController.class);
    private final String V2_URL = "/v2";

    @Autowired
    private BuildPackService buildPackService;


    /**
     * 빌드팩 리스트 가져오기
     *
     * @param request   the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @GetMapping(value = {V2_URL + "/buildpacks"})
    public Map<String, Object> getBuildPacks(HttpServletRequest request) throws Exception {

        LOGGER.info("getBuildPacks Start");

        //service call
        Map<String, Object> buildPacks = buildPackService.getBuildPacks();

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
     */
    @PutMapping(value = {V2_URL + "/buildpacks/{guid}"})
    public Map<String, Object> updateBuildPack(@RequestBody BuildPack buildPack,@PathVariable String guid,HttpServletRequest request) throws Exception {

        LOGGER.info("updateBuildPack Start : " + guid);

        Map<String, Object> resultMap = new HashMap<>();

        //token setting
        //CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        buildPack.setGuid(UUID.fromString(guid));
        buildPackService.updateBuildPack(buildPack);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        LOGGER.info("updateBuildPack End ");

        return resultMap;
    }
}
