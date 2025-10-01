package com.isu.hr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {}) // CORS 활성화
                .csrf(csrf -> csrf.disable()) // REST API는 CSRF 불필요
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // H2 콘솔용
                )
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/h2-console/**").permitAll() // H2 콘솔 허용
                                .requestMatchers("/docs/**").permitAll() // REST Docs 허용
                                .requestMatchers("/**").permitAll() // API 모든 요청 허용 (개발용)
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}
