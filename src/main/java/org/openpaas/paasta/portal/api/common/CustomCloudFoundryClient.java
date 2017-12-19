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
import org.cloudfoundry.client.lib.HttpProxyConfiguration;
import org.cloudfoundry.client.lib.domain.*;
import org.cloudfoundry.identity.uaa.scim.ScimUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.Assert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A Java client to exercise the Cloud Foundry API.
 *
 * @author Ramnivas Laddad
 * @author A.B.Srinivasan
 * @author Jennifer Hickey
 * @author Dave Syer
 * @author Thomas Risberg
 * @author Alexander Orlov
 */
public class CustomCloudFoundryClient {

	private CustomCloudControllerClient cc;

	private CloudInfo info;
	/**
	 * Construct client for anonymous user. Useful only to get to the '/info' endpoint.
	 */


	public CustomCloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl,
									boolean trustSelfSignedCerts) throws Exception {
		this(credentials, cloudControllerUrl, null, (HttpProxyConfiguration) null, trustSelfSignedCerts);
	}

	public CustomCloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, CloudSpace sessionSpace,
									HttpProxyConfiguration httpProxyConfiguration, boolean trustSelfSignedCerts) throws Exception {
		Assert.notNull(cloudControllerUrl, "URL for cloud controller cannot be null");
		CustomCloudControllerClientFactory cloudControllerClientFactory =
				new CustomCloudControllerClientFactory(httpProxyConfiguration, trustSelfSignedCerts);
		this.cc = cloudControllerClientFactory.newCloudController(cloudControllerUrl, credentials, sessionSpace);
	}

	/**
	 * Construct a client with a default space name and org name.
	 */


	public CustomCloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName,
									boolean trustSelfSignedCerts)  throws Exception {
		this(credentials, cloudControllerUrl, orgName, spaceName, null, trustSelfSignedCerts);
	}


	public CustomCloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, String orgName, String spaceName,
									HttpProxyConfiguration httpProxyConfiguration, boolean trustSelfSignedCerts) throws Exception {
		Assert.notNull(cloudControllerUrl, "URL for cloud controller cannot be null");
		CustomCloudControllerClientFactory cloudControllerClientFactory =
				new CustomCloudControllerClientFactory(httpProxyConfiguration, trustSelfSignedCerts);
		this.cc = cloudControllerClientFactory.newCloudController(cloudControllerUrl, credentials, orgName, spaceName);
	}


	/**
	 * Construct a client with a pre-configured CloudControllerClient
	 */
//	public CustomCloudFoundryClient(CustomCloudControllerClient cc) {
//		this.cc = cc;
//	}

	public OAuth2AccessToken login() {
		return cc.login();
	}

	public CloudOrganization getOrgByName(String orgName, boolean required){
		return cc.getOrgByName(orgName, required);
	}


	/*
	 * Custom Methods
	 */

	public void renameOrg(String orgName, String newName) {
		cc.renameOrg(orgName, newName);
	}

	public void deleteOrg(String orgName) {
		cc.deleteOrg(orgName);
	}

	public void renameSpace(String orgName, String spaceName, String newName) {
		cc.renameSpace(orgName, spaceName, newName);
	}

	public void createOrg(String orgName) {
		cc.createOrg(orgName);
	}

	public void setOrgRole(String orgName, String userName, String orgRole) {
		cc.setOrgRole(orgName,userName, orgRole);
	}

	public void setSpaceRole(String orgName, String spaceName, String userName, String spaceRole){
		cc.setSpaceRole(orgName, spaceName, userName, spaceRole);
	}

	public void unsetOrgRole( String orgName,String userGuid, String orgRole){
		cc.unsetOrgRole(orgName,userGuid, orgRole);
	}

	public void unsetSpaceRole(String orgName, String spaceName, String userGuid,  String spaceRole){
		cc.unsetSpaceRole(orgName, spaceName,userGuid, spaceRole);
	}


	public String getOrgSummary(String orgName) {
		return cc.getOrgSummary(orgName);
	}

	public List<CloudSpace> getSpaces(){
		return cc.getSpaces();
	}

	public void createSpace(String orgName, String spaceName){
		cc.createSpace(orgName, spaceName);
	}

	//public Map listAllManagersForOrg(UUID orgGuid){ return cc.listAllManagersForOrg(orgGuid); }

