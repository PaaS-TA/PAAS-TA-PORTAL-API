package org.openpaas.paasta.portal.api.service;

//import org.openpaas.paasta.portal.api.mapper.cc.AdminMainCcMapper;
import org.openpaas.paasta.portal.api.model.AdminMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 운영자 포탈 관리자 대시보드 기능을 구현한 서비스 클래스이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.08 최초작성
 */
@Transactional
@Service
public class AdminMainService {

//    private final AdminMainCcMapper adminMainCcMapper;

//    @Autowired
//    public AdminMainService(AdminMainCcMapper adminMainCcMapper) {
//        this.adminMainCcMapper = adminMainCcMapper;
//    }


    /**
     * 전체 조직 수, 영역 수, APPLICATION 수, 사용자 수 목록을 조회한다.
     *
     * @param param AdminMain(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getTotalCountList(AdminMain param) {
        return new HashMap<String, Object>() {{
//            put("list", adminMainCcMapper.getTotalCountList(param));
            put("list","");
        }};
    }


    /**
     * 전체 조직 통계 목록을 조회한다.
     *
     * @param param AdminMain(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getTotalOrganizationList(AdminMain param) {
        return new HashMap<String, Object>() {{
//            put("list", adminMainCcMapper.getTotalOrganizationList(param));
            put("list","");
        }};
    }


    /**
     * 전체 공간 통계 목록을 조회한다.
     *
     * @param param AdminMain(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getTotalSpaceList(AdminMain param) {
        return new HashMap<String, Object>() {{
//            put("list", adminMainCcMapper.getTotalSpaceList(param));
            put("list","");
        }};
    }
}
