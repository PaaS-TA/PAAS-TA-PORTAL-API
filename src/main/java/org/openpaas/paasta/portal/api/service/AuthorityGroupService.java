package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.identity.uaa.api.UaaConnectionFactory;
import org.cloudfoundry.identity.uaa.api.common.UaaConnection;
import org.cloudfoundry.identity.uaa.api.common.model.expr.FilterRequestBuilder;
import org.cloudfoundry.identity.uaa.api.group.UaaGroupOperations;
import org.cloudfoundry.identity.uaa.error.UaaException;
import org.cloudfoundry.identity.uaa.rest.SearchResults;
import org.cloudfoundry.identity.uaa.scim.ScimGroup;
import org.cloudfoundry.identity.uaa.scim.ScimGroupMember;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.util.SSLUtils;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthorityGroupService extends Common {

    public Collection<ScimGroup> getAuthorityGroups() throws Exception {
        UaaGroupOperations operations = getUaaGroupOperations(uaaAdminClientId);
        FilterRequestBuilder builder = new FilterRequestBuilder();
        SearchResults<ScimGroup> results = operations.getGroups(builder.build());
        Collection<ScimGroup> groups = results.getResources();
        return groups;
    }

    /**
     * Create authority group scim group.
     *
     * @param displayName the display name
     * @param memberList  the member list
     * @return ScimGroup scim group
     * @throws Exception the exception
     * @since 2016.10.18 최초작성
     */
    public ScimGroup createAuthorityGroup(String displayName, List<ScimGroupMember> memberList) throws Exception {
        if (!stringNullCheck(displayName)) {
            throw new UaaException("BAD_REQUEST", "Required request body content is missing", 400);
        }

        UaaGroupOperations operations = getUaaGroupOperations(uaaAdminClientId);
        ScimGroup requestedGroup = new ScimGroup();
        requestedGroup.setDisplayName(displayName);

        //멤버가 요청된 경우
        if (memberList != null && memberList.size() != 0) {
            //요청된 멤버 중 멤버ID가 존재하지 않는 멤버가 있는 경우
            for (ScimGroupMember member : memberList) {
                if (!stringNullCheck(displayName, member.getMemberId())) {
                    throw new UaaException("BAD_REQUEST", "Required request body content is missing", 400);
                }
            }
            requestedGroup.setMembers(memberList);
        }

        ScimGroup group = new ScimGroup();
        try {
            group = operations.createGroup(requestedGroup);
        } catch (HttpClientErrorException e) {
            if (e.getMessage().equals("409 Conflict")) {
                throw new UaaException("CONFLICT", "Duplicated group display name", 409);
            }

        }

        return group;
    }


    /**
     * 권한 그룹을 삭제한다.
     *
     * @param groupGuid the group guid
     * @throws Exception the exception
     * @since 2016.10.19 최초작성
     */
    public void deleteAuthorityGroup(String groupGuid) throws Exception {
        if (!stringNullCheck(groupGuid)) {
            throw new UaaException("BAD_REQUEST", "Required request body content is missing", 400);
        }

        UaaGroupOperations operations = getUaaGroupOperations(uaaAdminClientId);
        try {
            operations.deleteGroup(groupGuid);
        } catch (Exception e) {
            if (e.getMessage().equals("404 Not Found")) {
                throw new UaaException("NOT_FOUND", "Invalid group id", 404);
            }
        }
    }


    /**
     * 권한 그룹에 유저(멤버)를 등록한다. 여러명의 유저를 등록할 수 있게 한다.
     *
     * @param groupGuid          the group guid
     * @param memberUserNameList the member user name list
     * @return scim group
     * @throws Exception the exception
     * @since 2016.10.19 최초작성
     */
    public ScimGroup addGroupMembers(String groupGuid, List<String> memberUserNameList) throws Exception {
        if (!stringNullCheck(groupGuid)) {
            throw new UaaException("BAD_REQUEST", "Required request body content is missing", 400);
        }
        if (memberUserNameList == null || memberUserNameList.size() == 0) {
            throw new UaaException("BAD_REQUEST", "Required request body content is missing", 400);
        }

        UaaGroupOperations groupOperations = getUaaGroupOperations(uaaAdminClientId);
        List<ScimGroup> scimGroupList = new ArrayList<>();

        for (String memberUserName : memberUserNameList) {
            if (!stringNullCheck(memberUserName)) {
                throw new UaaException("BAD_REQUEST", "Required request body content is missing", 400);
            } else {
                scimGroupList.add(groupOperations.addMember(groupGuid, memberUserName));
            }
        }

        if (scimGroupList.size() == memberUserNameList.size()) {
            return scimGroupList.get(0);
        } else {
            throw new UaaException("INTERNAL_SERVER_ERROR", "unknown internal server error", 500);
        }
    }

    /**
     * 권한 그룹에 등록된 멤버를 삭제한다. 여러명의 멤버를 삭제할 수 있다.
     *
     * @param groupGuid          the group guid
     * @param memberUserNameList the member user name list
     * @return scim group
     * @throws Exception the exception
     */
    public ScimGroup deleteGroupMembers(String groupGuid, List<String> memberUserNameList) throws Exception {
        if (!stringNullCheck(groupGuid)) {
            throw new UaaException("BAD_REQUEST", "Required request body content is missing", 400);
        }
        if (memberUserNameList == null || memberUserNameList.size() == 0) {
            throw new UaaException("BAD_REQUEST", "Required request body content is missing", 400);
        }

        List<ScimGroup> scimGroupList = new ArrayList<>();
        UaaGroupOperations groupOperations = getUaaGroupOperations(uaaAdminClientId);

        for (String memberUserName : memberUserNameList) {
            if (!stringNullCheck(memberUserName)) {
                throw new UaaException("BAD_REQUEST", "Required request body content is missing", 400);
            } else {
                scimGroupList.add(groupOperations.deleteMember(groupGuid, memberUserName));
            }
        }

        if (scimGroupList.size() == memberUserNameList.size()) {
            return scimGroupList.get(0);
        } else {
            throw new UaaException("INTERNAL_SERVER_ERROR", "unknown internal server error", 500);
        }
    }

    /**
     * 예전 API라서 예전 API에서 쓰는 애들 사용하도로 위치 변경
     *
     */

    /**
     * 권한그룹 조회 등록시 사용
     *
     * @param uaaClientId
     * @return UaaGroupOperations
     * @throws Exception
     */
    public UaaGroupOperations getUaaGroupOperations(String uaaClientId) throws Exception {
        UaaConnection connection = getUaaConnection(uaaClientId);
        return connection.groupOperations();
    }


    /**
     * UAA 커넥션 생성
     *
     * @param uaaClientId
     * @return UaaConnection
     * @throws Exception
     */
    private UaaConnection getUaaConnection(String uaaClientId) throws Exception {
        ResourceOwnerPasswordResourceDetails credentials = getCredentials(uaaClientId);
        URL uaaHost = new URL(uaaTarget);

        //ssl 유효성 체크 비활성
        if (skipSSLValidation) {
            SSLUtils.turnOffSslChecking();
        }

        UaaConnection connection = UaaConnectionFactory.getConnection(uaaHost, credentials);
        return connection;
    }


    /**
     * credentials 세팅
     *
     * @param uaaClientId
     * @return ResourceOwnerPasswordResourceDetails
     */
    private ResourceOwnerPasswordResourceDetails getCredentials(String uaaClientId) {
        ResourceOwnerPasswordResourceDetails credentials = new ResourceOwnerPasswordResourceDetails();
        credentials.setAccessTokenUri(uaaTarget + "/oauth/token?grant_type=client_credentials&response_type=token");
        credentials.setClientAuthenticationScheme(AuthenticationScheme.header);

        credentials.setClientId(uaaClientId);

        if (uaaClientId.equals(uaaAdminClientId)) {
            credentials.setClientSecret(uaaAdminClientSecret);
        }
        return credentials;
    }

}
