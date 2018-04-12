package org.openpaas.paasta.portal.api.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The type Security config.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.username}")
    String username;

    @Value("${spring.security.password}")
    String password;

    /**
     * Configure global.
     *
     * @throws Exception the exception
     */

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(username).password(password).roles("USER");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
				.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/external/**").permitAll()
                //Spring boot Admin 정보 접근 URL -  시작
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/info**").permitAll()
                .antMatchers("/env**").permitAll()
                .antMatchers("/metrics**").permitAll()
                .antMatchers("/trace**").permitAll()
                .antMatchers("/dump**").permitAll()
                .antMatchers("/jolokia**").permitAll()
                .antMatchers("/configprops**").permitAll()
                .antMatchers("/logfile**").permitAll()
                .antMatchers("/logging**").permitAll()
                .antMatchers("/refresh**").permitAll()
                .antMatchers("/flyway**").permitAll()
                .antMatchers("/liquibase**").permitAll()
                .antMatchers("/httptrace**").permitAll()
                .antMatchers("/threaddump**").permitAll()
                .antMatchers("/heapdump**").permitAll()
                .antMatchers("/loggers**").permitAll()
                .antMatchers("/auditevents**").permitAll()
                .antMatchers("/hystrix.stream**").permitAll()
                .antMatchers("/docs**").permitAll()
                .antMatchers("/jmx**").permitAll()
                .antMatchers("/management/**").permitAll()
                .antMatchers("/applications/**").permitAll()
                .antMatchers("/applications/**/**").permitAll()
                .antMatchers("/applications/**/**/**").permitAll()
                .antMatchers("/health**").permitAll()
                .antMatchers("/health/**").permitAll()
                //Spring boot Admin 정보 접근 URL - 끝
                .antMatchers("/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable().cors().disable();
        /*http

                .authorizeRequests()
                .antMatchers("/external/**").permitAll()
                .antMatchers("/**")
                .permitAll()
                .and()
                .csrf().disable();*/
    }

}
