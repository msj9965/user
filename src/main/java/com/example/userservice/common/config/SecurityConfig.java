package com.example.userservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder makePassword(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                // Basic 인증 비활성화
                // Basic 인증은 REST API에서는 일반적으로 사용하지 않으므로 비활성화합니다.
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션방식을 비활성화
                .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(
                                "/member/signin",
                                "/member/signup"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));//모든 HTTP 메소드 허용
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));//모든 헤더 허용
        corsConfiguration.setAllowCredentials(true); // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로에 대해 CORS 설정 적용
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