//	public void removeUserFromOrg(String orgGuid, String userGuid){
//		cc.removeUserFromOrg(orgGuid, userGuid);
//	}

	public String getUserGuid(){
		return cc.getUserGuid();
	}

	public String getSpaceSummary(String orgName, String spaceName) {
		return cc.getSpaceSummary(orgName,spaceName);
	}


	public void removeOrgFromUser(String userGuid, String orgName){
		cc.removeOrgFromUser(userGuid, orgName);
	}

	public void removeSpaceFromUser(String userGuid, String orgName,String spaceName){
		cc.removeSpaceFromUser(userGuid, orgName, spaceName);
	}

	public Map listAllOrgOrSpaceForTheUser(String userGuid, String role){
		return cc.listAllOrgOrSpaceForTheUser(userGuid, role);
	}

	//public Map getUserSummary(String userGuid){
//		return cc.getUserSummary(userGuid);
//	}

	public void deleteSpace(String orgName,String spaceName){
		cc.deleteSpace(orgName,spaceName);
	}

	public void updatePassword(String newPassword) {
		cc.updatePassword(newPassword);
	}

	public void updatePassword(CloudCredentials credentials, String newPassword) {
		cc.updatePassword(credentials, newPassword);
	}

	public void deleteUser(String userGuid) {
		cc.deleteUser(userGuid);
	}

	//public CloudInfo getInfo(){
	//	return cc.getInfo();
	//};

	public CloudInfo getCloudInfo() {
		if (info == null) {
			info = cc.getInfo();
		}
		return info;
	}
	public String getAppSummary(UUID appGuid) {
		return cc.getAppSummary(appGuid);
	}

	public String getAppStats(UUID appGuid) {
		return cc.getAppStats(appGuid);
	}

	public void restageApp(UUID appGuid) {
		cc.restageApp(appGuid);
	}

	public String getAppEvents(UUID appGuid) {
		return cc.getAppEvents(appGuid);
	}

	public List getOrganizations(){
		return cc.getOrganizations();
	}

	// REX :: GET BUILD PACKS
	public Map getBuildPacks() {
		return cc.getBuildPacks();
	}

	// REX :: CREATE SERVICE
	public String createService(String name, String servicePlanGuid, String orgName, String spaceName) {
		return cc.createService(name, servicePlanGuid, orgName, spaceName);
	}

	// REX :: TERMINATE APP INSTANCE BY INDEX
	public void terminateAppInstanceByIndex(UUID appGuid, int appInstanceIndex, String orgName, String spaceName) {
		cc.terminateAppInstanceByIndex(appGuid, appInstanceIndex, orgName, spaceName);
	}

	// REX :: BIND SERVICE V2
	public String bindServiceV2(UUID serviceInstanceGuid, String appName, Map<String, Object> parameters) {
		return cc.bindServiceV2(serviceInstanceGuid, appName, parameters);
	}

	// REX :: CREATE APPLICATION V2
	public void createApplicationV2(String appName, Staging staging, Integer disk, Integer memory, List<String> uris, Map<String, Object> paramMap) {
		cc.createApplicationV2(appName, staging, disk, memory, uris, paramMap);
	}

	public void bindRoute(String host, String domainName, String appName){
		cc.bindRoute(host,domainName,appName);
	}

	public void unbindRoute(String host, String domainName, String appName){
		cc.unbindRoute(host,domainName,appName);
	}

	public void deleteRoute(String host, String domainName){
		cc.deleteRoute(host, domainName);
	}

	public void renameInstanceService(UUID guid, String newName) {
		cc.renameInstanceService(guid, newName);
	}

	public void deleteInstanceService(UUID guid) {
		cc.deleteInstanceService(guid);
	}

	public Map<String, CloudUser> getUsersForOrgRole(UUID orgGuid, String orgRole){
		return cc.getUsersForOrgRole(orgGuid,orgRole);
	}

	public List<Map<String, Object>> getUsers(UUID orgGuid){
		return cc.getUsers(orgGuid);
	}

	public Map<String, CloudUser> getUsersForSpaceRole(UUID orgGuid, String spaceName, String spaceRole){
		return cc.getUsersForSpaceRole(orgGuid, spaceName,spaceRole);
	}

