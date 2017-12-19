package org.openpaas.paasta.portal.api.mapper;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.CommonCode;

import java.util.List;

/**
 * Mybatis Mapper Interface 클래스로 공통코드 관련 메소드 정의만 한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.06.15 최초작성
 */
@Portal
public interface CommonCodeMapper {

    /**
     * 공통코드 목록을 조회한다.
     *
     * @param param CommonCode(모델클래스)
     * @return List(자바클래스)
     */
    List<CommonCode> getCommonCodeById(CommonCode param);

    /**
     * 공통코드 목록을 조회한다.
     *
     * @param param CommonCode(모델클래스)
     * @return List(자바클래스)
     */
    List<CommonCode> getCommonCodeGroup(CommonCode param);

    /**
     * 공통코드 그룹 개수를 조회한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Integer(자바클래스)
     */
    int getCommonCodeGroupCount(CommonCode param);

    /**
     * 공통코드 상세 조회를 한다.
     *
     * @param param CommonCode(모델클래스)
     * @return List(자바클래스)
     */
    List<CommonCode> getCommonCodeDetail(CommonCode param);

    /**
     * 공통코드 상세 개수를 조회한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Integer(자바클래스)
     */
    int getCommonCodeDetailCount(CommonCode param);

    /**
     * 공통코드 그룹을 등록한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Integer(자바클래스)
     */
    int insertCommonCodeGroup(CommonCode param);

    /**
     * 공통코드 상세를 등록한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Integer(자바클래스)
     */
    int insertCommonCodeDetail(CommonCode param);

    /**
     * 공통코드 그룹을 수정한다.
     *
     * @param commonCode the common code
     * @return Integer(자바클래스)
     */
    int updateCommonCodeGroup(CommonCode commonCode);

    /**
     * 공통코드 상세를 수정한다.
     *
     * @param commonCode the common code
     * @return Integer(자바클래스)
     */
    int updateCommonCodeDetail(CommonCode commonCode);

    /**
     * 공통코드 그룹을 삭제한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Integer(자바클래스)
     */
    int deleteCommonCodeGroup(CommonCode param);

    /**
     * 공통코드 상세를 삭제한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Integer(자바클래스)
     */
    int deleteCommonCodeDetail(CommonCode param);
}
