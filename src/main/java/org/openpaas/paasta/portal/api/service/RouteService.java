package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 라우트 서비스 - 라우트 존재여부를 체크한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.08.16
 */
@Transactional
@Service
class RouteService extends Common {

    /**
     * Gets check route exists.
     *
     * @param domain             the domain
     * @param route              the route
     * @param cloudFoundryClient the cloud foundry client
     * @return check route exists
     * @throws Exception the exception
     */
    Boolean getCheckRouteExists(String domain, String route, CloudFoundryClient cloudFoundryClient) throws Exception {
        return cloudFoundryClient.getRoutes(domain).stream().anyMatch(cloudRoute -> cloudRoute.toString().equals(route));
    }
}
