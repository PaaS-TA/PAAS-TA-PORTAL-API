package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.mapper.autoscail.AppAutoScaleModalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * org.openpaas.paasta.portal.api.service.AppAutoScaleModalService.Class
 * 내  용 :자동 인스턴스 증가. 감소 관련 모달 서비스 기능 클래스객체
 * 작성일 : 2016. 07.12.
 * 작성자 : 이인정
 */
@Transactional
@Service
public class AppAutoScaleModalService extends Common {

    /** 로그객체*/
    private  static final Logger LOGGER = LoggerFactory.getLogger(AppAutoScaleModalService.class);

    @Autowired
    private AppAutoScaleModalMapper appAutoScaleModalMapper;
    @Autowired
    private OrgService orgService;
    @Autowired
    private SpaceService spaceService;
    /**
     * getAppAutoScaleInfo(HashMap appAutoScale)
     * 앱의 자동인스턴스 증가, 감소 관련 정보를 가져온다.
     *
     * @param appAutoScale 자동인스턴스 증가, 감소 관련 모델 파라메터
     * @return {mode, list}
     */
    public HashMap<String, Object> getAppAutoScaleInfo(HashMap appAutoScale)  {

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", appAutoScaleModalMapper.getAppAutoScaleInfo((String) appAutoScale.get("guid")));
        resultMap.put("mode", (String) appAutoScale.getOrDefault("mode",""));

        return resultMap;

    }

    /**
     * getAppAutoScaleInfo(HashMap appAutoScale)
     * 앱의 자동인스턴스 증가, 감소 관련 정보를 가져온다.
     *
     * @param appAutoScale 자동인스턴스 증가, 감소 관련 모델 파라메터
     * @return {mode, list}
     */
    public HashMap<String, Object> getAppAutoScaleList(HashMap appAutoScale) {

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", appAutoScaleModalMapper.getAppAutoScaleList((String) appAutoScale.get("guid")));
        resultMap.put("mode", (String) appAutoScale.getOrDefault("mode",""));

        return resultMap;

    }


    /**
     * insertAppAutoScale (HashMap<String,Object> appAutoScale)
     * 앱의 자동인스턴스 증가, 감소 관련 정보를 저장한다.
     * 앱의 자동 증가감소를 admin 사용자가 모니터링하므로 ORG에space에 대한 매니저 권한을 부여한다.
     * @param appAutoScale 자동인스턴스 증가, 감소 관련 모델 파라메터
     * @return {결과 값}
     */
    @Transactional
    public int insertAppAutoScale (HashMap<String,Object> appAutoScale) throws Exception {

//        String org =String.valueOf(appAutoScale.get("org"));
//        String space =String.valueOf(appAutoScale.get("space"));
        LOGGER.info("SERVICE insertAppAutoScale map : ",  appAutoScale.toString());
        String token = getCloudFoundryClient(adminUserName,adminPassword).login().getValue();
//        orgService.setOrgRole(org, adminUserName, Constants.USERS, token);
//        orgService.setOrgRole(org, adminUserName, Constants.ORGMANAGER,token);
//        spaceService.setSpaceRole(org, space,adminUserName,Constants.SPACEMANAGER,token);

        int result = appAutoScaleModalMapper.insertAppAutoScale(appAutoScale);

        return result;

    }

    /**
     * updateAppAutoScale (HashMap<String,Object> appAutoScale)
     * 앱의 자동인스턴스 증가, 감소 관련 정보를 수정한다.
     *
     * @param appAutoScale 자동인스턴스 증가, 감소 관련 모델 파라메터
     * @return {결과 값}
     */
    public int updateAppAutoScale(HashMap<String,Object> appAutoScale) {

        LOGGER.info("SERVICE updateAppAutoScale  : ",  appAutoScale.toString());
        int result = 0;
        result = appAutoScaleModalMapper.updateAppAutoScale(appAutoScale);
        return result;


    }

    /**
     * deleteAppAutoScale (HashMap<String,Object> appAutoScale)
     * 앱의 자동인스턴스 증가, 감소 관련 정보를 삭제한다..
     *
     * @param guid 자동인스턴스 증가, 감소 관련 모델 아이디
     * @return {결과 값}
     */
    @Transactional
    public int deleteAppAutoScale(String guid)   throws Exception {
        LOGGER.info("SERVICE deleteAppAutoScale guid : ",  guid);
        HashMap autoScailInfo = new HashMap();
        autoScailInfo.put("guid", guid);
        Map autoScaleInfo = (Map) getAppAutoScaleInfo(autoScailInfo).get("list");
//        String org =String.valueOf(autoScaleInfo.get("org"));
//        String space =String.valueOf(autoScaleInfo.get("space"));

        LOGGER.info("SERVICE insertAppAutoScale map : ",  autoScaleInfo.toString());

//        String token = getCloudFoundryClient(adminUserName,adminPassword).login().getValue();
//        orgService.unsetOrgRole(org, adminUserName, Constants.USERS, token);
//        orgService.unsetOrgRole(org, adminUserName, Constants.ORGMANAGER,token);
//        spaceService.setSpaceRole(org, space,adminUserName,Constants.SPACEMANAGER,token);
        int resultMap = appAutoScaleModalMapper.deleteAppAutoScale(guid);
        return resultMap;

    }
}
