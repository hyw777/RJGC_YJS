package com.sgrej.config;

import com.sgrej.filter.JwtAuthenticationTokenFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationConfiguration configuration;


    //创建BCryptPasswordEncoder注入容器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return configuration.getAuthenticationManager();
    }


//"/ws/**"


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       // http.formLogin((authorize) -> authorize.permitAll());
        http.csrf((authorize) -> authorize.disable());
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/user/login","/user/register"
                                ,"/user/sendCode/**","swagger-ui/index.html/**"
                        ,"/user/register","/business/search","/business/searchByName","/business/businessInfo","/business/stores","/business/AIRecommend","/business/category","/user/findUser","/images/**").permitAll()
//                                .requestMatchers("**").permitAll()
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
        //配置自定义的异常处理类
        http.exceptionHandling()
                //认证失败
                .authenticationEntryPoint(authenticationEntryPoint)
                //授权失败
                .accessDeniedHandler(accessDeniedHandler);



        return http.build();
    }


    // 跨域配置源

/*    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://example.com", "http://www.example.com"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        configuration.setAllowCredentials(true);

        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        configuration.setMaxAge(3600L); // 1 hour



        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;

    }*/


}
