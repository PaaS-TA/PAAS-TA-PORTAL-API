package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v2.spacequotadefinitions.GetSpaceQuotaDefinitionRequest;
import org.cloudfoundry.client.v2.spacequotadefinitions.GetSpaceQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.spacequotadefinitions.ListSpaceQuotaDefinitionsRequest;
import org.cloudfoundry.client.v2.spacequotadefinitions.ListSpaceQuotaDefinitionsResponse;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * 할당량 관리(공간) 서비스 - 공간의 할당량에 대한 리스트,조회,등록,수정,삭제,지정,해제 기능을 제공한다.
 *
 * @author 최윤석
 * @version 1.0
 * @since 2018.4.11 최초작성
 */
@EnableAsync
@Service
public class spaceQuotaService extends Common {

    /**
     * 공간 할당량 정보를 조회한다.
     *
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public ListSpaceQuotaDefinitionsResponse getSpaceQuotaDefinitionsList(String token) throws Exception {

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
               .spaceQuotaDefinitions()
                .list(ListSpaceQuotaDefinitionsRequest.builder()
                        .build()
                ).log()
                .block();
    }

    /**
     * 특정 공간 할당량 정의 정보를 조회한다.
     *
     * @param spaceQuotaDefinitionId the Space QuotaDefinition Guid
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    public GetSpaceQuotaDefinitionResponse getSpaceQuotaDefinitions(String spaceQuotaDefinitionId, String token) throws Exception {

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
                .spaceQuotaDefinitions()
                .get(GetSpaceQuotaDefinitionRequest.builder()
                        .spaceQuotaDefinitionId(spaceQuotaDefinitionId)
                        .build()
                ).log()
                .block();
    }

//    /**
//     * 조직 할당량 정보를 생성한다.
//     *
//     * @param quota Quota Info
//     * @param token the token
//     * @return ModelAndView model
//     * @throws Exception the exception
//     */
//    public CreateOrganizationQuotaDefinitionResponse createOrgQuotaDefinitions(org.openpaas.paasta.portal.api.model.Quota quota, String token) throws Exception {
//
//        /* required
//        (*)이름  name
//        (*)메모리 memory_limit
//        (*)인스턴스 메모리 instance_memory_limit
//        라우트 total_routes
//        서비스 인스턴스 total_services
//        APP 인스턴스 app_instance_limit
//        (*)무료 여부  non_basic_services_allowed
//        예약된 라우트 포트 total_reserved_route_ports  Y : 0(Default)    N : 무제한 (-1)
//        */
//
//        return Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
//                .organizationQuotaDefinitions()
//                .create(CreateOrganizationQuotaDefinitionRequest.builder()
//                        .name(quota.getName())
//                        .nonBasicServicesAllowed(quota.isNonBasicServicesAllowed())
//                        .totalServices(quota.getTotalServices())
//                        .totalRoutes(quota.getTotalRoutes())
//                        .totalReservedRoutePorts(quota.getTotalReservedRoutePorts())
//                        .memoryLimit(quota.getMemoryLimit())
//                        .instanceMemoryLimit(quota.getInstanceMemoryLimit())
//                        .applicationInstanceLimit(quota.getAppInstanceLimit())
//                        .build()
//                ).log()
//                .block();
//    }
//
//    /**
//     * 특정 조직 할당량 정보를 수정한다.
//     *
//     * @param token the token
//     * @return ModelAndView model
//     * @throws Exception the exception
//     */
//    public UpdateOrganizationQuotaDefinitionResponse updateOrgQuotaDefinitions(org.openpaas.paasta.portal.api.model.Quota quota, String token) throws Exception {
//
//        return Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
//                .organizationQuotaDefinitions()
//                .update(UpdateOrganizationQuotaDefinitionRequest.builder()
//                        .organizationQuotaDefinitionId(quota.getGuid().toString())
//                        .name(quota.getName())
//                        .nonBasicServicesAllowed(quota.isNonBasicServicesAllowed())
//                        .totalServices(quota.getTotalServices())
//                        .totalRoutes(quota.getTotalRoutes())
//                        .totalReservedRoutePorts(quota.getTotalReservedRoutePorts())
//                        .memoryLimit(quota.getMemoryLimit())
//                        .instanceMemoryLimit(quota.getInstanceMemoryLimit())
//                        .applicationInstanceLimit(quota.getAppInstanceLimit())
//                        .build()
//                ).log()
//                .block();
//    }
//
//    /**
//     * 특정 조직 할당량 정보를 삭제한다.
//     *
//     * @param token the token
//     * @return ModelAndView model
//     * @throws Exception the exception
//     */
//    public DeleteOrganizationQuotaDefinitionResponse deleteOrgQuotaDefinitions(String guid, String token) throws Exception {
//
//        return Common.cloudFoundryClient(connectionContext(), tokenProvider(adminUserName,adminPassword))
//                .organizationQuotaDefinitions()
//                .delete(DeleteOrganizationQuotaDefinitionRequest.builder()
//                        .organizationQuotaDefinitionId(guid)
//                        .async(true) // background 처리 여부(recommend:true)
//                        .build()
//                ).log()
//                .block();
//    }

}