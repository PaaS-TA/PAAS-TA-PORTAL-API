package org.openpaas.paasta.portal.api.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .authorizeRequests()
                    .antMatchers("/external/**").permitAll()
                .antMatchers("/**").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/adminMain/**").hasRole("USER")
                .antMatchers("/app/**").hasRole("USER")
                .antMatchers("/authority/**").hasRole("USER")
                .antMatchers("/buildPack/**").hasRole("USER")
                .antMatchers("/catalog/**").hasRole("USER")
                .antMatchers("/client/**").hasRole("USER")
                .antMatchers("/commonCode/**").hasRole("USER")
                .antMatchers("/configInfo/**").hasRole("USER")
                .antMatchers("/documents/**").hasRole("USER")
                .antMatchers("/domain/**").hasRole("USER")
                .antMatchers("/file/**").hasRole("USER")
                .antMatchers("/log/**").hasRole("USER")
                .antMatchers("/menu/**").hasRole("USER")
                .antMatchers("/login/**").hasRole("USER")
                .antMatchers("/org/**").hasRole("USER")
                .antMatchers("/service/**").hasRole("USER")
                .antMatchers("/space/**").hasRole("USER")
                .antMatchers("/support/**").hasRole("USER")
                .antMatchers("/usage/**").hasRole("USER")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/userManagement/**").hasRole("USER")
                .antMatchers("/webIdeUser/**").hasRole("USER")
//                .antMatchers("/**").permitAll()//hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
        /*http

                .authorizeRequests()
                .antMatchers("/external/**").permitAll()
                .antMatchers("/**")
                .permitAll()
                .and()
                .csrf().disable();*/
    }

}
