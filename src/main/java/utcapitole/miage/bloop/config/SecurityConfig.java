package utcapitole.miage.bloop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // A enlever au plus vite
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/utilisateurs","/api/utilisateurs/**","/login", "/profil/register", "/profil/register_user", "confirm", "/confirm**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()

                )

               .formLogin(form -> form
                     //.loginPage("/login") // ou autre page personnalisée
                      .permitAll()
                )
               .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
