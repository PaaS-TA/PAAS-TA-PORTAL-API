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

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.*;
import org.cloudfoundry.client.lib.rest.LoggregatorClient;
import org.cloudfoundry.client.lib.util.CloudEntityResourceMapper;
import org.cloudfoundry.client.lib.util.CloudUtil;
import org.cloudfoundry.client.lib.util.JsonUtil;
import org.cloudfoundry.identity.uaa.scim.ScimUser;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import io.netty.handler.codec.base64.Base64Decoder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

//import org.openpaas.paasta.portal.api.model.OrgSummary;

/**
 * Abstract implementation of the CloudControllerClient intended to serve as the base.
 *
 * @author Ramnivas Laddad
 * @author A.B.Srinivasan
 * @author Jennifer Hickey
 * @author Dave Syer
 * @author Thomas Risberg
 * @author Alexander Orlov
 * @author Scott Frederick
 */
public class CustomCloudControllerClientImpl implements CustomCloudControllerClient {

	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String PROXY_USER_HEADER_KEY = "Proxy-User";
	private final CloudSpace sessionSpace;
	private final CloudEntityResourceMapper resourceMapper = new CloudEntityResourceMapper();
	private final Log LOGGER;
	protected CloudCredentials cloudCredentials;
	private CustomOauthClient oauthClient;
	private RestTemplate restTemplate;
	private URL cloudControllerUrl;
	private LoggregatorClient loggregatorClient;


	public CustomCloudControllerClientImpl(URL cloudControllerUrl, RestTemplate restTemplate,
										   CustomOauthClient oauthClient, LoggregatorClient loggregatorClient,
										   CloudCredentials cloudCredentials, CloudSpace sessionSpace) {
		LOGGER = LogFactory.getLog(getClass().getName());

		initialize(cloudControllerUrl, restTemplate, oauthClient, loggregatorClient, cloudCredentials);

		this.sessionSpace = sessionSpace;
	}
//
//	public void setLoggregatorClient(LoggregatorClient loggregatorClient) {
//		this.loggregatorClient = loggregatorClient;
//	}
//
//	/**
//	 * Only for unit tests. This works around the fact that the initialize method is called within the constructor and
//	 * hence can not be overloaded, making it impossible to write unit tests that don't trigger network calls.
//	 */
//	protected CustomCloudControllerClientImpl() {
//		LOGGER = LogFactory.getLog(getClass().getName());
//	}

	public CustomCloudControllerClientImpl(URL cloudControllerUrl, RestTemplate restTemplate,
										   CustomOauthClient oauthClient, LoggregatorClient loggregatorClient,
										   CloudCredentials cloudCredentials, String orgName, String spaceName) {
		LOGGER = LogFactory.getLog(getClass().getName());
		CustomCloudControllerClientImpl tempClient =
				new CustomCloudControllerClientImpl(cloudControllerUrl, restTemplate,
						oauthClient, loggregatorClient, cloudCredentials, null);

		initialize(cloudControllerUrl, restTemplate, oauthClient, loggregatorClient, cloudCredentials);

		this.sessionSpace = validateSpaceAndOrg(spaceName, orgName, tempClient);
	}

	//	public CloudSpace getSessionSpace() {
//		return sessionSpace;
//	}
//
//	public void setSessionSpace(CloudSpace sessionSpace) {
//		this.sessionSpace = sessionSpace;
//	}
//
	public LoggregatorClient getLoggregatorClient() {
		return loggregatorClient;
	}

	private CloudSpace validateSpaceAndOrg(String spaceName, String orgName, CustomCloudControllerClientImpl client) {
		List<CloudSpace> spaces = client.getSpaces();

		for (CloudSpace space : spaces) {
			if (space.getName().equals(spaceName)) {
				CloudOrganization org = space.getOrganization();
				if (orgName == null || org.getName().equals(orgName)) {
					return space;
				}
			}
		}

		throw new IllegalArgumentException("No matching organization and space found for org: " + orgName + " space: " + spaceName);
	}


	private void initialize(URL cloudControllerUrl, RestTemplate restTemplate, CustomOauthClient oauthClient,
	                        LoggregatorClient loggregatorClient, CloudCredentials cloudCredentials) {
		Assert.notNull(cloudControllerUrl, "CloudControllerUrl cannot be null");
		Assert.notNull(restTemplate, "RestTemplate cannot be null");
		Assert.notNull(oauthClient, "OauthClient cannot be null");

		oauthClient.init(cloudCredentials);

		this.cloudCredentials = cloudCredentials;

		this.cloudControllerUrl = cloudControllerUrl;

		this.restTemplate = restTemplate;
		configureCloudFoundryRequestFactory(restTemplate);

		this.oauthClient = oauthClient;

		this.loggregatorClient = loggregatorClient;
	}


	protected RestTemplate getRestTemplate() {
		restTemplate.setErrorHandler(new CustomCloudControllerResponseErrorHandler());
		return this.restTemplate;
	}

	protected String getUrl(String path) {
		return cloudControllerUrl + (path.startsWith("/") ? path : "/" + path);
	}

	protected void configureCloudFoundryRequestFactory(RestTemplate restTemplate) {
		ClientHttpRequestFactory requestFactory = restTemplate.getRequestFactory();
		if (!(requestFactory instanceof 	CustomCloudControllerClientImpl.CloudFoundryClientHttpRequestFactory)) {
			restTemplate.setRequestFactory(
					new CustomCloudControllerClientImpl.CloudFoundryClientHttpRequestFactory(requestFactory));
		}
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getAllResources(String urlPath, Map<String, Object> urlVars) {
		List<Map<String, Object>> allResources = new ArrayList<Map<String, Object>>();
		String resp;
		if (urlVars != null) {
			resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, urlVars);
		} else {
			resp = getRestTemplate().getForObject(getUrl(urlPath), String.class);
		}
		Map<String, Object> respMap = JsonUtil.convertJsonToMap(resp);
		List<Map<String, Object>> newResources = (List<Map<String, Object>>) respMap.get("resources");
		if (newResources != null && newResources.size() > 0) {
			allResources.addAll(newResources);
		}
		String nextUrl = (String) respMap.get("next_url");
//		while (nextUrl != null && nextUrl.length() > 0) {
//			nextUrl = addPageOfResources(nextUrl, allResources);
//		}
		return allResources;
	}

//
//	@Override
//	public CloudSpace getSpace(String spaceName) {
//		String urlPath = "/v2/spaces?inline-relations-depth=1&q=name:{name}";
//		HashMap<String, Object> spaceRequest = new HashMap<String, Object>();
//		spaceRequest.put("name", spaceName);
//		List<Map<String, Object>> resourceList = getAllResources(urlPath, spaceRequest);
//		CloudSpace space = null;
//		if (resourceList.size() > 0) {
//			Map<String, Object> resource = resourceList.get(0);
//			space = resourceMapper.mapResource(resource, CloudSpace.class);
//		}
//		return space;
//	}

