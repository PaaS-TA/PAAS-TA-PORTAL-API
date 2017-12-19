package org.openpaas.paasta.portal.api.mapper;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.Support;

import java.util.List;

/**
 * DocumentsMapper.java
 * Mybatis Mapper Inteface 클래스로 커뮤니티 게시판 관련 메소드 정의만 한다
 *
 * @author yjkim
 * @version 1.0
 * @since 2016.07.28
 */

@Portal
public interface DocumentsMapper {

    /**
     * 문서 목록 조회
     *
     * @param param Support
     * @return List<Support>
     */
    List<Support> getDocumentsList(Support param);

    /**
     * 문서 상세정보 조회
     *
     * @param param Support
     * @return Support
     */
    Support getDocument(Support param);

    /**
     * 문서 등록
     *
     * @param param Support
     * @return int
     */
    int insertDocument(Support param);

    /**
     * 문서 수정
     *
     * @param param Support
     * @return int
     */
    int updateDocument(Support param);

    /**
     * 문서 삭제
     *
     * @param param Support
     * @return int
     */
    int deleteDocument(Support param);

}

