package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class RootController extends Common {

    //////////////////////////////////////////////////////////////////////
    //////   * CLOUD FOUNDRY CLIENT API VERSION 2                   //////
    //////   Document : http://apidocs.cloudfoundry.org             //////
    //////////////////////////////////////////////////////////////////////


    @RequestMapping(value = {"/", "/info", "/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public Map index() throws Exception {

        Map map = new HashMap();
        Map info = new HashMap();

        info.put("cf-api", apiTarget);
        info.put("uaa-api", uaaTarget);
        info.put("monitoring-api", monitoringApiTarget);
        map.put("info", info);
        map.put("version", "v3");
        map.put("name", "PaaS-TA API");
        return map;
    }

}
