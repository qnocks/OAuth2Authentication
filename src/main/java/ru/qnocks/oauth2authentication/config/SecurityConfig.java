package ru.qnocks.oauth2authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OidcUserService oidcUserService;

    private final DefaultOAuth2UserService oAuth2UserService;

    @Autowired
    public SecurityConfig(OidcUserService oidcUserService, DefaultOAuth2UserService oAuth2UserService) {
        this.oidcUserService = oidcUserService;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/index", "/webjars/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
            .logout()
                .logoutSuccessUrl("/").permitAll()
            .and()
            .oauth2Login()
                .userInfoEndpoint()
                    .oidcUserService(oidcUserService)
                    .userService(oAuth2UserService)
                .and()
                .defaultSuccessUrl("/success", true);
    }
}
