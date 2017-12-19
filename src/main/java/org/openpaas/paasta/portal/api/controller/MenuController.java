package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.model.Menu;
import org.openpaas.paasta.portal.api.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 메뉴 목록 조회, 등록, 삭제 등 메뉴 관리의 API 를 호출 받는 컨트롤러이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.29 최초작성
 */
@RestController
@RequestMapping(value = {"/menu"})
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    /**
     * 메뉴 최대값을 조회한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getMenuMaxNoList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getMenuMaxNoList(@RequestBody Menu param) {
        return menuService.getMenuMaxNoList(param);
    }


    /**
     * 메뉴를 조회한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getMenuList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getMenuList(@RequestBody Menu param) {
        return menuService.getMenuList(param);
    }


    /**
     * 메뉴를 상세 조회한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getMenuDetail"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getMenuDetail(@RequestBody Menu param) {
        return menuService.getMenuDetail(param);
    }


    /**
     * 메뉴를 등록한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/insertMenu"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> insertMenu(@RequestBody Menu param) {
        return menuService.insertMenu(param);
    }


    /**
     * 메뉴를 수정한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/updateMenu"}, method = RequestMethod.PUT, consumes = "application/json")
    public Map<String, Object> updateMenu(@RequestBody Menu param) {
        return menuService.updateMenu(param);
    }


    /**
     * 메뉴를 삭제한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/deleteMenu"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> deleteMenu(@RequestBody Menu param) {
        return menuService.deleteMenu(param);
    }


    /**
     * 사용자 메뉴를 조회한다.
     *
     * @return Map(자바클래스)
     */
    @RequestMapping(value = {"/getUserMenuList"}, method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> getUserMenuList() {
        return menuService.getUserMenuList();
    }
}