	/**
	 * Get organization by given name.
	 *
	 * @param orgName
	 * @param required
	 * @return CloudOrganization instance
	 */
	public CloudOrganization getOrgByName(String orgName, boolean required) {

		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/organizations?inline-relations-depth=1&q=name:{name}";
		urlVars.put("name", orgName);
		CloudOrganization org = null;
		LOGGER.info("getorg");
		List<Map<String, Object>> resourceList = getAllResources(urlPath,
				urlVars);
		if (resourceList.size() > 0) {
			Map<String, Object> resource = resourceList.get(0);
			org = resourceMapper.mapResource(resource, CloudOrganization.class);
		}

		LOGGER.info("getResourceList");
		if (org == null && required) {
			throw new IllegalArgumentException("Organization '" + orgName
					+ "' not found.");
		}
		return org;
	}

//
//	@SuppressWarnings("unchecked")
//	private String addPageOfResources(String nextUrl, List<Map<String, Object>> allResources) {
//		String resp = getRestTemplate().getForObject(getUrl(nextUrl), String.class);
//		Map<String, Object> respMap = JsonUtil.convertJsonToMap(resp);
//		List<Map<String, Object>> newResources = (List<Map<String, Object>>) respMap.get("resources");
//		if (newResources != null && newResources.size() > 0) {
//			allResources.addAll(newResources);
//		}
//		return (String) respMap.get("next_url");
//	}

	@Override
	public List<CloudSpace> getSpaces() {
		String urlPath = "/v2/spaces?inline-relations-depth=1";
		List<Map<String, Object>> resourceList = getAllResources(urlPath, null);
		List<CloudSpace> spaces = new ArrayList<CloudSpace>();
		for (Map<String, Object> resource : resourceList) {
			spaces.add(resourceMapper.mapResource(resource, CloudSpace.class));
		}
		return spaces;
	}

	@Override
	public OAuth2AccessToken login() {
		oauthClient.init(cloudCredentials);
		return oauthClient.getToken();
	}

	@Override
	public void createSpace(String orgName, String spaceName) {
		UUID orgGuid = getOrgByName(orgName, true).getMeta().getGuid();
		UUID spaceGuid = getSpaceGuid(spaceName, orgGuid);
		if (spaceGuid == null) {
			doCreateSpace(spaceName, orgGuid);
		}
	}

	private UUID doCreateSpace(String spaceName, UUID orgGuid) {
		String urlPath = "/v2/spaces";
		HashMap<String, Object> spaceRequest = new HashMap<String, Object>();
		spaceRequest.put("organization_guid", orgGuid);
		spaceRequest.put("name", spaceName);
		String resp = getRestTemplate().postForObject(getUrl(urlPath), spaceRequest, String.class);
		Map<String, Object> respMap = JsonUtil.convertJsonToMap(resp);
		return resourceMapper.getGuidOfResource(respMap);
	}

