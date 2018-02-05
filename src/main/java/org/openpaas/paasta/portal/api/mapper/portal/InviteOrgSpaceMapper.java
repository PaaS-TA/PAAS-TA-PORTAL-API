package org.openpaas.paasta.portal.api.mapper;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.InviteOrgSpace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Login Mapper
 *
 * @author nawkm
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Portal
public interface InviteOrgSpaceMapper {

    /**
     * 조직과 공간의 초대 정보를 등록한다.
     *
     * @return int insert()
     */
    int insertInviteOrgSpace(HashMap map);
    int updateOrgSpaceUser(HashMap map);
    List<InviteOrgSpace> selectOrgSpaceUser(InviteOrgSpace inviteOrgSpace);
    List<InviteOrgSpace> getUsersByInvite(InviteOrgSpace inviteOrgSpace);
    int updateInviteY(Map map);
    int updateAccessCnt(Map map);
    List selectInviteInfo(Map map);
    int updateOrgSpaceUserToken(HashMap map);
    int deleteOrgSpaceUserToken(Map map);

}