package org.openpaas.paasta.portal.api.config.service;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.openpaas.paasta.portal.api.service.AppAutoScaleModalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by mg on 2016-07-05.
 */
@Configuration
public class GlusterfsConfig {

    /** 로그객체*/
    private  static final Logger logger = LoggerFactory.getLogger(GlusterfsConfig.class);

    @Autowired
    private Environment env;

    @Bean
    public AccountConfig accountConfig(){
        String tenantName = env.getRequiredProperty("spring.objectStorage.tenantName");
        String username = env.getRequiredProperty("spring.objectStorage.username");
        String password = env.getRequiredProperty("spring.objectStorage.password");
        String authUrl = env.getRequiredProperty("spring.objectStorage.authUrl");

        logger.info("tenantName : " + tenantName);
        logger.info("username : " + username);
        logger.info("password : " + password);
        logger.info("authUrl : " + authUrl);



        AccountConfig config = new AccountConfig();
        config.setUsername(username);
        config.setTenantName(tenantName);
        config.setPassword(password);
        config.setAuthUrl(authUrl + "/tokens");
        config.setAuthenticationMethod(AuthenticationMethod.KEYSTONE);
        if(authUrl.contains("localhost")) {
            config.setPreferredRegion("Local");
        } else {
            config.setPreferredRegion("Public");
        }
        return config;
    }

    @Bean
    public AccountFactory accountFactory(AccountConfig accountConfig){
        return new AccountFactory(accountConfig);
    }

    @Bean
    public Account account(AccountFactory accountFactory){
        return accountFactory.createAccount();
    }

    @Bean
    public Container container(Account account) {
        String containerName = env.getRequiredProperty("spring.objectStorage.container");

        Container container = account.getContainer(containerName);
        if(!container.exists()){
            container.create();
            container.makePublic();
        }

        return container;
    }
}
