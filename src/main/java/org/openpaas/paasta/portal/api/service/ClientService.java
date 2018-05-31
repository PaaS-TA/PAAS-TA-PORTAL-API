package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.uaa.clients.*;
import org.cloudfoundry.uaa.tokens.GrantType;
import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ClientService.java
 * 클라이언트 목록 , 클라이언트 삭제 등 클라이언트 관리에 필요한 기능을 구현한 서비스 클래스
 * Common 클래스를 상속하여 구현
 *
 * @author 김영지
 * @version 1.0
 * @since 2016.9.29 최초작성
 */
@Service
@Transactional
public class ClientService extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

    //V2
    public ListClientsResponse getClientList() throws Exception {

        return Common.uaaClient(connectionContext(),"yschoi2" ,"1qaz@WSX" )
                        .clients()
                        .list(ListClientsRequest.builder()
                                .build())
                        .log()
                        .block();
        /* ERROR io.netty.util.ResourceLeakDetector - LEAK: ByteBuf.release() was not called before it's garbage-collected. */
        //ListClientsResponse listClientsResponse = Common.cloudFoundryOperations(connectionContext(), tokenProvider(token)).getUaaClient().clients().list(ListClientsRequest.builder().build()).block();
    }

    /**
     * 클라이언트 정보 조회
     *
     * @param clientId clientId
     * @return GetClientResponse
     * @throws Exception the exception
     */
    public GetClientResponse getClient(String clientId) throws Exception {
        return Common.uaaClient(connectionContext(),"yschoi2" ,"1qaz@WSX" )
                .clients()
                .get(GetClientRequest.builder()
                    .clientId(clientId)
                    .build()
                ).log()
                .block();
    }

    /**
     * 클라이언트 등록
     *
     * @param param
     * @return CreateClientResponse
     * @throws Exception the exception
     */
    public CreateClientResponse registerClient(Map<String, Object> param) throws Exception {
        // authorizedGrantTypes
        List<GrantType> authorizedGrantTypesList = new ArrayList<>();

        Iterator<String> keys = param.keySet().iterator();
        while( keys.hasNext() ){
            String key = keys.next();
            Object value = param.get(key);
            LOGGER.info( "key : " + key + ", value : " + value);

            //authorizedGrantTypes
            if(key.equals("authorized_grant_types")){
                if( value.getClass().equals(ArrayList.class)) {
                    for(Object obj :  (ArrayList)value){
                        switch ((String)obj) {
                            case "client_credentials":
                                authorizedGrantTypesList.add(GrantType.CLIENT_CREDENTIALS);
                                break;
                            case "authorization_code":
                                authorizedGrantTypesList.add(GrantType.AUTHORIZATION_CODE);
                                break;
                            case "implicit":
                                authorizedGrantTypesList.add(GrantType.IMPLICIT);
                                break;
                            case "password":
                                authorizedGrantTypesList.add(GrantType.PASSWORD);
                                break;
                            case "refresh_token":
                                authorizedGrantTypesList.add(GrantType.REFRESH_TOKEN);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        // Client ID
        String clientId = (String)param.get("client_id");
        // Client Secret
        String clientSecret = (String)param.get("client_secret");

        return Common.uaaClient(connectionContext(),"yschoi2" ,"1qaz@WSX" )
                .clients()
                .create(CreateClientRequest.builder()
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    //.redirectUriPattern("http://aaaa/*")
                    .scope("")
                    //.scopes()
                    .authorizedGrantTypes(authorizedGrantTypesList)
                    .build()
                ).log()
                .block();
    }

    /**
     * 클라이언트 수정
     *
     * @param param
     * @return Map map
     * @throws Exception the exception
     */
    public UpdateClientResponse updateClient(Map<String, Object> param) throws Exception {
        return Common.uaaClient(connectionContext(),"yschoi2" ,"1qaz@WSX" )
                .clients()
                .update(UpdateClientRequest.builder()
                    .authority()
                        .autoApprove()
                        .build()
                ).log()
                .block();
    }

    /**
     * 클라이언트 삭제
     *
     * @param clientId
     * @return Map map
     * @throws Exception the exception
     */
    public DeleteClientResponse deleteClient(String clientId) throws Exception {
        return Common.uaaClient(connectionContext(),"yschoi2" ,"1qaz@WSX" )
                .clients()
                .delete(DeleteClientRequest.builder()
                        .clientId(clientId)
                        .build()
                ).log()
                .block();
    }

}