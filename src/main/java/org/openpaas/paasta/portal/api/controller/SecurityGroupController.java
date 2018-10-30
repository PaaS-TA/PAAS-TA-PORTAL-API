package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v2.securitygroups.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.SecurityGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SecurityGroupController extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceController.class);

    @Autowired
    SecurityGroupService securityGroupService;

    /**
     * 시큐리티그룹을 상세조회한다.
     *
     * @param securityid  the security guid
     * @return GetSecurityGroupResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL+"/securitygroup/{securityid}")
    public GetSecurityGroupResponse getSecurityGroupResponse(@PathVariable String securityid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.getSecurityGroupResponse(securityid);
    }

    /**
     * 시큐리티그룹을 조회한다.
     *
     * @param page  ListSecurityGroups page
     * @return ListSecurityGroupsResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL+"/securitygroups/{page}")
    public ListSecurityGroupsResponse listSecurityGroupsResponse(@PathVariable int page, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        LOGGER.info("시작");
        return securityGroupService.listSecurityGroupsResponse(page);
    }


    /**
     * 시큐리티그룹을 생성한다.
     *
     * @param groupname  the SecurityGroup Name
     * @param rule the RuleEntity
     * @return CreateSecurityGroupResponse
     * @throws Exception the exception
     */
    @PostMapping(Constants.V2_URL+"/securitygroup/{groupname:.+}")
    public Map createSecurityGroupResponse(@PathVariable String groupname, @RequestBody List<RuleEntity> rule, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.createSecurityGroupResponse(groupname, rule);
    }


    /**
     * 시큐리티그룹을 수정한다.
     *
     * @param securityid  the security guid
     * @param groupname  the SecurityGroup Name
     * @param rule the RuleEntity
     * @return CreateSecurityGroupResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V2_URL+"/securitygroup/{securityid}/{groupname:.+}")
    public Map updateSecurityGroupResponse(@PathVariable String securityid, @PathVariable String groupname, @RequestBody List<RuleEntity> rule, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) {
        try {
            return new HashMap<String, Object>() {{
                put("RESULT", securityGroupService.updateSecurityGroupResponse(securityid, groupname, rule));
            }};
        }catch (Exception e){
            e.printStackTrace();
            return new HashMap<String, Object>() {{
                put("message", e.getMessage());
            }};
        }
    }


    /**
     * 시큐리티그룹을 삭제한다.
     *
     * @param securityid  the security guid
     * @return DeleteSecurityGroupResponse
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V2_URL+"/securitygroup/{securityid}")
    public Map deleteSecurityGroupResponse(@PathVariable String securityid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        try {
            securityGroupService.deleteSecurityGroupResponse(securityid);
            return new HashMap<String, Object>() {{
                put("RESULT", "SUCCESS");
            }};
        }catch (Exception e){
            e.printStackTrace();
            return new HashMap<String, Object>() {{
                put("message", e.getMessage());
            }};
        }
    }


    /**
     * 시큐리티그룹과 공간을 연결한다.
     *
     * @param securityid  the security guid
     * @param spaceid the space guid
     * @return AssociateSecurityGroupSpaceResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V2_URL+"/securitygroup/{securityid}/spaces/{spaceid}")
    public AssociateSecurityGroupSpaceResponse associateSecurityGroupSpaceResponse(@PathVariable String securityid, @PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.associateSecurityGroupSpaceResponse(securityid, spaceid);
    }


    /**
     * 공간 시큐리티 그룹을 언바인드 한다.
     *
     * @param securityid  the security guid
     * @param spaceid the space guid
     * @return Map
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V2_URL+"/securitygroup/{securityid}/spaces/{spaceid}")
    public Map removeSecurityGroupSpace(@PathVariable String securityid, @PathVariable String spaceid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.removeSecurityGroupSpace(securityid, spaceid);
    }


    /**
     * 준비 응용 프로그램에 사용할 보안 그룹 목록에 보안 그룹을 바인딩합니다.
     *
     * @param securityid  the security guid
     * @return SetSecurityGroupStagingDefaultResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V2_URL+"/securitygroup/{securityid}/staging")
    public SetSecurityGroupStagingDefaultResponse setSecurityGroupStagingDefaultResponse(@PathVariable String securityid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.setSecurityGroupStagingDefaultResponse(securityid);
    }


    /**
     * 시큐리티 스테이징 그룹을 조회한다.
     *
     * @param page  listStagingDefaults page
     * @return ListSecurityGroupStagingDefaultsResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL+"/securitygroup/{page}/staging")
    public ListSecurityGroupStagingDefaultsResponse listSecurityGroupStagingDefaultsResponse(@PathVariable int page, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.listSecurityGroupStagingDefaultsResponse(page);
    }


    /**
     * 스테이징 시큐리티 그룹을 언바인드 한다.
     *
     * @param securityid  the security guid
     * @return Map
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V2_URL+"/securitygroup/{securityid}/staging")
    public Map removeSecurityGroupStaging(@PathVariable String securityid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.removeSecurityGroupStaging(securityid);
    }


    /**
     *
     * 응용 프로그램 실행에 사용할 보안 그룹 목록에 보안 그룹을 바인딩합니다.
     *
     * @param securityid  the security guid
     * @return SetSecurityGroupRunningDefaultResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V2_URL+"/securitygroup/{securityid}/running")
    public SetSecurityGroupRunningDefaultResponse setSecurityGroupRunningDefaultResponse(@PathVariable String securityid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.setSecurityGroupRunningDefaultResponse(securityid);
    }


    /**
     * 실행중인 응용 프로그램에 대한 시큐리티 그룹 조회
     *
     * @param page  ListSecurityGroups page
     * @return ListSecurityGroupRunningDefaultsResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL+"/securitygroup/{page}/running")
    public ListSecurityGroupRunningDefaultsResponse listSecurityGroupRunningDefaultsResponse(@PathVariable int page, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.listSecurityGroupRunningDefaultsResponse(page);
    }


    /**
     * 실행중인 응용 프로그램에 대한 시큐리티 그룹 언바인드
     *
     * @param securityid  the security guid
     * @return Map
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V2_URL+"/securitygroup/{securityid}/running")
    public Map removeSecurityGroupRunning(@PathVariable String securityid, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token) throws Exception {
        return securityGroupService.removeSecurityGroupRunning(securityid);
    }



    /**
     * 공간 시큐리티그룹을 조회한다.
     *
     * @param page  ListSecurityGroups page
     * @param securityid the security guid
     * @return ListSecurityGroupSpacesResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL+"/securitygroup/{securityid}/{page}")
    public ListSecurityGroupSpacesResponse listSecurityGroupSpacesResponse(@PathVariable String securityid, @PathVariable int page) throws Exception {
        return securityGroupService.listSecurityGroupSpacesResponse(securityid, page);
    }



}
