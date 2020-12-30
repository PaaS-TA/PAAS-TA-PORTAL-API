package org.openpaas.paasta.portal.api.controller;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.LinkedHashMap;
import java.util.Map;

import org.cloudfoundry.client.v2.info.GetInfoRequest;
import org.cloudfoundry.client.v2.info.GetInfoResponse;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.LoginServiceV1;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Created on 2020-11-05.
 */
@RestController
public class SshController extends Common {

    private final Logger LOGGER = getLogger(SshController.class);
    
    @Autowired
    LoginServiceV1 loginService;

    /**
     * cf 정보 조회
     * 
     * @param token
     * @return
     */
    @GetMapping(Constants.SSH_URL + "/v2/Info")
    public GetInfoResponse getV2Info(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {

    	ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
    	Mono<GetInfoResponse> infoResponse = reactorCloudFoundryClient.info().get(GetInfoRequest.builder().build());
    	
        return infoResponse.block();
    }

    /**
     * SSH-CODE 를 조회 한다
     *
     * @param token
     * @return
     */
    @GetMapping(Constants.SSH_URL + "/ssh-code")
    public Map<String, Object> getSshCode(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
    	Map<String, Object> resultMap = new LinkedHashMap<>();
    	
    	Mono<String> sshCode = cloudFoundryOperations().advanced().sshCode();
    	
    	resultMap.put("sshCode", sshCode.block());
    	return resultMap;
    }

}
