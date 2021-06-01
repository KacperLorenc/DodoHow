package pl.lorenc.dodohow.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserPrincipalDetailsService service;

    @Autowired
    public SecurityConfiguration(UserPrincipalDetailsService service) {
        this.service = service;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/console").permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/quizzes").authenticated()
                .antMatchers("/quizzes/**").authenticated()
                .antMatchers("/course").authenticated()
                .antMatchers("/course/**").authenticated()
                .antMatchers("/next-exercise").authenticated()
                .antMatchers("/teachers").hasRole("ADMIN")
                .antMatchers("/teachers/**").hasRole("ADMIN")
                .antMatchers("/classes").authenticated()
                .antMatchers("/classes/**").hasAnyRole("TEACHER")
                .antMatchers("/new-class").hasAnyRole("TEACHER")
                .antMatchers("/delete-teacher").hasAnyRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
                .and()
                .rememberMe().tokenValiditySeconds(2592000).rememberMeParameter("remember-me")
                .and()
                .addFilterBefore(new NoBrowserCacheFilter(), BasicAuthenticationFilter.class)
        ;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.service);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
