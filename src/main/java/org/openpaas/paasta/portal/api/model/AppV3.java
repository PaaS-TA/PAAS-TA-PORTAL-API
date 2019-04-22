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

    private String name;

    private String state;

    private String guid;

    private String spaceGuid;

    private String spaceName;

    private String type;

    private String executionMetadata;

    private String command;

    private String stack;

    private String dropletState;

    private String image;

    private Integer instance;

    private Integer disk;

    private Integer memory;

    private Map<String, Object> applicationEnvironmentVariables;

    private Map<String, Object> environmentVariables;

    private Map<String, Object> runningEnvironmentVariables;

    private Map<String, Object> stagingEnvironmentVariables;

    private Map<String, Object> systemEnvironmentVariables;

    private Map<String, String> processTypes;

    private HealthCheck healthCheck;

    private List<ProcessStatisticsResource> processStatisticsResources;

    private List<Buildpack> buildpacks;

    private Checksum checksum;

    @JsonProperty("lifecycle")
    private LifecycleData lifecycle;

    private String lifecycleType;

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

    public String getCommand() {
        return command;
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

    public Integer getInstance() {
        return instance;
    }

    public Integer getDisk() {
        return disk;
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
                ", command='" + command + '\'' +
                ", stack='" + stack + '\'' +
                ", dropletState='" + dropletState + '\'' +
                ", image='" + image + '\'' +
                ", instance=" + instance +
                ", disk=" + disk +
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
        this.disk = builder.applicationProcessResponse.getDiskInMb();
        this.guid = builder.applicationProcessResponse.getId();
        this.instance = builder.applicationProcessResponse.getInstances();
        this.memory = builder.applicationProcessResponse.getMemoryInMb();
        this.type = builder.applicationProcessResponse.getType();
        this.command = builder.applicationProcessResponse.getCommand();
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