package com.psawesome.demorestapi.configs;

import com.psawesome.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService)
                .passwordEncoder(passwordEncoder);
    }

    /*
        WebSecurity -> HttpSecurity
        1차          -> 2차
    */
    /* 1 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        /*index는 무시함.*/
        web.ignoring().mvcMatchers("/docs/index.html");
        /*기본 위치는 security 영향을 받지 않음*/
        web.ignoring().requestMatchers((PathRequest.toStaticResources().atCommonLocations()));
    }

    /* 2 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .anonymous()
                .and()
                //사용자 계정 추가 방법, 이메일 인증을 추가하는 방법 등 할 일이 많다.
            .formLogin()
                .and()
            .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**").authenticated()
                .anyRequest().authenticated();
    }
    //@formatter:on
}
