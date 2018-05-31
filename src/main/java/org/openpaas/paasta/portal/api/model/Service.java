package org.openpaas.paasta.portal.api.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.UUID;

/**
 * 서비스 모델
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.5.30 최초작성
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
    private String name;
    private String newName;

    private String orgName;
    private String spaceName;

    @JsonProperty("guid")
    private UUID guid;

    private String servicePlanName;

    private String serviceLabel;

    @JsonProperty("bound_app_count")
    private int boundAppCount;

    @JsonProperty("service_plan")
    private ServicePlan servicePlan;

    private String serviceName;

    @JsonProperty("dashboard_url")
    private String dashboardUrl;

    @JsonProperty("dashboard_use_yn")
    private String dashboardUseYn;

    @JsonProperty("serviceInstanceName")
    private String serviceInstanceName;

    @JsonProperty("newServiceInstanceName")
    private String newServiceInstanceName;

    @JsonProperty("credentials")
    private String credentials;

    @JsonProperty("syslogDrainUrl")
    private String syslogDrainUrl;

    @JsonProperty("spaceGuid")
    private String spaceGuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServicePlan getServicePlan() {
        return servicePlan;
    }

    public void setServicePlan(ServicePlan servicePlan) {
        this.servicePlan = servicePlan;
    }

    public String getServicePlanName() {
        return servicePlanName;
    }

    public void setServicePlanName(String servicePlanName) {
        this.servicePlanName = servicePlanName;
    }

    public String getServiceLabel() {
        return serviceLabel;
    }

    public void setServiceLabel(String serviceLabel) {
        this.serviceLabel = serviceLabel;
    }

    public int getBoundAppCount() {
        return boundAppCount;
    }

    public void setBoundAppCount(int boundAppCount) {
        this.boundAppCount = boundAppCount;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public String getDashboardUseYn() {
        return dashboardUseYn;
    }

    public void setDashboardUseYn(String dashboardUseYn) {
        this.dashboardUseYn = dashboardUseYn;
    }

    public String getServiceInstanceName() {return serviceInstanceName;}

    public void setServiceInstanceName(String serviceInstanceName) {this.serviceInstanceName = serviceInstanceName;}

    public String getCredentials() {return credentials;}

    public void setCredentials(String credentials) {this.credentials = credentials;}

    public String getSyslogDrainUrl() {return syslogDrainUrl;}

    public void setSyslogDrainUrl(String syslogDrainUrl) {this.syslogDrainUrl = syslogDrainUrl;}

    public String getSpaceGuid() {return spaceGuid;}

    public void setSpaceGuid(String spaceGuid) {this.spaceGuid = spaceGuid;}

    public String getNewServiceInstanceName() {return newServiceInstanceName;}

    public void setNewServiceInstanceName(String newServiceInstanceName) {this.newServiceInstanceName = newServiceInstanceName;}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ServicePlan {

        private String name;

        @JsonProperty("service")
        private ServiceInfo service;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
            setServicePlanName(name);
        }

        public ServiceInfo getService() {
            return service;
        }

        public void setService(ServiceInfo service) {
            this.service = service;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class ServiceInfo {
            private String label;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
                setServiceLabel(label);
            }
        }

    }
}
