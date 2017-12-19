package org.openpaas.paasta.portal.api.mapper;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.Support;

import java.util.List;

/**
 * SupportBoardMapper.java
 * Mybatis Mapper Inteface 클래스로 커뮤니티 게시판 관련 메소드 정의만 한다
 *
 * @author yjkim
 * @version 1.0
 * @since 2016.07.28
 */

@Portal
public interface SupportBoardMapper {

    /**
     * 게시판 게시글 목록 조회
     *
     * @param param Support
     * @return List<Support>
     */
    List<Support> getBoardList(Support param);

    /**
     * 게시판 댓글 목록 조회
     *
     * @param param Support
     * @return List<Support>
     */
    List<Support> getBoardCommentList(Support param);

    /**
     * 게시판 게시글 상세정보 조회
     *
     * @param param Support
     * @return Support
     */
    Support getBoard(Support param);

    /**
     * 게시판 게시글 등록
     *
     * @param param Support
     * @return int
     */
    int insertBoard(Support param);

    /**
     * 게시판 게시글 등록시 groupNo 설정
     *
     * @param param Support
     * @return int
     */
    int setGroupNo(Support param);

    /**
     * 게시판 게시글 수정
     *
     * @param param Support
     * @return int
     */
    int updateBoard(Support param);

    /**
     * 게시판 게시글의 덧글 수 조회
     *
     * @param param Support
     * @return Support
     */
    Support getReplyNum(Support param);

    /**
     * 게시판 게시글 삭제
     *
     * @param param Support
     * @return int
     */
    int deleteBoard(Support param);

    /**
     * 게시판 게시글 삭제시 해당 글의 댓글 전부 삭제
     *
     * @param param Support
     * @return int
     */
    int deleteAllComments(Support param);

    /**
     * 게시판 댓글 등록
     *
     * @param param Support
     * @return int
     */
    int insertBoardComment(Support param);
    /**
     * 게시판 댓글 등록시 groupNo 설정
     *
     * @param param Support
     * @return int
     */
    int setCommentGroupNo(Support param);

    /**
     * 게시판 댓글 수정
     *
     * @param param Support
     * @return Support
     */
    int updateBoardComment(Support param);

    /**
     * 게시판 댓글의 댓글 수 조회
     *
     * @param param Support
     * @return Support
     */
    Support getCommentReplyNum(Support param);

    /**
     * 게시판 댓글 삭제
     *
     * @param param Support
     * @return int
     */
    int deleteBoardComment(Support param);


}

