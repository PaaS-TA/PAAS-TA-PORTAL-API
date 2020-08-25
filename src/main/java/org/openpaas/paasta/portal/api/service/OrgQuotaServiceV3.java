package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.client.v2.organizationquotadefinitions.*;
import org.cloudfoundry.operations.organizationadmin.SetQuotaRequest;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Quota;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * 할당량 관리(조직) 서비스 - 조직의 할당량에 대한 리스트,조회,등록,수정,삭제,지정 기능을 제공한다.
 *
 * @author 최윤석
 * @version 1.0
 * @since 2018.4.11 최초작성
 */
@EnableAsync
@Service
public class OrgQuotaServiceV3 extends Common {

    /**
     * 조직 할당량 정보를 조회한다.
     *
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public ListOrganizationQuotaDefinitionsResponse getOrgQuotaDefinitionsList(String token) throws Exception {

        return cloudFoundryClient().organizationQuotaDefinitions().list(ListOrganizationQuotaDefinitionsRequest.builder().build()).block();
    }

    /**
     * 특정 조직의 할당량 정보를 조회한다.
     *
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public GetOrganizationQuotaDefinitionResponse getOrgQuotaDefinitions(String quotaGuid, String token) throws Exception {

        return cloudFoundryClient().organizationQuotaDefinitions().get(GetOrganizationQuotaDefinitionRequest.builder().organizationQuotaDefinitionId(quotaGuid).build()).block();
    }

    /**
     * 조직 할당량 정보를 생성한다.
     *
     * @param quota Quota Info
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public CreateOrganizationQuotaDefinitionResponse createOrgQuotaDefinitions(Quota quota, String token) throws Exception {

        /* required
        (*)이름  name
        (*)메모리 memory_limit
        (*)인스턴스 메모리 instance_memory_limit   default -1 unlimited amount
        라우트 total_routes
        서비스 인스턴스 total_services             default -1 unlimited amount
        APP 인스턴스 app_instance_limit           default -1 unlimited amount
        (*)무료 여부  non_basic_services_allowed
        예약된 라우트 포트 total_reserved_route_ports  Y : 0(Default)    N : 무제한 (-1)
        */


        return cloudFoundryClient().organizationQuotaDefinitions().create(CreateOrganizationQuotaDefinitionRequest.builder().name(quota.getName()).nonBasicServicesAllowed(quota.isNonBasicServicesAllowed()).totalServices(quota.getTotalServices()).totalRoutes(quota.getTotalRoutes()).totalReservedRoutePorts(quota.getTotalReservedRoutePorts()).memoryLimit(quota.getMemoryLimit()).instanceMemoryLimit(quota.getInstanceMemoryLimit()).applicationInstanceLimit(quota.getAppInstanceLimit()).build()).block();
    }

    /**
     * 특정 조직 할당량 정보를 수정한다.
     *
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public UpdateOrganizationQuotaDefinitionResponse updateOrgQuotaDefinitions(Quota quota, String token) throws Exception {

        return cloudFoundryClient().organizationQuotaDefinitions().update(UpdateOrganizationQuotaDefinitionRequest.builder().organizationQuotaDefinitionId(quota.getGuid().toString()).name(quota.getName()).nonBasicServicesAllowed(quota.isNonBasicServicesAllowed()).totalServices(quota.getTotalServices()).totalRoutes(quota.getTotalRoutes()).totalReservedRoutePorts(quota.getTotalReservedRoutePorts()).memoryLimit(quota.getMemoryLimit()).instanceMemoryLimit(quota.getInstanceMemoryLimit()).applicationInstanceLimit(quota.getAppInstanceLimit()).build()).block();
    }

    /**
     * 특정 조직 할당량 정보를 삭제한다.
     *
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public DeleteOrganizationQuotaDefinitionResponse deleteOrgQuotaDefinitions(String quotaGuid, String token) throws Exception {

        return cloudFoundryClient().organizationQuotaDefinitions().delete(DeleteOrganizationQuotaDefinitionRequest.builder().organizationQuotaDefinitionId(quotaGuid).async(false) // background async 처리 여부(recommend:true)
                .build()).block();
    }

    /**
     * 특정 조직 할당량 정보를 지정한다.
     *
     * @param quota the Quota Info
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public boolean setOrgQuotaDefinitions(Quota quota) throws Exception {

        // 공간할당량 셋팅은 operation 에서 구현(admin권한)
        cloudFoundryOperations().organizationAdmin().setQuota(SetQuotaRequest.builder().quotaName(quota.getName()).organizationName(quota.getOrganizationName()).build()).block();

        return true;
    }

}