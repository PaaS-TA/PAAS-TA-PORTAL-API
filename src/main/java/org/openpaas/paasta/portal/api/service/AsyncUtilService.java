package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.domain.CloudUser;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * 사용자 권한 서비스 - 권한별 사용자를 가져온다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.19 최초작성
 */
@Service
public class AsyncUtilService extends Common {

    /**
     * Gets users.
     *
     * @param orgGuid the org guid
     * @param client  the client
     * @return the users
     * @throws Exception the exception
     */
//    @Async
    public Future<List<Map<String, Object>>> getUsers(UUID orgGuid, CustomCloudFoundryClient client) throws Exception{
        List<Map<String, Object>> users = client.getUsers(orgGuid);
        return  new AsyncResult<>(users);
    }

    /**
     * Gets users for org role managers.
     *
     * @param orgGuid the org guid
     * @param client  the client
     * @return the users for org role managers
     * @throws Exception the exception
     */
//users
    @Async
    public Future< Map<String, CloudUser>> getUsersForOrgRole_managers(UUID orgGuid, CustomCloudFoundryClient client) throws Exception{
        Map<String, CloudUser> managers = client.getUsersForOrgRole(orgGuid, "managers");
        return  new AsyncResult<>(managers);
    }

    /**
     * Gets users for org role billing managers.
     *
     * @param orgGuid the org guid
     * @param client  the client
     * @return the users for org role billing managers
     * @throws Exception the exception
     */
    @Async
    public Future< Map<String, CloudUser>> getUsersForOrgRole_billingManagers(UUID orgGuid, CustomCloudFoundryClient client) throws Exception{
        Map<String, CloudUser> billingManagers = client.getUsersForOrgRole(orgGuid, "billing_managers");
        return new AsyncResult<>(billingManagers);
    }

    /**
     * Gets users for org role auditors.
     *
     * @param orgGuid the org guid
     * @param client  the client
     * @return the users for org role auditors
     * @throws Exception the exception
     */
    @Async
    public Future< Map<String, CloudUser>> getUsersForOrgRole_auditors(UUID orgGuid, CustomCloudFoundryClient client) throws Exception{
        Map<String, CloudUser> auditors = client.getUsersForOrgRole(orgGuid, "auditors");
        return new AsyncResult<>(auditors);
    }

    /**
     * Gets users for space role managers.
     *
     * @param orgGuid   the org guid
     * @param spaceName the space name
     * @param client    the client
     * @return the users for space role managers
     * @throws Exception the exception
     */
    @Async
    public Future< Map<String, CloudUser>> getUsersForSpaceRole_managers(UUID orgGuid,String spaceName, CustomCloudFoundryClient client) throws Exception{
        Map<String, CloudUser> managers = client.getUsersForSpaceRole(orgGuid, spaceName,"managers");
        return  new AsyncResult<>(managers);
    }

    /**
     * Gets users for space role developers.
     *
     * @param orgGuid   the org guid
     * @param spaceName the space name
     * @param client    the client
     * @return the users for space role developers
     * @throws Exception the exception
     */
    @Async
    public Future< Map<String, CloudUser>> getUsersForSpaceRole_developers(UUID orgGuid,String spaceName, CustomCloudFoundryClient client) throws Exception{
        Map<String, CloudUser> billingManagers = client.getUsersForSpaceRole(orgGuid,spaceName, "developers");
        return new AsyncResult<>(billingManagers);
    }

    /**
     * Gets users for space role auditors.
     *
     * @param orgGuid   the org guid
     * @param spaceName the space name
     * @param client    the client
     * @return the users for space role auditors
     * @throws Exception the exception
     */
    @Async
    public Future< Map<String, CloudUser>> getUsersForSpaceRole_auditors(UUID orgGuid, String spaceName, CustomCloudFoundryClient client) throws Exception{
        Map<String, CloudUser> auditors = client.getUsersForSpaceRole(orgGuid,spaceName, "auditors");
        return new AsyncResult<>(auditors);
    }
}
