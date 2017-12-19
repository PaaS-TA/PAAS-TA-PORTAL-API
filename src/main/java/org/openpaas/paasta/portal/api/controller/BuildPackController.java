package org.openpaas.paasta.portal.api.controller;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.model.BuildPack;
import org.openpaas.paasta.portal.api.service.BuildPackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
     * @param buildPack the buildPack
     * @param request   the request
     * @return boolean boolean
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/buildPack/getBuildPacks"}, method = RequestMethod.POST)
    public Map<String, Object> getBuildPacks(@RequestBody BuildPack buildPack, HttpServletRequest request) throws Exception {

        LOGGER.info("getBuildPacks Start : " + buildPack.getGuid());

        //token setting
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        Map<String, Object> buildPacks = buildPackService.getBuildPacks(buildPack, client);

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
    @RequestMapping(value = {"/buildPack/updateBuildPack"}, method = RequestMethod.POST)
    public Map<String, Object> updateBuildPack(@RequestBody BuildPack buildPack, HttpServletRequest request) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();

        LOGGER.info("updateBuildPack Start : " + buildPack.getGuid());

        //token setting
        CustomCloudFoundryClient client = getCustomCloudFoundryClient(request.getHeader(AUTHORIZATION_HEADER_KEY));

        //service call
        buildPackService.updateBuildPack(buildPack, client);

        LOGGER.info("updateBuildPack End ");

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

}
