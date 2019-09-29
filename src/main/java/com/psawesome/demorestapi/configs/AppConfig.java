package com.psawesome.demorestapi.configs;

import com.psawesome.demorestapi.accounts.Account;
import com.psawesome.demorestapi.accounts.AccountRole;
import com.psawesome.demorestapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.beans.BeanProperty;
import java.util.Set;

@Configuration
public class AppConfig {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account ps = Account.builder()
                        .email("psawesome@gmail.com")
                        .password("ps")
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();
                accountService.saveAccout(ps);
            }
        };
    }
}
