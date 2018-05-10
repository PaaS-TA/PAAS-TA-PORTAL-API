package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

//import org.openpaas.paasta.portal.api.mapper.cc.AppCcMapper;

/**
 * 앱 서비스 - 애플리케이션 정보 조회, 구동, 정지 등의 API 를 호출 하는 서비스이다.
 *
 * @author 이인정
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Service
public class ExternalAppService extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalAppService.class);

//    @Autowired
//    private AppCcMapper appCcMapper;
    @Autowired
    private AppService appService;
    @Autowired
    private SpaceService spaceService;
    @Autowired
    private AppAutoScaleModalService appAutoScaleModalService;


    /**
     * 앱 Guid를 통해 정보를 조회한다.
     *
     * @param appGuid the app
     * @return the app image url
     */
    public App getAppInfo(String appGuid) {
        App newApp = new App();
        try {

            //HashMap hashMap = appCcMapper.getAppInfo(appGuid);
            HashMap hashMap = new HashMap();
            if(null==hashMap) {
                String orgName = String.valueOf(hashMap.get("orgName"));
                String spaceName = String.valueOf(hashMap.get("spaceName"));
                newApp.setOrgName(orgName);
                newApp.setSpaceName(spaceName);
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return newApp;
    }

    /**
     * 앱 Guid를 통해 정보를 조회한다.
     *
     * @param hashApp the app
     * @return the app image url
     */
    public ResponseEntity callUpdategAppInfo(HashMap hashApp) throws Exception {
//        int instanceCnt = 0;
//        try {
//            OAuth2AccessToken oAuth2AccessToken = new CloudFoundryClient(new CloudCredentials(adminUserName, adminPassword), getTargetURL(apiTarget), true).login();
//            String token = oAuth2AccessToken.getValue();
//            CustomCloudFoundryClient admin = getCustomCloudFoundryClient(token);
//            CloudFoundryClient cloudFoundryClient = getCloudFoundryClient(token);
//            App app = new App();
//            String appName = String.valueOf(hashApp.get("appName"));
//            String appGuid = String.valueOf(hashApp.get("appGuid"));
//            String sAction = String.valueOf(hashApp.get("action"));
//
//
//            //apps instance 개수 가져오기
//            LOGGER.info("updateApp Start : " + app.getName());
//            String spaceString = admin.getAppStats(UUID.fromString(appGuid));
//            Map apps  = new ObjectMapper().readValue(spaceString, Map.class);
//            instanceCnt = apps.size();
//
//            app.setName(appName);
//            if ("".equals(appName) || "".equals(appGuid) || instanceCnt == 0 || null==hashApp.get("action")) {
//                return new ResponseEntity(false, HttpStatus.BAD_REQUEST.BAD_REQUEST);
//            }
//            LOGGER.info("updateApp Start : " + app.getName());
//
//            App newApp = getAppInfo(appGuid);
//            if(null == newApp){
//               return new ResponseEntity(HttpStatus.NO_CONTENT);
//            }
//            app.setOrgName(newApp.getOrgName());
//            app.setSpaceName(newApp.getSpaceName());
//
//            //오토 스케일링 정보가져오기
//            HashMap autoScailInfo = new HashMap();
//            autoScailInfo.put("guid", appGuid);
//            Map appAutoScale = (Map) appAutoScaleModalService.getAppAutoScaleInfo(autoScailInfo).get("list");
//
//            //Auto 스케줄링 사용 유무 체크
//            if ((int) appAutoScale.get("instanceMinCnt") < instanceCnt) {
//                if ("I".equals(sAction)) {
//                    instanceCnt = instanceCnt - 1;
//                    app.setInstances(instanceCnt);
//                }
//            }
//            if ((int) appAutoScale.get("instanceMaxCnt") > instanceCnt) {
//                if ("O".equals(sAction)) {
//                    instanceCnt = instanceCnt + 1;
//                    app.setInstances(instanceCnt);
//                }
//            }
//            LOGGER.info("updateApp :: " + sAction + ":::" + instanceCnt);
//            //service call
//            //CISS appService.updateApp(app, cloudFoundryClient);
//            LOGGER.info("updateApp End ");
//        }catch (Exception e){
//            e.printStackTrace();
//            return new ResponseEntity(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
//        }

//        return new ResponseEntity(Integer.toString(instanceCnt),HttpStatus.OK);
        return null;
    }

}
