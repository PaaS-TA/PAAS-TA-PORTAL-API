/*
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openpaas.paasta.portal.api.common;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.domain.*;
import org.cloudfoundry.identity.uaa.scim.ScimUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Interface defining operations available for the cloud controller REST client implementations
 *
 * @author Thomas Risberg
 */
public interface CustomCloudControllerClient {

    // User and Info methods

    //CloudSpace getSpace(String spaceName);

    List<CloudSpace> getSpaces();

    OAuth2AccessToken login();

	CloudOrganization getOrgByName(String orgName, boolean required);

    void createSpace(String orgName, String spaceNamem);

	/*
     * Custom Methods
	 */

    void renameOrg(String orgName, String newName);

    void deleteOrg(String orgName);

    void renameSpace(String orgName, String spaceName, String newName);

	void createOrg(String orgName);

	void setOrgRole(String orgName, String userName, String orgRole);

    void setSpaceRole(String orgName, String spaceName, String userName, String spaceRole);

    void unsetOrgRole(String orgName,String userGuid, String orgRole);

    void unsetSpaceRole(String orgName, String spaceName, String userGuid, String spaceRole);


    String getOrgSummary(String orgName);

    //void removeUserFromOrg(String orgGuid, String userGuid);

    String getUserGuid();

    //Map listAllManagersForOrg(UUID orgGuid);

    String getSpaceSummary(String orgName, String spaceName);

    void removeOrgFromUser(String userGuid, String orgName);

    void removeSpaceFromUser(String userGuid, String orgName, String spaceName);

    Map listAllOrgOrSpaceForTheUser(String userGuid, String role);

    //public Map getUserSummary(String userGuid);

    void deleteSpace(String orgName,String spaceName);

    CloudInfo getInfo();

    String getAppSummary(UUID appGuid);

    String getAppStats(UUID appGuid);

    List getOrganizations();

    String getAppEvents(UUID appGuid);

    void restageApp(UUID appGuid);

    void renameInstanceService(UUID guid, String newName);

    void deleteInstanceService(UUID guid);

    // user
    void updatePassword(String newPassword);

    void updatePassword(CloudCredentials credentials, String newPassword);

    void deleteUser(String userGuid);

    // REX :: GET BUILD PACKS
    HashMap getBuildPacks();

    // REX :: CREATE SERVICE
    String createService(String name, String servicePlanGuid, String orgName, String spaceName);

    // REX :: TERMINATE APP INSTANCE BY INDEX
    void terminateAppInstanceByIndex(UUID appGuid, int appInstanceIndex, String orgName, String spaceName);

    // REX :: BIND SERVICE
    String bindServiceV2(UUID serviceInstanceGuid, String appName, Map<String, Object> app_bind_parameters);

    // REX :: CREATE APPLICATION V2
    void createApplicationV2(String appName, Staging staging, Integer disk, Integer memory, List<String> uris, Map<String, Object> paramMap);

    // REX :: CREATE APPLICATION V2
    CloudStack getStack(String name);

    //app
    void bindRoute(String host, String domainName, String appName);

    void unbindRoute(String host, String domainName, String appName);

    void deleteRoute(String host, String domainName);

    List<Map<String, Object>> getUsers(UUID orgGuid);

    Map<String, CloudUser> getUsersForOrgRole(UUID orgGuid, String orgRole);

    Map<String, CloudUser> getUsersForSpaceRole(UUID orgGuid, String spaceName, String spaceRole);

    //CloudServiceInstance getServiceInstance(String serviceInstanceName);

    //Map<String, Object> getUserProvidedServiceInstance(String orgName, String spaceName, String serviceInstanceName);

    UUID getUserProvidedServiceInstanceGuid(String orgName, String spaceName, String serviceInstanceName);

    void createUserProvidedService(CloudService service, Map<String, Object> credentials);

    void createUserProvidedService(CloudService service, Map<String, Object> credentials, String syslogDrainUrl);

    Map<String, Object> getUserProvidedServiceInstance(String orgName, String spaceName, String serviceInstanceName);

    //Map<String, List<String>> getAllSpaceUsers(String orgName, String spaceName);
    void updateUserProvidedService(String orgName, String spaceName, String serviceInstanceName, String newServiceInstanceName, Map<String, Object> credentials, String syslogDrainUrl);

    Map resetPassword(String userId,String newPassword, String clientId, String clientScret, String uaaTarget) throws URISyntaxException, MalformedURLException;

    Map register(ScimUser scimuser) throws Exception;

    void updateBuildPack(UUID guid, int position, boolean enable, boolean lock);

    Map getUaaAccessToken(String clientId, String clientSecret, String uaaTarget) throws MalformedURLException;

    void renameServiceBroker(String name, String newName);

    ResponseEntity<String> getClientList(String clientId, String clientScret, String uaaTarget) throws URISyntaxException, MalformedURLException;

    ResponseEntity<String> getClient(String clientId, String clientScret, String uaaTarget, Map<String, Object> param) throws URISyntaxException, MalformedURLException;

    ResponseEntity<String> registerClient(String clientId, String clientScret, String uaaTarget, Map<String, Object> param) throws URISyntaxException, MalformedURLException;

    ResponseEntity<String> updateClient(String clientId, String clientScret, String uaaTarget, Map<String, Object> param) throws URISyntaxException, MalformedURLException;

    ResponseEntity<String> deleteClient(String clientId, String clientScret, String uaaTarget, Map<String, Object> param) throws URISyntaxException, MalformedURLException;

    String getSpaceQuota(String guid);

    String getSpace(String orgName, String spaceName);

    // REX :: BIND SERVICE
    String createServiceV2(String name, String servicePlanGuid, String orgName, String spaceName, Map<String, Object> service_create_param);
}
