package com.xebia.fs101.writerpad.config;

import com.xebia.fs101.writerpad.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity//(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {

        return new CustomUserDetailService();
    }

    //@formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().ignoringAntMatchers("/api/**")
                .and()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/profiles/{username}").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
                .and()
                .httpBasic();
    }
    //@formatter:on
//
//    //@formatter:off
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .csrf().ignoringAntMatchers("/api/**")
//                     .and()
//                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
//                    .addFilter(new JwtAuthorizationFilter(authenticationManager()))
//                .authorizeRequests()
//                    .antMatchers("/").permitAll()
//                   // .antMatchers(HttpMethod.POST,"/api/profiles/users").permitAll()
//                    .antMatchers(HttpMethod.POST,SIGN_UP_URL).permitAll()
//                    .antMatchers("/login").permitAll()
////                    .anyRequest().authenticated()
//                    .and()
//
////                .formLogin()
////                    .loginPage("/login")
////                    .permitAll()
////                    .and()
//                .logout()
//                    .permitAll();
////                .and()
//////                    .httpBasic();
//    }
//    // @formatter:on

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {

        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_WRITER\n"
                                           + "ROLE_ADMIN > ROLE_EDITOR");
        return roleHierarchy;
    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.authenticationProvider(authenticationProvider());
////        auth.userDetailsService(userDetailsService()).passwordEncoder
// (passwordEncoder());
//    }
//    @Bean
//    DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider=new
//        DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
//        return daoAuthenticationProvider;
//    }
}
