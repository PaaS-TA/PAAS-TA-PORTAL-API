package org.openpaas.paasta.portal.api.service;

import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.mapper.portal.CommonCodeMapper;
import org.openpaas.paasta.portal.api.model.CommonCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 공통코드 기능을 구현한 서비스 클래스이다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.06.15 최초작성
 */
@Transactional
@Service
public class CommonCodeService {

    private final CommonCodeMapper commonCodeMapper;
    private final CommonService commonService;

    @Autowired
    public CommonCodeService(CommonCodeMapper commonCodeMapper, CommonService commonService) {
        this.commonCodeMapper = commonCodeMapper;
        this.commonService = commonService;
    }


    /**
     * 공통코드 목록을 조회한다.
     *
     * @param codeId String(아이디)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getCommonCodeById(String codeId) {
        return new HashMap<String, Object>() {{
            put("list", commonCodeMapper.getCommonCodeById(new CommonCode() {{
                setId(Optional.ofNullable(codeId).orElse(Constants.STARTER_CATALOG_ID));
            }}));
        }};
    }


    /**
     * 공통코드 목록을 조회한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> getCommonCode(CommonCode param) {
        Map<String, Object> pageInfoMap = new HashMap<>();
        List<CommonCode> tempList = new ArrayList<>();

        int pageNo = Constants.PAGE_NO;
        float pageSize = Constants.PAGE_SIZE;
        float resultListCount = 0;

        param.setProcType(Optional
                .ofNullable(param)
                .map(CommonCode::getProcType)
                .filter(procType -> procType.length() > 0)
                .orElse(Constants.PROC_NAME_COMMON_CODE_GROUP));

        // SET PAGING INIT VALUE
        if (param.getPageNo() > 0) pageNo = param.getPageNo();
        if (param.getPageSize() > 0) pageSize = param.getPageSize();

        param.setPageNo((int) ((pageSize * (pageNo - 1))));

        // COMMON CODE DETAIL
        if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {
            tempList = commonCodeMapper.getCommonCodeDetail(param);
            resultListCount = commonCodeMapper.getCommonCodeDetailCount(param);
        }

        // COMMON CODE GROUP
        if (Constants.PROC_NAME_COMMON_CODE_GROUP.equals(param.getProcType())) {
            tempList = commonCodeMapper.getCommonCodeGroup(param);
            resultListCount = commonCodeMapper.getCommonCodeGroupCount(param);
        }

        // SET PAGING VALUE
        pageInfoMap.put("pageNo", pageNo);
        pageInfoMap.put("pageSize", pageSize);
        pageInfoMap.put("pageCount", (int) Math.ceil(resultListCount / pageSize));
        pageInfoMap.put("listCount", resultListCount);

        List<CommonCode> resultList = tempList;

        return new HashMap<String, Object>() {{
            put("page", pageInfoMap);
            put("result", "ok");      // FIX VALUE
            put("list", resultList);   // FIX VALUE
        }};
    }


    /**
     * 공통코드를 저장한다.
     *
     * @param param CommonCode(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> insertCommonCode(CommonCode param, HttpServletResponse res) throws Exception {
        // CHECK REQUEST PARAM
        Map<String, Object> resultMap = this.checkRequestParam(param);

        if (Constants.RESULT_STATUS_FAIL.equals(resultMap.get("RESULT"))) {
            commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.empty.req.data");
            return resultMap;
        }

        // CHECK DUPLICATED
        resultMap = this.checkDuplicated(param);

        if (Constants.RESULT_STATUS_FAIL_DUPLICATED.equals(resultMap.get("RESULT"))) {
            commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated"
                    + Constants.DUPLICATION_SEPARATOR + resultMap.get("DUPLICATED_VALUE"));

            return resultMap;
        }

        // CHECK LIST
        if (null != param.getCommonCodeList()) {
            this.saveCommonCodeList(param, res);

        }

        if (null == param.getCommonCodeList()) {
            // COMMON CODE DETAIL
            if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {
                this.insertCommonCodeDetail(param);
            }

            // COMMON CODE GROUP
            if (Constants.PROC_NAME_COMMON_CODE_GROUP.equals(param.getProcType())) {
                this.insertCommonCodeGroup(param);
            }
        }

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        return resultMap;
    }


    /**
     * 공통코드 목록을 저장한다.
     *
     * @param param CommonCode(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private void saveCommonCodeList(CommonCode param, HttpServletResponse res) throws Exception {
        for (CommonCode tempModel : param.getCommonCodeList()) {
            tempModel.setUserId(param.getUserId());

            // INSERT
            if (Constants.CUD_C.equals(tempModel.getReqCud())) {

                // COMMON CODE DETAIL
                if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {
                    tempModel.setGroupId(param.getGroupId());

                    this.insertCommonCodeDetail(tempModel);
                }

                // COMMON CODE GROUP
                if (Constants.PROC_NAME_COMMON_CODE_GROUP.equals(param.getProcType())) {
                    this.insertCommonCodeGroup(tempModel);
                }
            }

            // UPDATE
            if (Constants.CUD_U.equals(tempModel.getReqCud())) {

                // COMMON CODE DETAIL
                if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {
                    tempModel.setGroupId(param.getGroupId());

                    this.updateCommonCodeDetail(tempModel, res);
                }

                // COMMON CODE GROUP
                if (Constants.PROC_NAME_COMMON_CODE_GROUP.equals(param.getProcType())) {
                    this.updateCommonCodeGroup(tempModel, res);

                    // COMMON CODE DETAIL
                    this.updateCommonCodeDetail(tempModel, res);
                }
            }
        }
    }


    /**
     * 공통코드그룹을 저장한다.
     *
     * @param param CommonCode(모델클래스)
     */
    private void insertCommonCodeGroup(CommonCode param) {
        commonCodeMapper.insertCommonCodeGroup(param);
    }


