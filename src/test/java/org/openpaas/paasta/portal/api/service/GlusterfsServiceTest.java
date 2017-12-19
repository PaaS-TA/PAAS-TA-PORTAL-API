package org.openpaas.paasta.portal.api.service;

import org.apache.commons.io.IOUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openpaas.paasta.portal.api.config.ApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;

/**
 * Created by mg on 2016-07-05.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringApplicationConfiguration(classes = {ApiApplication.class})
public class GlusterfsServiceTest {

    @Autowired
    GlusterfsServiceImpl glusterfsService;
    private static String path = "";
    private static String path_c = "";


    @Test
    public void a_upload() throws Exception {

        System.out.println("Glusterfs update test");
        File file = new File("./src/test/java/resources/images/test.jpg");

        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        if (file.exists()) {
            path = glusterfsService.upload(multipartFile);
            System.out.println("Public URL: " + glusterfsService.upload(multipartFile));
        } else {
            System.out.println("File not exists.");
        }
    }

    @Test
    public void b_deleteByURI() throws URISyntaxException {

        glusterfsService.delete(path);
    }

    @Test
    public void c_upload() throws Exception {

        System.out.println("Glusterfs update test");
        File file = new File("./src/test/java/resources/images/test.jpg");

        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        if (file.exists()) {
            path_c = glusterfsService.upload(multipartFile);

        } else {
            System.out.println("File not exists.");
        }
    }

    @Test
    public void delete() {
        String[] name = path_c.split("/");
        String objectName = name[name.length - 1];
        glusterfsService.delete(objectName);
    }


}
