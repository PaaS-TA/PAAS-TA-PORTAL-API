package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.identity.uaa.scim.ScimGroup;
import org.cloudfoundry.identity.uaa.scim.ScimGroupMember;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.service.AuthorityGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class AuthorityGroupController extends Common {

    @Autowired
    private AuthorityGroupService authorityGroupService;
    private final String V2_URL = "/v2";


    /**
     * 권한 그룹을 조회한다.
     *
     * @return Map (자바 클래스)
     * @throws Exception the exception
     */
    @GetMapping(V2_URL + "/authority")
    public Map<String, Object> getAuthorityGroups() throws Exception {
        Collection<ScimGroup> groups = authorityGroupService.getAuthorityGroups();
        return new HashMap<String, Object>() {{
            put("groups", groups);
        }};
    }


    /**
     * 권한 그룹 생성
     *
     * @param body (자바 클래스)
     * @return Map (자바 클래스)
     * @throws Exception the exception
     */
    @PostMapping(V2_URL + "/authority/group")
    public Map<String, Object> createAuthorityGroup(@RequestBody Map<String, Object> body) throws Exception {
        String displayName = (String) body.get("displayName");
        List<ScimGroupMember> memberList = (List) body.get("memberList");
        ScimGroup group = authorityGroupService.createAuthorityGroup(displayName, memberList);
        return new HashMap<String, Object>() {{
            put("group", group);
        }};
    }


    /**
     * 권한그룹을 삭제한다.
     *
     * @param body (자바 클래스)
     * @return Map (자바 클래스)
     * @throws Exception the exception
     * @author 김도준
     * @since 2016.09.19
     */
    @DeleteMapping(V2_URL + "/authority/group/{groupguid}")
    public Map<String, Object> deleteAuthorityGroup(@PathVariable String groupguid, @RequestBody Map<String, Object> body) throws Exception {
        authorityGroupService.deleteAuthorityGroup(groupguid);
        return new HashMap<String, Object>() {{
            put("status", 200);
            put("statusText", "ok");
        }};
    }


    /**
     * 사용자 권한 그룹에 사용자 등록
     *
     * @param body the body
     * @return map
     * @throws Exception the exception
     */
    @PostMapping(V2_URL + "/authority/member")
    public Map<String, Object> addGroupMembers(@RequestBody Map<String, Object> body) throws Exception {
        String groupGuid = (String) body.get("groupGuid");
        List<String> memberUserNameList = (List<String>) body.get("memberUserNameList");
        ScimGroup group = authorityGroupService.addGroupMembers(groupGuid, memberUserNameList);
        return new HashMap<String, Object>() {{
            put("group", group);
        }};
    }


    /**
     * 권한 그룹 사용자 삭제
     *
     * @param body the body
     * @return map
     * @throws Exception the exception
     */
    @DeleteMapping(V2_URL + "/authority/member/{groupguid}")
    public Map<String, Object> deleteGroupMembers(@PathVariable String groupguid, @RequestBody Map<String, Object> body) throws Exception {
        List<String> memberUserNameList = (List<String>) body.get("memberUserNameList");
        ScimGroup group = authorityGroupService.deleteGroupMembers(groupguid, memberUserNameList);
        return new HashMap<String, Object>() {{
            put("group", group);
        }};
    }


}
