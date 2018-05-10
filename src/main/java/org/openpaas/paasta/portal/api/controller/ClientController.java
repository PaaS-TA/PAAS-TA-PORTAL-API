package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.service.AppService;
import org.openpaas.paasta.portal.api.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping(value = {"/client"})
public class ClientController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

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
    public Map<String, Object> getClientList(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception {
//        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(adminUserName, adminPassword);
//
//        LOGGER.info("AUTHORIZATION_HEADER_KEY:::"+request.getHeader(AUTHORIZATION_HEADER_KEY));
//
//        return clientService.getClientList(adminCcfc, param);
        return null;

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
//        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(adminUserName, adminPassword);
//        return clientService.getClient(adminCcfc, param);

        return null;
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
//        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(uaaAdminClientId, uaaAdminClientSecret);
//        return clientService.registerClient(adminCcfc, param);

        return null;
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
//        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(uaaAdminClientId, uaaAdminClientSecret);
//        return clientService.updateClient(adminCcfc, param);

        return null;
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
//        CustomCloudFoundryClient adminCcfc = getCustomCloudFoundryClient(uaaAdminClientId, uaaAdminClientSecret);
//        return clientService.deleteClient(adminCcfc, param);

        return null;
    }

}