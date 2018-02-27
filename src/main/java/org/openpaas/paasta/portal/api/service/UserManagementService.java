package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.common.CustomCloudFoundryClient;
import org.openpaas.paasta.portal.api.mapper.portal.UserDetailMapper;
import org.openpaas.paasta.portal.api.mapper.portal.UserManagementMapper;
import org.openpaas.paasta.portal.api.mapper.uaa.UserMapper;
import org.openpaas.paasta.portal.api.model.UserManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 사용자 목록, 사용자 삭제 및 운영자 권한 부여 등 관리자에게 필요한 기능을 구현한 서비스 클래스로 Common(1.3.8) 클래스를 상속하여 구현한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.12 최초작성
 */
@Transactional
@Service
public class UserManagementService extends Common {


//    private final UserManagementMapper userManagementMapper;
//    private final UserMapper userMapper;
//    private final UserDetailMapper userDetailMapper;
    private final UserService userService;


//    @Autowired
//    public UserManagementService(UserManagementMapper userManagementMapper,
//                                 UserMapper userMapper,
//                                 UserDetailMapper userDetailMapper,
//                                 UserService userService) {
//        this.userManagementMapper = userManagementMapper;
//        this.userMapper = userMapper;
//        this.userDetailMapper = userDetailMapper;
//        this.userService = userService;
//    }

    @Autowired
    public UserManagementService(UserService userService) {
        this.userService = userService;
    }


    /**
     * 사용자 정보 목록을 조회한다.
     *
     * @param param UserManagement(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getUserInfoList(UserManagement param) {
        int pageNo = Constants.PAGE_NO;
        float pageSize = Constants.PAGE_SIZE;

        // SET PAGING INIT VALUE
        if (param.getPageNo() > 0) pageNo = param.getPageNo();
        if (param.getPageSize() > 0) pageSize = param.getPageSize();

        param.setPageNo((int) ((pageSize * (pageNo - 1))));

        return new HashMap<String, Object>() {{
//            put("list", userManagementMapper.getUserInfoList(param));
            put("list","");
        }};
    }


    /**
     * 사용자 패스워드 초기화를 한다.
     *
     * @param param UserManagement(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> setResetPassword(UserManagement param) throws Exception {
        userService.resetPassword(new HashMap<String, Object>() {{
            put("userId", param.getUserId());
            put("searchUserId", param.getUserId());
        }});

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 사용자에게 운영자 권한을 부여한다.
     *
     * @param param UserManagement(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> updateOperatingAuthority(UserManagement param) {
        param.setPageSize(1);
        param.setPageNo(0);
//        param.setAdminYn(Constants.USE_YN_Y.equals(userManagementMapper.getUserInfoList(param).get(0).getAdminYn()) ? Constants.USE_YN_N : Constants.USE_YN_Y);
        param.setAdminYn("Y");
//        userManagementMapper.updateOperatingAuthority(param);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 사용자를 삭제한다.
     *
     * @param param UserManagement(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> deleteUserAccount(UserManagement param) throws Exception {
        CustomCloudFoundryClient adminCustomCloudFoundryClient = getCustomCloudFoundryClient(adminUserName, adminPassword);
        String userId = param.getUserId();
//        String resultUserGuid = userMapper.getUserGuid(userId);
        String resultUserGuid = "";
        if (null != resultUserGuid) {
            adminCustomCloudFoundryClient.deleteUser(resultUserGuid);
        }

//        userDetailMapper.delete(userId);

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }
}
