//package com.sca.notesspringboot.config;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Autowired
//    UserDetailsService userDetailsService;
//
//    @Autowired
//    MyAuthenctiationSuccessHandler myAuthenctiationSuccessHandler;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/static/**"
//                        )
//                        .permitAll()  // 静态资源放行
//                        .anyRequest().authenticated()              // 其他请求需要认证
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")                       // 指定自定义的登录页面
//                        .loginProcessingUrl("/form")               // 指定处理登录请求的URL
//                        .defaultSuccessUrl("/main")              // 登录成功后跳转的URL（如果需要）
//                        .successHandler(myAuthenctiationSuccessHandler) // 登录成功处理器
//                        .failureUrl("/fail").permitAll()           // 登录失败跳转页面
//                )
//                .csrf(csrf -> csrf.disable());                 // 关闭CSRF保护
//
//        return http.build();
//    }
//}
//
