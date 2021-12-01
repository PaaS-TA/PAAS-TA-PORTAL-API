package org.openpaas.paasta.portal.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cloudfoundry.client.v3.Link;
import org.cloudfoundry.client.v3.processes.HealthCheck;

import java.util.Map;

public class ProcessInfo extends org.cloudfoundry.client.v3.processes.Process {

    private String type;
    private String command;
    private Integer instances;
    private Integer memory_in_mb;
    private Integer disk_in_mb;
    private HealthCheck health_check;
    private String id;
    private String created_at;
    private String updateed_at;
    private Map<String, Link> links;


    @Override
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @Override
    @JsonProperty("command")
    public String getCommand() {
        return command;
    }

    @Override
    @JsonProperty("instances")
    public Integer getInstances() {
        return instances;
    }

    @Override
    @JsonProperty("memory_in_mb")
    public Integer getMemoryInMb() {
        return memory_in_mb;
    }

    @Override
    @JsonProperty("disk_in_mb")
    public Integer getDiskInMb() {
        return disk_in_mb;
    }

    @Override
    @JsonProperty("health_check")
    public HealthCheck getHealthCheck() {
        return health_check;
    }

    @Override
    @JsonProperty("created_at")
    public String getCreatedAt() {
        return created_at;
    }

    @Override
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @Override
    @JsonProperty("links")
    public Map<String, Link> getLinks() {
        return links;
    }

    @Override
    @JsonProperty("updateed_at")
    public String getUpdatedAt() {
        return updateed_at;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setInstances(Integer instances) {
        this.instances = instances;
    }

    public void setMemoryInMb(Integer memory_in_mb) {
        this.memory_in_mb = memory_in_mb;
    }

    public void setDiskInMb(Integer disk_in_mb) {
        this.disk_in_mb = disk_in_mb;
    }

    public void setHealthCheck(HealthCheck health_check) {
        this.health_check = health_check;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdateed_at(String updateed_at) {
        this.updateed_at = updateed_at;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }
}
