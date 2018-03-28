package org.openpaas.paasta.portal.api.controller;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.openpaas.paasta.portal.api.service.BuildPackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 빌드팩 컨트롤러 - 빌드팩 정보를 조회, 수정한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
@Transactional
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildPackController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildPackController.class);

    @Autowired
    private BuildPackService buildPackService;


    /**
     * 빌드팩 리스트 가져오기
     *
     * @param request   the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/buildPack/buildpacks"}, method = RequestMethod.GET)
    public Map<String, Object> getBuildPacks(HttpServletRequest request) throws Exception {

        LOGGER.info("getBuildPacks Start");

        //token setting
        //CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

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
    @RequestMapping(value = {"/buildPack/buildpacks/{guid}"}, method = RequestMethod.PUT)
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
