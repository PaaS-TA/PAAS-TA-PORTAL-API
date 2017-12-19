package org.openpaas.paasta.portal.api.model;

/**
 * 사용량(모델클래스)
 * 사용량 정보를 가지고 있는 데이터 모델 bean 클래스
 * Json 어노테이션을 사용해서 JSON 형태로 제공
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.22 최초작성
 */
public class Usage {

    private String orgName;
    private String spaceName;
    private String orgGuid;
    private String fromMonth;
    private String toMonth;
    private int searchPeriodSum;
    private String monthlyUsageArr;
    private String month;
    private int monthlySum;
    private String spaces;
    private String spaceGuid;
    private String spaceSum;
    private String appUsageArr;
    private String appGuid;
    private String appName;
    private int appInstance;
    private String appMemory;
    private int appUsage;

    /**
     * GETTER 조직명
     *
     * @return 조직명
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * SETTER 조직명
     *
     * @param orgName 조직명
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * GETTER 공간명
     *
     * @return 공간명
     */
    public String getSpaceName() {
        return spaceName;
    }

    /**
     * SETTER 공간명
     *
     * @param spaceName 공간명
     */
    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    /**
     * GETTER 조직 GUID
     *
     * @return 조직 GUID
     */
    public String getOrgGuid() {
        return orgGuid;
    }

    /**
     * SETTER 조직 GUID
     *
     * @param orgGuid 조직 GUID
     */
    public void setOrgGuid(String orgGuid) {
        this.orgGuid = orgGuid;
    }

    /**
     * GETTER 시작월
     *
     * @return 시작월
     */
    public String getFromMonth() {
        return fromMonth;
    }

    /**
     * SETTER 시작월
     *
     * @param fromMonth 시작월
     */
    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    /**
     * GETTER 종료월
     *
     * @return 종료월
     */
    public String getToMonth() {
        return toMonth;
    }

    /**
     * SETTER 종료월
     *
     * @param toMonth 종료월
     */
    public void setToMonth(String toMonth) {
        this.toMonth = toMonth;
    }

    /**
     * GETTER 검색 기간 합
     *
     * @return 검색 기간 합
     */
    public int getSearchPeriodSum() {
        return searchPeriodSum;
    }

    /**
     * SETTER 검색 기간 합
     *
     * @param searchPeriodSum 검색 기간 합
     */
    public void setSearchPeriodSum(int searchPeriodSum) {
        this.searchPeriodSum = searchPeriodSum;
    }

    /**
     * GETTER 월 사용량
     *
     * @return 월 사용량
     */
    public String getMonthlyUsageArr() {
        return monthlyUsageArr;
    }

    /**
     * SETTER 월 사용량
     *
     * @param monthlyUsageArr 월 사용량
     */
    public void setMonthlyUsageArr(String monthlyUsageArr) {
        this.monthlyUsageArr = monthlyUsageArr;
    }

    /**
     * GETTER 월
     *
     * @return 월
     */
    public String getMonth() {
        return month;
    }

    /**
     * SETTER 월
     *
     * @param month 월
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * GETTER 월간 합
     *
     * @return 월간 합
     */
    public int getMonthlySum() {
        return monthlySum;
    }

    /**
     * SETTER 월간 합
     *
     * @param monthlySum 월간 합
     */
    public void setMonthlySum(int monthlySum) {
        this.monthlySum = monthlySum;
    }

    /**
     * GETTER 공간
     *
     * @return 공간
     */
    public String getSpaces() {
        return spaces;
    }

    /**
     * SETTER 공간
     *
     * @param spaces 공간
     */
    public void setSpaces(String spaces) {
        this.spaces = spaces;
    }

    /**
     * GETTER 공간 GUID
     *
     * @return 공간 GUID
     */
    public String getSpaceGuid() {
        return spaceGuid;
    }

    /**
     * SETTER 공간 GUID
     *
     * @param spaceGuid 공간 GUID
     */
    public void setSpaceGuid(String spaceGuid) {
        this.spaceGuid = spaceGuid;
    }

    /**
     * GETTER 공간 합
     *
     * @return 공간 합
     */
    public String getSpaceSum() {
        return spaceSum;
    }

    /**
     * SETTER 공간 합
     *
     * @param spaceSum 공간 합
     */
    public void setSpaceSum(String spaceSum) {
        this.spaceSum = spaceSum;
    }

    /**
     * GETTER 애플리케이션 사용량
     *
     * @return 애플리케이션 사용량
     */
    public String getAppUsageArr() {
        return appUsageArr;
    }

    /**
     * SETTER 애플리케이션 사용량
     *
     * @param appUsageArr 애플리케이션 사용량
     */
    public void setAppUsageArr(String appUsageArr) {
        this.appUsageArr = appUsageArr;
    }

    /**
     * GETTER 애플리케이션 GUID
     *
     * @return 애플리케이션 GUID
     */
    public String getAppGuid() {
        return appGuid;
    }

    /**
     * SETTER 애플리케이션 GUID
     *
     * @param appGuid 애플리케이션 GUID
     */
    public void setAppGuid(String appGuid) {
        this.appGuid = appGuid;
    }

    /**
     * GETTER 애플리케이션명
     *
     * @return 애플리케이션명
     */
    public String getAppName() {
        return appName;
    }

    /**
     * SETTER 애플리케이션명
     *
     * @param appName 애플리케이션명
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * GETTER 애플리케이션 인스턴스
     *
     * @return 애플리케이션 인스턴스
     */
    public int getAppInstance() {
        return appInstance;
    }

    /**
     * SETTER 애플리케이션 인스턴스
     *
     * @param appInstance 애플리케이션 인스턴스
     */
    public void setAppInstance(int appInstance) {
        this.appInstance = appInstance;
    }

    /**
     * GETTER 애플리케이션 메모리
     *
     * @return 애플리케이션 메모리
     */
    public String getAppMemory() {
        return appMemory;
    }

    /**
     * SETTER 애플리케이션 메모리
     *
     * @param appMemory 애플리케이션 메모리
     */
    public void setAppMemory(String appMemory) {
        this.appMemory = appMemory;
    }

    /**
     * GETTER 애플리케이션 사용량
     *
     * @return 애플리케이션 사용량
     */
    public int getAppUsage() {
        return appUsage;
    }

    /**
     * SETTER 애플리케이션 사용량
     *
     * @param appUsage 애플리케이션 사용량
     */
    public void setAppUsage(int appUsage) {
        this.appUsage = appUsage;
    }

    @Override
    public String toString() {
        return "Usage{" +
                "orgName='" + orgName + '\'' +
                ", spaceName='" + spaceName + '\'' +
                ", orgGuid='" + orgGuid + '\'' +
                ", fromMonth='" + fromMonth + '\'' +
                ", toMonth='" + toMonth + '\'' +
                ", searchPeriodSum=" + searchPeriodSum +
                ", monthlyUsageArr='" + monthlyUsageArr + '\'' +
                ", month='" + month + '\'' +
                ", monthlySum=" + monthlySum +
                ", spaces='" + spaces + '\'' +
                ", spaceGuid='" + spaceGuid + '\'' +
                ", spaceSum='" + spaceSum + '\'' +
                ", appUsageArr='" + appUsageArr + '\'' +
                ", appGuid='" + appGuid + '\'' +
                ", appName='" + appName + '\'' +
                ", appInstance=" + appInstance +
                ", appMemory='" + appMemory + '\'' +
                ", appUsage=" + appUsage +
                '}';
    }
}
