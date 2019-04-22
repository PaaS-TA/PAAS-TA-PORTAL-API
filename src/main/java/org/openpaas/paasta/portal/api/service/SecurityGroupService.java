package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.securitygroups.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableAsync
@Service
public class SecurityGroupService extends Common {


    /**
     * 시큐리티그룹을 상세조회한다.
     *
     * @param securityid the security guid
     * @return GetSecurityGroupResponse
     * @throws Exception the exception
     */
    public GetSecurityGroupResponse getSecurityGroup(String securityid) throws Exception {
        return cloudFoundryClient().securityGroups().get(GetSecurityGroupRequest.builder().securityGroupId(securityid).build()).block();
    }


    /**
     * 시큐리티그룹을 조회한다.
     *
     * @param page ListSecurityGroups page
     * @return ListSecurityGroupsResponse
     * @throws Exception the exception
     */
    public ListSecurityGroupsResponse listSecurityGroups(int page) throws Exception {

        return cloudFoundryClient().securityGroups().list(ListSecurityGroupsRequest.builder().page(page).build()).block();
    }


    /**
     * 시큐리티그룹을 생성한다.
     *
     * @param groupname the SecurityGroup Name
     * @param rule      the RuleEntity
     * @return CreateSecurityGroupResponse
     * @throws Exception the exception
     */
    public Map createSecurityGroup(String groupname, List<RuleEntity> rule) throws Exception {
        try {
            CreateSecurityGroupResponse createSecurityGroupResponse = cloudFoundryClient().securityGroups().create(CreateSecurityGroupRequest.builder().name(groupname).rules(rule).build()).block();
            return new HashMap() {{
                put("RESULT", "SUCCESS");
                put("DATA", createSecurityGroupResponse);
            }};
        } catch (Exception e) {
            return new HashMap() {{
                put("RESULT", "FALE");
                put("MSG", e.getMessage());
            }};
        }
    }


    /**
     * 시큐리티그룹을 수정한다.
     *
     * @param securityid the security guid
     * @param groupname  the SecurityGroup Name
     * @param rule       the RuleEntity
     * @return CreateSecurityGroupResponse
     * @throws Exception the exception
     */
    public UpdateSecurityGroupResponse updateSecurityGroup(String securityid, String groupname, List<RuleEntity> rule) {
        return cloudFoundryClient().securityGroups().update(UpdateSecurityGroupRequest.builder().securityGroupId(securityid).name(groupname).rules(rule).build()).block();
    }


    /**
     * 시큐리티그룹을 삭제한다.
     *
     * @param securityid the security guid
     * @return DeleteSecurityGroupResponse
     * @throws Exception the exception
     */
    public DeleteSecurityGroupResponse deleteSecurityGroup(String securityid) throws Exception {
        return cloudFoundryClient().securityGroups().delete(DeleteSecurityGroupRequest.builder().securityGroupId(securityid).build()).block();
    }


    /**
     * 시큐리티그룹과 공간을 연결한다.
     *
     * @param securityid the security guid
     * @param spaceid    the space guid
     * @return AssociateSecurityGroupSpaceResponse
     * @throws Exception the exception
     */
    public AssociateSecurityGroupSpaceResponse associateSecurityGroupSpace(String securityid, String spaceid) throws Exception {
        return cloudFoundryClient().securityGroups().associateSpace(AssociateSecurityGroupSpaceRequest.builder().securityGroupId(securityid).spaceId(spaceid).build()).block();
    }


    /**
     * 공간 시큐리티 그룹을 언바인드 한다.
     *
     * @param securityid the security guid
     * @param spaceid    the space guid
     * @return Map
     * @throws Exception the exception
     */
    public Map removeSecurityGroupSpace(String securityid, String spaceid) throws Exception {
        try {
            cloudFoundryClient().securityGroups().removeSpace(RemoveSecurityGroupSpaceRequest.builder().securityGroupId(securityid).spaceId(spaceid).build()).block();
            return new HashMap() {{
                put("RESULT", "SUCCESS");
            }};
        } catch (Exception e) {
            return new HashMap() {{
                put("RESULT", "FALE");
                put("MSG", e.getMessage());
            }};
        }
    }


