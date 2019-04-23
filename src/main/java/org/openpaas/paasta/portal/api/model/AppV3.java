package org.openpaas.paasta.portal.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Override
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