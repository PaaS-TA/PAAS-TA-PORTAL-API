package org.openpaas.paasta.portal.api.mapper.portal;

import org.apache.ibatis.annotations.Param;
import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.Catalog;

import java.util.List;

/**
 * org.openpaas.paasta.portal.api.mapper
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.07.04
 */
@Portal
public interface CatalogMapper {

    /**
     * 빌드팩 카탈로그 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return List(자바클래스)
     */
    List<Catalog> getBuildPackCatalogList(Catalog param);

    /**
     * 서비스팩 카탈로그 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return List(자바클래스)
     */
    List<Catalog> getServicePackCatalogList(Catalog param);

    /**
     * 빌드팩 카탈로그 개수를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int getBuildPackCatalogCount(Catalog param);

    /**
     * 서비스팩 카탈로그 개수를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int getServicePackCatalogCount(Catalog param);

    /**
     * 빌드팩 카탈로그를 등록한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int insertBuildPackCatalog(Catalog param);

    /**
     * 서비스팩 카탈로그를 등록한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int insertServicePackCatalog(Catalog param);

    /**
     * 빌드팩 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int updateBuildPackCatalog(Catalog param);

    /**
     * 서비스팩 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int updateServicePackCatalog(Catalog param);

    /**
     * 빌드팩 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int deleteBuildPackCatalog(Catalog param);

    /**
     * 서비스팩 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int deleteServicePackCatalog(Catalog param);

    /**
     * 빌드팩 카탈로그 삭제 유효성 확인을 한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int getCheckDeleteBuildPackCatalogCount(Catalog param);

    /**
     * 서비스팩 카탈로그 삭제 유효성 확인을 한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int getCheckDeleteServicePackCatalogCount(Catalog param);

    /**
     * 스타터 카탈로그 개수를 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int getStarterCatalogCount(Catalog param);

    /**
     * 스타터명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return List(자바클래스)
     */
    List<Catalog> getStarterNamesList(Catalog param);

    /**
     * 빌드팩명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return List(자바클래스)
     */
    List<Catalog> getBuildPackNamesList(Catalog param);

    /**
     * 서비스팩명 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return List(자바클래스)
     */
    List<Catalog> getServicePackNamesList(Catalog param);

    /**
     * 선택한 서비스팩 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return List(자바클래스)
     */
    List<Integer> getSelectedServicePackList(Catalog param);

    /**
     * 스타터 카탈로그 상세 조회를 한다.
     *
     * @param param Catalog(모델클래스)
     * @return the one starter catalog
     */
    Catalog getOneStarterCatalog(Catalog param);

    /**
     * 스타터 카탈로그 최대값을 조회한다.
     *
     * @return Integer(자바클래스)
     */
    int getStarterCatalogMaxNumber();

    /**
     * 스타터 카탈로그를 등록한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int insertStarterCatalog(Catalog param);

    /**
     * 선택한 서비스팩 카탈로그를 등록한다.
     *
     * @param selectedServicePackCategoryNoListValue the selected service pack category no list value
     * @return Integer(자바클래스)
     */
    int insertSelectedServicePackList(int selectedServicePackCategoryNoListValue);

    /**
     * 업데이트를 위한 선택한 서비스팩 카탈로그를 등록한다.
     *
     * @param param Catalog(모델클래스)
     * @param selectedServicePackCategoryNoListValue the selected service pack category no list value
     * @return Integer(자바클래스)
     */
    int insertSelectedServicePackListForUpdate(@Param("param") Catalog param, @Param("selectedServicePackCategoryNoListValue") int selectedServicePackCategoryNoListValue);

    /**
     * 스타터 카탈로그를 수정한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int updateStarterCatalog(@Param("param") Catalog param);

    /**
     * 스타터 카탈로그를 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int deleteStarterCatalog(Catalog param);

    /**
     * 선택한 서비스팩을 삭제한다.
     *
     * @param param Catalog(모델클래스)
     * @return Integer(자바클래스)
     */
    int deleteSelectedServicePackList(Catalog param);

    /**
     * 카탈로그 내역 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return List(자바클래스)
     */
    List<Catalog> getCatalogHistoryList(Catalog param);

    /**
     * 카탈로그 내역을 등록한다.
     *
     * @param param Catalog(모델클래스)
     */
    void insertCatalogHistory(Catalog param);

    /**
     * 카탈로그 스타터 빌드팩 관계 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return List(자바클래스)
     */
    List<Catalog> getCatalogStarterRelationBuildPackList(Catalog param);

    /**
     * 카탈로그 스타터 서비스팩 관계 목록을 조회한다.
     *
     * @param param Catalog(모델클래스)
     * @return List(자바클래스)
     */
    List<Catalog> getCatalogStarterRelationServicePackList(Catalog param);


}
