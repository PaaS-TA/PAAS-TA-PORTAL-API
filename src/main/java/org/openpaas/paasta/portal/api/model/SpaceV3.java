package org.openpaas.paasta.portal.api.model;

import org.cloudfoundry.Nullable;
import org.cloudfoundry.client.v3.serviceInstances.ServiceInstance;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpaceV3 {
    @JsonProperty("detected_buildpack")
    private String detected_buildpack;

    @JsonProperty("detected_buildpack_guid")
    private String detected_buildpack_guid;

    @JsonProperty("enable_ssh")
    private String enable_ssh;

    @JsonProperty("guid")
    private UUID guid;

    @JsonProperty("package_state")
    private String package_state;

    @JsonProperty("routes")
    private Map<String, Object> routes;


    @JsonProperty("running_instances")
    private int running_instances;

    @JsonProperty("service_count")
    private int service_count;

    @JsonProperty("service_names")
    private String service_names;

    @JsonProperty("urls")
    private String urls;

    @JsonProperty("detected_start_command")
    private String detected_start_command;

    @JsonProperty("version")
    private String version;

    @JsonProperty("buildpack")
    private String buildpack;

    @JsonProperty("command")
    private String command;

    @JsonProperty("console")
    private String console;

    @JsonProperty("debug")
    private String debug;

    @JsonProperty("diego")
    private String diego;

    @JsonProperty("disk_quota")
    private int disk_quota;

    @JsonProperty("memory")
    private String memory;

    @JsonProperty("name")
    private String name;

    @JsonProperty("production")
    private String production;

    @JsonProperty("space_guid")
    private String space_guid;

    @JsonProperty("stack_guid")
    private String stack_guid;

    @JsonProperty("staging_failed_description")
    private String staging_failed_description;

    @JsonProperty("staging_failed_reason")
    private String staging_failed_reason;

    @JsonProperty("staging_task_id")
    private String staging_task_id;

    @JsonProperty("state")
    private String state;

    @JsonProperty("services")
    private List<ServiceInstance> serviceInstances;

    public String getDetected_buildpack() {
        return detected_buildpack;
    }

    public void setDetected_buildpack(String detected_buildpack) {
        this.detected_buildpack = detected_buildpack;
    }

    public String getDetected_buildpack_guid() {
        return detected_buildpack_guid;
    }

    public void setDetected_buildpack_guid(String detected_buildpack_guid) {
        this.detected_buildpack_guid = detected_buildpack_guid;
    }

    public String getEnable_ssh() {
        return enable_ssh;
    }

    public void setEnable_ssh(String enable_ssh) {
        this.enable_ssh = enable_ssh;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getPackage_state() {
        return package_state;
    }

    public void setPackage_state(String package_state) {
        this.package_state = package_state;
    }

    public Map<String, Object> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Object> routes) {
        this.routes = routes;
    }

    public int getRunning_instances() {
        return running_instances;
    }

    public void setRunning_instances(int running_instances) {
        this.running_instances = running_instances;
    }

    public int getService_count() {
        return service_count;
    }

    public void setService_count(int service_count) {
        this.service_count = service_count;
    }

    public String getService_names() {
        return service_names;
    }

    public void setService_names(String service_names) {
        this.service_names = service_names;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getDetected_start_command() {
        return detected_start_command;
    }

    public void setDetected_start_command(String detected_start_command) {
        this.detected_start_command = detected_start_command;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildpack() {
        return buildpack;
    }

    public void setBuildpack(String buildpack) {
        this.buildpack = buildpack;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getDiego() {
        return diego;
    }

    public void setDiego(String diego) {
        this.diego = diego;
    }

    public int getDisk_quota() {
        return disk_quota;
    }

    public void setDisk_quota(int disk_quota) {
        this.disk_quota = disk_quota;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getSpace_guid() {
        return space_guid;
    }

    public void setSpace_guid(String space_guid) {
        this.space_guid = space_guid;
    }

    public String getStack_guid() {
        return stack_guid;
    }

    public void setStack_guid(String stack_guid) {
        this.stack_guid = stack_guid;
    }

    public String getStaging_failed_description() {
        return staging_failed_description;
    }

    public void setStaging_failed_description(String staging_failed_description) {
        this.staging_failed_description = staging_failed_description;
    }

    public String getStaging_failed_reason() {
        return staging_failed_reason;
    }

    public void setStaging_failed_reason(String staging_failed_reason) {
        this.staging_failed_reason = staging_failed_reason;
    }

    public String getStaging_task_id() {
        return staging_task_id;
    }

    public void setStaging_task_id(String staging_task_id) {
        this.staging_task_id = staging_task_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ServiceInstance> getServiceInstances() {
        return serviceInstances;
    }

    public void setServiceInstances(List<ServiceInstance> serviceInstances) {
        this.serviceInstances = serviceInstances;
    }

    //    /**
//     * The health check HTTP endpoint
//     */
//    @JsonProperty("health_check_http_endpoint")
//    public @Nullable
//    String getHealthCheckHttpEndpoint() {
//        return healthCheckHttpEndpoint;
//    }
//
//    /**
//     * The health check timeout
//     */
//    @JsonProperty("health_check_timeout")
//    public @Nullable Integer getHealthCheckTimeout() {
//        return healthCheckTimeout;
//    }
//
//    /**
//     * The health check type
//     */
//    @JsonProperty("health_check_type")
//    public @Nullable String getHealthCheckType() {
//        return healthCheckType;
//    }


    @Override
    public String toString() {
        return "SpaceV3{" +
                "detected_buildpack='" + detected_buildpack + '\'' +
                ", detected_buildpack_guid='" + detected_buildpack_guid + '\'' +
                ", enable_ssh='" + enable_ssh + '\'' +
                ", guid=" + guid +
                ", package_state='" + package_state + '\'' +
                ", routes=" + routes +
                ", running_instances=" + running_instances +
                ", service_count=" + service_count +
                ", service_names='" + service_names + '\'' +
                ", urls='" + urls + '\'' +
                ", detected_start_command='" + detected_start_command + '\'' +
                ", version='" + version + '\'' +
                ", buildpack='" + buildpack + '\'' +
                ", command='" + command + '\'' +
                ", console='" + console + '\'' +
                ", debug='" + debug + '\'' +
                ", diego='" + diego + '\'' +
                ", disk_quota=" + disk_quota +
                ", memory='" + memory + '\'' +
                ", name='" + name + '\'' +
                ", production='" + production + '\'' +
                ", space_guid='" + space_guid + '\'' +
                ", stack_guid='" + stack_guid + '\'' +
                ", staging_failed_description='" + staging_failed_description + '\'' +
                ", staging_failed_reason='" + staging_failed_reason + '\'' +
                ", staging_task_id='" + staging_task_id + '\'' +
                ", state='" + state + '\'' +
                ", serviceInstances=" + serviceInstances +
                '}';
    }
}
