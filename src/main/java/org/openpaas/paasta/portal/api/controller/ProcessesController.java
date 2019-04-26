package org.openpaas.paasta.portal.api.controller;


import org.cloudfoundry.client.v3.processes.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Process;
import org.openpaas.paasta.portal.api.service.ProcessesServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

public class ProcessesController extends Common {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////                            * CLOUD FOUNDRY CLIENT API VERSION 3                                      //////
    //////             Document : https://v3-apidocs.cloudfoundry.org/version/3.69.0/#processes                  //////
    //////                                     Not-implemented                                                  //////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    ProcessesServiceV3 processesServiceV3;

    /**
     * process 정보를 가져온다.
     * @param  processId    the process id
     * @param  token    user token
     * @return GetProcessResponse
     * 권한 : 사용자권한
     */
    @GetMapping(Constants.V3_URL+"/processes/{processId:.+}")
    public GetProcessResponse getProcess(@PathVariable String processId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
       return processesServiceV3.getProcess(processId, token);
    }

    /**
     * process statistics 정보를 가져온다.
     * @param  processId    the process id
     * @param  token    user token
     * @return GetProcessStatisticsResponse
     * 권한 : 사용자권한
     */
    @GetMapping(Constants.V3_URL+"/processes/{processId:.+}")
    public GetProcessStatisticsResponse getProcessStatistics(@PathVariable String processId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return processesServiceV3.getProcessStatistics(processId, token);
    }

    /**
     * process 리스트를 가져온다.
     * @param  token    user token
     * @return GetProcessStatisticsResponse
     * 권한 : 사용자권한
     */
    @GetMapping(Constants.V3_URL+"/processes")
    public ListProcessesResponse listProcesses(@RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return processesServiceV3.listProcesses(token);
    }

    /**
     * process 스케일을 수정한다.
     * @param  process    the process
     * @param  token    user token
     * @return GetProcessStatisticsResponse
     * 권한 : 사용자권한
     */
    @PostMapping(Constants.V3_URL+"/processes")
    public ScaleProcessResponse scaleProcess(@RequestBody Process process, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
      return processesServiceV3.scaleProcess(process, token);
    }

}
