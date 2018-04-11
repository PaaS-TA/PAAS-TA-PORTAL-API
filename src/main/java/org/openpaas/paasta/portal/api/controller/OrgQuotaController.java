package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v2.organizationquotadefinitions.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Quota;
import org.openpaas.paasta.portal.api.service.OrgQuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 할당량 관리(조직,공간) 컨트롤러 - 조직/공간의 할당량에 대한 리스트,조회,등록,수정,삭제 기능을 제공한다.
 *
 * @author 최윤석
 * @version 1.0
 * @since 2018.4.11 최초작성
 */
@RestController
@Transactional
public class OrgQuotaController extends Common {

    private final String V2_URL = "/v2";

    private static final Logger LOGGER = LoggerFactory.getLogger(OrgQuotaController.class);

    /**
     * The Org service.
     */
    @Autowired
    OrgQuotaService orgQuotaService;

    // 조직 할당량 관리 //

    /**
     * 조직 할당량 리스트를 조회한다.
     *
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @GetMapping(V2_URL + "/orgs/quotaDefinitions")
    public ListOrganizationQuotaDefinitionsResponse listOrgQuotaDefinitions(HttpServletRequest request) throws Exception {
        LOGGER.info("summary Start : ");
        return orgQuotaService.getOrgQuotaDefinitionsList(request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 특정 조직의 할당량 정의를 조회한다.
     *
     * @param guid The guid of the Organization Quota Definition
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @GetMapping(V2_URL + "/orgs/quotaDefinitions/{guid}")
    public GetOrganizationQuotaDefinitionResponse getOrgQuotaDefinitions(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("summary Start : ");
        return orgQuotaService.getOrgQuotaDefinitions(guid ,request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 조직 할당량 리스트를 등록한다.
     *
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @PostMapping(V2_URL + "/orgs/quotaDefinitions")
    public CreateOrganizationQuotaDefinitionResponse createOrgQuotaDefinitions(@RequestBody Quota quota, HttpServletRequest request) throws Exception {
        LOGGER.info("summary Start : ");
        return orgQuotaService.createOrgQuotaDefinitions(quota, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 해당 조직 할당량 정의를 수정한다.
     *
     * @param guid The guid of the Organization Quota Definition
     * @param quota Quota Info
     * @param request the request
     * @return UpdateOrganizationQuotaDefinitionResponse Response Object
     * @throws Exception the exception
     */
    @PutMapping(V2_URL + "/orgs/quotaDefinitions/{guid}")
    public UpdateOrganizationQuotaDefinitionResponse updateOrgQuotaDefinitions(@PathVariable String guid, @RequestBody Quota quota, HttpServletRequest request) throws Exception {
        LOGGER.info("summary Start : ");
        quota.setGuid(UUID.fromString(guid));
        return orgQuotaService.updateOrgQuotaDefinitions(quota, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 해당 조직 할당량 정의를 삭제한다.
     *
     * @param guid The guid of the Organization Quota Definition
     * @param request the request
     * @return UpdateOrganizationQuotaDefinitionResponse Response Object
     * @throws Exception the exception
     */
    @DeleteMapping(V2_URL + "/orgs/quotaDefinitions/{guid}")
    public DeleteOrganizationQuotaDefinitionResponse deleteOrgQuotaDefinitions(@PathVariable String guid, HttpServletRequest request) throws Exception {
        LOGGER.info("summary Start : ");
        return orgQuotaService.deleteOrgQuotaDefinitions(guid, request.getHeader(AUTHORIZATION_HEADER_KEY));

        /*
            "entity": {
        "error": null,
        "error_details": null,
        "guid": "35c690de-bbff-4384-9415-dee4e5767ad3",
        "status": "queued"
            },
         */
    }


    // 공간 할당량 관리 //


}
