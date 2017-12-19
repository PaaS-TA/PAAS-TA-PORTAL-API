package org.openpaas.paasta.portal.api.service;

import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

/**
 * glusterfs 서비스 - glusterfs 파일을 조회하고 삭제한다.
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.09.19 최초작성
 */
@Service("glusterfsService")
public class GlusterfsServiceImpl implements FileService{

    /**
     * Glusterfs의 저장공간 Container
     */
    @Autowired
    Container container;


    /**
     *
     * MultipartFile을 업로드 하고 접근할 수 있는 Path를 반환한다.
     *
     * @param multipartFile
     * @return String
     * @throws IOException
     */
    @Override
    public String upload(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");

        String filename = uuid+originalFilename.substring(originalFilename.lastIndexOf("."));

        StoredObject object = container.getObject(filename);
        object.uploadObject(multipartFile.getInputStream());

        return object.getPublicURL();
    }

    /**
     *
     * Container 안의 파일 이름으로 파일을 찾아서 삭제한다.
     * Container 경로가 포함된 완전한 Path를 주었을 경우에도 정상적으로 동작한다.
     *
     * @param objectName
     */
    // MODIFIED BY REX
    @Override
    public void delete(String objectName) {
        String containerPath = container.getPath();
        String reqObjectName = objectName;  // MODIFIED BY REX

        if (objectName.contains(containerPath)) {
            reqObjectName = objectName.substring( objectName.indexOf(containerPath) + containerPath.length()+1 );   // MODIFIED BY REX
        }

        StoredObject object  = container.getObject(reqObjectName);  // MODIFIED BY REX

        if(object.exists()) {
            object.delete();
        }
    }

    /**
     * Gets container.
     *
     * @return the container
     */
    public Container getContainer() {
        return this.container;
    }

    /**
     * 요청된 이미지 주소에서 이미지를 받아와 byte array로 변환하여 응답한다.
     *
     * @param imgPath
     * @return
     * @throws IOException
     */
    @Override
    public byte[] getImageByte(String imgPath) throws IOException {
        URL file   = new URL(imgPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = file.openStream ();
            byte[] byteChunk = new byte[4096];
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
        }
        catch (IOException e) {
            e.printStackTrace ();
        }
        finally {
            if (is != null) { is.close(); }
        }
        byte[] fileByte = baos.toByteArray();

        return fileByte;
    }

}
