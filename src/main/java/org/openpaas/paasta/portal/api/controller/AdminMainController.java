package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.model.AdminMain;
import org.openpaas.paasta.portal.api.service.AdminMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 운영자 포탈 관리자 대시보드 관련 API 를 호출 받는 컨트롤러이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.08 최초작성
 */
@RestController
@RequestMapping(value = {"/adminMain"})
public class AdminMainController {

    private final AdminMainService adminMainService;

    @Autowired
    public AdminMainController(AdminMainService adminMainService) {
        this.adminMainService = adminMainService;
    }


    /**
     * 전체 조직 수, 영역 수, APPLICATION 수, 사용자 수 목록을 조회한다.
     *
     * @param param AdminMain(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getTotalCountList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getTotalCountList(@RequestBody AdminMain param) {
        //return adminMainService.getTotalCountList(param);
        return new HashMap<String, Object>() {{
            put("list", "");
        }};
    }


    /**
     * 전체 조직 통계 목록을 조회한다.
     *
     * @param param AdminMain(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getTotalOrganizationList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getTotalOrganizationList(@RequestBody AdminMain param) {
        //return adminMainService.getTotalOrganizationList(param);
        return new HashMap<String, Object>() {{
            put("list", "");
        }};
    }


    /**
     * 전체 공간 통계 목록을 조회한다.
     *
     * @param param AdminMain(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getTotalSpaceList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getTotalSpaceList(@RequestBody AdminMain param) {
        //return adminMainService.getTotalSpaceList(param);
        return new HashMap<String, Object>() {{
            put("list", "");
        }};
    }
}
