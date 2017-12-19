package org.openpaas.paasta.portal.api.mapper.cc;

import org.openpaas.paasta.portal.api.config.service.surport.Cc;

import java.util.List;

/**
 * Mybatis Mapper Interface 클래스로 카탈로그 관련 메소드 정의만 한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.01 최초작성
 */
@Cc
public interface CatalogCcMapper {

    /**
     * 도메인 아이디를 조회한다.
     *
     * @param domainName 도메인명(String)
     * @return 도메인 아이디(Integer)
     */
    int getDomainId(String domainName);

    /**
     * 라우트명 목록을 조회한다.
     *
     * @param domainId 도메인 아이디(Integer)
     * @return List(자바클래스)
     */
    List getRouteHostNameList(int domainId);
}
