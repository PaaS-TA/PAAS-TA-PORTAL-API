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
        if(builder.getApplicationResponse != null){
            this.ApplicationResponseBuild(builder);
        } if(builder.getApplicationEnvironmentResponse != null){
           this.ApplicationEnvironmentResponseBuild(builder);
        } if(builder.getApplicationProcessResponse != null){
            this.ApplicationProcessResponseBuild(builder);
        } if(builder.getApplicationCurrentDropletResponse != null){
            ApplicationCurrentDropletResponseBuild(builder);
        }
    }

    private void ApplicationResponseBuild(AppV3.Builder builder){
        this.name = builder.getApplicationResponse.getName();
        this.guid = builder.getApplicationResponse.getId();
        this.spaceGuid = builder.getApplicationResponse.getRelationships().getSpace().getData().getId();
        this.state = builder.getApplicationResponse.getState().getValue();
        this.lifecycle = builder.getApplicationResponse.getLifecycle().getData();
        this.lifecycleType = builder.getApplicationResponse.getLifecycle().getType().getValue();
    }

    private void ApplicationEnvironmentResponseBuild(AppV3.Builder builder){
        this.applicationEnvironmentVariables = builder.getApplicationEnvironmentResponse.getApplicationEnvironmentVariables();
        this.environmentVariables = builder.getApplicationEnvironmentResponse.getEnvironmentVariables();
        this.runningEnvironmentVariables = builder.getApplicationEnvironmentResponse.getRunningEnvironmentVariables();
        this.stagingEnvironmentVariables = builder.getApplicationEnvironmentResponse.getStagingEnvironmentVariables();
        this.systemEnvironmentVariables = builder.getApplicationEnvironmentResponse.getSystemEnvironmentVariables();
    }

    private void ApplicationProcessResponseBuild(AppV3.Builder builder){
        this.healthCheck = builder.getApplicationProcessResponse.getHealthCheck();
        this.disk = builder.getApplicationProcessResponse.getDiskInMb();
        this.guid = builder.getApplicationProcessResponse.getId();
        this.instance = builder.getApplicationProcessResponse.getInstances();
        this.memory = builder.getApplicationProcessResponse.getMemoryInMb();
        this.type = builder.getApplicationProcessResponse.getType();
        this.command = builder.getApplicationProcessResponse.getCommand();
    }

    private void ApplicationProcessStatisticsResponseBuild(AppV3.Builder builder){
        this.processStatisticsResources = builder.getApplicationProcessStatisticsResponse.getResources();
    }

    private void ApplicationCurrentDropletResponseBuild(AppV3.Builder builder){
        this.buildpacks = builder.getApplicationCurrentDropletResponse.getBuildpacks();
        this.checksum = builder.getApplicationCurrentDropletResponse.getChecksum();
        this.executionMetadata = builder.getApplicationCurrentDropletResponse.getExecutionMetadata();
        this.image = builder.getApplicationCurrentDropletResponse.getImage();
        this.processTypes = builder.getApplicationCurrentDropletResponse.getProcessTypes();
        this.stack = builder.getApplicationCurrentDropletResponse.getStack();
        this.dropletState = builder.getApplicationCurrentDropletResponse.getState().getValue();
        this.guid = builder.getApplicationCurrentDropletResponse.getId();
    }


    public static AppV3.Builder builder() {
        return new AppV3.Builder();
    }

    public static final class Builder {
        private GetApplicationResponse getApplicationResponse;
        private GetApplicationEnvironmentResponse getApplicationEnvironmentResponse;
//        private GetApplicationEnvironmentVariablesResponse getApplicationEnvironmentVariablesResponse;
        private GetApplicationProcessResponse getApplicationProcessResponse;
        private GetApplicationProcessStatisticsResponse getApplicationProcessStatisticsResponse;
        private GetApplicationCurrentDropletResponse getApplicationCurrentDropletResponse;
//        private GetApplicationCurrentDropletRelationshipResponse getApplicationCurrentDropletRelationshipResponse;

        private Builder() {

        }

        public final Builder GetApplicationResponse(GetApplicationResponse response) {
            this.getApplicationResponse = Objects.requireNonNull(response, "getApplicationResponse");
            return this;
        }

        public final Builder GetApplicationEnvironmentResponse(GetApplicationEnvironmentResponse response) {
            this.getApplicationEnvironmentResponse = Objects.requireNonNull(response, "getApplicationEnvironmentResponse");
            return this;
        }

//        public final Builder GetApplicationEnvironmentVariablesResponse(GetApplicationEnvironmentVariablesResponse response) {
//            this.getApplicationEnvironmentVariablesResponse = Objects.requireNonNull(response, "getApplicationEnvironmentVariablesResponse");
//            return this;
//        }

        public final Builder GetApplicationProcessResponse(GetApplicationProcessResponse response) {
            this.getApplicationProcessResponse = Objects.requireNonNull(response, "getApplicationEnvironmentVariablesResponse");
            return this;
        }

        public final Builder GetApplicationCurrentDropletResponse(GetApplicationCurrentDropletResponse response) {
            this.getApplicationCurrentDropletResponse = Objects.requireNonNull(response, "getApplicationEnvironmentVariablesResponse");
            return this;
        }

//        public final Builder GetApplicationCurrentDropletRelationshipResponse(GetApplicationCurrentDropletRelationshipResponse response) {
//            this.getApplicationCurrentDropletRelationshipResponse = Objects.requireNonNull(response, "getApplicationEnvironmentVariablesResponse");
//            return this;
//        }

        public AppV3 build() {
            return new AppV3(this);
        }
    }

}