    /**
     * 공통코드상세를 저장한다.
     *
     * @param param CommonCode(모델클래스)
     */
    private void insertCommonCodeDetail(CommonCode param) {
        commonCodeMapper.insertCommonCodeDetail(param);
    }


    /**
     * 공통코드그룹을 수정한다.
     *
     * @param param CommonCode(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    public Map<String, Object> updateCommonCode(CommonCode param, HttpServletResponse res) throws Exception {
        // COMMON CODE DETAIL
        if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {
            this.updateCommonCodeDetail(param, res);
        }

        // COMMON CODE GROUP
        if (Constants.PROC_NAME_COMMON_CODE_GROUP.equals(param.getProcType())) {
            this.updateCommonCodeGroup(param, res);
            this.updateCommonCodeDetail(param, res);
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 공통코드그룹을 수정한다.
     *
     * @param param CommonCode(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private void updateCommonCodeGroup(CommonCode param, HttpServletResponse res) throws Exception {
        // CHECK ORIGINAL ID
        if (param.getId().equals(param.getOrgId())) {
            commonCodeMapper.updateCommonCodeGroup(param);

        } else {
            String orgUseYn = param.getUseYn();
            param.setUseYn(null);

            // CHECK ID EXIST
            if (commonCodeMapper.getCommonCodeGroupCount(param) > 0) {
                commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated");

            } else {
                param.setUseYn(orgUseYn);
                commonCodeMapper.updateCommonCodeGroup(param);
            }
        }
    }


    /**
     * 공통코드상세를 수정한다.
     *
     * @param param CommonCode(모델클래스)
     * @param res   HttpServletResponse(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private void updateCommonCodeDetail(CommonCode param, HttpServletResponse res) throws Exception {
        // CHECK ORIGINAL KEY
        if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {

            // CHECK NULL
            if (null == param.getKey()) {
                throw new Exception(commonService.getCustomMessage("common.info.result.fail"));
            }

            if (null == param.getOrgKey()) {
                throw new Exception(commonService.getCustomMessage("common.info.result.fail"));
            }

            if (param.getKey().equals(param.getOrgKey())) {
                commonCodeMapper.updateCommonCodeDetail(param);

            } else {
                // CHECK KEY EXIST
                if (commonCodeMapper.getCommonCodeDetailCount(param) > 0) {
                    commonService.getCustomSendError(res, HttpStatus.CONFLICT, "common.info.result.fail.duplicated");

                } else {
                    commonCodeMapper.updateCommonCodeDetail(param);
                }
            }
        } else {
            commonCodeMapper.updateCommonCodeDetail(param);
        }
    }


    /**
     * 공통코드를 삭제한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Map(자바클래스)
     */
    public Map<String, Object> deleteCommonCode(CommonCode param) {
        // CHECK LIST
        if (null != param.getCommonCodeList()) {
            this.deleteCommonCodeList(param);

        } else {
            // COMMON CODE DETAIL
            if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {
                commonCodeMapper.deleteCommonCodeDetail(param);
            }

            // COMMON CODE GROUP
            if (Constants.PROC_NAME_COMMON_CODE_GROUP.equals(param.getProcType())) {
                commonCodeMapper.deleteCommonCodeGroup(param);
                commonCodeMapper.deleteCommonCodeDetail(param);
            }
        }

        return new HashMap<String, Object>() {{
            put("RESULT", Constants.RESULT_STATUS_SUCCESS);
        }};
    }


    /**
     * 공통코드 목록을 삭제한다.
     *
     * @param param CommonCode(모델클래스)
     */
    private void deleteCommonCodeList(CommonCode param) {
        param.getCommonCodeList().forEach(commonCodeMapper::deleteCommonCodeDetail);
    }


