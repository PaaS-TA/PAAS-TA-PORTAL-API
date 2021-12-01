package org.openpaas.paasta.portal.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cloudfoundry.Nullable;
import org.cloudfoundry.client.v2.Metadata;
import org.cloudfoundry.client.v2.applications.ApplicationEntity;
import org.cloudfoundry.client.v2.applications.DockerCredentials;
import org.cloudfoundry.client.v2.applications.Resource;
import org.cloudfoundry.client.v2.applications.SummaryApplicationResponse;
import org.cloudfoundry.client.v2.domains.Domain;
import org.cloudfoundry.client.v2.routes.Route;
import org.cloudfoundry.client.v2.serviceinstances.ServiceInstance;
import org.cloudfoundry.client.v3.Checksum;
import org.cloudfoundry.client.v3.LifecycleData;
import org.cloudfoundry.client.v3.applications.*;
import org.cloudfoundry.client.v3.droplets.Buildpack;
import org.cloudfoundry.client.v3.processes.HealthCheck;
import org.cloudfoundry.client.v3.processes.ProcessStatisticsResource;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppV3 {

    @JsonProperty("name")
    private String name;

    @JsonProperty("state")
    private String state;

    @JsonProperty("guid")
    private String guid;

    @JsonProperty("spaceGuid")
    private String spaceGuid;

    @JsonProperty("spaceName")
    private String spaceName;

    @JsonProperty("type")
    private String type;

    @JsonProperty("executionMetadata")
    private String executionMetadata;

    @JsonProperty("detected_start_command")
    private String detected_start_command;

    @JsonProperty("stack")
    private String stack;

    @JsonProperty("dropletState")
    private String dropletState;

    @JsonProperty("image")
    private String image;

    @JsonProperty("buildpack")
    private String buildpack;

    @JsonProperty("detected_buildpack")
    private String detected_buildpack;

    @JsonProperty("instances")
    private Integer instances;

    @JsonProperty("disk_quota")
    private Integer disk_quota;

    @JsonProperty("memory")
    private Integer memory;

    @JsonProperty("applicationEnvironmentVariables")
    private Map<String, Object> applicationEnvironmentVariables;

    @JsonProperty("environmentVariables")
    private Map<String, Object> environmentVariables;

    @JsonProperty("runningEnvironmentVariables")
    private Map<String, Object> runningEnvironmentVariables;

    @JsonProperty("stagingEnvironmentVariables")
    private Map<String, Object> stagingEnvironmentVariables;

    @JsonProperty("systemEnvironmentVariables")
    private Map<String, Object> systemEnvironmentVariables;

    @JsonProperty("processTypes")
    private Map<String, String> processTypes;

    @JsonProperty("healthCheck")
    private HealthCheck healthCheck;

    @JsonProperty("processStatisticsResources")
    private List<ProcessStatisticsResource> processStatisticsResources;

    @JsonProperty("buildpacks")
    private List<Buildpack> buildpacks;

    @JsonProperty("checksum")
    private Checksum checksum;

    @JsonProperty("lifecycle")
    private LifecycleData lifecycle;

    @JsonProperty("lifecycleType")
    private String lifecycleType;

    @JsonProperty("package_updated_at")
    private String package_updated_at;

    @JsonProperty("resources")
    private List<Resources> resources;

    @JsonProperty("entity")
    private Entity entity;

    @JsonProperty("metadata")
    private Metadata metadata;

    @JsonProperty("routes")
    List<Route> routes;

    @JsonProperty("available_domains")
    List<Domain> domains;

    @JsonProperty("services")
    private List<ServiceInstance> serviceInstances;

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getGuid() {
        return guid;
    }

    public String getSpaceGuid() {
        return spaceGuid;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public String getType() {
        return type;
    }

    public String getExecutionMetadata() {
        return executionMetadata;
    }

    public String getDetected_start_command() {
        return detected_start_command;
    }

    public String getStack() {
        return stack;
    }

    public String getDropletState() {
        return dropletState;
    }

    public String getImage() {
        return image;
    }

    public String getBuildpack() {
        return buildpack;
    }

    public String getDetected_buildpack() {
        return detected_buildpack;
    }

    public Integer getInstances() {
        return instances;
    }

    public Integer getDisk_quota() {
        return disk_quota;
    }

    public Integer getMemory() {
        return memory;
    }

    public Map<String, Object> getApplicationEnvironmentVariables() {
        return applicationEnvironmentVariables;
    }

    public Map<String, Object> getEnvironmentVariables() {
        return environmentVariables;
    }

    public Map<String, Object> getRunningEnvironmentVariables() {
        return runningEnvironmentVariables;
    }

    public Map<String, Object> getStagingEnvironmentVariables() {
        return stagingEnvironmentVariables;
    }

    public Map<String, Object> getSystemEnvironmentVariables() {
        return systemEnvironmentVariables;
    }

    public Map<String, String> getProcessTypes() {
        return processTypes;
    }

    public HealthCheck getHealthCheck() {
        return healthCheck;
    }

    public List<ProcessStatisticsResource> getProcessStatisticsResources() {
        return processStatisticsResources;
    }

    public List<Buildpack> getBuildpacks() {
        return buildpacks;
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public LifecycleData getLifecycle() {
        return lifecycle;
    }

    public String getLifecycleType() {
        return lifecycleType;
    }

    public String getPackage_updated_at() {
        return package_updated_at;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public List<Domain> getDomains() {
        return domains;
    }

    public List<ServiceInstance> getServiceInstances() {
        return serviceInstances;
    }

    /*public void setResource(List<Resources> resources) {
        this.resources = resources;
    }*/

    /*public void setEntity(Entity entity) {
        this.entity = entity;
    }*/

    /*public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }*/


    public String toString() {
        return "AppV3{" +
                "name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", guid='" + guid + '\'' +
                ", spaceGuid='" + spaceGuid + '\'' +
                ", spaceName='" + spaceName + '\'' +
                ", type='" + type + '\'' +
                ", executionMetadata='" + executionMetadata + '\'' +
                ", detected_start_command='" + detected_start_command + '\'' +
                ", stack='" + stack + '\'' +
                ", dropletState='" + dropletState + '\'' +
                ", image='" + image + '\'' +
                ", buildpack='" + buildpack + '\'' +
                ", detected_buildpack='" + detected_buildpack + '\'' +
                ", instances=" + instances +
                ", disk_quota=" + disk_quota +
                ", memory=" + memory +
                ", applicationEnvironmentVariables=" + applicationEnvironmentVariables +
                ", environmentVariables=" + environmentVariables +
                ", runningEnvironmentVariables=" + runningEnvironmentVariables +
                ", stagingEnvironmentVariables=" + stagingEnvironmentVariables +
                ", systemEnvironmentVariables=" + systemEnvironmentVariables +
                ", processTypes=" + processTypes +
                ", healthCheck=" + healthCheck +
                ", processStatisticsResources=" + processStatisticsResources +
                ", buildpacks=" + buildpacks +
                ", checksum=" + checksum +
                ", lifecycle=" + lifecycle +
                ", lifecycleType='" + lifecycleType + '\'' +
                ", package_updated_at='" + package_updated_at + '\'' +
                ", routes=" + routes +
                ", domains=" + domains +
                ", serviceInstances=" + serviceInstances +
                '}';
    }

    private AppV3(AppV3.Builder builder) {
        if(builder.applicationResponse != null){
            this.ApplicationResponseBuild(builder);
        } if(builder.applicationEnvironmentResponse != null){
           this.ApplicationEnvironmentResponseBuild(builder);
        } if(builder.applicationProcessResponse != null){
            this.ApplicationProcessResponseBuild(builder);
        } if(builder.applicationCurrentDropletResponse != null){
            this.ApplicationCurrentDropletResponseBuild(builder);
        } if(builder.summaryApplicationResponse != null){
            this.SummaryApplicationResponseBuild(builder);
        }
    }

    private void ApplicationResponseBuild(AppV3.Builder builder){
        this.name = builder.applicationResponse.getName();
        this.guid = builder.applicationResponse.getId();
        this.spaceGuid = builder.applicationResponse.getRelationships().getSpace().getData().getId();
        this.state = builder.applicationResponse.getState().getValue();
        this.lifecycle = builder.applicationResponse.getLifecycle().getData();
        this.lifecycleType = builder.applicationResponse.getLifecycle().getType().getValue();
        this.package_updated_at = builder.applicationResponse.getUpdatedAt();
    }

    private void ApplicationEnvironmentResponseBuild(AppV3.Builder builder){
        this.applicationEnvironmentVariables = builder.applicationEnvironmentResponse.getApplicationEnvironmentVariables();
        this.environmentVariables = builder.applicationEnvironmentResponse.getEnvironmentVariables();
        this.runningEnvironmentVariables = builder.applicationEnvironmentResponse.getRunningEnvironmentVariables();
        this.stagingEnvironmentVariables = builder.applicationEnvironmentResponse.getStagingEnvironmentVariables();
        this.systemEnvironmentVariables = builder.applicationEnvironmentResponse.getSystemEnvironmentVariables();
    }

    private void ApplicationProcessResponseBuild(AppV3.Builder builder){
        this.healthCheck = builder.applicationProcessResponse.getHealthCheck();
        this.disk_quota = builder.applicationProcessResponse.getDiskInMb();
        this.guid = builder.applicationProcessResponse.getId();
        this.instances = builder.applicationProcessResponse.getInstances();
        this.memory = builder.applicationProcessResponse.getMemoryInMb();
        this.type = builder.applicationProcessResponse.getType();
        this.detected_start_command = builder.applicationProcessResponse.getCommand();

    }

    private void ApplicationProcessStatisticsResponseBuild(AppV3.Builder builder){
        this.processStatisticsResources = builder.applicationProcessStatisticsResponse.getResources();
    }

    private void ApplicationCurrentDropletResponseBuild(AppV3.Builder builder){
        this.buildpacks = builder.applicationCurrentDropletResponse.getBuildpacks();
        this.checksum = builder.applicationCurrentDropletResponse.getChecksum();
        this.executionMetadata = builder.applicationCurrentDropletResponse.getExecutionMetadata();
        this.image = builder.applicationCurrentDropletResponse.getImage();
        this.processTypes = builder.applicationCurrentDropletResponse.getProcessTypes();
        this.stack = builder.applicationCurrentDropletResponse.getStack();
        this.dropletState = builder.applicationCurrentDropletResponse.getState().getValue();
        this.guid = builder.applicationCurrentDropletResponse.getId();
        this.buildpack = builder.applicationCurrentDropletResponse.getBuildpacks().get(0).getName();
    }

    private void SummaryApplicationResponseBuild(AppV3.Builder builder){
        this.serviceInstances = builder.summaryApplicationResponse.getServices();
        this.domains = builder.summaryApplicationResponse.getAvailableDomains();
        this.routes = builder.summaryApplicationResponse.getRoutes();
    }

    public static class Resources{

        private Entity entity;

        private Metadata metadata;

        @JsonProperty("entity")
        public Entity getEntity() {
            return entity;
        }

        public void setEntity(Entity entity) {
            this.entity = entity;
        }

        @JsonProperty("metadata")
        public Metadata getMetadata() {
            return metadata;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }

        public Resources(org.cloudfoundry.client.v2.applications.ApplicationResource applicationResource){
            entity = new Entity(applicationResource.getEntity());
            metadata = new Metadata(applicationResource.getMetadata());
        }

        public Resources(){

        }

    }

    public static class Entity {
        private String detectedBuildpack;
        private String detectedBuildpackId;
        private Boolean enableSsh;
        private String eventsUrl;
        private String packageState;
        private String packageUpdatedAt;
        private List<Integer> ports;
        private String routeMappingsUrl;
        private String routesUrl;
        private String serviceBindingsUrl;
        private String spaceUrl;
        private String stackUrl;
        private String version;
        private String buildpack;
        private String command;
        private Boolean console;
        private String debug;
        private String detectedStartCommand;
        private Boolean diego;
        private Integer diskQuota;
        private DockerCredentials dockerCredentials;
        private Map<String, Object> dockerCredentialsJsons;
        private String dockerImage;
        private Map<String, Object> environmentJsons;
        private String healthCheckHttpEndpoint;
        private Integer healthCheckTimeout;
        private String healthCheckType;
        private Integer instances;
        private Integer memory;
        private String name;
        private Boolean production;
        private String spaceId;
        private String stackId;
        private String stagingFailedDescription;
        private String stagingFailedReason;
        private String stagingTaskId;
        private String state;

        public Entity(){

        }

        public Entity(ApplicationEntity entity){
            this.detectedBuildpack = entity.getDetectedBuildpack();
            this.detectedBuildpackId = entity.getDetectedBuildpackId();
            this.enableSsh = entity.getEnableSsh();
            this.eventsUrl = entity.getEventsUrl();
            this.packageState = entity.getPackageState();
            this.packageUpdatedAt = entity.getPackageUpdatedAt();
            this.ports = entity.getPorts();
            this.routeMappingsUrl = entity.getRouteMappingsUrl();
            this.routesUrl = entity.getRoutesUrl();
            this.serviceBindingsUrl = entity.getServiceBindingsUrl();
            this.spaceUrl = entity.getSpaceUrl();
            this.stackUrl = entity.getStackUrl();
            this.version = entity.getVersion();
            this.buildpack = entity.getBuildpack();
            this.command = entity.getCommand();
            this.console = entity.getConsole();
            this.debug = entity.getDebug();
            this.detectedStartCommand = entity.getDetectedStartCommand();
            this.diego = entity.getDiego();
            this.diskQuota = entity.getDiskQuota();
            this.dockerCredentials = entity.getDockerCredentials();
            this.dockerImage = entity.getDockerImage();
            this.environmentJsons = entity.getEnvironmentJsons();
            this.healthCheckHttpEndpoint = entity.getHealthCheckHttpEndpoint();
            this.healthCheckTimeout = entity.getHealthCheckTimeout();
            this.healthCheckType = entity.getHealthCheckType();
            this.instances = entity.getInstances();
            this.memory = entity.getMemory();
            this.name = entity.getName();
            this.production = entity.getProduction();
            this.spaceId = entity.getSpaceId();
            this.stackId = entity.getStackId();
            this.stagingFailedDescription = entity.getStagingFailedDescription();
            this.stagingFailedReason = entity.getStagingFailedReason();
            this.stagingTaskId = entity.getStagingTaskId();
            this.state = entity.getState();
        }

        /**
         * The detected buildpack
         */
        @JsonProperty("detected_buildpack")
        public @Nullable String getDetectedBuildpack() {
            return detectedBuildpack;
        }

        /**
         * The detected buildpack id
         */
        @JsonProperty("detected_buildpack_guid")
        public @Nullable String getDetectedBuildpackId() {
            return detectedBuildpackId;
        }

        /**
         * Whether SSH is enabled
         */
        @JsonProperty("enable_ssh")
        public @Nullable Boolean getEnableSsh() {
            return enableSsh;
        }

        /**
         * The events url
         */
        @JsonProperty("events_url")
        public @Nullable String getEventsUrl() {
            return eventsUrl;
        }

        /**
         * The package state
         */
        @JsonProperty("package_state")
        public @Nullable String getPackageState() {
            return packageState;
        }

        /**
         * When the package was update
         */
        @JsonProperty("package_updated_at")
        public @Nullable String getPackageUpdatedAt() {
            return packageUpdatedAt;
        }

        /**
         * The ports
         */
        @JsonProperty("ports")
        public @Nullable List<Integer> getPorts() {
            return ports;
        }

        /**
         * The route mappings url
         */
        @JsonProperty("route_mappings_url")
        public @Nullable String getRouteMappingsUrl() {
            return routeMappingsUrl;
        }

        /**
         * The routes url
         */
        @JsonProperty("routes_url")
        public @Nullable String getRoutesUrl() {
            return routesUrl;
        }

        /**
         * The service bindings url
         */
        @JsonProperty("service_bindings_url")
        public @Nullable String getServiceBindingsUrl() {
            return serviceBindingsUrl;
        }

        /**
         * The space url
         */
        @JsonProperty("space_url")
        public @Nullable String getSpaceUrl() {
            return spaceUrl;
        }

        /**
         * The stack url
         */
        @JsonProperty("stack_url")
        public @Nullable String getStackUrl() {
            return stackUrl;
        }

        /**
         * The version
         */
        @JsonProperty("version")
        public @Nullable String getVersion() {
            return version;
        }

        /**
         * The buildpack
         */
        @JsonProperty("buildpack")
        public @Nullable String getBuildpack() {
            return buildpack;
        }

        /**
         * The command
         */
        @JsonProperty("command")
        public @Nullable String getCommand() {
            return command;
        }

        /**
         * The console
         */
        @JsonProperty("console")
        @Deprecated
        public @Nullable Boolean getConsole() {
            return console;
        }

        /**
         * Debug
         */
        @JsonProperty("debug")
        @Deprecated
        public @Nullable String getDebug() {
            return debug;
        }

        /**
         * The detected start command
         */
        @JsonProperty("detected_start_command")
        public @Nullable String getDetectedStartCommand() {
            return detectedStartCommand;
        }

        /**
         * Diego
         */
        @JsonProperty("diego")
        public @Nullable Boolean getDiego() {
            return diego;
        }

        /**
         * The disk quota in megabytes
         */
        @JsonProperty("disk_quota")
        public @Nullable Integer getDiskQuota() {
            return diskQuota;
        }

        /**
         * The docker credentials
         */
        @JsonProperty("docker_credentials")
        public @Nullable DockerCredentials getDockerCredentials() {
            return dockerCredentials;
        }

        /**
         * The docker credentials JSONs
         */
        @JsonProperty("docker_credentials_json")
        public @Nullable Map<String, Object> getDockerCredentialsJsons() {
            return dockerCredentialsJsons;
        }

        /**
         * The docker image
         */
        @JsonProperty("docker_image")
        public @Nullable String getDockerImage() {
            return dockerImage;
        }

        /**
         * The environment JSONs
         */
        @JsonProperty("environment_json")
        public @Nullable Map<String, Object> getEnvironmentJsons() {
            return environmentJsons;
        }

        /**
         * The health check HTTP endpoint
         */
        @JsonProperty("health_check_http_endpoint")
        public @Nullable String getHealthCheckHttpEndpoint() {
            return healthCheckHttpEndpoint;
        }

        /**
         * The health check timeout
         */
        @JsonProperty("health_check_timeout")
        public @Nullable Integer getHealthCheckTimeout() {
            return healthCheckTimeout;
        }

        /**
         * The health check type
         */
        @JsonProperty("health_check_type")
        public @Nullable String getHealthCheckType() {
            return healthCheckType;
        }

        /**
         * The instances
         */
        @JsonProperty("instances")
        public @Nullable Integer getInstances() {
            return instances;
        }

        /**
         * The memory in megabytes
         */
        @JsonProperty("memory")
        public @Nullable Integer getMemory() {
            return memory;
        }

        /**
         * The name
         */
        @JsonProperty("name")
        public @Nullable String getName() {
            return name;
        }

        /**
         * Production
         */
        @JsonProperty("production")
        @Deprecated
        public @Nullable Boolean getProduction() {
            return production;
        }

        /**
         * The space id
         */
        @JsonProperty("space_guid")
        public @Nullable String getSpaceId() {
            return spaceId;
        }

        /**
         * The stack id
         */
        @JsonProperty("stack_guid")
        public @Nullable String getStackId() {
            return stackId;
        }

        /**
         * The staging failed description
         */
        @JsonProperty("staging_failed_description")
        public @Nullable String getStagingFailedDescription() {
            return stagingFailedDescription;
        }

        /**
         * The staging failed reason
         */
        @JsonProperty("staging_failed_reason")
        public @Nullable String getStagingFailedReason() {
            return stagingFailedReason;
        }

        /**
         * The staging task id
         */
        @JsonProperty("staging_task_id")
        public @Nullable String getStagingTaskId() {
            return stagingTaskId;
        }

        /**
         * The state
         */
        @JsonProperty("state")
        public @Nullable String getState() {
            return state;
        }

        public void setDetectedBuildpack(String detectedBuildpack) {
            this.detectedBuildpack = detectedBuildpack;
        }

        public void setDetectedBuildpackId(String detectedBuildpackId) {
            this.detectedBuildpackId = detectedBuildpackId;
        }

        public void setEnableSsh(Boolean enableSsh) {
            this.enableSsh = enableSsh;
        }

        public void setEventsUrl(String eventsUrl) {
            this.eventsUrl = eventsUrl;
        }

        public void setPackageState(String packageState) {
            this.packageState = packageState;
        }

        public void setPackageUpdatedAt(String packageUpdatedAt) {
            this.packageUpdatedAt = packageUpdatedAt;
        }

        public void setPorts(List<Integer> ports) {
            this.ports = ports;
        }

        public void setRouteMappingsUrl(String routeMappingsUrl) {
            this.routeMappingsUrl = routeMappingsUrl;
        }

        public void setRoutesUrl(String routesUrl) {
            this.routesUrl = routesUrl;
        }

        public void setServiceBindingsUrl(String serviceBindingsUrl) {
            this.serviceBindingsUrl = serviceBindingsUrl;
        }

        public void setSpaceUrl(String spaceUrl) {
            this.spaceUrl = spaceUrl;
        }

        public void setStackUrl(String stackUrl) {
            this.stackUrl = stackUrl;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setBuildpack(String buildpack) {
            this.buildpack = buildpack;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public void setConsole(Boolean console) {
            this.console = console;
        }

        public void setDebug(String debug) {
            this.debug = debug;
        }

        public void setDetectedStartCommand(String detectedStartCommand) {
            this.detectedStartCommand = detectedStartCommand;
        }

        public void setDiego(Boolean diego) {
            this.diego = diego;
        }

        public void setDiskQuota(Integer diskQuota) {
            this.diskQuota = diskQuota;
        }

        public void setDockerCredentials(DockerCredentials dockerCredentials) {
            this.dockerCredentials = dockerCredentials;
        }

        public void setDockerCredentialsJsons(Map<String, Object> dockerCredentialsJsons) {
            this.dockerCredentialsJsons = dockerCredentialsJsons;
        }

        public void setDockerImage(String dockerImage) {
            this.dockerImage = dockerImage;
        }

        public void setEnvironmentJsons(Map<String, Object> environmentJsons) {
            this.environmentJsons = environmentJsons;
        }

        public void setHealthCheckHttpEndpoint(String healthCheckHttpEndpoint) {
            this.healthCheckHttpEndpoint = healthCheckHttpEndpoint;
        }

        public void setHealthCheckTimeout(Integer healthCheckTimeout) {
            this.healthCheckTimeout = healthCheckTimeout;
        }

        public void setHealthCheckType(String healthCheckType) {
            this.healthCheckType = healthCheckType;
        }

        public void setInstances(Integer instances) {
            this.instances = instances;
        }

        public void setMemory(Integer memory) {
            this.memory = memory;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProduction(Boolean production) {
            this.production = production;
        }

        public void setSpaceId(String spaceId) {
            this.spaceId = spaceId;
        }

        public void setStackId(String stackId) {
            this.stackId = stackId;
        }

        public void setStagingFailedDescription(String stagingFailedDescription) {
            this.stagingFailedDescription = stagingFailedDescription;
        }

        public void setStagingFailedReason(String stagingFailedReason) {
            this.stagingFailedReason = stagingFailedReason;
        }

        public void setStagingTaskId(String stagingTaskId) {
            this.stagingTaskId = stagingTaskId;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public static class Metadata{
        private String guid;
        private String url;
        private String created_at;
        private String updated_at;

        @JsonProperty("guid")
        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        @JsonProperty("url")
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @JsonProperty("created_at")
        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        @JsonProperty("updated_at")
        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public Metadata(){

        }

        public Metadata(org.cloudfoundry.client.v2.Metadata metadata){
            this.guid = metadata.getId();
            this.url = metadata.getUrl();
            this.created_at = metadata.getCreatedAt();
            this.updated_at = metadata.getUpdatedAt();
        }

        @Override
        public String toString() {
            return "Metadata{" +
                    "guid='" + guid + '\'' +
                    ", url='" + url + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    '}';
        }
    }

    public static AppV3.Builder builder() {
        return new AppV3.Builder();
    }

    public static final class Builder {
        private GetApplicationResponse applicationResponse;
        private GetApplicationEnvironmentResponse applicationEnvironmentResponse;
        private GetApplicationProcessResponse applicationProcessResponse;
        private GetApplicationProcessStatisticsResponse applicationProcessStatisticsResponse;
        private GetApplicationCurrentDropletResponse applicationCurrentDropletResponse;
        private SummaryApplicationResponse summaryApplicationResponse;

        private Builder() {

        }

        public final Builder applicationResponse(GetApplicationResponse response) {
            this.applicationResponse = Objects.requireNonNull(response, "applicationResponse");
            return this;
        }

        public final Builder applicationEnvironmentResponse(GetApplicationEnvironmentResponse response) {
            this.applicationEnvironmentResponse = Objects.requireNonNull(response, "applicationEnvironmentResponse");
            return this;
        }

        public final Builder applicationProcessResponse(GetApplicationProcessResponse response) {
            this.applicationProcessResponse = Objects.requireNonNull(response, "applicationEnvironmentVariablesResponse");
            return this;
        }

        public final Builder applicationProcessStatisticsResponse(GetApplicationProcessStatisticsResponse response) {
            this.applicationProcessStatisticsResponse = Objects.requireNonNull(response, "applicationProcessStatisticsResponse");
            return this;
        }

        public final Builder applicationCurrentDropletResponse(GetApplicationCurrentDropletResponse response) {
            this.applicationCurrentDropletResponse = Objects.requireNonNull(response, "applicationEnvironmentVariablesResponse");
            return this;
        }

        //v3 기능나오면 대체 바람
        public final Builder summaryApplicationResponse(SummaryApplicationResponse response){
            this.summaryApplicationResponse = Objects.requireNonNull(response, "summaryApplicationResponse");
            return this;
        }

        public AppV3 build() {
            return new AppV3(this);
        }

    }

}