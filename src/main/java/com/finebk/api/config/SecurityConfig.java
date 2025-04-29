package com.finebk.api.config;

import com.finebk.api.repository.UserRepository;
import com.finebk.api.security.JwtAuthenticationEntryPoint;
import com.finebk.api.security.JwtAuthenticationFilter;
import com.finebk.api.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true
)
public class SecurityConfig {

	private final CustomUserDetailsServiceImpl customUserDetailsService;
	private final JwtAuthenticationEntryPoint unauthorizedHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	public SecurityConfig(UserRepository userRepository,
						  CustomUserDetailsServiceImpl customUserDetailsService,
						  JwtAuthenticationEntryPoint unauthorizedHandler,
						  JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.customUserDetailsService = customUserDetailsService;
		this.unauthorizedHandler = unauthorizedHandler;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(Customizer.withDefaults())                 // enable CORS filter
				.csrf(csrf -> csrf.disable())                    // disable CSRF for JWT
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint(unauthorizedHandler)
				)
				.sessionManagement(sm -> sm
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.authorizeHttpRequests(auth -> auth             // endpoint authorization rules
						.requestMatchers(HttpMethod.GET,  "/api/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
						.requestMatchers(HttpMethod.GET,
								"/api/users/checkUsernameAvailability",
								"/api/users/checkEmailAvailability"
						).permitAll()
						.anyRequest().authenticated()
				);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
