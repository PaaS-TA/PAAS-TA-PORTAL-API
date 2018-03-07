package org.openpaas.paasta.portal.api.controller;

import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.model.Support;
//import org.openpaas.paasta.portal.api.service.GlusterfsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 파일 컨트롤러 - 파일 업로드, 다운로드 한다.
 *
 * @author 김영지
 * @version 1.0
 * @since 2016.07.28
 */
@RestController
@RequestMapping(value = {"/file"})
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

//    @Autowired
//    private GlusterfsServiceImpl glusterfsService;


    /**
     * Upload file map.
     *
     * @param multipartFile the multipart file
     * @param res           the res
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/uploadFile"}, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public Map<String, Object> uploadFile(@RequestParam(value="file", required=false) MultipartFile multipartFile, HttpServletResponse res) throws Exception {

        LOGGER.info("uploadFile :: param:: {}", multipartFile.toString());
//        String path = glusterfsService.upload(multipartFile);
        String path = "";
        LOGGER.info("path: " + path);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("path", path);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


    /**
     * Download file map.
     *
     * @param multipartFile the multipart file
     * @param res           the res
     * @return the map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/downloadFile"}, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public Map<String, Object> downloadFile(@RequestParam(value="file", required=false) MultipartFile multipartFile, HttpServletResponse res) throws Exception {

        LOGGER.info("downloadFile :: param:: {}", multipartFile.toString());
        //String path = glusterfsService.upload(multipartFile);
        String path = "";
                LOGGER.info("path: " + path);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("path", path);
        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }


    /**
     * delete file map.
     *
     * @param param the param
     * @param res   the res
     * @return map map
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/deleteFile"}, method = RequestMethod.POST)
    public Map<String, Object> deleteFile(@RequestBody Support param, HttpServletResponse res) throws Exception {
        String filePath = param.getFilePath();
        LOGGER.info("deleteFile :: param:: {}", filePath);

        Map<String, Object> resultMap = new HashMap<>();
        //glusterfsService.delete(filePath);

        resultMap.put("RESULT", Constants.RESULT_STATUS_SUCCESS);

        return resultMap;
    }

    /**
     * 이미지를 byte [] 타입으로 리턴한다.
     *
     * @param param the param
     * @return byte [ ]
     * @throws Exception the exception
     */
    @RequestMapping(value = {"/getImage"}, method = RequestMethod.POST)
    public byte[] getImageByte(@RequestBody Map<String, String> param) throws Exception {
        String imgPath = param.get("imgPath");
        LOGGER.info("imgPath : " + imgPath.trim());
        //byte[] imgByte = glusterfsService.getImageByte(imgPath.trim());
        byte[] imgByte = null;
        LOGGER.info("imgByte Fixed null For Test."); // StorageAPI 사용구현시 제거 할것
        return imgByte;
    }

}
