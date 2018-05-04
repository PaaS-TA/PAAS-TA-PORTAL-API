package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.domain.CloudOrganization;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.util.JsonUtil;
import org.cloudfoundry.client.v2.organizations.OrganizationResource;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Usage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * 사용량 조회 기능을 구현한 서비스 클래스이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.22 최초작성
 */
@Transactional
@Service
public class UsageService {

    private final OrgService orgService;
    private final SpaceService spaceService;

    @Value("${cloudfoundry.authorization}")
    private String cfAuthorizationHeaderKey;

    @Value("${abacus.url}")
    private String abacusUrl;


    @Autowired
    public UsageService(OrgService orgService, SpaceService spaceService) {
        this.orgService = orgService;
        this.spaceService = spaceService;
    }


    /**
     * 사용량 조직을 조회한다.
     *
     * @param req HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getUsageOrganizationList(HttpServletRequest req) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<OrganizationResource> organizationList = orgService.getOrgs(req.getHeader(cfAuthorizationHeaderKey));

        organizationList.stream().collect(toList()).forEach(
        	organization -> resultList.add(new HashMap<String, Object>() {
				private static final long serialVersionUID = 3768241859823289161L;
				{
		            put("name", organization.getEntity().getName());
		            put("value", organization.getEntity().getName());
		            put("guid", organization.getMetadata().getId());
				}
			})
        );

        return new HashMap<String, Object>() {
			private static final long serialVersionUID = 8001794184100601997L;
			{
        		put("list", resultList);
        	}
        };
    }


    /**
     * 사용량 공간을 조회한다.
     *
     * @param param Usage(모델클래스)
     * @param req   HttpServletRequest(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getUsageSpaceList(Usage param, HttpServletRequest req) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<SpaceResource> spaceList = spaceService.getSpaces(new Org() {{
            setOrgName(param.getOrgName());
        }}, req.getHeader(cfAuthorizationHeaderKey)).getResources();

        spaceList.stream().collect(toList()).forEach(spaceResource -> resultList.add(new HashMap<String, Object>() {{
            put("name", spaceResource.getEntity().getName());
            put("value", spaceResource.getEntity().getName());
            put("guid", spaceResource.getMetadata().getId());
        }}));

        return new HashMap<String, Object>() {{
            put("list", resultList);
        }};
    }


    /**
     * 사용량을 조회한다.
     *
     * @param param Usage(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> getUsageSearchList(Usage param) throws Exception {
        String reqUrl = abacusUrl + "/org/{org_id}/space/{space_id}/from/{from_month}/to/{to_month}";

        return new HashMap<String, Object>() {{
            put("RESULT_ABACUS", JsonUtil.convertJsonToMap(new RestTemplate().getForObject(reqUrl, String.class, new HashMap<String, Object>() {{
                put("org_id", param.getOrgGuid());
                put("space_id", param.getSpaceGuid());
                put("from_month", param.getFromMonth());
                put("to_month", param.getToMonth());
            }})));
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }
}
