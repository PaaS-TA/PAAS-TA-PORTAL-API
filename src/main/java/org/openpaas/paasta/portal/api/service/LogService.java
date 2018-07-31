package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.ApplicationLog;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService extends Common {

    /**
     * 앱 로그 정보 가져오기(API)
     *
     * @param app    the app
     * @param client the client
     * @return ModelAndView model
     */
    //@HystrixCommand(commandKey = "getLog")
    public List getLog(App app, CloudFoundryClient client) {

        String sAppName = app.getName();
        List<ApplicationLog> list = client.getRecentLogs(sAppName);

        return list;
    }

}