    /**
     * 요청 파라미터를 검사한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private Map<String, Object> checkRequestParam(CommonCode param) throws Exception {
        Map<String, Object> checkedMap = new HashMap<>();

        // CHECK LIST
        if (param.getCommonCodeList() != null) {
            List<CommonCode> commonCodeList = param.getCommonCodeList();

            for (CommonCode tempModel : commonCodeList) {
                tempModel.setProcType(param.getProcType());

                if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {
                    tempModel.setGroupId(param.getGroupId());
                }

                checkedMap = this.procCheckRequestParam(tempModel);

                if (Constants.RESULT_STATUS_FAIL.equals(checkedMap.get("RESULT"))) {
                    return checkedMap;
                }
            }
        } else {
            checkedMap = this.procCheckRequestParam(param);

            if (Constants.RESULT_STATUS_FAIL.equals(checkedMap.get("RESULT"))) {
                return checkedMap;
            }
        }

        return checkedMap;
    }


    /**
     * 요청 파라미터 검사를 실행한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private Map<String, Object> procCheckRequestParam(CommonCode param) throws Exception {
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        // COMMON CODE DETAIL
        if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {
            if ("".equals(Optional.of(param).map(CommonCode::getKey).orElse(""))) {
                tempMap.put("RESULT", Constants.RESULT_STATUS_FAIL);
                tempMap.put("RESULT_MESSAGE", commonService.getCustomMessage("common.info.empty.req.data"));
            }

            if ("".equals(Optional.of(param).map(CommonCode::getValue).orElse(""))) {
                tempMap.put("RESULT", Constants.RESULT_STATUS_FAIL);
                tempMap.put("RESULT_MESSAGE", commonService.getCustomMessage("common.info.empty.req.data"));
            }
        }

        // COMMON CODE GROUP
        if (Constants.PROC_NAME_COMMON_CODE_GROUP.equals(param.getProcType())) {
            if ("".equals(Optional.of(param).map(CommonCode::getId).orElse(""))) {
                tempMap.put("RESULT", Constants.RESULT_STATUS_FAIL);
                tempMap.put("RESULT_MESSAGE", commonService.getCustomMessage("common.info.empty.req.data"));
            }

            if ("".equals(Optional.of(param).map(CommonCode::getName).orElse(""))) {
                tempMap.put("RESULT", Constants.RESULT_STATUS_FAIL);
                tempMap.put("RESULT_MESSAGE", commonService.getCustomMessage("common.info.empty.req.data"));
            }
        }

        return tempMap;
    }


    /**
     * 중복 검사를 한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private Map<String, Object> checkDuplicated(CommonCode param) throws Exception {
        Map<String, Object> checkedMap = new HashMap<>();

        // CHECK LIST
        if (param.getCommonCodeList() != null) {
            List<CommonCode> commonCodeList = param.getCommonCodeList();

            for (CommonCode tempModel : commonCodeList) {
                tempModel.setProcType(param.getProcType());

                // INSERT
                if (Constants.CUD_C.equals(tempModel.getReqCud())) {
                    checkedMap = this.procCheckDuplicated(tempModel);
                }

                // UPDATE
                if (Constants.CUD_U.equals(tempModel.getReqCud())) {

                    // COMMON CODE DETAIL
                    if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())) {

                        // CHECK ORIGINAL KEY
                        if (!tempModel.getOrgKey().equals(tempModel.getKey())) {
                            checkedMap = this.procCheckDuplicated(tempModel);

                        } else {
                            checkedMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
                        }
                    }

                    // COMMON CODE GROUP
                    if (Constants.PROC_NAME_COMMON_CODE_GROUP.equals(param.getProcType())) {

                        // CHECK ORIGINAL ID
                        if (!tempModel.getOrgId().equals(tempModel.getId())) {
                            checkedMap = this.procCheckDuplicated(tempModel);

                        } else {
                            checkedMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);
                        }
                    }
                }

                if (Constants.RESULT_STATUS_FAIL_DUPLICATED.equals(checkedMap.get("RESULT"))) {
                    return checkedMap;
                }
            }
        } else {
            checkedMap = this.procCheckDuplicated(param);

            if (Constants.RESULT_STATUS_FAIL_DUPLICATED.equals(checkedMap.get("RESULT"))) {
                return checkedMap;
            }
        }

        return checkedMap;
    }


    /**
     * 중복 검사를 실행한다.
     *
     * @param param CommonCode(모델클래스)
     * @return Map(자바클래스)
     * @throws Exception Exception(자바클래스)
     */
    private Map<String, Object> procCheckDuplicated(CommonCode param) throws Exception {
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        // COMMON CODE DETAIL
        if (Constants.PROC_NAME_COMMON_CODE_DETAIL.equals(param.getProcType())
                && commonCodeMapper.getCommonCodeDetailCount(param) > 0) {
            tempMap.put("RESULT", Constants.RESULT_STATUS_FAIL_DUPLICATED);
            tempMap.put("DUPLICATED_VALUE", param.getKey());
            tempMap.put("RESULT_MESSAGE", commonService.getCustomMessage("common.info.result.fail.duplicated"));
        }

        // COMMON CODE GROUP
        if (Constants.PROC_NAME_COMMON_CODE_GROUP.equals(param.getProcType())
                && commonCodeMapper.getCommonCodeGroupCount(param) > 0) {
            tempMap.put("RESULT", Constants.RESULT_STATUS_FAIL_DUPLICATED);
            tempMap.put("DUPLICATED_VALUE", param.getId());
            tempMap.put("RESULT_MESSAGE", commonService.getCustomMessage("common.info.result.fail.duplicated"));
        }

        return tempMap;
    }
}
