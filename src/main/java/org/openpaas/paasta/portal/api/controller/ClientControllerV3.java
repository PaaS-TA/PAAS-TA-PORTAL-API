package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.uaa.clients.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.ClientServiceV2;
import org.openpaas.paasta.portal.api.service.ClientServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by CissC2 on 2017-03-20.
 */
@RestController
public class ClientControllerV3 extends Common {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientControllerV3.class);

    @Autowired
    private ClientServiceV3 clientServiceV3;

    /**
     * 클라이언트 목록 조회
     *
     * @param
     * @return Map client list
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V3_URL + "/clients"})
    public ListClientsResponse getClientList() throws Exception {
        return clientServiceV3.getClientList();
    }

    /**
     * 클라이언트 상세 정보 조회
     *
     * @param clientId clientId
     * @return Map client
     * @throws Exception the exception
     */
    @GetMapping(value = {Constants.V3_URL + "/clients/{clientId}"})
    public GetClientResponse getClient(@PathVariable String clientId) throws Exception {
        return clientServiceV3.getClient(clientId);
    }

    /**
     * 클라이언트 등록
     *
     * @param param Map
     * @return Map map
     * @throws Exception the exception
     */
    @PostMapping(value = {Constants.V3_URL + "/clients"})
    public Map registerClient(@RequestBody Map<String, Object> param) throws Exception {
        LOGGER.info("PARAM " + param.toString());

        return clientServiceV3.registerClient(param);
    }

    /**
     * 클라이언트 수정
     *
     * @param param Map
     * @return Map map
     * @throws Exception the exception
     */
    @PutMapping(value = {Constants.V3_URL + "/clients"})
    public Map updateClient(@RequestBody Map<String, Object> param) throws Exception {
        return clientServiceV3.updateClient(param);
    }

    /**
     * 클라이언트 삭제
     *
     * @param clientId
     * @return Map map
     * @throws Exception the exception
     */
    @DeleteMapping(value = {Constants.V3_URL + "/clients/{clientId}"})
    public Map deleteClient(@PathVariable String clientId) throws Exception {
        return clientServiceV3.deleteClient(clientId);
    }

}