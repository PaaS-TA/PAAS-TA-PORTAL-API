package org.openpaas.paasta.portal.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.ApplicationLog;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 로그 서비스 - 앱-최근로그를 조회한다.
 *
 * @author 이인정
 * @version 1.0
 * @since 2016.7.11 최초작성
 */
@Service
public class LogService extends Common {

    /**
     * 앱 로그 정보 가져오기(API)
     *
     * @param app    the app
     * @param client the client
     * @return ModelAndView model
     */
    @HystrixCommand(commandKey = "getLog")
    public List getLog(App app, CloudFoundryClient client) {

        String sAppName = app.getName();
        List<ApplicationLog> list = client.getRecentLogs(sAppName);

        return list;
    }

}
