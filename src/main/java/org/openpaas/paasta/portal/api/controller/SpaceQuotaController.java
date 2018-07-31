package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v2.spacequotadefinitions.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Quota;
import org.openpaas.paasta.portal.api.service.SpaceQuotaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 할당량 관리(공간) 컨트롤러 - 공간의 할당량에 대한 리스트,조회,등록,수정,삭제,지정,해제 기능을 제공한다.
 *
 * @author 최윤석
 * @version 1.0
 * @since 2018.4.11 최초작성
 */
@RestController
public class SpaceQuotaController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceQuotaController.class);

    @Autowired
    SpaceQuotaService spaceQuotaService; // The Space Quota service.

    /**
     * 공간 할당량 리스트를 조회한다.
     *
     * @param request the request
     * @return ListSpaceQuotaDefinitionsResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL + "/spaces/quota-definitions")
    public ListSpaceQuotaDefinitionsResponse listSpaceQuotaDefinitions(HttpServletRequest request) throws Exception {
        LOGGER.info("listSpaceQuotaDefinitions Start : ");
        return spaceQuotaService.getSpaceQuotaDefinitionsList(request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 특정 공간의 할당량 정의를 조회한다.
     *
     * @param spaceQuotaId The guid of the Space Quota Definition
     * @param request the request
     * @return GetSpaceQuotaDefinitionResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL + "/spaces/quota-definitions/{spaceQuotaId}")
    public GetSpaceQuotaDefinitionResponse getSpaceQuotaDefinitions(@PathVariable String spaceQuotaId, HttpServletRequest request) throws Exception {
        LOGGER.info("getSpaceQuotaDefinitions Start : ");
        return spaceQuotaService.getSpaceQuotaDefinitions(spaceQuotaId ,request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 공간 할당량 리스트를 등록한다.
     *
     * @param quota Quota Info
     * @param request the request
     * @return CreateSpaceQuotaDefinitionResponse
     * @throws Exception the exception
     */
    @PostMapping(Constants.V2_URL + "/spaces/quota-definitions")
    public CreateSpaceQuotaDefinitionResponse createSpaceQuotaDefinitions(@RequestBody Quota quota, HttpServletRequest request) throws Exception {
        LOGGER.info("summary Start : ");
        return spaceQuotaService.createSpaceQuotaDefinitions(quota, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 해당 조직 할당량 정의를 삭제한다.
     *
     * @param guid The guid of the Organization Quota Definition
     * @param request the request
     * @return DeleteSpaceQuotaDefinitionResponse
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V2_URL + "/spaces/quota-definitions/{guid}")
    public DeleteSpaceQuotaDefinitionResponse deleteSpaceQuotaDefinitions(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("deleteSpaceQuotaDefinitions Start : ");

        return spaceQuotaService.deleteSpaceQuotaDefinitions(guid, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }


    /**
     * 공간 할당량 정의를 특정 공간에 지정한다.
     *
     * @param spaceQuotaId The guid of the Space Quota Definition
     * @param spaceId The guid of the Space
     * @param quota Quota Info
     * @param request the request
     * @return AssociateSpaceQuotaDefinitionResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V2_URL + "/spaces/quota-definitions/{spaceQuotaId}/spaces/{spaceId}")
    public AssociateSpaceQuotaDefinitionResponse associateSpaceQuotaDefinitions(@PathVariable String spaceQuotaId, @PathVariable String spaceId,
                                                                                @RequestBody Quota quota, HttpServletRequest request) throws Exception {
        LOGGER.info("associateSpaceQuotaDefinitions Start : ");
        quota.setGuid(UUID.fromString(spaceQuotaId));
        quota.setSpaceGuid(UUID.fromString(spaceId));
        return spaceQuotaService.associateSpaceQuotaDefinitions(quota, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 해당 공간 할당량 정의를 사용하는 공간 리스트를 조회한다.
     *
     * @param spaceQuotaId The guid of the Space Quota Definition
     * @param request the request
     * @return ListSpaceQuotaDefinitionSpacesResponse
     * @throws Exception the exception
     */
    @GetMapping(Constants.V2_URL + "/spaces/quota-definitions/{spaceQuotaId}/spaces")
    public ListSpaceQuotaDefinitionSpacesResponse getListSpaceUsedSpaceQuotaDefinitions(@PathVariable String spaceQuotaId, HttpServletRequest request) throws Exception {
        LOGGER.info("getListSpaceUsedSpaceQuotaDefinitions Start : ");
        return spaceQuotaService.getListSpaceUsedSpaceQuotaDefinitions(spaceQuotaId, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 해당 공간에 설정된 할당량 정의를 삭제한다.
     *
     * @param spaceQuotaId The guid of the Space Quota Definition
     * @param spaceId The guid of the Space
     * @param quota Quota Info
     * @param request the request
     * @return boolean
     * @throws Exception the exception
     */
    @DeleteMapping(Constants.V2_URL + "/spaces/quota-definitions/{spaceQuotaId}/spaces/{spaceId}")
    public boolean getListSpaceUsedSpaceQuotaDefinitions(@PathVariable String spaceQuotaId, @PathVariable String spaceId,
                                                                                        @RequestBody Quota quota, HttpServletRequest request) throws Exception {
        LOGGER.info("getListSpaceUsedSpaceQuotaDefinitions Start : ");
        quota.setGuid(UUID.fromString(spaceQuotaId));
        quota.setSpaceGuid(UUID.fromString(spaceId));
        return spaceQuotaService.removeSpaceQuotaDefinitionsFromSpace(quota, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 공간 할당량 정의를 수정한다.
     *
     * @param spaceQuotaId The guid of the Space Quota Definition
     * @param quota Quota Info
     * @param request the request
     * @return UpdateSpaceQuotaDefinitionResponse
     * @throws Exception the exception
     */
    @PutMapping(Constants.V2_URL + "/spaces/quota-definitions/{spaceQuotaId}")
    public UpdateSpaceQuotaDefinitionResponse updateSpaceQuotaDefinitions(@PathVariable String spaceQuotaId, @RequestBody Quota quota,
                                                                             HttpServletRequest request) throws Exception {
        LOGGER.info("updateSpaceQuotaDefinitions Start : ");
        quota.setGuid(UUID.fromString(spaceQuotaId));
        return spaceQuotaService.updateSpaceQuotaDefinitions(quota, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

}
