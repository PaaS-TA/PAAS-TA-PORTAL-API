package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.mapper.portal.WebIdeUserMapper;
import org.openpaas.paasta.portal.api.model.WebIdeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * WEB IDE 관리 컨트롤러 - WEB IDE 신청자를 관리하는 컨트롤러이다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Transactional
@Service
public class WebIdeUserService extends Common {

 //   private  static final Logger LOGGER = LoggerFactory.getLogger(WebIdeUserService.class);

    @Autowired
    private WebIdeUserMapper webIdeUserMapper;


    /**
     * WEB IDE 사용자 정보를 조회한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    public WebIdeUser getUser(WebIdeUser webIdeUser)  {

        return webIdeUserMapper.getUser(webIdeUser);

    }

    /**
     * WEB IDE 사용자를 저장한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    public void insertUser(WebIdeUser webIdeUser) {

        webIdeUserMapper.insertUser(webIdeUser);

    }

    /**
     * WEB IDE 사용자를 수정한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    public void updateUser(WebIdeUser webIdeUser) {

        webIdeUserMapper.updateUser(webIdeUser);

    }

    /**
     * WEB IDE 사용자를 삭제한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    public void deleteUser(WebIdeUser webIdeUser)  {

       webIdeUserMapper.deleteUser(webIdeUser);

    }

    /**
     * WEB IDE 사용자 리스트를 조회한다.
     *
     * @param webIdeUser the webIdeUser
     * @return ModelAndView model
     */
    public List<WebIdeUser> getList(WebIdeUser webIdeUser)  {

        return webIdeUserMapper.getList(webIdeUser);

    }

}
