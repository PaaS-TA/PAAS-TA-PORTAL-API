package org.openpaas.paasta.portal.api.common;

/**
 * Constants 클래스
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.06.16 최초작성
 */
public class Constants {

    /**
     * 생성자
     */
    public Constants(){};

    /**
     * 사용유무 사용
     */
    public static final String USE_YN_Y = "Y";
    /**
     * 사용유무 미사용
     */
    public static final String USE_YN_N = "N";
    /**
     * 페이지 번호
     */
    public static final int PAGE_NO = 1;
    /**
     * 페이지 크기
     */
    public static final int PAGE_SIZE = 10;
    /**
     * 공통코드 그룹 처리명
     */
    public static final String PROC_NAME_COMMON_CODE_GROUP = "CommonCodeGroup";
    /**
     * 공통코드 상세 처리명
     */
    public static final String PROC_NAME_COMMON_CODE_DETAIL = "CommonCodeDetail";
    /**
     * 결과 코드 성공
     */
    public static final String RESULT_STATUS_SUCCESS = "SUCCESS";
    /**
     * 결과 코드 실패
     */
    public static final String RESULT_STATUS_FAIL = "FAIL";
    /**
     * 결과 코드 실패(중복)
     */
    public static final String RESULT_STATUS_FAIL_DUPLICATED = "DUPLICATED";
    /**
     * 등록/수정/삭제 상태 등록
     */
    public static final String CUD_C = "C";
    /**
     * 등록/수정/삭제 상태 수정
     */
    public static final String CUD_U = "U";
    /**
     * 값 없음
     */
    public static final String NONE_VALUE = "NONE";
    /**
     * 중복처리 문자열
     */
    public static final String DUPLICATION_SEPARATOR = "::";
    /**
     * 스타터 카탈로그 아이디
     */
    public static final String STARTER_CATALOG_ID = "STARTER_CATALOG";
    /**
     * 빌드팩 카탈로그 아이디
     */
    public static final String BUILD_PACK_CATALOG_ID = "BUILD_PACK_CATALOG";
    /**
     * 서비스팩 카탈로그 아이디
     */
    public static final String SERVICE_PACK_CATALOG_ID = "SERVICE_PACK_CATALOG";
    /**
     * 카탈로그 타입 스타터
     */
    public static final String CATALOG_TYPE_STARTER = "starter";
    /**
     * 카탈로그 타입 빌드팩
     */
    public static final String CATALOG_TYPE_BUILD_PACK = "buildPack";
    /**
     * 카탈로그 타입 서비스팩
     */
    public static final String CATALOG_TYPE_SERVICE_PACK = "servicePack";
    /**
     * 카탈로그 내역 목록 제한 크기
     */
    public static final int CATALOG_HISTORY_LIMIT_SIZE = 3;
    /**
     * 요청 도메인 상태 SHARED
     */
    public static final String REQUEST_DOMAIN_STATUS_SHARED = "shared";
    /**
     * 앱 생성 스테이징 명령
     */
    public static final String CREATE_APPLICATION_STAGING_COMMAND = "";
    /**
     * 앱 생성 디스크 크기
     */
    public static final int CREATE_APPLICATION_DISK_SIZE = 1024;
    /**
     * 앱 생성 메모리 크기
     */
    public static final int CREATE_APPLICATION_MEMORY_SIZE = 512;
    /**
     * 내 문의 상태 대기
     */
    public static final String MY_QUESTION_STATUS_WAITING = "waiting";
    /**
     * 카탈로그 EGOV 빌드팩 문자열
     */
    public static final String CATALOG_EGOV_BUILD_PACK_CHECK_STRING = "egov";
    /**
     * 카탈로그 EGOV 빌드팩 환경 키
     */
    public static final String CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_KEY = "JBP_CONFIG_COMPONENTS";

    /**
     * 카탈로그 EGOV 빌드팩 환경 값
     */
    public static final String CATALOG_EGOV_BUILD_PACK_ENVIRONMENT_VALUE = "[containers: Tomcat]";

    /**
     * Org 권한 users
     */
    public static final String USERS = "users";
    public static final String ORGMANAGER = "OrgManager";
    /**
     * Org 권한 users
     */
    public static final String SPACEMANAGER = "SpaceManager";






}
