package org.openpaas.paasta.portal.api.controller;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.service.ExternalAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 앱 컨트롤러 - 외부에서 호출해오는 API를 컨트롤
 *
 * @author 이인정
 * @version 1.0
 * @since 2017.4.4 최초작성
 */
@RestController
@Transactional
@JsonIgnoreProperties(ignoreUnknown = true)
@RequestMapping(value = {"/external"})
public class ExternalCallAutoController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalCallAutoController.class);

    private final ExternalAppService externalAppService;

    @Autowired
    public ExternalCallAutoController(ExternalAppService externalAppService) {
        this.externalAppService = externalAppService;
    }
    /**
     * 앱 인스턴스를 변경한다.
     *
     * @param hashApp     the app
     *                appName
     *                appGuid
     *                cpuUsage
     *                memoryUsage
     *                Action
     *                cause
     * @return ResponseEntity model
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/app/updateApp"}, method = RequestMethod.POST)
    public ResponseEntity updateAppExternalCall(@RequestBody HashMap hashApp) throws Exception {

        if(null!=hashApp){
            List<String> lstKey = new ArrayList(hashApp .keySet());
            List<Object[]> lstValue = new ArrayList(hashApp .values());
            for(int i = 0 ; i < lstKey.size(); i++){
                LOGGER.info("lstKey.get("+i+")"+lstKey.get(i)+"::: lstValue.get("+i+")"+ lstValue.get(i));
                System.out.println("lstKey.get("+i+")"+lstKey.get(i)+"::: lstValue.get("+i+")"+ lstValue.get(i));
//                map.put(lstKey.get(i), lstValue.get(i));
            }

        }
        return externalAppService.callUpdategAppInfo(hashApp);
    }

}
