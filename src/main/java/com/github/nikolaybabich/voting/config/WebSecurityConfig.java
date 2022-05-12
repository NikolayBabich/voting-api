package com.github.nikolaybabich.voting.config;

import com.github.nikolaybabich.voting.model.Role;
import com.github.nikolaybabich.voting.model.User;
import com.github.nikolaybabich.voting.repository.UserRepository;
import com.github.nikolaybabich.voting.web.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Bean
    @Override
    // https://stackoverflow.com/a/70176629
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(email -> {
                    log.debug("Authenticating '{}'", email);
                    User user = userRepository.findByEmailIgnoreCase(email)
                            .orElseThrow(() -> new UsernameNotFoundException("User '" + email + "' was not found"));
                    return new AuthUser(user);
                })
                .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                    .antMatchers("/api/profile/**").hasRole(Role.USER.name())
                    .antMatchers("/api/**").authenticated()
                    .and()
                .httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}
