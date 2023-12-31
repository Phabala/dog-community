package com.ssafy.dog.security.config;

import com.ssafy.dog.security.JwtAuthenticationFilter;
import com.ssafy.dog.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",

            /* APIs */
            "/api/email/**",
            "/api/user/login",
            "/api/user/signup",
            "/api/user/duplicate-email/**",
            "/api/user/duplicate-nickname/**",
            "/api/jwt/valid",

            /* WebSocket */
            "/ws-stomp/**"
    };

    private final JwtTokenProvider jwtTokenProvider;

    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // authenticationManager 를 Bean 등록합니다.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 기본 설정
                .authorizeRequests()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
                .anyRequest().hasRole("USER") // 위 API들 제외하고는 모두 "USER" 역할이 필요하도록 설정
                .and()
                .csrf().disable() // csrf 보안 토큰 disable 처리
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반이므로 세션 사용 X
                .and()

                // 2. 로그인 관련 설정
                .formLogin()
                .disable()

                // 3. 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                // JwtAuthenticationFilter 를 UsernamePasswordAuthenticationFilter 전에 넣는다.

                // 4. HTTP 기본 인증 비활성화
                .httpBasic().disable(); // rest api 만을 고려하여 기본 설정은 해제

        return http.build();
    }
}
