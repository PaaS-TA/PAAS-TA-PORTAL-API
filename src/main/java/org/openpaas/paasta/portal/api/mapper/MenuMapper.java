package org.openpaas.paasta.portal.api.mapper;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.Menu;

import java.util.List;

/**
 * Mybatis Mapper Interface 클래스로 메뉴 관련 메소드 정의만 한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.29 최초작성
 */
@Portal
public interface MenuMapper {

    /**
     * 메뉴 최대값을 조회한다.
     *
     * @param param Menu(모델클래스)
     * @return Integer(자바클래스)
     */
    int getMenuMaxNoList(Menu param);

    /**
     * 메뉴 목록을 조회한다.
     *
     * @param param Menu(모델클래스)
     * @return List(자바클래스)
     */
    List<Menu> getMenuList(Menu param);

    /**
     * 메뉴 상세 조회를 한다.
     *
     * @param param Menu(모델클래스)
     * @return List(자바클래스)
     */
    List<Menu> getMenuDetail(Menu param);

    /**
     * 메뉴를 등록한다.
     *
     * @param param Menu(모델클래스)
     * @return Integer(자바클래스)
     */
    int insertMenu(Menu param);

    /**
     * 메뉴를 수정한다.
     *
     * @param param Menu(모델클래스)
     * @return Integer(자바클래스)
     */
    int updateMenu(Menu param);

    /**
     * 메뉴를 삭제한다.
     *
     * @param param Menu(모델클래스)
     * @return Integer(자바클래스)
     */
    int deleteMenu(Menu param);
}
