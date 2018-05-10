package org.openpaas.paasta.portal.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.uaa.clients.ListClientsRequest;
import org.cloudfoundry.uaa.clients.ListClientsResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

//    public Map getClientList(CustomCloudFoundryClient customCloudFoundryClient, Map<String, Object> param) throws Exception {
//
//        ResponseEntity<String> responseEntity = customCloudFoundryClient.getClientList(uaaClientId, uaaClientSecret, uaaTarget);
//
//        String str = responseEntity.getBody();
//        JSONArray rtnArray = new JSONArray();
//
//        try {
//
//            JSONParser jsonParser = new JSONParser();
//
//            //JSON데이터를 넣어 JSON Object 로 만들어 준다.
//            JSONObject stringToJson = (JSONObject) jsonParser.parse(str);
//
//            //배열을 추출
//            rtnArray = (JSONArray) stringToJson.get("resources");
//
//        } catch (Exception e) {
//            return null;
//        }
//
//        Map<String, Object> resultMap = new HashMap<>();
//
//        resultMap.put("list", rtnArray);
//        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
//
//        return resultMap;
//
//    }

    //V2
    public Map getClientList(Map<String, Object> param) throws Exception {

        //ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(connectionContext(), tokenProvider(token));


        //Test
        String tempToken = this.getToken();

//        LOGGER.info("::token:::::::"+tempToken);
//        ReactorUaaClient uaaClient = uaaClient(connectionContext(), tokenProvider(tempToken));


//        ListOrganizationsResponse listOrganizationsResponse =
//
//        ListOrganizationsResponse listOrganizationsResponse = Common.cloudFoundryClient(connectionContext(), tokenProvider(tempToken)).organizations()
//                .list(ListOrganizationsRequest.builder()
//                        //.page(1)
//                        .build()).block();
//                .flatMapIterable(ListOrganizationsResponse::getResources)
//                .map(resource -> OrganizationSummary.builder()
//                        .id(resource.getMetadata().getId())
//                        .name(resource.getEntity().getName())
//                        .build());


//        LOGGER.info(":::::::::::::::::::"+listOrganizationsResponse.toString());

        LOGGER.info(":::::::::::::::::SJJJSJJJS::::"+tokenProvider(adminUserName,adminPassword).getToken(connectionContext()).block());

//tokenProvider(tempToken)
        ListClientsResponse listClientsResponse = Common.uaaClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                .clients()
                .list(ListClientsRequest.builder()
                        .build())
                .block();


        /* ERROR io.netty.util.ResourceLeakDetector - LEAK: ByteBuf.release() was not called before it's garbage-collected. */
        //ListClientsResponse listClientsResponse = Common.cloudFoundryOperations(connectionContext(), tokenProvider(token)).getUaaClient().clients().list(ListClientsRequest.builder().build()).block();


LOGGER.info("WHAT:::::::::");
        //ListClientsRequest.builder()::filter
        ObjectMapper objectMapper = new ObjectMapper();

        //String str = responseEntity.getBody();

        String str = "";
        JSONArray rtnArray = new JSONArray();

//        LOGGER.info("objectMapper.writeValueAsString(listClientsResponse)::::::::"+objectMapper.writeValueAsString(listClientsResponse));

        try {

            JSONParser jsonParser = new JSONParser();

            //JSON데이터를 넣어 JSON Object 로 만들어 준다.
            JSONObject stringToJson = (JSONObject) jsonParser.parse(objectMapper.writeValueAsString(listClientsResponse));

            //배열을 추출
            rtnArray = (JSONArray) stringToJson.get("resources");

        } catch (Exception e) {
            return null;
        }

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("list", rtnArray);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;

    }

    /**
     * 클라이언트 정보 조회
     *
     * @param customCloudFoundryClient CustomCloudFoundryClient
     * @param param                    Map
     * @return Map client
     * @throws Exception the exception
     */
//    public Map getClient(CustomCloudFoundryClient customCloudFoundryClient, Map<String, Object> param) throws Exception {
//
//        ResponseEntity<String> responseEntity = customCloudFoundryClient.getClient(uaaClientId, uaaClientSecret, uaaTarget, param);
//        String str = responseEntity.getBody();
//
//        JSONObject stringToJson = new JSONObject();
//
//        try {
//
//            JSONParser jsonParser = new JSONParser();
//
//            //JSON데이터를 넣어 JSON Object 로 만들어 준다.
//            stringToJson = (JSONObject) jsonParser.parse(str);
//
//        } catch (Exception e) {
//            return null;
//        }
//
//        Map<String, Object> resultMap = new HashMap<>();
//
//        resultMap.put("info", stringToJson);
//        resultMap.put("infoString", str);
//
//        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
//
//        return resultMap;
//    }

    /**
     * 클라이언트 등록
     *
     * @param customCloudFoundryClient CustomCloudFoundryClient
     * @param param                    Map
     * @return Map map
     * @throws Exception the exception
     */
//    public Map registerClient(CustomCloudFoundryClient customCloudFoundryClient, Map<String, Object> param) throws Exception {
//
//        ResponseEntity<String> responseEntity = customCloudFoundryClient.registerClient(uaaAdminClientId, uaaAdminClientSecret, uaaTarget, param);
//
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
//        resultMap.put("CODE", responseEntity.getStatusCode());
//
//        return resultMap;
//    }

    /**
     * 클라이언트 수정
     *
     * @param customCloudFoundryClient CustomCloudFoundryClient
     * @param param                    Map
     * @return Map map
     * @throws Exception the exception
     */
//    public Map updateClient(CustomCloudFoundryClient customCloudFoundryClient, Map<String, Object> param) throws Exception {
//
//        ResponseEntity<String> responseEntity = customCloudFoundryClient.updateClient(uaaAdminClientId, uaaAdminClientSecret, uaaTarget, param);
//
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
//        resultMap.put("CODE", responseEntity.getStatusCode());
//
//        return resultMap;
//
//
//    }

    /**
     * 클라이언트 삭제
     *
     * @param customCloudFoundryClient CustomCloudFoundryClient
     * @param param                    Map
     * @return Map map
     * @throws Exception the exception
     */
//    public Map deleteClient(CustomCloudFoundryClient customCloudFoundryClient, Map<String, Object> param) throws Exception {
//
//        ResponseEntity<String> responseEntity = customCloudFoundryClient.deleteClient(uaaAdminClientId, uaaAdminClientSecret, uaaTarget, param);
//
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
//        resultMap.put("CODE", responseEntity.getStatusCode());
//
//        return resultMap;
//    }

}