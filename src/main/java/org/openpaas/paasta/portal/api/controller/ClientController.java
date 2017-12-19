package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ClientController.java
 * 클라이언트 목록 , 클라이언트 삭제 등 클라이언트 관련 API를 호출 받는 컨트롤러
 *
 * @author 김영지
 * @version 1.0
 * @since 2016.9.29 최초작성
 */
@RestController
@RequestMapping(value = {"/client"})
public class ClientController extends Common {

    @Autowired
    private ClientService clientService;

    /**
     * 클라이언트 목록 조회
     *
     * @param param Map
     * @return Map client list
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getClientList"}, method = RequestMethod.POST)
    public Map<String, Object> getClientList(@RequestBody Map<String, Object> param) throws Exception {
        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(adminUserName, adminPassword);
        return clientService.getClientList(adminCcfc, param);
    }

    /**
     * 클라이언트 상세 정보 조회
     *
     * @param param Map
     * @return Map client
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getClient"}, method = RequestMethod.POST)
    public Map<String, Object> getClient(@RequestBody Map<String, Object> param) throws Exception {
        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(adminUserName, adminPassword);
        return clientService.getClient(adminCcfc, param);
    }

    /**
     * 클라이언트 등록
     *
     * @param param Map
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/registerClient"}, method = RequestMethod.POST)
    public Map<String, Object> registerClient(@RequestBody Map<String, Object> param) throws Exception {
        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(uaaAdminClientId, uaaAdminClientSecret);
        return clientService.registerClient(adminCcfc, param);
    }

    /**
     * 클라이언트 수정
     *
     * @param param Map
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/updateClient"}, method = RequestMethod.POST)
    public Map<String, Object> updateClient(@RequestBody Map<String, Object> param) throws Exception {
        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(uaaAdminClientId, uaaAdminClientSecret);
        return clientService.updateClient(adminCcfc, param);
    }

    /**
     * 클라이언트 삭제
     *
     * @param param Map
     * @return Map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteClient"}, method = RequestMethod.POST)
    public Map<String, Object> deleteClient(@RequestBody Map<String, Object> param) throws Exception {
        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(uaaAdminClientId, uaaAdminClientSecret);
        return clientService.deleteClient(adminCcfc, param);
    }

}