    /**
     * 준비 응용 프로그램에 사용할 보안 그룹 목록에 보안 그룹을 바인딩합니다.
     *
     * @param securityid the security guid
     * @return SetSecurityGroupStagingDefaultResponse
     * @throws Exception the exception
     */
    public SetSecurityGroupStagingDefaultResponse setSecurityGroupStagingDefault(String securityid) throws Exception {
        return cloudFoundryClient().securityGroups().setStagingDefault(SetSecurityGroupStagingDefaultRequest.builder().securityGroupId(securityid).build()).block();
    }


    /**
     * 시큐리티 스테이징 그룹을 조회한다.
     *
     * @param page ListSecurityGroups page
     * @return ListSecurityGroupStagingDefaultsResponse
     * @throws Exception the exception
     */
    public ListSecurityGroupStagingDefaultsResponse listSecurityGroupStagingDefaultsResponse(int page) throws Exception {
        return cloudFoundryClient().securityGroups().listStagingDefaults(ListSecurityGroupStagingDefaultsRequest.builder().page(page).build()).block();
    }


    /**
     * 스테이징 시큐리티 그룹을 언바인드 한다.
     *
     * @param securityid the security guid
     * @return Map
     * @throws Exception the exception
     */
    public Map removeSecurityGroupStaging(String securityid) throws Exception {
        try {
            cloudFoundryClient().securityGroups().removeStagingDefault(RemoveSecurityGroupStagingDefaultRequest.builder().securityGroupId(securityid).build()).block();
            return new HashMap() {{
                put("RESULT", "SUCCESS");
            }};
        } catch (Exception e) {
            return new HashMap() {{
                put("RESULT", "FALE");
                put("MSG", e.getMessage());
            }};
        }
    }


    /**
     * 응용 프로그램 실행에 사용할 보안 그룹 목록에 보안 그룹을 바인딩합니다.
     *
     * @param securityid the security guid
     * @return SetSecurityGroupRunningDefaultResponse
     * @throws Exception the exception
     */
    public SetSecurityGroupRunningDefaultResponse setSecurityGroupRunningDefault(String securityid) throws Exception {
        return cloudFoundryClient().securityGroups().setRunningDefault(SetSecurityGroupRunningDefaultRequest.builder().securityGroupId(securityid).build()).block();
    }


    /**
     * 실행중인 응용 프로그램에 대한 시큐리티 그룹 조회
     *
     * @param page ListSecurityGroups page
     * @return ListSecurityGroupRunningDefaultsResponse
     * @throws Exception the exception
     */
    public ListSecurityGroupRunningDefaultsResponse listSecurityGroupRunningDefaults(int page) throws Exception {
        return cloudFoundryClient().securityGroups().listRunningDefaults(ListSecurityGroupRunningDefaultsRequest.builder().page(page).build()).block();
    }


    /**
     * 실행중인 응용 프로그램에 대한 시큐리티 그룹 언바인드
     *
     * @param securityid the security guid
     * @return Map
     * @throws Exception the exception
     */
    public Map removeSecurityGroupRunning(String securityid) throws Exception {

        try {
            cloudFoundryClient().securityGroups().removeRunningDefault(RemoveSecurityGroupRunningDefaultRequest.builder().securityGroupId(securityid).build()).block();
            return new HashMap() {{
                put("RESULT", "SUCCESS");
            }};
        } catch (Exception e) {
            return new HashMap() {{
                put("RESULT", "FALE");
                put("MSG", e.getMessage());
            }};
        }
    }

    /**
     * 실행중인 응용 프로그램에 대한 시큐리티 그룹 조회
     *
     * @param page ListSecurityGroups page
     * @return ListSecurityGroupRunningDefaultsResponse
     * @throws Exception the exception
     */
    public ListSecurityGroupSpacesResponse listSecurityGroupSpaces(String securityid, int page) throws Exception {
        return cloudFoundryClient().securityGroups().listSpaces(ListSecurityGroupSpacesRequest.builder().securityGroupId(securityid).page(page).build()).block();
    }


}
