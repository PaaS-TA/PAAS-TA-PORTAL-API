package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.mapper.MenuMapper;
import org.openpaas.paasta.portal.api.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 메뉴 관리 기능을 구현한 서비스 클래스이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.29 최초작성
 */
@Transactional
@Service
public class MenuService {

    private final MenuMapper menuMapper;

    @Autowired
    public MenuService(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }


    /**
     * 메뉴 최대값을 조회한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getMenuMaxNoList(Menu param) {
        return new HashMap<String, Object>() {{
            put("MAX_NO", menuMapper.getMenuMaxNoList(param));
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 메뉴 목록을 조회한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getMenuList(Menu param) {
        HashMap<String, Object> resultMap = (HashMap<String, Object>) this.getRecursiveMenuList(param);

        return new HashMap<String, Object>() {{
            put("RESULT_DATA", new HashMap<String, Object>() {{
                put("id", 0);
                put("text", "ROOT");
                put("item", new ArrayList<Object>() {{
                            add(new HashMap<String, Object>() {{
                                put("id", -1);
                                put("text", "메뉴");
                                put("open", 1);
                                put("item", resultMap.get("item"));
                            }});
                        }}
                );
            }});

            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 메뉴 목록을 조회한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    private Map<String, Object> getRecursiveMenuList(Menu param) {
        List<HashMap<String, Object>> resultList = new ArrayList<>();

        // GET ROOT NODE
        List<Menu> menuList = menuMapper.getMenuList(new Menu() {{
            setParentNo((null != param) ? param.getNo() : 0);
        }});

        for (Menu menuModel : menuList) {
            HashMap<String, Object> menuItemMap = new HashMap<String, Object>() {{
                put("id", menuModel.getNo());
                put("text", menuModel.getMenuName());
            }};

            if (menuModel.getChildCount() > 0) {
                // GET CHILD NODE (RECURSIVE)
                menuItemMap.put("item", this.getRecursiveMenuList(menuModel).get("item"));
            }

            resultList.add(menuItemMap);
        }

        return new HashMap<String, Object>() {{
            put("item", resultList);
        }};
    }


    /**
     * 메뉴 상세를 조회한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getMenuDetail(Menu param) {
        return new HashMap<String, Object>() {{
            put("RESULT_DATA", menuMapper.getMenuDetail(param));
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 메뉴를 등록한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> insertMenu(Menu param) {
        menuMapper.insertMenu(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 메뉴를 수정한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> updateMenu(Menu param) {
        menuMapper.updateMenu(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 메뉴를 삭제한다.
     *
     * @param param Menu(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> deleteMenu(Menu param) {
        menuMapper.deleteMenu(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 사용자 메뉴 목록을 조회한다.
     *
     * @return Map(자바클래스)
     */
    public Map<String, Object> getUserMenuList() {
        return new HashMap<String, Object>() {{
            put("list", menuMapper.getMenuList(new Menu() {{
                setParentNo(0);
                setUseYn(Constants.USE_YN_Y);
            }}));
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};

    }
}
