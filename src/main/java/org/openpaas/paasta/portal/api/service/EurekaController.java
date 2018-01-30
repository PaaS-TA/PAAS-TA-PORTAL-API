package org.openpaas.paasta.portal.api.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mg on 2016-10-10.
 */
@RestController
@RequestMapping(value = {""})
public class EurekaController {

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Map<String, Object> getInfo() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", "portal-api");
        result.put("version", 2.0);
        result.put("description", "cloudfoundry Api, paasta");

        return result;
    }

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public Map<String, Object> getHealth() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", "UP");

        return result;
    }
}