	private UUID getSpaceGuid(String spaceName, UUID orgGuid) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/organizations/{orgGuid}/spaces?inline-relations-depth=1&q=name:{name}";
		urlVars.put("orgGuid", orgGuid);
		urlVars.put("name", spaceName);
		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		if (resourceList.size() > 0) {
			Map<String, Object> resource = resourceList.get(0);
			return resourceMapper.getGuidOfResource(resource);
		}
		return null;
	}

	private UUID getDomainGuid(String domainName, boolean required) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/domains?inline-relations-depth=1&q=name:{name}";
		urlVars.put("name", domainName);
		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		UUID domainGuid = null;
		if (resourceList.size() > 0) {
			Map<String, Object> resource = resourceList.get(0);
			domainGuid = resourceMapper.getGuidOfResource(resource);
		}
		if (domainGuid == null && required) {
			throw new IllegalArgumentException("Domain '" + domainName + "' not found.");
		}
		return domainGuid;
	}

	private UUID getRouteGuid(String host, UUID domainGuid) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2";
		urlPath = urlPath + "/routes?inline-relations-depth=0&q=host:{host}";
		urlVars.put("host", host);
		List<Map<String, Object>> allRoutes = getAllResources(urlPath, urlVars);
		UUID routeGuid = null;
		for (Map<String, Object> route : allRoutes) {
			UUID routeSpace = CloudEntityResourceMapper.getEntityAttribute(route, "space_guid", UUID.class);
			UUID routeDomain = CloudEntityResourceMapper.getEntityAttribute(route, "domain_guid", UUID.class);
			if (sessionSpace.getMeta().getGuid().equals(routeSpace) &&
					domainGuid.equals(routeDomain)) {
				routeGuid = CloudEntityResourceMapper.getMeta(route).getGuid();
			}
		}
		return routeGuid;
	}

	private UUID doAddRoute(String host, UUID domainGuid) {
		assertSpaceProvided("add route");

		HashMap<String, Object> routeRequest = new HashMap<String, Object>();
		routeRequest.put("host", host);
		routeRequest.put("domain_guid", domainGuid);
		routeRequest.put("space_guid", sessionSpace.getMeta().getGuid());
		String routeResp = getRestTemplate().postForObject(getUrl("/v2/routes"), routeRequest, String.class);
		Map<String, Object> routeEntity = JsonUtil.convertJsonToMap(routeResp);
		return CloudEntityResourceMapper.getMeta(routeEntity).getGuid();
	}

	private void assertSpaceProvided(String operation) {
		Assert.notNull(sessionSpace, "Unable to " + operation + " without specifying organization and space to use.");
	}

	@Override
	public List<CloudOrganization> getOrganizations() {
		String urlPath = "/v2/organizations?inline-relations-depth=0";
		List<Map<String, Object>> resourceList = getAllResources(urlPath, null);
		List<CloudOrganization> orgs = new ArrayList<CloudOrganization>();
		for (Map<String, Object> resource : resourceList) {
			orgs.add(resourceMapper.mapResource(resource, CloudOrganization.class));
		}
		return orgs;
	}

	@SuppressWarnings("restriction")
	private Map<String, Object> getUserInfo() {

		String userJson = "{}";
		OAuth2AccessToken accessToken = oauthClient.getToken();
		if (accessToken != null) {
			String tokenString = accessToken.getValue();
			int x = tokenString.indexOf('.');
			int y = tokenString.indexOf('.', x + 1);
			String encodedString = tokenString.substring(x + 1, y);
			try {
				//byte[] decodedBytes = new sun.misc.BASE64Decoder().decodeBuffer(encodedString);
			    byte[] decodedBytes = Base64.getDecoder().decode( encodedString );
				userJson = new String(decodedBytes, 0, decodedBytes.length, "UTF-8");
			} catch (IOException e) {
				LOGGER.error(e);
			}
		}
		return JsonUtil.convertJsonToMap(userJson);
	}


	/*
	private UUID getSpaceGuid(String orgName, String spaceName) {
		CloudOrganization org = getOrgByName(orgName, true);
		return getSpaceGuid(spaceName, org.getMeta().getGuid());
	}
	*/

	@SuppressWarnings("unchecked")
	@Override
	public CloudInfo getInfo() {
		// info comes from two end points: /info and /v2/info

		String infoV2Json = getRestTemplate().getForObject(getUrl("/v2/info"), String.class);
		Map<String, Object> infoV2Map = JsonUtil.convertJsonToMap(infoV2Json);

		Map<String, Object> userMap = getUserInfo();

		String infoJson = getRestTemplate().getForObject(getUrl("/info"), String.class);
		Map<String, Object> infoMap = JsonUtil.convertJsonToMap(infoJson);
		Map<String, Object> limitMap = (Map<String, Object>) infoMap.get("limits");
		Map<String, Object> usageMap = (Map<String, Object>) infoMap.get("usage");

		String name = CloudUtil.parse(String.class, infoV2Map.get("name"));
		String support = CloudUtil.parse(String.class, infoV2Map.get("support"));
		String authorizationEndpoint = CloudUtil.parse(String.class, infoV2Map.get("authorization_endpoint"));
		String build = CloudUtil.parse(String.class, infoV2Map.get("build"));
		String version = "" + CloudUtil.parse(Number.class, infoV2Map.get("version"));
		String description = CloudUtil.parse(String.class, infoV2Map.get("description"));

		CloudInfo.Limits limits = null;
		CloudInfo.Usage usage = null;
		boolean debug = false;
		if (oauthClient.getToken() != null) {
			limits = new CloudInfo.Limits(limitMap);
			usage = new CloudInfo.Usage(usageMap);
			debug = CloudUtil.parse(Boolean.class, infoMap.get("allow_debug"));
		}

		String loggregatorEndpoint = CloudUtil.parse(String.class, infoV2Map.get("logging_endpoint"));

		return new CloudInfo(name, support, authorizationEndpoint, build, version, (String)userMap.get("user_name"),
				description, limits, usage, debug, loggregatorEndpoint);
	}

	//App 이름으로 App GUID를 획득하는데 쓰임
	@SuppressWarnings("unchecked")
	private UUID getAppId(String appName) {
		Map<String, Object> resource = findApplicationResource(appName, false);
		UUID guid = null;
		if (resource != null) {
			Map<String, Object> appMeta = (Map<String, Object>) resource.get("metadata");
			guid = UUID.fromString(String.valueOf(appMeta.get("guid")));
		}
		return guid;
	}

	//App 이름으로 App GUID를 획득하는데 쓰임
	private Map<String, Object> findApplicationResource(String appName, boolean fetchServiceInfo) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2";
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		urlVars.put("q", "name:" + appName);
		urlPath = urlPath + "/apps?inline-relations-depth=1&q={q}";

		List<Map<String, Object>> allResources = getAllResources(urlPath, urlVars);
		if(!allResources.isEmpty()) {
			return processApplicationResource(allResources.get(0), fetchServiceInfo);
		}
		return null;
	}

	//App 이름으로 App GUID를 획득하는데 쓰임
	private Map<String, Object> processApplicationResource(Map<String, Object> resource, boolean fetchServiceInfo) {
		if (fetchServiceInfo) {
			fillInEmbeddedResource(resource, "service_bindings", "service_instance");
		}
		fillInEmbeddedResource(resource, "stack");
		return resource;
	}

	//App 이름으로 App GUID를 획득하는데 쓰임
	@SuppressWarnings("unchecked")
	private void fillInEmbeddedResource(Map<String, Object> resource, String... resourcePath) {
		if (resourcePath.length == 0) {
			return;
		}
		Map<String, Object> entity = (Map<String, Object>) resource.get("entity");

		String headKey = resourcePath[0];
		String[] tailPath = Arrays.copyOfRange(resourcePath, 1, resourcePath.length);

//		if (!entity.containsKey(headKey)) {
//			String pathUrl = entity.get(headKey + "_url").toString();
//			Object response = getRestTemplate().getForObject(getUrl(pathUrl), Object.class);
//			if (response instanceof Map) {
//				Map<String, Object> responseMap = (Map<String, Object>) response;
//				if (responseMap.containsKey("resources")) {
//					response = responseMap.get("resources");
//				}
//			}
//			entity.put(headKey, response);
//		}
		Object embeddedResource = entity.get(headKey);

		if (embeddedResource instanceof Map) {
			Map<String, Object> embeddedResourceMap = (Map<String, Object>) embeddedResource;
			fillInEmbeddedResource(embeddedResourceMap, tailPath);
		}
	}

	private void doDeleteRoute(UUID routeGuid) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/routes/{route}";
		urlVars.put("route", routeGuid);
		getRestTemplate().delete(getUrl(urlPath), urlVars);
	}

	/*
	 * Custom Methods
	 */
	public void renameOrg(String orgName, String newName) {
		UUID guid = getOrgByName(orgName, true).getMeta().getGuid();
		HashMap<String, Object> orgRequest = new HashMap<String, Object>();
		orgRequest.put("name", newName);
		if (guid != null) {
			getRestTemplate().put(getUrl("/v2/organizations/{guid}"), orgRequest, guid);
		}
	}

	public void deleteOrg(String orgName) {

		UUID guid = getOrgByName(orgName, true).getMeta().getGuid();
		if (guid != null) {
			getRestTemplate().delete(getUrl("/v2/organizations/{guid}?async=false"), guid);
		}
	}

	public void renameSpace(String orgName, String spaceName, String newSpaceName) {
		UUID orgGuid = getOrgByName(orgName, true).getMeta().getGuid();
		UUID spaceGuid = getSpaceGuid(spaceName, orgGuid);
		HashMap<String, Object> spaceRequest = new HashMap<String, Object>();
		spaceRequest.put("name", newSpaceName);

		if (spaceGuid != null) {
			getRestTemplate().put(getUrl("/v2/spaces/{spaceGuid}"), spaceRequest, spaceGuid);
		}
	}

	public void createOrg(String orgName) {
		HashMap<String, Object> orgRequest = new HashMap<String, Object>();
		orgRequest.put("name", orgName);
		LOGGER.info(orgRequest);
		getRestTemplate().postForObject(getUrl("/v2/organizations"), orgRequest, String.class);
	}

	public void deleteSpace(String orgName, String spaceName) {
		LOGGER.info("deleteSpace start");
		//assertSpaceProvided("delete a space");
		UUID orgGuid = getOrgByName(orgName, true).getMeta().getGuid();
		UUID spaceGuid = getSpaceGuid(spaceName, orgGuid);

		if (spaceGuid != null) {
			getRestTemplate().delete(getUrl("/v2/spaces/{guid}?async=false"), spaceGuid);
		}
		LOGGER.info("deleteSpace end");
	}

	public void setOrgRole(String orgName, String userName, String orgRole) {
		CloudOrganization org = getOrgByName(orgName, true);
		UUID orgGuid = org.getMeta().getGuid();

		HashMap<String, Object> orgRequest = new HashMap<String, Object>();
		orgRequest.put("username", userName);
		if (orgGuid != null){
			getRestTemplate().put(getUrl("/v2/organizations/{orgGuid}/{orgRole}"), orgRequest, orgGuid, orgRole);
		}
	}

	//space role을 부여하기 전에 해당 조직에 대한 role을 유저에게 부여해야 한다.
	public void setSpaceRole(String orgName, String spaceName, String userName, String spaceRole) {

		UUID orgGuid = getOrgByName(orgName, true).getMeta().getGuid();
		UUID spaceGuid = getSpaceGuid(spaceName, orgGuid);

		HashMap<String, Object> spaceRequest = new HashMap<String, Object>();
		spaceRequest.put("username",userName);

        getRestTemplate().put(getUrl("/v2/spaces/{spaceGuid}/{spaceRole}"),spaceRequest, spaceGuid, spaceRole);
	}

	//unset의 경우 set과 달리 userGuid를 파라미터로 받는다.
	public void unsetOrgRole(String orgName, String userGuid, String orgRole){

		UUID orgGuid =  getOrgByName(orgName, true).getMeta().getGuid();
		if (orgGuid != null && userGuid != null){
			getRestTemplate().delete(getUrl("/v2/organizations/{orgGuid}/{orgRole}/{userGuid}"), orgGuid, orgRole, userGuid);
		}
	}

	//unset의 경우 set과 달리 userGuid를 파라미터로 받는다.
	public void unsetSpaceRole(String orgName, String spaceName, String userGuid, String spaceRole){
		LOGGER.info("unsetSpaceRole start");

		UUID orgGuid =  getOrgByName(orgName, true).getMeta().getGuid();
		UUID spaceGuid = getSpaceGuid(spaceName,orgGuid);

        getRestTemplate().delete(getUrl("/v2/spaces/{spaceGuid}/{spaceRole}/{userGuid}"), spaceGuid, spaceRole, userGuid);

		LOGGER.info("unsetSpaceRole end");
	}

	public String getOrgSummary(String orgName) {
		UUID guid = getOrgByName(orgName, true).getMeta().getGuid();
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/organizations/{guid}/summary";
		LOGGER.info("urlPath: " + urlPath);
		urlVars.put("name", orgName);
		urlVars.put("guid", guid);
		String resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, urlVars);

		return resp;
	}


