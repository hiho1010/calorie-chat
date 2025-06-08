package com.sku.caloriechat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ❶ CSRF – REST API면 비활성화 권장
            .csrf(AbstractHttpConfigurer::disable)

            // ❷ CORS – 필요 시 설정
            .cors(Customizer.withDefaults())

            // ❸ 세션 정책 – JWT를 쓸 계획이라면 STATELESS
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ❹ 인가(Authorization) 규칙
            .authorizeHttpRequests(auth -> auth
                // Swagger UI & 정적 리소스 허용
                .requestMatchers(
                    "/swagger-ui/**", "/v3/api-docs/**",
                    "/swagger-resources/**", "/webjars/**").permitAll()
                .requestMatchers(HttpMethod.POST,
                    "/api/users/*/meals").permitAll()
                // 회원가입·로그인 공개
                .requestMatchers(HttpMethod.POST,
                    "/api/v1/users/register",
                    "/api/v1/users/login").permitAll()

                    // 프로필 관련도 임시로 공개 허용 나중에 지워야 함. 로그인 프로세스 완성되면
                    .requestMatchers("/api/profile/**").permitAll()

                    .requestMatchers(HttpMethod.POST,
                            "/api/weight-log/**").permitAll()
                    .requestMatchers(HttpMethod.GET,
                            "/api/weight-log/**").permitAll()

                    .requestMatchers(HttpMethod.GET,
                            "/api/feedback/**").permitAll()
                    .requestMatchers(HttpMethod.POST,
                            "/api/feedback/**").permitAll()




                    // 그 외는 인증 필요
                .anyRequest().authenticated())

            // ❺ 폼 로그인 or JWT/Bearer 설정
            .httpBasic(Customizer.withDefaults()); // 일단 BasicAuth로 테스트

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }
}