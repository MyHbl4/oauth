package com.example.oauth.config;

import com.example.oauth.security.JwtTokenFilter;
import com.example.oauth.security.oauth2.CustomOAuth2User;
import com.example.oauth.security.oauth2.CustomOAuth2UserService;
import com.example.oauth.service.UserDetailsServiceImpl;
import com.example.oauth.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService oauthUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/auth/**").anonymous()
            .antMatchers(
                HttpMethod.GET,
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/webjars/**",
                "favicon.ico"
            ).permitAll()
            // TODO: 09.08.2022 добавил отображение юзеров т.к. не знаю что должна возвращать авторизация через гугл
            .antMatchers("/oauth2/**").permitAll()
//            .antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
            .userInfoEndpoint()
            .userService(oauthUserService)
            .and()
            .successHandler(new AuthenticationSuccessHandler() {

                @Override
                public void onAuthenticationSuccess(HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                    System.out.println("AuthenticationSuccessHandler invoked");
                    System.out.println("Authentication name: " + authentication.getName());
                    CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

                    userService.processOAuthPostLogin(oauthUser.getAttributes());
                    // TODO: 09.08.2022 тот же юзер изза незнанки что возвращает авторизация через гугл
                    response.sendRedirect("/api/users");
                }
            });

        http.exceptionHandling()
            .authenticationEntryPoint(
                (request, response, ex) -> response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ex.getMessage()
                )
            );

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
