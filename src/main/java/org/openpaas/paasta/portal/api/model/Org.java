package org.openpaas.paasta.portal.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class Org extends Entity {
    private String orgName;
    private String newOrgName;
    private boolean recursive = true;

    private UUID guid;
    private String name;
    private String status;
    private int memoryUsage;
    private int memoryLimit;


    private List<Space> spaces = new ArrayList<Space>();

    private boolean billingEnabled = false;

    private Quota quota;
    private String quotaGuid;
    private String userId;


    public Org() {
        //empty
    }

    public boolean isBillingEnabled() {
        return billingEnabled;
    }

    public void setBillingEnabled(boolean billingEnabled) {
        this.billingEnabled = billingEnabled;
    }

    public Quota getQuota() {
        return quota;
    }

    public void setQuota(Quota quota) {
        this.quota = quota;
    }
    
    public String getQuotaGuid() {
        if (this.quotaGuid != null) {
            return quotaGuid;
        } else if (this.quota != null && this.quota.getGuid() != null) {
            return this.quota.getGuid().toString();
        } else {
            return null;
        }
    }
    
    public void setQuotaGuid( String quotaGuid ) {
        if (this.quota != null && this.quota.getGuid() != null)
            this.quotaGuid = this.quota.getGuid().toString();
        else
            this.quotaGuid = quotaGuid;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getNewOrgName() {
        return newOrgName;
    }

    public void setNewOrgName(String newOrgName) {
        this.newOrgName = newOrgName;
    }

    public boolean isRecursive() {
        return recursive;
    }
    
    public void setRecursive( boolean recursive ) {
        this.recursive = recursive;
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

    public List<Space> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<Space> spaces) {
        this.spaces = spaces;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }
}
