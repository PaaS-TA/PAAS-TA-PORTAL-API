package org.openpaas.paasta.portal.api.service;


import org.cloudfoundry.client.v3.processes.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.Process;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ProcessServiceV3 extends Common {
    private final Logger LOGGER = getLogger(this.getClass());


    /**
     * process 정보를 가져온다.
     * @param  processId    the process id
     * @param  token    user token
     * @return GetProcessResponse
     * 권한 : 사용자권한
     */
    public GetProcessResponse getProcess(String processId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient();
        return reactorCloudFoundryClient.processes().get(GetProcessRequest.builder().processId(processId).build()).block();
    }

    /**
     * process statistics 정보를 가져온다.
     * @param  processId    the process id
     * @param  token    user token
     * @return GetProcessStatisticsResponse
     * 권한 : 사용자권한
     */
    public GetProcessStatisticsResponse getProcessStatistics(String processId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient();
        return reactorCloudFoundryClient.processes().getStatistics(GetProcessStatisticsRequest.builder().processId(processId).build()).block();
    }

    /**
     * process 리스트를 가져온다.
     * @param  token    user token
     * @return GetProcessStatisticsResponse
     * 권한 : 사용자권한
     */
    public ListProcessesResponse listProcesses(String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient();
        return reactorCloudFoundryClient.processes().list(ListProcessesRequest.builder().build()).block();
    }

    /**
     * process 스케일을 수정한다.
     * @param  process    the process
     * @param  token    user token
     * @return GetProcessStatisticsResponse
     * 권한 : 사용자권한
     */
    public ScaleProcessResponse scaleProcess(Process process, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient =  cloudFoundryClient();
        return reactorCloudFoundryClient.processes().scale(ScaleProcessRequest.builder().processId(process.getId()).diskInMb(process.getDiskInMb()).instances(process.getInstances()).memoryInMb(process.getMemoryInMb()).build()).block();
    }



}
