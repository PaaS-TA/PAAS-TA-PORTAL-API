package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.model.CommonCode;
import org.openpaas.paasta.portal.api.service.CommonCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 코드 목록 조회, 등록, 삭제 등 코드 관리의 API 를 호출 받는 컨트롤러이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.06.15 최초작성
 */
@RestController
@RequestMapping(value = {"/commonCode"})
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    @Autowired
    public CommonCodeController(CommonCodeService commonCodeService) {
        this.commonCodeService = commonCodeService;
    }


    /**
     * 공통코드 목록을 조회한다.
     *
     * @param codeId String(아이디)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getCommonCode/{codeId}"}, method = RequestMethod.GET, consumes = "application/json")
    public Map<String, Object> getCommonCode(@PathVariable("codeId") String codeId) {
        return commonCodeService.getCommonCodeById(codeId);
    }


    /**
     * 공통코드 목록을 조회한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getCommonCode"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getCommonCode(@RequestBody CommonCode param) {
        return commonCodeService.getCommonCode(param);
    }


    /**
     * 공통코드를 저장한다.
     *
     * @param param CommonCode(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/insertCommonCode"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertCommonCode(@RequestBody CommonCode param, HttpServletResponse res) throws Exception {
        return commonCodeService.insertCommonCode(param, res);
    }


    /**
     * 공통코드를 수정한다.
     *
     * @param param CommonCode(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/updateCommonCode"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateCommonCode(@RequestBody CommonCode param, HttpServletResponse res) throws Exception {
        return commonCodeService.updateCommonCode(param, res);
    }


    /**
     * 공통코드를 삭제한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/deleteCommonCode"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteCommonCode(@RequestBody CommonCode param) {
        return commonCodeService.deleteCommonCode(param);
    }
}
