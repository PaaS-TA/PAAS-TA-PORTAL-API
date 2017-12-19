package org.openpaas.paasta.portal.api.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Map;

/**
 * 클래스 설명
 *
 * @author injeong
 * @version 1.0
 * @since 2016.7.01 최초작성
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppAutoScale {
    private int no;
    private String guid;


    private String org;
    private String space;
    private String app;


    private int instanceMinCnt;
    private int instanceMaxCnt;

    private Map<String, Object> map;

    private double cpuThresholdMinPer;
    private double cpuThresholdMaxPer;

    private int memoryMinSize;
    private int memoryMaxSize;

    private int checkTimeSec;

    private String autoIncreaseYn;
    private String autoDecreaseYn;

    public AppAutoScale() {

    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public int getMemoryMinSize() {
        return memoryMinSize;
    }

    public void setMemoryMinSize(int memoryMinSize) {
        this.memoryMinSize = memoryMinSize;
    }

    public int getMemoryMaxSize() {
        return memoryMaxSize;
    }

    public void setMemoryMaxSize(int memoryMaxSize) {
        this.memoryMaxSize = memoryMaxSize;
    }

    public String getAutoIncreaseYn() {
        return autoIncreaseYn;
    }

    public void setAutoIncreaseYn(String autoIncreaseYn) {
        this.autoIncreaseYn = autoIncreaseYn;
    }

    public String getAutoDecreaseYn() {
        return autoDecreaseYn;
    }

    public void setAutoDecreaseYn(String autoDecreaseYn) {
        this.autoDecreaseYn = autoDecreaseYn;
    }

    public int getInstanceMinCnt() {
        return instanceMinCnt;
    }

    public void setInstanceMinCnt(int instanceMinCnt) {
        this.instanceMinCnt = instanceMinCnt;
    }

    public int getInstanceMaxCnt() {
        return instanceMaxCnt;
    }

    public void setInstanceMaxCnt(int instanceMaxCnt) {
        this.instanceMaxCnt = instanceMaxCnt;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public double getCheckTimeSec() {
        return checkTimeSec;
    }

    public void setCheckTimeSec(int checkTimeSec) {
        this.checkTimeSec = checkTimeSec;
    }

    public double getCpuThresholdMaxPer() {
        return cpuThresholdMaxPer;
    }

    public void setCpuThresholdMaxPer(double cpuThresholdMaxPer) {
        this.cpuThresholdMaxPer = cpuThresholdMaxPer;
    }

    public void setCpu_threshold_max_per(double cpuThresholdMaxPer) {
        this.cpuThresholdMaxPer = cpuThresholdMaxPer;
    }

    public double getCpuThresholdMinPer() {
        return cpuThresholdMinPer;
    }

    public void setCpuThresholdMinPer(double cpuThresholdMinPer) {
        this.cpuThresholdMinPer = cpuThresholdMinPer;
    }

    @Override
    public String toString() {
        return "AppAutoScale{" +
                "no=" + no +
                ", guid='" + guid + '\'' +
                ", org='" + org + '\'' +
                ", space='" + space + '\'' +
                ", app='" + app + '\'' +
                ", instanceMinCnt=" + instanceMinCnt +
                ", instanceMaxCnt=" + instanceMaxCnt +
                ", map=" + map +
                ", cpuThresholdMinPer=" + cpuThresholdMinPer +
                ", cpuThresholdMaxPer=" + cpuThresholdMaxPer +
                ", memoryMinSize=" + memoryMinSize +
                ", memoryMaxSize=" + memoryMaxSize +
                ", checkTimeSec=" + checkTimeSec +
                ", autoIncreaseYn='" + autoIncreaseYn + '\'' +
                ", autoDecreaseYn='" + autoDecreaseYn + '\'' +
                '}';
    }
}
