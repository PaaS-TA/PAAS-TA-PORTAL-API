package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.uaa.clients.*;
import org.cloudfoundry.uaa.tokens.GrantType;
import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ClientServiceV2 extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceV2.class);

    public ListClientsResponse getClientList() throws Exception {
        return uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().list(ListClientsRequest.builder().build()).log().block();
    }

    /**
     * 클라이언트 정보 조회
     *
     * @param clientId clientId
     * @return GetClientResponse
     * @throws Exception the exception
     */
    public GetClientResponse getClient(String clientId) throws Exception {
        return uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().get(GetClientRequest.builder().clientId(clientId).build()).log().block();
    }

    /**
     * 클라이언트 등록
     * cloudFoundryClient
     *
     * @param param
     * @return CreateClientResponse
     * @throws Exception the exception
     */
    public CreateClientResponse registerClient(Map<String, Object> param) throws Exception {

        ClientServiceV2.ClientOption clientOption = new ClientServiceV2.ClientOption();
        clientOption = clientOption.setClientOptionByMap(param);
        return uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().create(CreateClientRequest.builder().clientId(clientOption.clientId).clientSecret(clientOption.clientSecret).name(clientOption.name).scopes(clientOption.scopes).authorities(clientOption.authorities).resourceIds(clientOption.resourceIds).authorizedGrantTypes(clientOption.authorizedGrantTypes).redirectUriPatterns(clientOption.redirectUriPatterns).autoApproves(clientOption.autoApproves).tokenSalt(clientOption.tokenSalt).allowedProviders(clientOption.allowedProviders).accessTokenValidity(clientOption.accessTokenValidity).refreshTokenValidity(clientOption.refreshTokenValidity).build()).log().block();
    }

    /**
     * 클라이언트 수정
     *
     * @param param
     * @return Map map
     * @throws Exception the exception
     */
    public UpdateClientResponse updateClient(Map<String, Object> param) throws Exception {

        ClientServiceV2.ClientOption clientOption = new ClientServiceV2.ClientOption();
        clientOption = clientOption.setClientOptionByMap(param);

        LOGGER.info(clientOption.toString());

        //Secret, Token Validity 는 생성 이후 수정 불가
        return uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().update(UpdateClientRequest.builder().clientId(clientOption.clientId)
                .name(clientOption.name).scopes(clientOption.scopes).authorities(clientOption.authorities).resourceIds(clientOption.resourceIds).authorizedGrantTypes(clientOption.authorizedGrantTypes).redirectUriPatterns(clientOption.redirectUriPatterns).autoApproves(clientOption.autoApproves).tokenSalt(clientOption.tokenSalt).allowedProviders(clientOption.allowedProviders)
                .build()).log().block();
    }

    /**
     * 클라이언트 삭제
     *
     * @param clientId
     * @return Map map
     * @throws Exception the exception
     */
    public DeleteClientResponse deleteClient(String clientId) throws Exception {
        return uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().delete(DeleteClientRequest.builder().clientId(clientId).build()).log().block();
    }

    /**
     * ClientOption Inner Class
     * 생성/수정시 사용되는 Option 객체를 생성하기 위한 Inner Class
     *
     * @author CISS
     * @version 1.0
     * @since 2018.6.7 최초작성
     */
    private class ClientOption {

        private List<GrantType> authorizedGrantTypes;
        private List<String> authorities;
        private List<String> scopes;
        private List<String> resourceIds;
        private String name;
        private String clientId;
        private String clientSecret;
        private List<String> redirectUriPatterns;
        private List<String> autoApproves;
        private String tokenSalt;
        private List<String> allowedProviders;
        private long accessTokenValidity;
        private long refreshTokenValidity;

        /**
         * 클라리언트 옵션 Set
         * Map 으로 받은 Option 을 CreateClientRequest Argument 에 맞게 파싱처리
         *
         * @return Map map
         * @see //docs.cloudfoundry.org/api/uaa/version/4.12.0/#create-6
         */
        public ClientOption setClientOptionByMap(Map<String, Object> param) {

            this.authorizedGrantTypes = new ArrayList<>();
            this.authorities = new ArrayList<>();
            this.scopes = new ArrayList<>();
            this.resourceIds = new ArrayList<>();
            this.name = "";
            this.clientId = "";
            this.clientSecret = "";
            this.redirectUriPatterns = new ArrayList<>();
            this.autoApproves = new ArrayList<>();
            this.tokenSalt = "";
            this.allowedProviders = new ArrayList<>();
            this.accessTokenValidity = 0;
            this.refreshTokenValidity = 0;

            Iterator<String> keys = param.keySet().iterator();
            while (keys.hasNext()) {

                String key = keys.next();
                Object value = param.get(key);
                //LOGGER.info( "key : " + key + ", value : " + value);

                // ID
                if (key.equals("client_id")) {
                    clientId = (String) value;
                }

                // Secret
                if (key.equals("client_secret")) {
                    clientSecret = (String) value;
                }

                //authorizedGrantTypes
                if (key.equals("authorized_grant_types")) {
                    if (value.getClass().equals(ArrayList.class)) {
                        for (Object obj : (ArrayList) value) {
                            switch ((String) obj) {
                                case "client_credentials":
                                    authorizedGrantTypes.add(GrantType.CLIENT_CREDENTIALS);
                                    continue;
                                case "authorization_code":
                                    authorizedGrantTypes.add(GrantType.AUTHORIZATION_CODE);
                                    continue;
                                case "implicit":
                                    authorizedGrantTypes.add(GrantType.IMPLICIT);
                                    continue;
                                case "password":
                                    authorizedGrantTypes.add(GrantType.PASSWORD);
                                    continue;
                                case "refresh_token":
                                    authorizedGrantTypes.add(GrantType.REFRESH_TOKEN);
                                    continue;
                                default:
                                    continue;
                            }
                        }
                    }
                }

                // List Item proc
                if (key.equals("authorities") || key.equals("scope") || key.equals("resource_ids") || key.equals("redirect_uri") || key.equals("autoapprove") || key.equals("allowedproviders ") || key.equals("name")) {
                    if (value.getClass().equals(ArrayList.class)) {

                        for (Object obj : (ArrayList) value) {
                            switch (key) {
                                case "authorities": // authorities
                                    authorities.add((String) obj);
                                    continue;
                                case "scope": // scope
                                    scopes.add((String) obj);
                                    continue;
                                case "resource_ids": // resourceIds
                                    resourceIds.add((String) obj);
                                    continue;
                                case "redirect_uri": // redirectUriPatterns
                                    redirectUriPatterns.add((String) obj);
                                    continue;
                                case "autoapprove": // autoApproves
                                    autoApproves.add((String) obj);
                                    continue;
                                case "allowedproviders": // allowedProviders
                                    allowedProviders.add((String) obj);
                                    continue;
                                    // 아래부터는 단건이지만 Front에서 List로 전달하고 있음
                                case "name": // name
                                    name = (String) obj;
                                    break;
                                case "token_salt": // tokenSalt
                                    tokenSalt = (String) obj;
                                    break;
                                case "access_token_validity": // accessTokenValidity(unit: seconds)
                                    accessTokenValidity = Long.parseLong((String) obj);
                                    break;
                                case "refresh_token_validity": // refreshTokenValidity(unit: seconds)
                                    refreshTokenValidity = Long.parseLong((String) obj);
                                    break;
                                default:
                                    continue;
                            }

                        }
                    }
                }

            }
            return this;
        }

        @Override
        public String toString() {
            return "ClientOption{" + "authorizedGrantTypes=" + authorizedGrantTypes + ", authorities=" + authorities + ", scopes=" + scopes + ", resourceIds=" + resourceIds + ", name=" + name + ", clientId=" + clientId + ", clientSecret=" + clientSecret + ", redirectUriPatterns=" + redirectUriPatterns + ", autoApproves=" + autoApproves + ", tokenSalt=" + tokenSalt + ", allowedProviders=" + allowedProviders + ", accessTokenValidity=" + accessTokenValidity + ", refreshTokenValidity=" + refreshTokenValidity + "}";
        }
    }

}

