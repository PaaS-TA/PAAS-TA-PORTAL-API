package org.openpaas.paasta.portal.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 파일 서비스 - 파일을 업로드하고 삭제한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.19 최초작성
 */
public interface FileService {

    /**
     * MultipartFile을 업로드 하고 접근할 수 있는 Path를 반환한다.
     *
     * @param multipartFile the multipart file
     * @return String string
     * @throws IOException the io exception
     */
    public String upload(MultipartFile multipartFile) throws IOException;

    /**
     * 파일을 삭제한다.
     *
     * @param fileName the file name
     */
    public void delete(String fileName);

    /**
     * 이미지를 받아와 byte array로 변환해 반환한다.
     *
     * @param imgPath the img path
     * @return byte [ ]
     * @throws IOException the io exception
     */
    public byte[] getImageByte(String imgPath) throws IOException;
}
