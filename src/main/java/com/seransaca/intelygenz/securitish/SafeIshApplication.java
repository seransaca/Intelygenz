package com.seransaca.intelygenz.securitish;

import com.seransaca.intelygenz.securitish.security.JWTAuthorizationFilter;
import com.seransaca.intelygenz.securitish.service.exceptions.UnauthorizedException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
public class SafeIshApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeIshApplication.class, args);
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter{

		@Override
		protected void configure(HttpSecurity http) throws Exception{
			http.cors().and().csrf().disable()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.exceptionHandling()
					.authenticationEntryPoint(
							(request, response, ex) -> {
								response.sendError(
										HttpServletResponse.SC_UNAUTHORIZED,
										ex.toString()
								);
							}
					)
					.and()
					.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests()
					.antMatchers(HttpMethod.POST, "/safebox").permitAll()
					.antMatchers(HttpMethod.GET, "/safebox/{id}/open").permitAll()
					.anyRequest().authenticated();
		}
	}

}
