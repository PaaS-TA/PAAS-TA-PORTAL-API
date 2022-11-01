package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.applications.ApplicationStatisticsResponse;
import org.cloudfoundry.client.v2.spaces.*;
import org.cloudfoundry.client.v2.users.UserResource;
import org.cloudfoundry.client.v3.Link;
import org.cloudfoundry.client.v3.Metadata;
import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v3.applications.*;
import org.cloudfoundry.client.v3.processes.*;
import org.cloudfoundry.client.v3.servicebindings.ListServiceBindingsRequest;
import org.cloudfoundry.client.v3.servicebindings.ListServiceBindingsResponse;
import org.cloudfoundry.client.v3.serviceinstances.*;
import org.cloudfoundry.client.v3.serviceplans.GetServicePlanRequest;
import org.cloudfoundry.client.v3.serviceplans.GetServicePlanResponse;
import org.cloudfoundry.client.v3.spaces.AssignSpaceIsolationSegmentRequest;
import org.cloudfoundry.client.v3.spaces.AssignSpaceIsolationSegmentResponse;
import org.cloudfoundry.client.v3.spaces.SpaceRelationships;
import org.cloudfoundry.client.v3.serviceofferings.*;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpaceServiceV3 extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceServiceV3.class);


    @Autowired
    @Lazy // To resolve circular reference
    private OrgServiceV3 orgServiceV3;

    @Autowired
    private AppServiceV3 appServiceV3;


    /**
     * 공간(스페이스) 목록 조회한다.
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param reactorCloudFoundryClient the ReactorCloudFoundryClient
     * @return ListSpacesResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public ListSpacesResponse getSpaces(String orgId, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        ListSpacesResponse response = reactorCloudFoundryClient.spaces().list(ListSpacesRequest.builder().organizationId(orgId).build()).block();

        return response;
    }

    public ListSpacesResponse getSpacesWithOrgName(String orgName, ReactorCloudFoundryClient reactorCloudFoundryClient, String token) {
        final String orgId = orgServiceV3.getOrgId(orgName, token);

        return getSpaces(orgId, reactorCloudFoundryClient);
    }

    /**
     * 공간(스페이스) 목록 조회한다.
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param org   the org
     * @param token the token
     * @return ListSpacesResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public ListSpacesResponse getSpaces(Org org, String token) {
        String orgId = null;
        if (org.getGuid() != null) {
            orgId = org.getGuid().toString();
        } else if (org.getName() != null) {
            orgId = orgServiceV3.getOrgId(org.getName(), token);
        } else {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "To get spaces in org, you must be require org name or org id.");
        }

        Objects.requireNonNull(orgId, "Org id must not be null.");
        ListSpacesResponse response = cloudFoundryClient(tokenProvider(token)).spaces().list(ListSpacesRequest.builder().organizationId(orgId).build()).block();

        return response;
    }

    private static final List<String> SPACE_ROLES_FOR_ORGMANAGER = Arrays.asList("SpaceAuditor", "SpaceDeveloper", "SpaceManager");
    private static final List<String> SPACE_ROLES_FOR_ORGAUDITOR = Arrays.asList("SpaceAuditor", "SpaceDeveloper");
    private static final List<String> SPACE_ROLES_FOR_BILLINGMANAGER = Arrays.asList("SpaceAuditor");

    /**
     * 공간을 생성한다. (Space : Create)
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public Map createSpace(Space space, String token) {
        Map resultMap = new HashMap();

        try {
            Objects.requireNonNull(space.getSpaceName(), "Space name must not be null. Required request body is space name(spaceName) and org GUID (orgGuid).");
            Objects.requireNonNull(space.getOrgGuid(), "Space name must not be null. Required request body is space name(spaceName) and org GUID (orgGuid).");

            final CreateSpaceResponse response = cloudFoundryClient(tokenProvider(token)).spaces().create(CreateSpaceRequest.builder().name(space.getSpaceName()).organizationId(space.getOrgGuid()).build()).block();


            associateSpaceManager(response.getMetadata().getId(), space.getUserId());
            associateSpaceDeveloper(response.getMetadata().getId(), space.getUserId());
            associateSpaceAuditor(response.getMetadata().getId(), space.getUserId());

            // Results for association roles will be disposed
            //associateSpaceUserRolesByOrgIdAndRole(response.getMetadata().getId(), space.getOrgGuid() );

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    /**
     * 공간의 정보를 가져온다. (Space : Read)
     *
     * @param spaceId
     * @param token
     * @return GetSpaceResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public GetSpaceResponse getSpace(String spaceId, String token) {
        Objects.requireNonNull(spaceId, "Space Id");

        final TokenProvider internalTokenProvider;
        if (null != token && !"".equals(token)) internalTokenProvider = tokenProvider(token);
        else internalTokenProvider = tokenProvider();

        return cloudFoundryClient(internalTokenProvider).spaces().get(GetSpaceRequest.builder().spaceId(spaceId).build()).block();
    }

    public GetSpaceResponse getSpace(String spaceId) {
        return getSpace(spaceId, null);
    }

    public SpaceResource getSpaceUsingName(String orgName, String spaceName, String token) {
        final TokenProvider internalTokenProvider;
        if (null != token && !"".equals(token)) {
            internalTokenProvider = tokenProvider(token);
        } else {
            internalTokenProvider = tokenProvider();
        }
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(internalTokenProvider);
        final ListSpacesResponse response = this.getSpacesWithOrgName(orgName, reactorCloudFoundryClient, token);
        if (response.getTotalResults() <= 0) return null;
        else if (response.getResources() != null && response.getResources().size() <= 0) return null;

        List<SpaceResource> spaces = response.getResources().stream().filter(resource -> spaceName.equals(resource.getEntity().getName())).collect(Collectors.toList());
        if (spaces.size() <= 0) return null;

        return spaces.get(0);
    }


    public boolean isExistSpace(final String spaceId) {
        try {
            return spaceId.equals(getSpace(spaceId).getMetadata().getId());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 공간명을 변경한다. (Space : Update)
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public Map renameSpace(Space space, String token) {
        Map resultMap = new HashMap();

        try {
            String spaceGuid = space.getGuid().toString();
            String newSpaceName = space.getNewSpaceName();
            Objects.requireNonNull(spaceGuid, "Space GUID(guid) must be not null. Request body is made space GUID(guid) and new space name(newSpaceName).");
            Objects.requireNonNull(newSpaceName, "New space name must be not null. Request body is made space GUID(guid) and new space name(newSpaceName).");
            if (!stringNullCheck(spaceGuid, newSpaceName)) {
                throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content(guid or newSpaceName) is missing.");
            }

            cloudFoundryClient(tokenProvider(token)).spaces().update(UpdateSpaceRequest.builder().spaceId(spaceGuid).name(newSpaceName).build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    /**
     * 공간을 삭제한다. (Space : Delete)
     *
     * @param guid  the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public Map deleteSpace(String guid, boolean recursive, String token) {
        Map resultMap = new HashMap();

        try {
            if (!stringNullCheck(guid)) {
                throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing");
            }

            cloudFoundryClient(tokenProvider(token)).spaces().delete(DeleteSpaceRequest.builder().spaceId(guid).recursive(recursive).async(false).build()).block();

            resultMap.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", e);
        }

        return resultMap;
    }

    /**
     * 공간 요약 정보를 조회한다.
     *
     * @param spaceId the spaceId
     * @param token   the token
     * @return space summary
     * @throws Exception the exception
     */
    public GetSpaceSummaryResponse getSpaceSummary(String spaceId, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        GetSpaceSummaryResponse respSapceSummary = cloudFoundryClient.spaces().getSummary(GetSpaceSummaryRequest.builder().spaceId(spaceId).build()).block();
        return respSapceSummary;
    }

    public Map getSpaceSummary2(String spaceid, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> appArray = new ArrayList<>();

        if(apiType.equalsIgnoreCase("ap")){
            // apiType is ap
            GetSpaceSummaryResponse respSapceSummary = cloudFoundryClient.spaces().getSummary(GetSpaceSummaryRequest.builder().spaceId(spaceid).build()).block();

            List<SpaceApplicationSummary> appsArray = new ArrayList<>();

            //TODO
            resultMap.put("apps", respSapceSummary.getApplications());
            resultMap.put("guid", respSapceSummary.getId());
            resultMap.put("name", respSapceSummary.getName());
            resultMap.put("services", respSapceSummary.getServices());

            for (SpaceApplicationSummary sapceApplicationSummary : respSapceSummary.getApplications()) {
                Map<String, Object> resultMap2 = new HashMap<>();

                try {
                    if (sapceApplicationSummary.getState().equals("STARTED")) {
                        ApplicationStatisticsResponse applicationStatisticsResponse = this.appServiceV3.getAppStats(sapceApplicationSummary.getId(), cloudFoundryClient);

                        Double cpu = 0.0;
                        Double mem = 0.0;
                        Double disk = 0.0;
                        int cnt = 0;
                        for (int i = 0; i < applicationStatisticsResponse.getInstances().size(); i++) {
                            if (applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getState().equals("RUNNING")) {
                                Double instanceCpu = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getUsage().getCpu();
                                Long instanceMem = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getUsage().getMemory();
                                Long instanceMemQuota = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getMemoryQuota();
                                Long instanceDisk = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getUsage().getDisk();
                                Long instanceDiskQuota = applicationStatisticsResponse.getInstances().get(Integer.toString(i)).getStatistics().getDiskQuota();

                                if (instanceCpu != null) cpu = cpu + instanceCpu * 100;
                                if (instanceMem != null) mem = mem + (double) instanceMem / (double) instanceMemQuota * 100;
                                if (instanceDisk != null)
                                    disk = disk + (double) instanceDisk / (double) instanceDiskQuota * 100;

                                cnt++;
                            }
                        }

                        cpu = cpu / cnt;
                        mem = mem / cnt;
                        disk = disk / cnt;

                        resultMap2.put("guid", sapceApplicationSummary.getId());
                        resultMap2.put("cpuPer", Double.parseDouble(String.format("%.2f%n", cpu)));
                        resultMap2.put("memPer", Math.round(mem));
                        resultMap2.put("diskPer", Math.round(disk));
                    } else {
                        resultMap2.put("guid", sapceApplicationSummary.getId());
                        resultMap2.put("cpuPer", 0);
                        resultMap2.put("memPer", 0);
                        resultMap2.put("diskPer", 0);
                    }

                    appsArray.add(sapceApplicationSummary);
                    appArray.add(resultMap2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            resultMap.put("apps", appsArray);
            resultMap.put("appsPer", appArray);

            resultMap.put("apiType", apiType);
            return resultMap;
        }

        else if (apiType.equalsIgnoreCase("sidecar")) {
            // apiType is sidecar
            List<ApplicationResource> appsArray = new ArrayList<>();
            List<List<ProcessResource>> applicationListApplicationProcessArray = new ArrayList<>();

            //application in space
            ListApplicationsResponse listApplicationsResponse =  cloudFoundryClient.applicationsV3().list(ListApplicationsRequest.builder().spaceId(spaceid).build()).block();

            //apps 만들기
            int size = 0;

            resultMap.put("apps", listApplicationsResponse.getResources());



            GetApplicationProcessStatisticsResponse applicationStatisticsResponse;
            ListApplicationProcessesResponse getListApplicationProcess;
            size = listApplicationsResponse.getResources().size();
            for (int i=0; i < size; i++) {
                ApplicationResource spaceApplicationSummary = listApplicationsResponse.getResources().get(i);

                Map<String, Object> resultMap2 = new HashMap<>();
                try {
                    if (spaceApplicationSummary.getState().equals(ApplicationState.STARTED)) {
                        applicationStatisticsResponse = this.appServiceV3.getAppStatsV3(spaceApplicationSummary.getId(), cloudFoundryClient);
                        getListApplicationProcess = this.appServiceV3.getListApplicationProcess(spaceApplicationSummary.getId(), cloudFoundryClient);
                        Double cpu = 0.0;
                        Double mem = 0.0;
                        Double disk = 0.0;
                        int cnt = 0;
                        for (int j = 0; j < applicationStatisticsResponse.getResources().size(); j++) {
                            if (applicationStatisticsResponse.getResources().get(j).getState().equals(ProcessState.RUNNING)) {
                                Double instanceCpu = applicationStatisticsResponse.getResources().get(j).getUsage().getCpu();
                                Integer instanceMem = applicationStatisticsResponse.getResources().get(j).getUsage().getMemory();
                                Long instanceMemQuota = getListApplicationProcess.getResources().get(0).getMemoryInMb().longValue() * 1024 * 1024;
                                Integer instanceDisk = applicationStatisticsResponse.getResources().get(j).getUsage().getDisk();
                                Long instanceDiskQuota = getListApplicationProcess.getResources().get(0).getDiskInMb().longValue() * 1024 * 1024;

                                if (instanceCpu != null) cpu = cpu + instanceCpu * 100;
                                if (instanceMem != null) mem = mem + (double) instanceMem / (double) instanceMemQuota * 100;
                                if (instanceDisk != null)
                                    disk = disk + (double) instanceDisk / (double) instanceDiskQuota * 100;

                                cnt++;
                            }
                        }

                        cpu = cpu / cnt;
                        mem = mem / cnt;
                        disk = disk / cnt;

                        resultMap2.put("guid", spaceApplicationSummary.getId());
                        resultMap2.put("cpuPer", Double.parseDouble(String.format("%.2f%n", cpu)));
                        resultMap2.put("memPer", Math.round(mem));
                        resultMap2.put("diskPer", Math.round(disk));
                        applicationListApplicationProcessArray.add(getListApplicationProcess.getResources());

                    } else {
                        resultMap2.put("guid", spaceApplicationSummary.getId());
                        resultMap2.put("cpuPer", 0);
                        resultMap2.put("memPer", 0);
                        resultMap2.put("diskPer", 0);

                        List<ProcessResource> tmplist = new ArrayList<>();
                        tmplist.add(ProcessResource.builder().command("").diskInMb(0).healthCheck(HealthCheck.builder().type(HealthCheckType.NONE).build()).instances(0).memoryInMb(0).metadata(Metadata.builder().build()).relationships(ProcessRelationships.builder().build()).type("").createdAt("").id("").link("", Link.builder().href("").build()).build());
                        applicationListApplicationProcessArray.add(tmplist);

                    }
                    appsArray.add(spaceApplicationSummary);
                    appArray.add(resultMap2);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (spaceApplicationSummary != null){
                        appsArray.add(spaceApplicationSummary);

                        resultMap2.put("guid", spaceApplicationSummary.getId());
                        resultMap2.put("cpuPer", 0);
                        resultMap2.put("memPer", 0);
                        resultMap2.put("diskPer", 0);

                        List<ProcessResource> tmplist = new ArrayList<>();
                        tmplist.add(ProcessResource.builder().command("").diskInMb(0).healthCheck(HealthCheck.builder().type(HealthCheckType.NONE).build()).instances(0).memoryInMb(0).metadata(Metadata.builder().build()).relationships(ProcessRelationships.builder().build()).type("").createdAt("").id("").link("", Link.builder().href("").build()).build());
                        applicationListApplicationProcessArray.add(tmplist);
                        appArray.add(resultMap2);
                    }
                }
            }
            size = listApplicationsResponse.getResources().size();
            List<App> apps = new ArrayList<>();
            for(int i=0; i < size; i++){
                App app = new App();
                // app guid
                app.setGuid(UUID.fromString(listApplicationsResponse.getResources().get(i).getId()));
                // app lifecycle
                app.setBuildPacks(appsArray.get(i).getLifecycle());
                appsArray.get(i).getLifecycle().getData();
                // app disk quota
                app.setDiskQuota(applicationListApplicationProcessArray.get(i).get(0).getDiskInMb());
                // app instances
                app.setInstances(applicationListApplicationProcessArray.get(i).get(0).getInstances());
                // app memory
                app.setMemory(applicationListApplicationProcessArray.get(i).get(0).getMemoryInMb());
                // app name
                app.setName(listApplicationsResponse.getResources().get(i).getName());
                // app state
                app.setState(listApplicationsResponse.getResources().get(i).getState().toString());

                apps.add(app);
            }

            resultMap.put("apps", apps);
            resultMap.put("appsPer", appArray);
            resultMap.put("appProcessList", applicationListApplicationProcessArray);



            List<ServiceV3> services;

            services = getSpaceServices(spaceid, apps);
            resultMap.put("services", services);

            resultMap.put("apiType", apiType);
            return resultMap;


        }
        else {
            try {
                throw new Exception();
            } catch(Exception e) {
                LOGGER.info("cloudfoundry.cc.api.type value check!!");
            }
            return null;
        }
    }



    /**
     * 공간에 생성되어 있는 서비스를 조회한다. (List<App>을 추가할 경우 binding된 APP 정보까지 조회)
     *
     * @param spaceId, apps
     * @return
     * @throws Exception
     * @author 남동윤
     * @version 3.0
     * @since 2022.11.01
     */

    public List<ServiceV3> getSpaceServices(String spaceId) throws Exception {
        return getSpaceServices(spaceId, null);
    }
    public List<ServiceV3> getSpaceServices(String spaceId, List<App> apps) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient = cloudFoundryClient();
        ListServiceInstancesResponse listServiceResponse = cloudFoundryClient.serviceInstancesV3().list(ListServiceInstancesRequest.builder().spaceId(spaceId).build()).block();


        String service_guid = null;
        String service_plan_guid = null;
        String service_offering_id = null;
        String service_offering_name = null;
        GetServiceInstanceResponse getServiceInstanceResponse = null;
        GetServicePlanResponse getServicePlanResponse = null;
        ServiceV3 service = null;

        List<ServiceInstanceResource> serviceInstanceResources = new ArrayList<>();

        List<ServiceV3> services = new ArrayList<>();

        for (int i=0; i<listServiceResponse.getResources().size(); i++) {

            service = new ServiceV3();
            // service guid
            service_guid = listServiceResponse.getResources().get(i).getId();
            // service_plan_guid
            getServiceInstanceResponse = cloudFoundryClient.serviceInstancesV3().get(GetServiceInstanceRequest.builder().serviceInstanceId(service_guid).build()).block();
            service_plan_guid = getServiceInstanceResponse.getRelationships().getServicePlan().getData().getId();


            // service_offering_id
            getServicePlanResponse = cloudFoundryClient.servicePlansV3().get(GetServicePlanRequest.builder().servicePlanId(service_plan_guid).build()).block();
            service_offering_id = getServicePlanResponse.getRelationships().getServiceOffering().getData().getId();
            // service_offering_name
            service_offering_name = cloudFoundryClient.serviceOfferingsV3().get(GetServiceOfferingRequest.builder().serviceOfferingId(service_offering_id).build()).block().getName();

            service.setName(getServiceInstanceResponse.getName());
            service.setGuid(UUID.fromString(service_guid));

            serviceInstanceResources.add(ServiceInstanceResource.builder().from(listServiceResponse.getResources().get(i)).serviceOfferingName(service_offering_name).build());

            List<String> bindingAppNames = new ArrayList<>();

            ListServiceBindingsResponse listServiceBindingsResponse = cloudFoundryClient.serviceBindingsV3().list(ListServiceBindingsRequest.builder().serviceInstanceId("").build()).block();

            if (apps != null) {
                for (int j = 0; j < listServiceBindingsResponse.getResources().size(); j++) {
                    if (listServiceBindingsResponse.getResources().get(j).getRelationships().getServiceInstance().getData().getId().equals(service_guid) == true) {
                        for (int k = 0; k < apps.size(); k++) {
                            if (apps.get(k).getGuid().toString().equals(listServiceBindingsResponse.getResources().get(j).getRelationships().getApplication().getData().getId()) == true) {
                                bindingAppNames.add(apps.get(k).getName());
                                break;
                            }
                        }
                    }
                }
            }


            // bound_app_count
            service.setBinding_app_names(bindingAppNames);
            // dashboard_url
            service.setDashboard_url(listServiceResponse.getResources().get(i).getDashboardUrl());
            // guid
            service.setGuid(UUID.fromString(listServiceResponse.getResources().get(i).getId()));
            // last_operation
            service.setLast_operation(listServiceResponse.getResources().get(i).getLastOperation());
            // name
            service.setName(listServiceResponse.getResources().get(i).getName());
            service.setService_plan(new ServiceV3.ServicePlan(getServicePlanResponse.getName(), new ServiceV3.ServicePlan.ServiceInfo(service_offering_name)));

            services.add(service);
        }

        return services;
    }

    // TODO spaces role
    private enum SpaceRole {
        SpaceManager, SpaceDeveloper, SpaceAuditor, SPACEMANAGER, SPACEDEVELOPER, SPACEAUDITOR,
    }

    private List<UserSpaceRoleResource> listAllSpaceUsers(String spaceId, String token) {
        final ListSpaceUserRolesResponse response = cloudFoundryClient(tokenProvider(token)).spaces().listUserRoles(ListSpaceUserRolesRequest.builder().spaceId(spaceId).build()).block();

        return response.getResources();
    }

    private List<UserResource> listSpaceManagerUsers(String spaceId, String token) {
        final ListSpaceManagersResponse response = cloudFoundryClient(tokenProvider(token)).spaces().listManagers(ListSpaceManagersRequest.builder().spaceId(spaceId).build()).block();

        return response.getResources();
    }

    private List<UserResource> listSpaceDeveloperUsers(String spaceId, String token) {
        final ListSpaceDevelopersResponse response = cloudFoundryClient(tokenProvider(token)).spaces().listDevelopers(ListSpaceDevelopersRequest.builder().spaceId(spaceId).build()).block();

        return response.getResources();
    }

    private List<UserResource> listSpaceAuditorUsers(String spaceId, String token) {
        final ListSpaceAuditorsResponse response = cloudFoundryClient(tokenProvider(token)).spaces().listAuditors(ListSpaceAuditorsRequest.builder().spaceId(spaceId).build()).block();

        return response.getResources();
    }

    public ListSpaceUserRolesResponse getSpaceUserRoles(String spaceId, String token) {
        return cloudFoundryClient(tokenProvider(token)).spaces().listUserRoles(ListSpaceUserRolesRequest.builder().spaceId(spaceId).build()).block();
    }

    private AssociateSpaceManagerResponse associateSpaceManager(String spaceId, String userId) {
        return cloudFoundryClient().spaces().associateManager(AssociateSpaceManagerRequest.builder().spaceId(spaceId).managerId(userId).build()).block();
    }

    private AssociateSpaceDeveloperResponse associateSpaceDeveloper(String spaceId, String userId) {
        return cloudFoundryClient().spaces().associateDeveloper(AssociateSpaceDeveloperRequest.builder().spaceId(spaceId).developerId(userId).build()).block();
    }

    private AssociateSpaceAuditorResponse associateSpaceAuditor(String spaceId, String userId) {
        return cloudFoundryClient().spaces().associateAuditor(AssociateSpaceAuditorRequest.builder().spaceId(spaceId).auditorId(userId).build()).block();
    }

    public AbstractSpaceResource associateSpaceUserRole(String spaceId, String userId, String role) {
        Objects.requireNonNull(spaceId, "Space Id");
        Objects.requireNonNull(userId, "User Id");
        Objects.requireNonNull(role, "role");

        final SpaceServiceV3.SpaceRole roleEnum;
        try {
            roleEnum = SpaceServiceV3.SpaceRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            LOGGER.error("This role is invalid : {}", role);
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Request role is invalid : " + role);
        }

        switch (roleEnum) {
            case SpaceManager:
            case SPACEMANAGER:
                return associateSpaceManager(spaceId, userId);
            case SpaceDeveloper:
            case SPACEDEVELOPER:
                return associateSpaceDeveloper(spaceId, userId);
            case SpaceAuditor:
            case SPACEAUDITOR:
                return associateSpaceAuditor(spaceId, userId);
            default:
                throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Request role is invalid : " + role);
        }
    }

    public List<AbstractSpaceResource> associateAllSpaceUserRolesByOrgId(String orgId, String userId, Iterable<String> roles, ReactorCloudFoundryClient reactorCloudFoundryClient) {
        final List<AbstractSpaceResource> responses = new LinkedList<>();
        final List<String> spaceIds = this.getSpaces(orgId, reactorCloudFoundryClient).getResources().stream().map(space -> space.getMetadata().getId()).filter(id -> null != id).collect(Collectors.toList());
        for (String role : roles) {
            for (String spaceId : spaceIds) {
                final AbstractSpaceResource response = associateSpaceUserRole(spaceId, userId, role);
                responses.add(response);
            }
        }

        return responses;
    }


    private void removeSpaceManager(String spaceId, String userId) {
        //LOGGER.debug("---->> Remove SpaceManager role of member({}) in space({}).", userId, spaceId);
        cloudFoundryClient().spaces().removeManager(RemoveSpaceManagerRequest.builder().spaceId(spaceId).managerId(userId).build()).block();
    }

    private void removeSpaceDeveloper(String spaceId, String userId) {
        //LOGGER.debug("---->> Remove SpaceDeveloper role of member({}) in space({}).", userId, spaceId);
        cloudFoundryClient().spaces().removeDeveloper(RemoveSpaceDeveloperRequest.builder().spaceId(spaceId).developerId(userId).build()).block();
    }

    private void removeSpaceAuditor(String spaceId, String userId) {
        //LOGGER.debug("---->> Remove SpaceAuditor role of member({}) in space({}).", userId, spaceId);
        cloudFoundryClient().spaces().removeAuditor(RemoveSpaceAuditorRequest.builder().spaceId(spaceId).auditorId(userId).build()).block();
    }

    private void removeAllRoles(String spaceId, String userId) {
        //LOGGER.debug("--> Remove all member({})'s roles in space({}).", userId, spaceId);
        removeSpaceManager(spaceId, userId);
        removeSpaceDeveloper(spaceId, userId);
        removeSpaceAuditor(spaceId, userId);
        //LOGGER.debug("--> Done to remove all member({})'s roles in space({}).", userId, spaceId);
    }

    /**
     * 조직에 속한 유저에 대한 역할(Role)을 제거한다.
     *
     * @param spaceId
     * @param userId
     * @param role
     */
    public void removeSpaceUserRole(String spaceId, String userId, String role) {
        Objects.requireNonNull(spaceId, "Space Id");
        Objects.requireNonNull(userId, "User Id");
        Objects.requireNonNull(role, "role");

        final SpaceServiceV3.SpaceRole roleEnum;
        try {
            roleEnum = SpaceServiceV3.SpaceRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            //LOGGER.error("This role is invalid : {}", role);
            return;
        }
        switch (roleEnum) {
            case SpaceManager:
            case SPACEMANAGER:
                removeSpaceManager(spaceId, userId);
                break;
            case SpaceDeveloper:
            case SPACEDEVELOPER:
                removeSpaceDeveloper(spaceId, userId);
                break;
            case SpaceAuditor:
            case SPACEAUDITOR:
                removeSpaceAuditor(spaceId, userId);
                break;
            default:
                throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Request role is invalid : " + role);
        }
    }

    public void removeAllSpaceUserRolesByOrgId(String orgId, String userId, Iterable<String> roles) {
        final List<String> spaceIds = this.getSpaces(orgId, cloudFoundryClient()).getResources().stream().map(space -> space.getMetadata().getId()).filter(id -> null != id).collect(Collectors.toList());
        for (String role : roles) {
            for (String spaceId : spaceIds)
                removeSpaceUserRole(spaceId, userId, role);
        }
    }

    public boolean associateSpaceUserRoles(String spaceid, List<UserRole> Roles, String token) {
        Roles.forEach(userRole -> {
            boolean manager = true;
            boolean audiotr = true;
            boolean developer = true;
            for (String spacerole : userRole.getRoles()) {
                switch (spacerole) {
                    case "space_manager": {
                        manager = false;
                        cloudFoundryClient(tokenProvider(token)).spaces().associateManager(AssociateSpaceManagerRequest.builder().spaceId(spaceid).managerId(userRole.getUserId()).build()).block();
                        break;
                    }
                    case "space_auditor": {
                        audiotr = false;
                        cloudFoundryClient(tokenProvider(token)).spaces().associateDeveloper(AssociateSpaceDeveloperRequest.builder().spaceId(spaceid).developerId(userRole.getUserId()).build()).block();
                        break;
                    }
                    case "space_developer": {
                        developer = false;
                        cloudFoundryClient(tokenProvider(token)).spaces().associateAuditor(AssociateSpaceAuditorRequest.builder().spaceId(spaceid).auditorId(userRole.getUserId()).build()).block();
                        break;
                    }
                }
            }
            ;
            if (manager) {
                cloudFoundryClient(tokenProvider(token)).spaces().removeManager(RemoveSpaceManagerRequest.builder().spaceId(spaceid).managerId(userRole.getUserId()).build()).block();
            }
            if (audiotr) {
                cloudFoundryClient(tokenProvider(token)).spaces().removeAuditor(RemoveSpaceAuditorRequest.builder().spaceId(spaceid).auditorId(userRole.getUserId()).build()).block();
            }
            if (developer) {
                cloudFoundryClient(tokenProvider(token)).spaces().removeDeveloper(RemoveSpaceDeveloperRequest.builder().spaceId(spaceid).developerId(userRole.getUserId()).build()).block();
            }
        });
        return true;
    }


    /**
     * Space 정보를 가져온다.
     *
     * @param spaceId the space guid
     * @param token   user token
     * @return GetSpaceResponse
     * 권한 : 사용자권한
     */
    public org.cloudfoundry.client.v3.spaces.GetSpaceResponse getSpaceV3(String spaceId, String token) {
        return cloudFoundryClient(tokenProvider(token)).spacesV3().get(org.cloudfoundry.client.v3.spaces.GetSpaceRequest.builder().spaceId(spaceId).build()).block();
    }

    /**
     * Space 리스트 정보를 가져온다.
     *
     * @param organizationId the organization guid
     * @param token          user token
     * @return ListSpacesResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public org.cloudfoundry.client.v3.spaces.ListSpacesResponse listSpace(String organizationId, String token) {
        return this.allSpaceList(cloudFoundryClient(tokenProvider(token)), organizationId);
    }

    /**
     * Space 리스트 정보를 가져온다.
     *
     * @param organizationId the organization guid
     * @return ListSpacesResponse
     * 권한 : 관리자 권한
     * @throws Exception the exception
     */
    public org.cloudfoundry.client.v3.spaces.ListSpacesResponse listSpaceAdmin(String organizationId) {
        return this.allSpaceList(cloudFoundryClient(), organizationId);
    }

    private org.cloudfoundry.client.v3.spaces.ListSpacesResponse allSpaceList(ReactorCloudFoundryClient reactorCloudFoundryClient, String orgGuid) {
        org.cloudfoundry.client.v3.spaces.ListSpacesResponse listSpacesResponse = reactorCloudFoundryClient.spacesV3().list(org.cloudfoundry.client.v3.spaces.ListSpacesRequest.builder().organizationId(orgGuid).build()).block();
        int i;
        for (i = 1; listSpacesResponse.getPagination().getTotalPages().intValue() > i; i++) {
            listSpacesResponse.getResources().addAll(reactorCloudFoundryClient.spacesV3().list(org.cloudfoundry.client.v3.spaces.ListSpacesRequest.builder().page(i + 1).build()).block().getResources());
        }
        return listSpacesResponse;
    }

    /**
     * Space명 중복검사를 실행한다.
     *
     * @param organizationId the organization guid
     * @param spaceName      this space name
     * @return boolean
     * 권한 : 사용자
     */
    public boolean isExistSpaceName(String organizationId, String spaceName) {
        org.cloudfoundry.client.v3.spaces.ListSpacesResponse listSpacesResponse = this.listSpaceAdmin(organizationId);
        long number = listSpacesResponse.getResources().stream().filter(space -> space.getName().equals(spaceName)).count();
        if (number > 0) {
            return false;
        }
        return true;
    }

    /**
     * Space 을 생성한다.
     *
     * @param organizationId the organization guid
     * @param name           the space name
     * @param token          user token
     * @return CreateOrganizationResponse
     * 권한 : 사용자권한
     * @throws Exception the exception
     */
    public org.cloudfoundry.client.v3.spaces.CreateSpaceResponse createSpace(String name, String organizationId, String usrid, String token) {

        org.cloudfoundry.client.v3.spaces.CreateSpaceResponse response =  cloudFoundryClient(tokenProvider(token)).spacesV3().create(org.cloudfoundry.client.v3.spaces.CreateSpaceRequest.builder().name(name).relationships(SpaceRelationships.builder().organization(ToOneRelationship.builder().data(Relationship.builder().id(organizationId).build()).build()).build()).build()).block();

        associateSpaceManager(response.getId(), usrid);
        associateSpaceDeveloper(response.getId(), usrid);
        associateSpaceAuditor(response.getId(), usrid);
         return response;
    }

    /**
     * Space Isolation 에 Isolation Segments default 를 설정한다.
     *
     * @param spaceId            the space id
     * @param isolationSegmentId the isolation segement id
     * @return AssignSpaceIsolationSegmentResponse
     * @throws Exception the exception
     */
    public AssignSpaceIsolationSegmentResponse setSpaceDefaultIsolationSegments(String spaceId, String isolationSegmentId) throws Exception {
        return cloudFoundryClient().spacesV3().assignIsolationSegment(AssignSpaceIsolationSegmentRequest.builder().spaceId(spaceId).data(Relationship.builder().id(isolationSegmentId).build()).build()).block();
    }

    /**
     * Space Isolation 에 Isolation Segments default 를 해제한다.
     *
     * @param spaceId the space id
     * @return AssignSpaceIsolationSegmentResponse
     * @throws Exception the exception
     */
    public AssignSpaceIsolationSegmentResponse resetSpaceDefaultIsolationSegments(String spaceId) throws Exception {
        return cloudFoundryClient().spacesV3().assignIsolationSegment(AssignSpaceIsolationSegmentRequest.builder().spaceId(spaceId).build()).block();
    }
}
