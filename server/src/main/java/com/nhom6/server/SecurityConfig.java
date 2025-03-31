//package com.nhom6.server;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//
//@Configuration
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable) // 🔥 Tắt CSRF để gọi API từ frontend
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/**").permitAll() // ✅ Cho phép tất cả API truy cập mà không cần login
//                        .anyRequest().permitAll()
//                )
//                .formLogin(form -> form.disable()) // 🛑 Tắt trang login mặc định
//                .httpBasic(httpBasic -> httpBasic.disable()); // 🛑 Tắt Basic Auth
//
//        return http.build();
//    }
//}
