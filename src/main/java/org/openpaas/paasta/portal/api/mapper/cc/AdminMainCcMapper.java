package org.openpaas.paasta.portal.api.mapper.cc;

import org.openpaas.paasta.portal.api.config.service.surport.Cc;
import org.openpaas.paasta.portal.api.model.AdminMain;

import java.util.List;

/**
 * org.openpaas.paasta.portal.api.mapper.cc
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.08
 */
@Cc
public interface AdminMainCcMapper {

    List<AdminMain> getTotalCountList(AdminMain param);

    List<AdminMain> getTotalOrganizationList(AdminMain param);

    List<AdminMain> getTotalSpaceList(AdminMain param);
}
