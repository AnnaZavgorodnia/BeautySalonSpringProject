package com.salon.config;

import com.salon.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http        .authorizeRequests()
                    .antMatchers("/","/registration","/js/**", "/static/**").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/clients").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/appointments").hasAuthority("ADMIN")
//                    .antMatchers("/api/masters/**",
//                            "/api/positions/**",
//                            "/api/services/**",
//                            "/api/users/**",
//                            "/api/appointments/**",
//                            "/create_master",
//                            "/all_masters",
//                            "/all_appointments").hasAuthority("ADMIN")
//                    .
                    //.antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                    //.antMatchers( "/api/appointments").hasAuthority("CLIENT")
                    //.antMatchers("/api/appointments/**").hasAnyAuthority("ADMIN","CLIENT")
                    //.antMatchers(HttpMethod.GET,"/api/masters/{masterId}/appointments").hasAnyAuthority("ADMIN","MASTER")
                    .anyRequest().authenticated()
                .and()
                    .formLogin().loginPage("/login").permitAll()
                    .defaultSuccessUrl("/",true)
                .and()
                    .logout()
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                .and()
                    .csrf().disable()
        ;
    }

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder());
    }
}
