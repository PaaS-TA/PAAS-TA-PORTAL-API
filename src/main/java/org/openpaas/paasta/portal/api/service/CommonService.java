package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 공통서비스 - 사용자 정의 에러메세지 처리
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.07.01
 */
@Service
public class CommonService {

    private final MessageSource messageSource;

    /**
     * Instantiates a new Common service.
     *
     * @param messageSource the message source
     */
    @Autowired
    public CommonService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    /**
     * 사용자 정의 에러메시지 전송
     *
     * @param res        the res
     * @param httpStatus the http status
     * @param reqMessage the req message
     * @throws Exception the exception
     */
    void getCustomSendError(HttpServletResponse res, HttpStatus httpStatus, String reqMessage) throws Exception {
        String[] reqMessageArray = reqMessage.split(Constants.DUPLICATION_SEPARATOR);

        if (reqMessageArray.length > 1) {
            res.sendError(httpStatus.value(), this.getCustomMessage(reqMessageArray[0])
                    + " " + Constants.DUPLICATION_SEPARATOR + " " + reqMessageArray[1]);

        } else {
            res.sendError(httpStatus.value(), this.getCustomMessage(reqMessage));
        }
    }


    /**
     * 사용자 정의 에러메시지 조회
     *
     * @param reqMessage the req message
     * @return custom message
     * @throws Exception the exception
     */
    String getCustomMessage(String reqMessage) throws Exception {
        return messageSource.getMessage(reqMessage, null, Locale.KOREA);
    }
}