/*
	public CloudServiceInstance getServiceInstance(String serviceInstanceName){
		return cc.getServiceInstance(serviceInstanceName);
	}
*/
/*
	public Map<String, Object> getUserProvidedServiceInstance(String orgName, String spaceName, String serviceInstanceName) {
		return cc.getUserProvidedServiceInstance(orgName,spaceName, serviceInstanceName );
	}
*/

	public UUID getUserProvidedServiceInstanceGuid(String orgName, String spaceName, String serviceInstanceName){
		return cc.getUserProvidedServiceInstanceGuid(orgName,spaceName, serviceInstanceName );
	}

	public void createUserProvidedService(CloudService service, Map<String, Object> credentials){
		cc.createUserProvidedService(service,credentials);
	}

	public void createUserProvidedService(CloudService service, Map<String, Object> credentials, String syslogDrainUrl){
		cc.createUserProvidedService(service,credentials,syslogDrainUrl);
	}

	public void updateUserProvidedService(String orgName, String spaceName, String serviceInstanceName, String newServiceInstanceName, Map<String, Object> credentials, String syslogDrainUrl){
		cc.updateUserProvidedService(orgName, spaceName, serviceInstanceName, newServiceInstanceName, credentials, syslogDrainUrl);
	}

	public Map<String, Object> getUserProvidedServiceInstance(String orgName, String spaceName, String serviceInstanceName){
		return cc.getUserProvidedServiceInstance(orgName, spaceName, serviceInstanceName);
	}

	public Map<String, Object> register(ScimUser scimuser ) throws Exception {
		return cc.register(scimuser);
	}

	public void updateBuildPack(UUID guid, int position, boolean enable, boolean lock) {
		cc.updateBuildPack(guid, position, enable, lock);

	}

	public Map<String, Object> resetPassword(String userId, String newPassword, String clientId, String clientScret, String uaaTarget)  throws URISyntaxException, MalformedURLException {
		return cc.resetPassword(userId, newPassword, clientId, clientScret, uaaTarget);
	}
	//public Map<String, List<String>> getAllSpaceUsers(String orgName, String spaceName) {return cc.getAllSpaceUsers(orgName, spaceName);}

	public Map getUaaAccessToken(String clientId, String clientSecret, String uaaTarget) throws MalformedURLException{
		return cc.getUaaAccessToken(clientId, clientSecret, uaaTarget);
	}

	public void renameServiceBroker(String name, String newName ){
		cc.renameServiceBroker(name, newName);
	}


	public ResponseEntity<String> getClientList(String clientId, String clientScret, String uaaTarget)  throws URISyntaxException, MalformedURLException {
		return cc.getClientList(clientId, clientScret, uaaTarget);
	}

	public ResponseEntity<String> getClient(String clientId, String clientScret, String uaaTarget, Map<String, Object> param)  throws URISyntaxException, MalformedURLException {
		return cc.getClient(clientId, clientScret, uaaTarget, param);
	}

	public ResponseEntity<String> registerClient(String clientId, String clientScret, String uaaTarget, Map<String, Object> param)  throws URISyntaxException, MalformedURLException {
		return cc.registerClient(clientId, clientScret, uaaTarget, param);
	}

	public ResponseEntity<String> updateClient(String clientId, String clientScret, String uaaTarget, Map<String, Object> param)  throws URISyntaxException, MalformedURLException {
		return cc.updateClient(clientId, clientScret, uaaTarget, param);
	}

	public ResponseEntity<String> deleteClient(String clientId, String clientScret, String uaaTarget, Map<String, Object> param)  throws URISyntaxException, MalformedURLException {
		return cc.deleteClient(clientId, clientScret, uaaTarget, param);
	}

	public String getSpaceQuota(String guid) {
		return cc.getSpaceQuota(guid);
	}

	public String getSpace(String orgName, String spaceName) {
		return cc.getSpace(orgName, spaceName);
	}


	// REX :: BIND SERVICE V2
	public String createServiceV2(String name, String servicePlanGuid, String orgName, String spaceName,Map<String, Object> parameter) {
		return cc.createServiceV2(name, servicePlanGuid, orgName, spaceName , parameter);
	}
}

