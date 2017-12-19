package org.openpaas.paasta.portal.api.mapper.cc;

import org.openpaas.paasta.portal.api.config.service.surport.Cc;
import org.openpaas.paasta.portal.api.model.AdminMain;

import java.util.List;

/**
 * Mybatis Mapper Interface 클래스로 운영자 포탈 관리자 대시보드 메소드 정의만 한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.08 최초작성
 */
@Cc
public interface AdminMainCcMapper {

    /**
     * 전체 조직 수, 영역 수, APPLICATION 수, 사용자 수 목록을 조회한다.
     *
     * @param param AdminMain(모델클래스)
     * @return List(자바클래스)
     */
    List<AdminMain> getTotalCountList(AdminMain param);

    /**
     * 전체 조직 통계 목록을 조회한다.
     *
     * @param param AdminMain(모델클래스)
     * @return List(자바클래스)
     */
    List<AdminMain> getTotalOrganizationList(AdminMain param);

    /**
     * 전체 공간 통계 목록을 조회한다.
     *
     * @param param AdminMain(모델클래스)
     * @return List(자바클래스)
     */
    List<AdminMain> getTotalSpaceList(AdminMain param);
}
