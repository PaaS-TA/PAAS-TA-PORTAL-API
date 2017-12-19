package org.openpaas.paasta.portal.api.config.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by mg on 2016-07-05.
 */
@Configuration
public class EmailConfig {
    @Autowired
    private Environment env;

    @Bean
    public MailProperties mailProperties() {

        MailProperties mailProperties = new MailProperties();
        mailProperties.setHost(env.getRequiredProperty("spring.mail.smtp.host"));
        mailProperties.setPort(Integer.valueOf(env.getRequiredProperty("spring.mail.smtp.port")));
        mailProperties.setUsername(env.getRequiredProperty("spring.mail.smtp.userEmail"));
        mailProperties.setPassword(env.getRequiredProperty("spring.mail.smtp.password"));
        return mailProperties;
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties() .getHost());
        if (mailProperties().getPort() != null) {
            mailSender.setPort(mailProperties().getPort());
        }
        mailSender.setUsername(mailProperties().getUsername());
        mailSender.setPassword(mailProperties().getPassword());
        return mailSender;
    }

    @Bean
    public Properties properties() {
        MailProperties mailProperties = mailProperties();
        Properties props = new Properties();
        // SSL 사용하는 경우
        props.put("mail.smtp.host", mailProperties.getHost()); //SMTP Host
        props.put("mail.smtp.socketFactory.port",mailProperties.getPort()); //SSL Port
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.port",mailProperties.getPort());
        props.put("mail.smtp.auth",env.getRequiredProperty("spring.mail.smtp.properties.auth")); //Enabling SMTP Authentication
        props.put("mail.smtp.maximumTotalQps",env.getRequiredProperty("spring.mail.smtp.properties.maximumTotalQps"));
        props.put("mail.smtp.authUrl",env.getRequiredProperty("spring.mail.smtp.properties.authUrl"));
        props.put("mail.smtp.imgUrl",env.getRequiredProperty("spring.mail.smtp.properties.imgUrl"));
        props.put("mail.smtp.sFile",env.getRequiredProperty("spring.mail.smtp.properties.sFile"));
//        props.put("mail.smtp.sDir",env.getRequiredProperty("spring.mail.smtp.properties.sDir"));
        props.put("mail.smtp.subject",env.getRequiredProperty("spring.mail.smtp.properties.subject"));
        props.put("mail.smtp.username",env.getRequiredProperty("spring.mail.smtp.username"));
        props.put("mail.smtp.userEmail",env.getRequiredProperty("spring.mail.smtp.userEmail"));
        props.put("mail.smtp.contextUrl",env.getRequiredProperty("spring.mail.smtp.properties.contextUrl"));
        return props;
    }

    // 인증
    @Bean
    public Authenticator auth() {
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                MailProperties mailProperties = mailProperties();
                return new PasswordAuthentication(mailProperties.getUsername(), mailProperties.getPassword());
            }

        };
        return auth;
    }
}