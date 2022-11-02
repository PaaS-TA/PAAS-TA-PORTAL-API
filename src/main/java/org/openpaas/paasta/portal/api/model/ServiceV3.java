package org.openpaas.paasta.portal.api.model;

import org.cloudfoundry.client.v3.LastOperation;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;
import java.util.UUID;

/*
 Portal Web User에서 SpaceSummary Call의 CF API V3응답을 V2형태로 응답하기 위한 Custom Model 입니다.
 이로 인해 해당 모델은 코딩 규칙이 다를 수 있습니다.
 사용 Method (SpaceServiceV3.java - getSpaceSummary2)
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceV3 {


    private List<String> binding_app_names;
    private LastOperation last_operation;

    @JsonProperty("dashboard_url")
    private String dashboard_url;

    private String name;

    @JsonProperty("guid")
    private UUID guid;

    @JsonProperty("service_plan")
    private ServicePlan service_plan;

    public List<String> getBinding_app_names() {
        return binding_app_names;
    }

    public void setBinding_app_names(List<String> binding_app_names) {
        this.binding_app_names = binding_app_names;
    }

    public LastOperation getLast_operation() {
        return last_operation;
    }

    public void setLast_operation(LastOperation last_operation) {
        this.last_operation = last_operation;
    }

    public String getDashboard_url() {
        return dashboard_url;
    }

    public void setDashboard_url(String dashboard_url) {
        this.dashboard_url = dashboard_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public ServicePlan getService_plan() {
        return service_plan;
    }

    public void setService_plan(ServicePlan service_plan) {
        this.service_plan = service_plan;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServicePlan {

        @Override
        public String toString() {
            return "ServicePlan{" +
                    "name='" + name + '\'' +
                    ", service=" + service +
                    '}';
        }

        private String name;

        @JsonProperty("service")
        private ServiceInfo service;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ServiceInfo getService() {
            return service;
        }

        public void setService(ServiceInfo service) {
            this.service = service;
    }

        public ServicePlan(String name, ServiceInfo service) {
            this.name = name;
            this.service = service;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ServiceInfo {
            private String label;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public ServiceInfo(String label) {
                this.label = label;
            }

            @Override
            public String toString() {
                return "ServiceInfo{" +
                        "label='" + label + '\'' +
                        '}';
            }
        }

    }

    @Override
    public String toString() {
        return "ServiceV3{" +
                "binding_app_names=" + binding_app_names +
                ", last_operation=" + last_operation +
                ", dashboard_url='" + dashboard_url + '\'' +
                ", name='" + name + '\'' +
                ", guid=" + guid +
                ", service_plan=" + service_plan +
                '}';
    }
}
