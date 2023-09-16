package mux.lib.auth;

import mux.web.models.UserProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHENTICATION_ENABLED = "mux.authentication.enabled";

    @Value("${"+AUTHENTICATION_ENABLED+":false}")
    private boolean authenticationEnabled;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (authenticationEnabled) {
            http
                .authorizeRequests()
                    .anyRequest().authenticated();
        } else {
            http
                .csrf().disable()
                .authorizeRequests()
                    .anyRequest().permitAll();
        }
    }


    @Bean
    public UserProvider permitAll() {
        if (authenticationEnabled) {
            return () -> UserStore.get(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
        } else {
            return () -> new User("default-user", new HashMap<>());
        }
    }


}
