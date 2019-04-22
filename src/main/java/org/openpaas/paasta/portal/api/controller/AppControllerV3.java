package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.service.AppServiceV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AppControllerV3 extends Common {

    @Autowired
    private AppServiceV3 appService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppControllerV3.class);

}
