package org.openpaas.paasta.portal.api.mapper.portal;

import org.openpaas.paasta.portal.api.config.service.surport.Cc;
import org.openpaas.paasta.portal.api.model.App;

import java.util.List;

/**
 * Login Mapper
 *
 * @author nawkm
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Cc
public interface LoginMapper {

    /**
     * 총 사용자 수 조회
     *
     * @return int getTotalUserCount()
     */
    int getTotalUserCount();

    /**
     * Gets list apps.
     *
     * @param userid the userid
     * @return the list apps
     */
    List<App> getListApps(String userid);
}