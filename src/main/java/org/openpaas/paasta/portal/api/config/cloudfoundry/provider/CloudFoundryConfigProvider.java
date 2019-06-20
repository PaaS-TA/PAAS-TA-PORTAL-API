package org.openpaas.paasta.portal.api.config.cloudfoundry.provider;


import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.openpaas.paasta.portal.api.common.PaastaConnectionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class CloudFoundryConfigProvider {


    @Bean
    PaastaConnectionContext connectionContext(@Value("${cloudfoundry.cc.api.url}") String apiTarget, @Value("${cloudfoundry.cc.api.sslSkipValidation}") Boolean sslSkipValidation) {
        return new PaastaConnectionContext(DefaultConnectionContext.builder().apiHost(apiTarget.replace("http://", "")).port(9022).skipSslValidation(sslSkipValidation).keepAlive(true).build(), new Date());
    }

    @Bean
    PasswordGrantTokenProvider tokenProvider(@Value("${cloudfoundry.user.admin.username}") String username, @Value("${cloudfoundry.user.admin.password}") String password) {
        return PasswordGrantTokenProvider.builder().password(password).username(username).build();
    }
}
