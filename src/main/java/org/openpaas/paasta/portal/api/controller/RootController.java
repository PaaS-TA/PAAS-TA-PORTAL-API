package org.openpaas.paasta.portal.api.controller;

import org.apache.wink.common.http.OPTIONS;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
public class RootController {


    @RequestMapping(value = "/", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    @ResponseBody
    public String root() throws Exception {

        return "PortalApi Started";
    }


    @RequestMapping(value = "/index", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    @ResponseBody
    public String index() throws Exception {

        return "PortalApi Started";
    }

}
