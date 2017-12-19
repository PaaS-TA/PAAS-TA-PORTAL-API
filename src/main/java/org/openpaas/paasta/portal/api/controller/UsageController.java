package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.model.Usage;
import org.openpaas.paasta.portal.api.service.UsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 사용량 관련 API 를 호출 받는 컨트롤러이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.22 최초작성
 */
@RestController
@RequestMapping(value = {"/usage"})
public class UsageController {

    private final UsageService usageService;

    @Autowired
    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }


    /**
     * 사용량 조직을 조회한다.
     *
     * @param req HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getUsageOrganizationList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getUsageOrganizationList(HttpServletRequest req) throws Exception {
        return usageService.getUsageOrganizationList(req);
    }


    /**
     * 사용량 공간을 조회한다.
     *
     * @param param Usage(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getUsageSpaceList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getUsageSpaceList(@RequestBody Usage param, HttpServletRequest req) throws Exception {
        return usageService.getUsageSpaceList(param, req);
    }


    /**
     * 사용량을 조회한다.
     *
     * @param param Usage(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/getUsageSearchList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getUsageSearchList(@RequestBody Usage param) throws Exception {
        return usageService.getUsageSearchList(param);
    }
}
