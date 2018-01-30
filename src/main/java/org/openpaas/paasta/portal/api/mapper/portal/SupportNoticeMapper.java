package org.openpaas.paasta.portal.api.mapper.portal;

import org.openpaas.paasta.portal.api.config.service.surport.Portal;
import org.openpaas.paasta.portal.api.model.Support;

import java.util.List;

/**
 * Created by YJKim on 2016-07-28.
 */
@Portal
public interface SupportNoticeMapper {

    /**
     * 공지사항 목록 조회
     *
     * @param param Support
     * @return List<Support>
     */
    List<Support> getNoticeList(Support param);

    /**
     * 공지사항 상세정보 조회
     *
     * @param param Support
     * @return Support
     */
    Support getNotice(Support param);

    /**
     * 공지사항 등록
     *
     * @param param Support
     * @return int
     */
    int insertNotice(Support param);

    /**
     * 공지사항 수정
     *
     * @param param Support
     * @return int
     */
    int updateNotice(Support param);

    /**
     * 공지사항 삭제
     *
     * @param param Support
     * @return int
     */
    int deleteNotice(Support param);

}

