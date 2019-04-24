package org.openpaas.paasta.portal.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Droplet {
    private String guid;
    private String state;
    private String spaceId;
    private String orgId;
    private String appId;

    @JsonProperty("guid")
    public String getGuid() {
        return guid;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("spaceId")
    public String getSpaceId() {
        return spaceId;
    }

    @JsonProperty("orgId")
    public String getOrgId() {
        return orgId;
    }

    @JsonProperty("appName")
    public String getAppId() {
        return appId;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "Droplet{" +
                "guid='" + guid + '\'' +
                ", state='" + state + '\'' +
                ", spaceId='" + spaceId + '\'' +
                ", orgId='" + orgId + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}
