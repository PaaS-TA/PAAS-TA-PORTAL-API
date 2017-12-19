package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.identity.uaa.scim.ScimGroup;
import org.cloudfoundry.identity.uaa.scim.ScimGroupMember;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.service.AuthorityGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 권한 컨트롤러 - 권한그룹과 권한을 조회, 수정, 삭제한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.19 최초작성
 */
@RestController
@Transactional
@RequestMapping(value = {"/authority"})
public class AuthorityGroupController extends Common {

    @Autowired
    private AuthorityGroupService authorityGroupService;

    /**
     * 권한 그룹을 조회한다.
     *
     * @return Map (자바 클래스)
     * @throws Exception the exception
     * @author 김도준
     * @since 2016.09.19
     */
    @RequestMapping(value = {"/getAuthorityGroups"}, method = RequestMethod.POST)
    public Map<String, Object> getAuthorityGroups() throws Exception{

        Collection<ScimGroup> groups = authorityGroupService.getAuthorityGroups();

        return new HashMap<String, Object>(){{put("groups",  groups);}};
    }

    /**
     * 권한 그룹 생성
     *
     * @param body (자바 클래스)
     * @return Map (자바 클래스)
     * @throws Exception the exception
     * @author 김도준
     * @since 2016.09.19
     */
    @RequestMapping(value = {"/createAuthorityGroup"}, method = RequestMethod.POST)
    public Map<String, Object> createAuthorityGroup(@RequestBody Map<String, Object> body) throws Exception{


        String displayName = (String)body.get("displayName");

        List<ScimGroupMember> memberList = (List)body.get("memberList");

        ScimGroup group = authorityGroupService.createAuthorityGroup(displayName, memberList);

        return new HashMap<String, Object>(){{put("group",  group);}};
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
    @RequestMapping(value = {"/deleteAuthorityGroup"}, method = RequestMethod.POST)
    public Map<String, Object> deleteAuthorityGroup(@RequestBody Map<String, Object> body) throws Exception{

        String groupGuid = (String)body.get("groupGuid");
        authorityGroupService.deleteAuthorityGroup(groupGuid);
        return new HashMap<String, Object>(){{put("status",  200);put("statusText","ok");}};
    }


/*
    @RequestMapping(value = {"/getUsers"}, method = RequestMethod.GET )
    public SearchResults<ScimUser> getUsers() throws Exception {
        //LOGGER.info("> into getUsers");

        //Map<String, Object> resultMap = new HashMap();

        return authorityGroupService.getUsers();

    }*/

    /**
     * 사용자 권한 그룹에 사용자 등록
     *
     * @param body the body
     * @return map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/addGroupMembers"}, method = RequestMethod.POST)
    public Map<String, Object> addGroupMembers(@RequestBody Map<String, Object> body) throws Exception {

        String groupGuid = (String)body.get("groupGuid");
        List<String> memberUserNameList = (List<String>)body.get("memberUserNameList");

        ScimGroup group = authorityGroupService.addGroupMembers(groupGuid, memberUserNameList);
        return new HashMap<String, Object>(){{put("group",  group);}};
    }

    /**
     * 권한 그룹 사용자 삭제
     *
     * @param body the body
     * @return map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteGroupMembers"}, method = RequestMethod.POST)
    public Map<String, Object> deleteGroupMembers(@RequestBody Map<String, Object> body) throws Exception {

        String groupGuid = (String)body.get("groupGuid");
        List<String> memberUserNameList = (List<String>)body.get("memberUserNameList");

        ScimGroup group = authorityGroupService.deleteGroupMembers(groupGuid, memberUserNameList);
        return new HashMap<String, Object>(){{put("group",  group);}};
    }




}
