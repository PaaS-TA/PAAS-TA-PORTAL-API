package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.model.UserManagement;
import org.openpaas.paasta.portal.api.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 사용자 목록, 사용자 삭제 및 운영자 권한 부여 등의 API 를 호출 받는 컨트롤러이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.08.31 최초작성
 */
@RestController
@RequestMapping(value = {"/userManagement"})
public class UserManagementController {

    private final UserManagementService userManagementService;

    @Autowired
    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }


    /**
     * 사용자 정보 목록을 조회한다.
     *
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getUserInfoList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getUserInfoList(@RequestBody UserManagement param) {
        return userManagementService.getUserInfoList(param);
    }


    /**
     * 비밀번호를 초기화한다.
     *
     * @param param UserManagement(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/setResetPassword"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> setResetPassword(@RequestBody UserManagement param) throws Exception {
        return userManagementService.setResetPassword(param);
    }


    /**
     * 운영권한을 부여한다.
     *
     * @param param UserManagement(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/updateOperatingAuthority"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> updateOperatingAuthority(@RequestBody UserManagement param) {
        return userManagementService.updateOperatingAuthority(param);
    }


    /**
     * 사용자 계정을 삭제한다.
     *
     * @param param UserManagement(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    @RequestMapping(value = {"/deleteUserAccount"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteUserAccount(@RequestBody UserManagement param) throws Exception {
        return userManagementService.deleteUserAccount(param);
    }
}
