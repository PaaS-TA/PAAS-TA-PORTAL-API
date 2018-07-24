package org.openpaas.paasta.portal.api.config.cloudfoundry.provider;


import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.openpaas.paasta.portal.api.common.Common;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CfConfigProvider {



    @Bean
    DefaultConnectionContext connectionContext(@Value("${cloudfoundry.cc.api.url}") String apiHost, @Value("${cloudfoundry.cc.api.sslSkipValidation}") Boolean sslSkipValidation) {
        return Common.crateConnectionContext(apiHost, sslSkipValidation);
    }

    @Bean
    PasswordGrantTokenProvider tokenProvider(@Value("${cloudfoundry.user.admin.username}") String username,
                                             @Value("${cloudfoundry.user.admin.password}") String password) {
        return PasswordGrantTokenProvider.builder()
                .password(password)
                .username(username)
                .build();
    }
}
