package org.openpaas.paasta.portal.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Override
    public String toString() {
        return "AppV3{" +
                "name='" + name + '\'' +
                ", package_state='" + state + '\'' +
                ", guid='" + guid + '\'' +
                ", spaceGuid='" + spaceGuid + '\'' +
                ", spaceName='" + spaceName + '\'' +
                ", type='" + type + '\'' +
                ", executionMetadata='" + executionMetadata + '\'' +
                ", detected_start_command='" + detected_start_command + '\'' +
                ", stack='" + stack + '\'' +
                ", dropletState='" + dropletState + '\'' +
                ", image='" + image + '\'' +
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
            ApplicationCurrentDropletResponseBuild(builder);
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


    public static AppV3.Builder builder() {
        return new AppV3.Builder();
    }

    public static final class Builder {
        private GetApplicationResponse applicationResponse;
        private GetApplicationEnvironmentResponse applicationEnvironmentResponse;
        private GetApplicationProcessResponse applicationProcessResponse;
        private GetApplicationProcessStatisticsResponse applicationProcessStatisticsResponse;
        private GetApplicationCurrentDropletResponse applicationCurrentDropletResponse;

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


        public AppV3 build() {
            return new AppV3(this);
        }

    }

}