//	public void removeUserFromOrg(String orgGuid, String userGuid){
//		if (orgGuid != null && userGuid != null){
//			getRestTemplate().delete(getUrl("/v2/organizations/{orgGuid}/users/{userGuid}"), orgGuid, userGuid);
//		}
//	}
//
//	public Map listAllManagersForOrg(UUID orgGuid) {
//		//HashMap<String, Object> orgRequest = new HashMap<String, Object>();
//		//orgRequest.put("name", orgGuid);
//		//logger.info(orgRequest);
//		String stringGuid = orgGuid.toString();
//		LOGGER.info(stringGuid);
//		Map managerList = getRestTemplate().getForObject(getUrl("/v2/organizations/{orgGuid}/managers"), HashMap.class, orgGuid);
//		return managerList;
//	}

	public String getUserGuid(){
		LOGGER.info("getUserGuid start");
		String userGuid = getUserInfo().get("user_id").toString();
		LOGGER.info(userGuid);
		return userGuid;
	}

	public String getSpaceSummary(String orgName,String spaceName) {

		String resp = null;
		if (orgName != null &&  spaceName!= null){
			UUID orgGuid =  getOrgByName(orgName, true).getMeta().getGuid();
			UUID spaceGuid = getSpaceGuid(spaceName, orgGuid);

			Map<String, Object> urlVars = new HashMap<String, Object>();
			String urlPath = "/v2/spaces/{spaceGuid}/summary";
			LOGGER.info("urlPath: " + urlPath);
			urlVars.put("spaceGuid", spaceGuid);

			resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, urlVars);
		}
		return resp;
	}

	public void removeOrgFromUser(String userGuid, String orgName) {
		LOGGER.info("removeOrgFromUser start");
		if (userGuid !=null && orgName != null){
			UUID orgGuid =  getOrgByName(orgName, true).getMeta().getGuid();
			getRestTemplate().delete(getUrl("/v2/users/{userGuid}/organizations/{orgGuid}"), userGuid, orgGuid);
		}
		LOGGER.info("removeOrgFromUser end");
	}

	public void removeSpaceFromUser(String userGuid, String orgName,String spaceName) {
		LOGGER.info("removeSpaceFromUser start");
		if (userGuid !=null && spaceName != null && orgName != null){
			UUID orgGuid =  getOrgByName(orgName, true).getMeta().getGuid();
			UUID spaceGuid = getSpaceGuid(spaceName, orgGuid);
			getRestTemplate().delete(getUrl("/v2/users/{userGuid}/spaces/{spaceGuid}"), userGuid, spaceGuid);
		}
		LOGGER.info("removeSpaceFromUser end");
	}

	public Map listAllOrgOrSpaceForTheUser(String userGuid, String role) {
		LOGGER.info("listAllOrgOrSpaceForTheUser start");

		Map<String, Object> listAllOrgOrSpace = new HashMap<String, Object>();

		if (userGuid != null && role != null){
			Map<String, Object> urlVars = new HashMap<String, Object>();
			urlVars.put("userGuid",userGuid);
			urlVars.put("role",role);
			listAllOrgOrSpace = getRestTemplate().getForObject(getUrl("/v2/users/{userGuid}/{role}"), HashMap.class, urlVars);
		}

		LOGGER.info("listAllOrgOrSpaceForTheUser end");
		return listAllOrgOrSpace;
	}

	public String getAppSummary(UUID appGuid) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/apps/{appGuid}/summary";
		LOGGER.info("urlPath: " + urlPath);
		urlVars.put("appGuid", appGuid);
		String resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, urlVars);

		return resp;
	}

	public void restageApp(UUID appGuid) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/apps/{appGuid}/restage";
		LOGGER.info("urlPath: " + urlPath);
		urlVars.put("appGuid", appGuid);
		getRestTemplate().postForObject(getUrl(urlPath), null , String.class, urlVars);

	}

	public String getAppStats(UUID appGuid) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/apps/{appGuid}/stats";
		LOGGER.info("urlPath: " + urlPath);
		urlVars.put("appGuid", appGuid);
		String resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, urlVars);

		return resp;
	}

	public String getAppEvents(UUID appGuid) {

		//Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/events?page=1&results-per-page=100&order-direction=desc&q=actee:{appGuid}";
		LOGGER.info("urlPath: " + urlPath);
		//urlVars.put("appGuid", appGuid);
		String resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, appGuid);

		return resp;
	}

	public void bindRoute(String host, String domainName, String appName) {
		UUID domainGuid = getDomainGuid(domainName,true);
		UUID routeGuid = getRouteGuid(host, domainGuid);
		UUID appGuid = getAppId(appName);
		if (routeGuid == null) {
			routeGuid = doAddRoute(host, domainGuid);
		}
		String bindPath = "/v2/apps/{app}/routes/{route}";
		Map<String, Object> bindVars = new HashMap<String, Object>();
		bindVars.put("app", appGuid);
		bindVars.put("route", routeGuid);
		HashMap<String, Object> bindRequest = new HashMap<String, Object>();
		getRestTemplate().put(getUrl(bindPath), bindRequest, bindVars);
	}

	public void unbindRoute(String host, String domainName, String appName) {
		UUID domainGuid = getDomainGuid(domainName,true);
		UUID routeGuid = getRouteGuid(host, domainGuid);
		UUID appGuid = getAppId(appName);
		if (routeGuid != null) {
			String bindPath = "/v2/apps/{app}/routes/{route}";
			Map<String, Object> bindVars = new HashMap<String, Object>();
			bindVars.put("app", appGuid);
			bindVars.put("route", routeGuid);
			getRestTemplate().delete(getUrl(bindPath), bindVars);
		}

	}

	@Override
	public void updatePassword(String newPassword) {
		updatePassword(cloudCredentials, newPassword);
	}

	@Override
	public void updatePassword(CloudCredentials credentials, String newPassword) {
		oauthClient.changePassword(credentials.getPassword(), newPassword);
		CloudCredentials newCloudCredentials = new CloudCredentials(credentials.getEmail(), newPassword);
		if (cloudCredentials.getProxyUser() != null) {
			cloudCredentials = newCloudCredentials.proxyForUser(cloudCredentials.getProxyUser());
		} else {
			cloudCredentials = newCloudCredentials;
		}
	}

	@Override
	public void deleteUser(String userGuid) {
		oauthClient.deleteUser(userGuid);
	}

	// REX :: GET BUILD PACKS
	@Override
	public HashMap getBuildPacks() {
		return getRestTemplate().getForObject(getUrl("/v2/buildpacks"), HashMap.class, "");
	}

	// REX :: CREATE SERVICE
	@Override
	public String createService(String name, String servicePlanGuid, String orgName, String spaceName) {
		String urlPath = "/v2/service_instances?accepts_incomplete=true";
		Map<String, Object> param = new HashMap<>();

		param.put("name", name);
		param.put("service_plan_guid", servicePlanGuid);
		param.put("space_guid", getSpaceGuid(spaceName, getOrgByName(orgName, true).getMeta().getGuid()));

		return getRestTemplate().postForObject(getUrl(urlPath), param, String.class);
	}

	// REX :: TERMINATE APP INSTANCE BY INDEX
	@Override
	public void terminateAppInstanceByIndex(UUID appGuid, int appInstanceIndex, String orgName, String spaceName) {
		String urlPath = "/v2/apps/{guid}/instances/{index}";
		Map<String, Object> param = new HashMap<>();

		param.put("guid", appGuid);
		param.put("index", appInstanceIndex);

		getRestTemplate().delete(getUrl(urlPath), param);
	}

	// REX :: BIND SERVICE V2
	@Override
	public String bindServiceV2(UUID serviceInstanceGuid, String appName, Map<String, Object> parameters) {
		HashMap<String, Object> param = new HashMap<>();

		param.put("service_instance_guid", serviceInstanceGuid);
		param.put("app_guid", getAppId(appName));
		param.put("parameters", parameters);
		LOGGER.info(getUrl("/v2/service_bindings"));
		LOGGER.info(param.toString());
		return getRestTemplate().postForObject(getUrl("/v2/service_bindings"), param, String.class);
	}

	// REX :: CREATE APPLICATION V2
	@Override
	public void createApplicationV2(String appName, Staging staging, Integer disk, Integer memory, List<String> uris, Map<String, Object> paramMap) {
		HashMap<String, Object> appRequest = new HashMap<String, Object>();
		appRequest.put("space_guid", sessionSpace.getMeta().getGuid());
		appRequest.put("name", appName);
		appRequest.put("memory", memory);
		if (disk != null) {
			appRequest.put("disk_quota", disk);
		}
		appRequest.put("instances", 1);
		addStagingToRequest(staging, appRequest);
		appRequest.put("state", CloudApplication.AppState.STOPPED);

		appRequest.put("environment_json", paramMap);

		String appResp = getRestTemplate().postForObject(getUrl("/v2/apps"), appRequest, String.class);
		Map<String, Object> appEntity = JsonUtil.convertJsonToMap(appResp);
		UUID newAppGuid = CloudEntityResourceMapper.getMeta(appEntity).getGuid();

		if (uris != null && uris.size() > 0) {
			addUris(uris, newAppGuid);
		}
	}

	// REX :: CREATE APPLICATION V2
	private void addStagingToRequest(Staging staging, HashMap<String, Object> appRequest) {
		if (staging.getBuildpackUrl() != null) {
			appRequest.put("buildpack", staging.getBuildpackUrl());
		}
		if (staging.getCommand() != null) {
			appRequest.put("command", staging.getCommand());
		}
		if (staging.getStack() != null) {
			appRequest.put("stack_guid", getStack(staging.getStack()).getMeta().getGuid());
		}
		if (staging.getHealthCheckTimeout() != null) {
			appRequest.put("health_check_timeout", staging.getHealthCheckTimeout());
		}
	}

	// REX :: CREATE APPLICATION V2
	@Override
	public CloudStack getStack(String name) {
		String urlPath = "/v2/stacks?q={q}";
		Map<String, Object> urlVars = new HashMap<String, Object>();
		urlVars.put("q", "name:" + name);
		List<Map<String, Object>> resources = getAllResources(urlPath, urlVars);
		if (resources.size() > 0) {
			Map<String, Object> resource = resources.get(0);
			return resourceMapper.mapResource(resource, CloudStack.class);
		}
		return null;
	}

	// REX :: CREATE APPLICATION V2
	private void addUris(List<String> uris, UUID appGuid) {
		Map<String, UUID> domains = getDomainGuids();
		for (String uri : uris) {
			Map<String, String> uriInfo = new HashMap<String, String>(2);
			extractUriInfo(domains, uri, uriInfo);
			UUID domainGuid = domains.get(uriInfo.get("domainName"));
			bindRoute(uriInfo.get("host"), domainGuid, appGuid);
		}
	}

	// REX :: CREATE APPLICATION V2
	private Map<String, UUID> getDomainGuids() {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2";
		if (sessionSpace != null) {
			urlVars.put("space", sessionSpace.getMeta().getGuid());
			urlPath = urlPath + "/spaces/{space}";
		}
		String domainPath = urlPath + "/domains?inline-relations-depth=1";
		List<Map<String, Object>> resourceList = getAllResources(domainPath, urlVars);
		Map<String, UUID> domains = new HashMap<String, UUID>(resourceList.size());
		for (Map<String, Object> d : resourceList) {
			domains.put(
					CloudEntityResourceMapper.getEntityAttribute(d, "name", String.class),
					CloudEntityResourceMapper.getMeta(d).getGuid());
		}
		return domains;
	}

	// REX :: CREATE APPLICATION V2
	private void extractUriInfo(Map<String, UUID> domains, String uri, Map<String, String> uriInfo) {
		URI newUri = URI.create(uri);
		String authority = newUri.getScheme() != null ? newUri.getAuthority() : newUri.getPath();
		for (String domain : domains.keySet()) {
			if (authority != null && authority.endsWith(domain)) {
				String previousDomain = uriInfo.get("domainName");
				if (previousDomain == null || domain.length() > previousDomain.length()) {
					//Favor most specific subdomains
					uriInfo.put("domainName", domain);
					if (domain.length() < authority.length()) {
						uriInfo.put("host", authority.substring(0, authority.indexOf(domain) - 1));
					} else if (domain.length() == authority.length()) {
						uriInfo.put("host", "");
					}
				}
			}
		}
		if (uriInfo.get("domainName") == null) {
			throw new IllegalArgumentException("Domain not found for URI " + uri);
		}
		if (uriInfo.get("host") == null) {
			throw new IllegalArgumentException("Invalid URI " + uri +
					" -- host not specified for domain " + uriInfo.get("domainName"));
		}
	}

	// REX :: CREATE APPLICATION V2
	private void bindRoute(String host, UUID domainGuid, UUID appGuid) {
		UUID routeGuid = getRouteGuid(host, domainGuid);
		if (routeGuid == null) {
			routeGuid = doAddRoute(host, domainGuid);
		}
		String bindPath = "/v2/apps/{app}/routes/{route}";
		Map<String, Object> bindVars = new HashMap<String, Object>();
		bindVars.put("app", appGuid);
		bindVars.put("route", routeGuid);
		HashMap<String, Object> bindRequest = new HashMap<String, Object>();
		getRestTemplate().put(getUrl(bindPath), bindRequest, bindVars);
	}

	@Override
	public void deleteRoute(String host, String domainName) {
		assertSpaceProvided("delete route for domain");
		UUID domainGuid = getDomainGuid(domainName, true);
		UUID routeGuid = getRouteGuid(host, domainGuid);
		if (routeGuid == null) {
			throw new IllegalArgumentException("Host '" + host + "' not found for domain '" + domainName + "'.");
		}
		doDeleteRoute(routeGuid);
	}

	public void renameInstanceService(UUID guid, String newName) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/service_instances/{guid}";
		LOGGER.info("urlPath: " + urlPath);
		urlVars.put("guid", guid);
		HashMap<String, Object> request = new HashMap<String, Object>();
		request.put("name", newName);

		try {
			getRestTemplate().put(getUrl(urlPath), request, urlVars);
		}catch(Exception e){
			urlPath = "/v2/user_provided_service_instances/{guid}";
			getRestTemplate().put(getUrl(urlPath), request, urlVars);
		}

	}

	public void deleteInstanceService(UUID guid) {
		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/service_instances/{guid}?accepts_incomplete=true";
		LOGGER.info("urlPath: " + urlPath + " / guid : " + guid);
		urlVars.put("guid", guid);
		if(guid != null) {
			try {
				getRestTemplate().delete(getUrl(urlPath), urlVars);
			}catch(Exception e){
				urlPath = "/v2/user_provided_service_instances/{guid}?accepts_incomplete=true";
				getRestTemplate().delete(getUrl(urlPath), urlVars);
			}
		}
	}

	@Override
	public Map<String, CloudUser> getUsersForOrgRole(UUID orgGuid, String orgRole) {

		String urlPath = "/v2/organizations/{orgGuid}/{orgRole}";

		Map<String, Object> urlVars = new HashMap<String, Object>();
		urlVars.put("orgGuid", orgGuid);
		urlVars.put("orgRole", orgRole);

		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		Map<String, CloudUser> orgUsers = new HashedMap();

		for (Map<String, Object> resource : resourceList) {
			CloudUser user = resourceMapper.mapResource(resource, CloudUser.class);
			if(user.getUsername() != null){
				orgUsers.put(user.getUsername(),user);
			}
		}

		return orgUsers;
	}

	@Override
	public Map<String, CloudUser> getUsersForSpaceRole(UUID orgGuid, String spaceName, String spaceRole) {

		String urlPath = "/v2/spaces/{spaceGuid}/{spaceRole}";

		Map<String, Object> urlVars = new HashMap<String, Object>();
		urlVars.put("spaceGuid", getSpaceGuid(spaceName, orgGuid));
		urlVars.put("spaceRole", spaceRole);

		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		Map<String, CloudUser> spaceUsers = new HashedMap();

		for (Map<String, Object> resource : resourceList) {
			CloudUser user = resourceMapper.mapResource(resource, CloudUser.class);
			if(user.getUsername() != null){
				spaceUsers.put(user.getUsername(),user);
			}
		}

		return spaceUsers;
	}

	@Override
	public List<Map<String, Object>> getUsers(UUID orgGuid) {

		String urlPath = "/v2/organizations/{orgGuid}/{orgRole}";

		Map<String, Object> urlVars = new HashMap<>();
		urlVars.put("orgGuid", orgGuid);
		urlVars.put("orgRole", "users");

		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		List<Map<String, Object>> orgUserList = new ArrayList<>();

		for (Map<String, Object> resource : resourceList) {

			CloudUser user = resourceMapper.mapResource(resource, CloudUser.class);

			if(user.getUsername() != null){
				Map<String, Object> userMap = new HashedMap();
				userMap.put("userName", user.getUsername());
				userMap.put("userGuid", user.getMeta().getGuid());
				orgUserList.add(userMap);
			}
		}

		return orgUserList;
	}

	@Override
	public Map<String, Object> getUserProvidedServiceInstance(String orgName, String spaceName, String serviceInstanceName) {
		UUID orgGuid = getOrgByName(orgName, true).getMeta().getGuid();
		UUID spaceGuid = getSpaceGuid(spaceName, orgGuid);

		Map<String, Object> urlVars = new HashMap<String, Object>();

		//urlVars.put("q", "organization_guid:" + orgGuid);
		//String urlPath = "/v2/user_provided_service_instances?q={q}";
		//현재 버전에서 필터 사용 안됨.

		String urlPath = "/v2/user_provided_service_instances";
		String resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, urlVars);

		Map<String, Object> respMap = JsonUtil.convertJsonToMap(resp);
		List<Map<String, Object>> resources = (List<Map<String, Object>>) respMap.get("resources");

		Map<String, Object> serviceInstance = resources.stream().filter(instance ->
				((Map)instance.get("entity")).get("name").equals(serviceInstanceName) &&
				((Map)instance.get("entity")).get("space_guid").toString().equals(spaceGuid.toString())
			).findFirst().get();

		return serviceInstance;
	}

	@Override
	public UUID getUserProvidedServiceInstanceGuid(String orgName, String spaceName, String serviceInstanceName){
		Map<String, Object> serviceInstance = getUserProvidedServiceInstance(orgName, spaceName,serviceInstanceName);
		UUID serviceInstanceGuid = UUID.fromString(String.valueOf(((Map)serviceInstance.get("metadata")).get("guid")));
		return serviceInstanceGuid;
	}

	@Override
	public void createUserProvidedService(CloudService service, Map<String, Object> credentials) {
		createUserProvidedServiceDelegate(service, credentials, "");
	}

	@Override
	public void createUserProvidedService(CloudService service, Map<String, Object> credentials, String syslogDrainUrl) {
		createUserProvidedServiceDelegate(service, credentials, syslogDrainUrl);
	}

	private void createUserProvidedServiceDelegate(CloudService service, Map<String, Object> credentials, String syslogDrainUrl) {
		assertSpaceProvided("create service");
		Assert.notNull(credentials, "service credentials must not be null");
		Assert.notNull(service, "service must not be null");
		Assert.notNull(service.getName(), "service name must not be null");
		Assert.isNull(service.getLabel(), "service label is not valid for user-provided services");
		Assert.isNull(service.getProvider(), "service provider is not valid for user-provided services");
		Assert.isNull(service.getVersion(), "service version is not valid for user-provided services");
		Assert.isNull(service.getPlan(), "service plan is not valid for user-provided services");

		HashMap<String, Object> serviceRequest = new HashMap<>();
		serviceRequest.put("space_guid", sessionSpace.getMeta().getGuid());
		serviceRequest.put("name", service.getName());
		serviceRequest.put("credentials", credentials);
		if (syslogDrainUrl != null && !syslogDrainUrl.equals("")) {
			serviceRequest.put("syslog_drain_url", syslogDrainUrl);
		}

		getRestTemplate().postForObject(getUrl("/v2/user_provided_service_instances"), serviceRequest, String.class);
	}

	@Override
	public void updateUserProvidedService(String orgName, String spaceName,String serviceInstanceName, String newServiceInstanceName,  Map<String, Object> credentials, String syslogDrainUrl) {
		UUID userProvidedGuid = getUserProvidedServiceInstanceGuid(orgName, spaceName,serviceInstanceName);
		HashMap<String, Object> updateRequest = new HashMap<>();

		updateRequest.put("name", newServiceInstanceName);
		updateRequest.put("credentials", credentials);
		updateRequest.put("syslog_drain_url", syslogDrainUrl);

		getRestTemplate().put(getUrl("/v2/user_provided_service_instances/{userProvidedGuid}"), updateRequest, userProvidedGuid);

	}

	@Override
	public Map resetPassword(String userId, String newPassword, String clientId, String clientSecret, String uaaTarget) throws URISyntaxException, MalformedURLException {
		return oauthClient.resetPassword(userId, newPassword, clientId, clientSecret, uaaTarget) ;
	}

	@Override
	public Map register(ScimUser scimUser) throws MalformedURLException {
		return oauthClient.register(scimUser) ;
	}

	public void updateBuildPack(UUID guid, int position, boolean enable, boolean lock) {
		HashMap<String, Object> updateRequest = new HashMap<>();

		updateRequest.put("position", position);
		updateRequest.put("enabled", enable);
		updateRequest.put("locked", lock);

		getRestTemplate().put(getUrl("/v2/buildpacks/{guid}"), updateRequest, guid);

	}

	public Map getUaaAccessToken(String clientId, String clientSecret, String uaaTarget) throws MalformedURLException{
		return oauthClient.getUaaAccessToken(clientId, clientSecret, uaaTarget) ;
	}

	public void renameServiceBroker(String name, String newName) {
		HashMap<String, Object> updateRequest = new HashMap<>();

		CloudServiceBroker existingBroker = getServiceBroker(name);

		HashMap<String, Object> serviceRequest = new HashMap<>();
		serviceRequest.put("name", newName);
		getRestTemplate().put(getUrl("/v2/service_brokers/{guid}"), serviceRequest, existingBroker.getMeta().getGuid());

	}

