package org.openpaas.paasta.portal.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


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
