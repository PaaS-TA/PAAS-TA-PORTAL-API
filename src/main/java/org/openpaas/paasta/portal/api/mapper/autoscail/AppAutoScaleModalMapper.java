package org.openpaas.paasta.portal.api.mapper.autoscail;

import org.openpaas.paasta.portal.api.config.service.surport.AutoScailing;
import org.openpaas.paasta.portal.api.model.AppAutoScale;

import java.util.HashMap;
import java.util.List;
/**
 * Mybatis Mapper Interface 클래스로 오토스케일링 데이터 맵핑만 한다.
 *
 * @author 이인정
 * @version 1.0
 * @since 2016.09.08 최초작성
 */
@AutoScailing
public interface AppAutoScaleModalMapper {

    HashMap<String,Object> getAppAutoScaleInfo(String guid);

    List<AppAutoScale> getAppAutoScaleList(String guid);

    int insertAppAutoScale(HashMap<String,Object> appAutoScale);

    int updateAppAutoScale(HashMap<String,Object> appAutoScale);

    int deleteAppAutoScale(String guid);

}
