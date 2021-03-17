package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.client.v2.serviceinstances.UpdateServiceInstanceRequest;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.uaa.UaaException;
import org.cloudfoundry.uaa.clients.*;
import org.cloudfoundry.uaa.tokens.GrantType;
import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLClientInfoException;
import java.util.*;

@Service
public class ClientServiceV3 extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceV3.class);

    public ListClientsResponse getClientList() throws Exception {
        return uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().list(ListClientsRequest.builder().build()).block();
    }

    /**
     * 클라이언트 정보 조회
     *
     * @param clientId clientId
     * @return GetClientResponse
     * @throws Exception the exception
     */
    public GetClientResponse getClient(String clientId) throws Exception {
        return uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().get(GetClientRequest.builder().clientId(clientId).build()).block();
    }

    /**
     * 클라이언트 등록
     * cloudFoundryClient
     *
     * @param param
     * @return CreateClientResponse
     * @throws Exception the exception
     */
    public Map registerClient(Map<String, Object> param) throws Exception {

        ClientServiceV3.ClientOption clientOption = new ClientServiceV3.ClientOption();
        clientOption = clientOption.setClientOptionByMap(param);
        //LOGGER.info("ClientOPTION ::: " + clientOption.toString());
        Map result = new HashMap();
        try {
            uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().create(CreateClientRequest.builder().clientId(clientOption.clientId).clientSecret(clientOption.clientSecret).name(clientOption.name).scopes(clientOption.scopes).authorities(clientOption.authorities).resourceIds(clientOption.resourceIds).authorizedGrantTypes(clientOption.authorizedGrantTypes).redirectUriPatterns(clientOption.redirectUriPatterns).autoApproves(clientOption.autoApproves).tokenSalt(clientOption.tokenSalt).allowedProviders(clientOption.allowedProviders).accessTokenValidity(clientOption.accessTokenValidity).refreshTokenValidity(clientOption.refreshTokenValidity).build()).block();
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (UaaException n) {

            if(n.getStatusCode() == 400) {
                result.put("result", false);
                result.put("msg", "If authorized_grant_types type contains authorization_code, enter redirect_uri.");
            }else{
                result.put("result", false);
                result.put("msg", n.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 클라이언트 수정
     *
     * @param param
     * @return Map map
     * @throws Exception the exception
     */
    public Map updateClient(Map<String, Object> param) {

        ClientServiceV3.ClientOption clientOption = new ClientServiceV3.ClientOption();
        clientOption = clientOption.setClientOptionByMap(param);


        Map result = new HashMap();
        try {
            uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().update(UpdateClientRequest.builder().clientId(clientOption.clientId).name(clientOption.name).scopes(clientOption.scopes).authorities(clientOption.authorities).resourceIds(clientOption.resourceIds).authorizedGrantTypes(clientOption.authorizedGrantTypes).redirectUriPatterns(clientOption.redirectUriPatterns).autoApproves(clientOption.autoApproves).tokenSalt(clientOption.tokenSalt).allowedProviders(clientOption.allowedProviders).build()).block();
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 클라이언트 삭제
     *
     * @param clientId
     * @return Map map
     * @throws Exception the exception
     */
    public Map deleteClient(String clientId) {
        Map result = new HashMap();
        try {
            uaaAdminClient(connectionContext(), adminUserName, adminPassword, uaaAdminClientId, uaaAdminClientSecret).clients().delete(DeleteClientRequest.builder().clientId(clientId).build()).block();
            result.put("result", true);
            result.put("msg", "You have successfully completed the task.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
            result.put("msg", e.getMessage());
        }
        return result;

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
                ////LOGGER.info( "key : " + key + ", value : " + value);

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
                            authorizedGrantTypes.add(grantType(obj.toString()));
                        }
                    } else if (value.getClass().equals(String.class)) {
                        String agts = value.toString().replace("\"", "").replace("[", "").replace("]", "");
                        String[] agts_split = agts.split(",");
                        for (String obj : agts_split) {
                            obj = obj.trim();
                            authorizedGrantTypes.add(grantType(obj));
                        }
                    }

                }

                // List Item proc
                if (key.equals("authorities") || key.equals("scope") || key.equals("resource_ids") || key.equals("redirect_uri") || key.equals("autoapprove") || key.equals("allowedproviders") || key.equals("name")) {
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
                                default:
                                    continue;
                            }

                        }
                    }
                } else if (key.equals("name") || key.equals("token_salt") || key.equals("access_token_validity") || key.equals("refresh_token_validity")) {
                    switch (key) {
                        case "name": // name
                            name = (String) value;
                            break;
                        case "token_salt": // tokenSalt
                            tokenSalt = (String) value;
                            break;
                        case "access_token_validity": // accessTokenValidity(unit: seconds)
                            if (value != "" && value != null) {
                                accessTokenValidity = Long.parseLong((String) value);
                            }
                            break;
                        case "refresh_token_validity": // refreshTokenValidity(unit: seconds)
                            if (value != "" && value != null) {
                                refreshTokenValidity = Long.parseLong((String) value);
                            }
                            break;
                        default:
                            continue;
                    }
                }


            }
            return this;
        }


        private GrantType grantType(String obj) {
            switch (obj.toLowerCase()) {
                case "client_credentials":
                    return GrantType.CLIENT_CREDENTIALS;
                case "authorization_code":
                    return GrantType.AUTHORIZATION_CODE;
                case "implicit":
                    return GrantType.IMPLICIT;
                case "password":
                    return GrantType.PASSWORD;
                case "refresh_token":
                    return GrantType.REFRESH_TOKEN;
                default:
                    return null;
            }
        }


        @Override
        public String toString() {
            return "ClientOption{" + "authorizedGrantTypes=" + authorizedGrantTypes + ", authorities=" + authorities + ", scopes=" + scopes + ", resourceIds=" + resourceIds + ", name=" + name + ", clientId=" + clientId + ", clientSecret=" + clientSecret + ", redirectUriPatterns=" + redirectUriPatterns + ", autoApproves=" + autoApproves + ", tokenSalt=" + tokenSalt + ", allowedProviders=" + allowedProviders + ", accessTokenValidity=" + accessTokenValidity + ", refreshTokenValidity=" + refreshTokenValidity + "}";
        }
    }

}

