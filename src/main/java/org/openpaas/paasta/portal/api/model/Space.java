package org.openpaas.paasta.portal.api.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 공간 모델
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Space {
    private String orgName;
    private String spaceName;
    private String newSpaceName;

    private UUID guid;
    private String name;

    @JsonProperty("service_count")
    private int serviceCount = 0;

    @JsonProperty("app_count")
    private int appCount = 0;

    private int appCountStarted = 0;
    private int appCountStopped = 0;
    private int appCountCrashed = 0;

    @JsonProperty("mem_dev_total")
    private int memDevTotal;

    @JsonProperty("mem_prd_total")
    private int memProdTotal;

    private int memoryUsage;
    private int memoryLimit;

    private int spaceId;
    private int orgId;
    private String spaceGuid;
    private String orgGuid;
    private Date created;
    private Date lastModified;

    private boolean recursive = true;

    private List<App> apps = new ArrayList<App>();

    private List<Service> services = new ArrayList<Service>();

    private Entity entity;

    public Space(){
        //empty
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getNewSpaceName() {
        return newSpaceName;
    }

    public void setNewSpaceName(String newSpaceName) {
        this.newSpaceName = newSpaceName;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
        this.setSpaceGuid( guid.toString() );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(int serviceCount) {
        this.serviceCount = serviceCount;
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }

    public int getAppCountStarted() {
        return appCountStarted;
    }

    public void setAppCountStarted(int appCountStarted) {
        this.appCountStarted = appCountStarted;
    }

    public int getAppCountStopped() {
        return appCountStopped;
    }

    public void setAppCountStopped(int appCountStopped) {
        this.appCountStopped = appCountStopped;
    }

    public int getAppCountCrashed() {
        return appCountCrashed;
    }

    public void setAppCountCrashed(int appCountCrashed) {
        this.appCountCrashed = appCountCrashed;
    }

    public int getMemDevTotal() {
        return memDevTotal;
    }

    public void setMemDevTotal(int memDevTotal) {
        this.memDevTotal = memDevTotal;
    }

    public int getMemProdTotal() {
        return memProdTotal;
    }

    public void setMemProdTotal(int memProdTotal) {
        this.memProdTotal = memProdTotal;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public int getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(int memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getSpaceGuid() {
        return spaceGuid;
    }

    public void setSpaceGuid(String spaceGuid) {
        this.spaceGuid = spaceGuid;
        this.setGuid( UUID.fromString( spaceGuid ) );
    }
    
    public String getOrgGuid() {
        return orgGuid;
    }
    
    public void setOrgGuid( String orgGuid ) {
        this.orgGuid = orgGuid;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Entity {
        @JsonProperty("space_quota_definition_guid")
        private String spaceQuotaDefinitionGuid;

        public String getSpaceQuotaDefinitionGuid() {
            return spaceQuotaDefinitionGuid;
        }

        public void setSpaceQuotaDefinitionGuid(String spaceQuotaDefinitionGuid) {
            this.spaceQuotaDefinitionGuid = spaceQuotaDefinitionGuid;
        }
    }

    public boolean isRecursive() {
        return recursive;
    }
    
    public void setRecursive( boolean recursive ) {
        this.recursive = recursive;
    }
}