//	@Override
//	public void register(String email, String password); {
//		oauthClient.register(email, password);
//	}

	public CloudServiceBroker getServiceBroker(String name) {
		String urlPath = "/v2/service_brokers?q={q}";
		Map<String, Object> urlVars = new HashMap<>();
		urlVars.put("q", "name:" + name);
		List<Map<String, Object>> resourceList = getAllResources(urlPath, urlVars);
		CloudServiceBroker serviceBroker = null;
		if (resourceList.size() > 0) {
			final Map<String, Object> resource = resourceList.get(0);
			serviceBroker = resourceMapper.mapResource(resource, CloudServiceBroker.class);
		}
		return serviceBroker;
	}

	@Override
	public ResponseEntity<String> getClientList(String clientId, String clientSecret, String uaaTarget) throws URISyntaxException, MalformedURLException {
		return oauthClient.getClientList(clientId, clientSecret, uaaTarget) ;
	}

	@Override
	public ResponseEntity<String> getClient(String clientId, String clientSecret, String uaaTarget, Map<String, Object> param) throws URISyntaxException, MalformedURLException {
		return oauthClient.getClient(clientId, clientSecret, uaaTarget, param) ;
	}

	@Override
	public ResponseEntity<String> registerClient(String clientId, String clientSecret, String uaaTarget, Map<String, Object> param) throws URISyntaxException, MalformedURLException {
		return oauthClient.registerClient(clientId, clientSecret, uaaTarget, param) ;
	}

	@Override
	public ResponseEntity<String> updateClient(String clientId, String clientSecret, String uaaTarget, Map<String, Object> param) throws URISyntaxException, MalformedURLException {
		return oauthClient.updateClient(clientId, clientSecret, uaaTarget, param) ;
	}

	@Override
	public ResponseEntity<String> deleteClient(String clientId, String clientSecret, String uaaTarget, Map<String, Object> param) throws URISyntaxException, MalformedURLException {
		return oauthClient.deleteClient(clientId, clientSecret, uaaTarget, param) ;
	}

	public String getSpaceQuota(String guid) {

		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/space_quota_definitions/{guid}";
		LOGGER.info("urlPath: " + urlPath);
		String resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, guid);

		return resp;
	}

	@Override
	public String getSpace(String orgName, String spaceName) {
		UUID orgGuid = getOrgByName(orgName, true).getMeta().getGuid();
		UUID spaceGuid = getSpaceGuid(spaceName, orgGuid);

		Map<String, Object> urlVars = new HashMap<String, Object>();
		String urlPath = "/v2/spaces/{spaceGuid}";
		LOGGER.info("urlPath: " + urlPath);
		String resp = getRestTemplate().getForObject(getUrl(urlPath), String.class, spaceGuid);

		return resp;
	}

	@Override
	public String createServiceV2(String name, String servicePlanGuid, String orgName, String spaceName, Map<String, Object> service_create_param) {
		String urlPath = "/v2/service_instances?accepts_incomplete=true";
		Map<String, Object> param = new HashMap<>();

		param.put("name", name);
		param.put("service_plan_guid", servicePlanGuid);
		param.put("space_guid", getSpaceGuid(spaceName, getOrgByName(orgName, true).getMeta().getGuid()));
		param.put("parameters", service_create_param);
		LOGGER.info(getUrl(urlPath));
		LOGGER.info(param.toString());
		return getRestTemplate().postForObject(getUrl(urlPath), param, String.class);


	}


	private static CloudFoundryException getCloudFoundryError(Throwable t) {
		if (t == null) {
			return null;
		}
		CloudFoundryException cloudException = null;
		if (t instanceof CloudFoundryException) {
			cloudException = (CloudFoundryException) t;
		} else {
			Throwable cause = t.getCause();
			if (cause instanceof CloudFoundryException) {
				cloudException = (CloudFoundryException) cause;
			}
		}
		return cloudException;
	}


	private class CloudFoundryClientHttpRequestFactory implements ClientHttpRequestFactory {
		private final ClientHttpRequestFactory delegate;
		//private Integer defaultSocketTimeout = 0;

		public CloudFoundryClientHttpRequestFactory(ClientHttpRequestFactory delegate) {
			this.delegate = delegate;
			captureDefaultReadTimeout();
		}

		@Override
		public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
			ClientHttpRequest request = delegate.createRequest(uri, httpMethod);

			String authorizationHeader = oauthClient.getAuthorizationHeader();
			if (authorizationHeader != null) {
				request.getHeaders().add(AUTHORIZATION_HEADER_KEY, authorizationHeader);
			}

			if (cloudCredentials != null && cloudCredentials.getProxyUser() != null) {
				request.getHeaders().add(PROXY_USER_HEADER_KEY, cloudCredentials.getProxyUser());
			}

			return request;
		}

		private void captureDefaultReadTimeout() {
			// As of HttpClient 4.3.x, obtaining the default parameters is deprecated and removed,
			// so we fallback to java.net.Socket.

//			if (defaultSocketTimeout == null) {
//				try {
//					defaultSocketTimeout = new Socket().getSoTimeout();
//				} catch (SocketException e) {
//					defaultSocketTimeout = 0;
//				}
//			}
		}

//		public void increaseReadTimeoutForStreamedTailedLogs(int timeout) {
//			// May temporary increase read timeout on other unrelated concurrent
//			// threads, but per-request read timeout don't seem easily
//			// accessible
//			if (delegate instanceof HttpComponentsClientHttpRequestFactory) {
//				HttpComponentsClientHttpRequestFactory httpRequestFactory =
//						(HttpComponentsClientHttpRequestFactory) delegate;
//
//				if (timeout > 0) {
//					httpRequestFactory.setReadTimeout(timeout);
//				} else {
//					httpRequestFactory
//							.setReadTimeout(defaultSocketTimeout);
//				}
//			}
//		}





	}
}
