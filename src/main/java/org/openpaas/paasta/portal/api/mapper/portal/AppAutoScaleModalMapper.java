package org.openpaas.paasta.portal.api.mapper.portal;



import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.AppAutoScale;

import java.util.HashMap;
import java.util.List;

@Portal
public interface AppAutoScaleModalMapper {


    HashMap<String,Object> getAppAutoScaleInfo(String guid);

    List<AppAutoScale> getAppAutoScaleList(String guid);

    int insertAppAutoScale(HashMap<String,Object> appAutoScale);

    int updateAppAutoScale(HashMap<String,Object> appAutoScale);

    int deleteAppAutoScale(String guid);
}
