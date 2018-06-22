package org.openpaas.paasta.portal.api.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.cloudfoundry.client.v2.organizationquotadefinitions.*;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
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
 * 할당량 관리(조직) 컨트롤러 - 조직의 할당량에 대한 리스트,조회,등록,수정,삭제,지정 기능을 제공한다.
 *
 * @author 최윤석
 * @version 1.0
 * @since 2018.4.11 최초작성
 */
@RestController
@Transactional
public class OrgQuotaController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrgQuotaController.class);

    @Autowired
    OrgQuotaService orgQuotaService; // The Org Quota service.

    /**
     * 조직 할당량 리스트를 조회한다.
     *
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "getOrgQuotaDefinitions")
    @GetMapping(Constants.V2_URL + "/orgs/quota-definitions")
    public ListOrganizationQuotaDefinitionsResponse getOrgQuotaDefinitions(HttpServletRequest request) throws Exception {
        LOGGER.info("getOrgQuotaDefinitions Start : ");
        return orgQuotaService.getOrgQuotaDefinitionsList(request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 특정 조직의 할당량 정의를 조회한다.
     *
     * @param quotaId The guid of the Organization Quota Definition
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "getOrgQuotaDefinition")
    @GetMapping(Constants.V2_URL + "/orgs/quota-definitions/{quotaId}")
    public GetOrganizationQuotaDefinitionResponse getOrgQuotaDefinition(@PathVariable String quotaId, HttpServletRequest request) throws Exception {
        LOGGER.info("getOrgQuotaDefinition Start : ");
        return orgQuotaService.getOrgQuotaDefinitions(quotaId ,request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 조직 할당량 리스트를 등록한다.
     *
     * @param request the request
     * @return ModelAndView model
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "createOrgQuotaDefinitions")
    @PostMapping(Constants.V2_URL + "/orgs/quota-definitions")
    public CreateOrganizationQuotaDefinitionResponse createOrgQuotaDefinitions(@RequestBody Quota quota, HttpServletRequest request) throws Exception {
        LOGGER.info("createOrgQuotaDefinitions Start : ");
        return orgQuotaService.createOrgQuotaDefinitions(quota, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 해당 조직 할당량 정의를 수정한다.
     *
     * @param quotaId The guid of the Organization Quota Definition
     * @param quota Quota Info
     * @param request the request
     * @return UpdateOrganizationQuotaDefinitionResponse Response Object
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "updateOrgQuotaDefinitions")
    @PutMapping(Constants.V2_URL + "/orgs/quota-definitions/{quotaId}")
    public UpdateOrganizationQuotaDefinitionResponse updateOrgQuotaDefinitions(@PathVariable String quotaId, @RequestBody Quota quota, HttpServletRequest request) throws Exception {
        LOGGER.info("updateOrgQuotaDefinitions Start : ");
        quota.setGuid(UUID.fromString(quotaId));
        return orgQuotaService.updateOrgQuotaDefinitions(quota, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 해당 조직 할당량 정의를 삭제한다.
     *
     * @param quotaId The guid of the Organization Quota Definition
     * @param request the request
     * @return UpdateOrganizationQuotaDefinitionResponse Response Object
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "deleteOrgQuotaDefinitions")
    @DeleteMapping(Constants.V2_URL + "/orgs/quota-definitions/{quotaId}")
    public DeleteOrganizationQuotaDefinitionResponse deleteOrgQuotaDefinitions(@PathVariable String quotaId, HttpServletRequest request) throws Exception {
        LOGGER.info("deleteOrgQuotaDefinitions Start : ");
        return orgQuotaService.deleteOrgQuotaDefinitions(quotaId, request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    /**
     * 해당 조직 할당량 정의를 지정한다.
     *
     * @param quota Quota Info
     * @return UpdateOrganizationQuotaDefinitionResponse Response Object
     * @throws Exception the exception
     */
    @HystrixCommand(fallbackMethod = "setOrgQuotaDefinitions")
    @PutMapping(Constants.V2_URL + "/orgs/quota-definitions/associations")
    public boolean setOrgQuotaDefinitions(@RequestBody Quota quota, HttpServletRequest request) throws Exception {
        // Name : 사용자가 입력하는 값이기 떄문에 URL 값으로 받지 않음
        LOGGER.info("setOrgQuotaDefinitions Start : ");
         orgQuotaService.setOrgQuotaDefinitions(quota);
        return true;
    }
}
