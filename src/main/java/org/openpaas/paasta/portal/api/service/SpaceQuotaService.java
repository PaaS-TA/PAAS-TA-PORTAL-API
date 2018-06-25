package org.openpaas.paasta.portal.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.cloudfoundry.client.v2.ClientV2Exception;
import org.cloudfoundry.client.v2.organizationquotadefinitions.DeleteOrganizationQuotaDefinitionRequest;
import org.cloudfoundry.client.v2.organizationquotadefinitions.DeleteOrganizationQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.spacequotadefinitions.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Quota;
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
public class SpaceQuotaService extends Common {

    /**
     * 공간 할당량 정보를 조회한다.
     *
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "getSpaceQuotaDefinitionsList")
    public ListSpaceQuotaDefinitionsResponse getSpaceQuotaDefinitionsList(String token) throws Exception {

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
               .spaceQuotaDefinitions()
                .list(ListSpaceQuotaDefinitionsRequest.builder()
                        .build()
                ).log()
                .block();
    }

    /**
     * 특정 공간 할당량 정의 정보를 조회한다.
     *
     * @param spaceQuotaId the Space QuotaDefinition Guid
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "getSpaceQuotaDefinitions")
    public GetSpaceQuotaDefinitionResponse getSpaceQuotaDefinitions(String spaceQuotaId, String token) throws Exception {

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .spaceQuotaDefinitions()
                .get(GetSpaceQuotaDefinitionRequest.builder()
                        .spaceQuotaDefinitionId(spaceQuotaId)
                        .build()
                ).log()
                .block();
    }

    /**
     * 공간 할당량 정보를 생성한다.
     *
     * @param quota Quota Info
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "createSpaceQuotaDefinitions")
    public CreateSpaceQuotaDefinitionResponse createSpaceQuotaDefinitions(org.openpaas.paasta.portal.api.model.Quota quota, String token) throws ClientV2Exception {

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .spaceQuotaDefinitions()
                .create(CreateSpaceQuotaDefinitionRequest.builder()
                        .name(quota.getName())
                        .nonBasicServicesAllowed(quota.isNonBasicServicesAllowed())
                        .totalServices(quota.getTotalServices())
                        .totalRoutes(quota.getTotalRoutes())
                        .totalReservedRoutePorts(quota.getTotalReservedRoutePorts())
                        .memoryLimit(quota.getMemoryLimit())
                        .instanceMemoryLimit(quota.getInstanceMemoryLimit())
                        .applicationInstanceLimit(quota.getAppInstanceLimit())
                        .organizationId(quota.getOrginazationGuid().toString())
                        .build()
                ).log()
                .block();
    }

    /**
     * 특정 공간 할당량 정보를 삭제한다.
     *
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "deleteSpaceQuotaDefinitions")
    public DeleteSpaceQuotaDefinitionResponse deleteSpaceQuotaDefinitions(String guid, String token) throws Exception {

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .spaceQuotaDefinitions()
                .delete(DeleteSpaceQuotaDefinitionRequest.builder()
                        .spaceQuotaDefinitionId(guid)
                        .async(false)  // background async 처리 여부(recommend:true)
                        .build()
                ).log()
                .block();
    }

    /**
     * 공간 할당량 정의를 특정 공간에 지정한다.
     *
     * @param quota Quota Info
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    //@HystrixCommand(fallbackMethod = "associateSpaceQuotaDefinitions")
    public AssociateSpaceQuotaDefinitionResponse associateSpaceQuotaDefinitions(org.openpaas.paasta.portal.api.model.Quota quota, String token) throws Exception {

        /* required
            - 공간 정의 GUID
            - 공간 GUID
        */
        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .spaceQuotaDefinitions()
                .associateSpace(AssociateSpaceQuotaDefinitionRequest.builder()
                        .spaceQuotaDefinitionId(quota.getGuid().toString())
                        .spaceId(quota.getSpaceGuid().toString())
                        .build()
                ).log()
                .block();
    }

    /**
     * 해당 공간 할당량 정의를 사용하는 공간 리스트를 조회한다.
     *
     * @param guid Space Definition Guid
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "getListSpaceUsedSpaceQuotaDefinitions")
    public ListSpaceQuotaDefinitionSpacesResponse getListSpaceUsedSpaceQuotaDefinitions(String guid, String token) throws Exception {

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .spaceQuotaDefinitions()
                .listSpaces(ListSpaceQuotaDefinitionSpacesRequest.builder()
                        .spaceQuotaDefinitionId(guid)
                        .build()
                ).log()
                .block();
    }

    /**
     * 해당 공간에 설정된 할당량 정의를 삭제한다.
     *
     * @param quota Quota Info
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    //@HystrixCommand(fallbackMethod = "removeSpaceQuotaDefinitionsFromSpace")
    public boolean removeSpaceQuotaDefinitionsFromSpace(org.openpaas.paasta.portal.api.model.Quota quota, String token) throws Exception {

         Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
            .spaceQuotaDefinitions()
            .removeSpace(RemoveSpaceQuotaDefinitionRequest.builder()
                    .spaceQuotaDefinitionId(quota.getGuid().toString())
                    .spaceId(quota.getSpaceGuid().toString())
                    .build()
            ).log()
            .block();
        return true;
    }

    /**
     * 특정 조직 할당량 정보를 수정한다.
     *
     * @param token the token
     * @return ModelAndView model
     * @throws Exception the exception
     */
    //@HystrixCommand(fallbackMethod = "updateSpaceQuotaDefinitions")
    public UpdateSpaceQuotaDefinitionResponse updateSpaceQuotaDefinitions(Quota quota, String token) throws Exception {

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
                .spaceQuotaDefinitions()
                .update(UpdateSpaceQuotaDefinitionRequest.builder()
                        .spaceQuotaDefinitionId(quota.getGuid().toString())
                        .name(quota.getName())
                        .nonBasicServicesAllowed(quota.isNonBasicServicesAllowed())
                        .totalServices(quota.getTotalServices())
                        .totalRoutes(quota.getTotalRoutes())
                        .totalReservedRoutePorts(quota.getTotalReservedRoutePorts())
                        .memoryLimit(quota.getMemoryLimit())
                        .instanceMemoryLimit(quota.getInstanceMemoryLimit())
                        .applicationInstanceLimit(quota.getAppInstanceLimit())
                        .organizationId(quota.getOrginazationGuid().toString())
                        .build()
                ).log()
                .block();
    }

}