package ru.practicum.bankapp.ui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.practicum.bankapp.ui.controller.UiUrls;
import ru.practicum.bankapp.ui.service.impl.CustomAuditLogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(UiUrls.SignUp.FULL).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(customizer -> customizer
                        .permitAll()
                        .loginPage(UiUrls.LogIn.FULL)
                        .usernameParameter("login")
                        .passwordParameter("password")
                        .defaultSuccessUrl(UiUrls.Main.FULL)
                )
//                .logout(Customizer.withDefaults())
//                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout
                        .logoutUrl(UiUrls.LogOut.FULL)                  // URL выхода (по умолчанию /logout)
                        .logoutSuccessUrl(UiUrls.LogIn.FULL + "?logout")     // Страница после выхода (по умолчанию /login?logout)
                        .invalidateHttpSession(true)           // Аннулировать HttpSession
                        .clearAuthentication(true)             // Удалить Authentication из SecurityContext
                        .deleteCookies("JSESSIONID", "remember-me") // Удалить куки сессии и «запомнить меня»
                        .addLogoutHandler(new CustomAuditLogoutHandler()) // Добавить свои действия при выходе
                        .permitAll()) // Разрешить выход всем)

        ;
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    AuthenticationManager authenticationManager() {
//
//    }
}
