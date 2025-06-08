package com.sku.caloriechat.config;

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
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                // ── 정적 리소스 & 뷰 ───────────────────────────
                .requestMatchers("/", "/login", "/signup",
                    "/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()

                // ── Swagger ───────────────────────────────────
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                    "/swagger-resources/**").permitAll()

                // ── JSON 인증·회원가입 API ─────────────────────
                // 1) 프론트 fetch 경로
                .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
                // 2) 백엔드 REST 경로
                .requestMatchers(HttpMethod.POST,
                    "/api/v1/users/login", "/api/v1/users/register").permitAll()

                // ── 기타 공개 API (weight-log, feedback 등) ────
                .requestMatchers("/api/weight-log/**",
                    "/api/feedback/**",
                    "/api/profile/**").permitAll()

                // ── 나머지는 인증 필요 ───────────────────────
                .anyRequest().authenticated())

            // 폼 로그인 사용 안 할 경우 반드시 disable
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(Customizer.withDefaults());   // = BasicAuth (테스트용)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {   // 중복 필드 제거
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration conf) throws Exception {
        return conf.getAuthenticationManager();
    }
}