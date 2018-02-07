package org.afdemp.uisux.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.afdemp.uisux.service.impl.UserSecurityService;
import org.afdemp.uisux.utility.SecurityUtility;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private Environment env;

	@Autowired
	private UserSecurityService userSecurityService;

	private BCryptPasswordEncoder passwordEncoder() {
		return SecurityUtility.passwordEncoder();
	}

	private static final String[] MEMBER_MATCHERS = {
			"/css/**",
			"/js/**",
			"/image/**",
			"/login",
			"/fonts/**"
	};
	
	private static final String[] PUBLIC_MATCHERS = {
			"/css/**",
			"/js/**",
			"/login",
			"/image/**"
	};
	
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
//		 http
//		 .csrf().disable().cors().disable()
//	      .authorizeRequests()
//	        .antMatchers(PUBLIC_MATCHERS).permitAll() // #4
//	        .antMatchers(ADMIN_MATCHERS).hasRole("ADMIN") // #6
//	        .anyRequest().authenticated() // 7
//	        .and()
//	    .formLogin().failureUrl("/login?error")
//		.defaultSuccessUrl("/")  // #8
//	        .loginPage("/login") // #9
//	        .permitAll().and()
//			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//			.logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
//			.and()
//			.rememberMe();; // #5
		
		http
			.authorizeRequests().
//			antMatchers("/**").
			antMatchers(MEMBER_MATCHERS).
			hasRole("MEMBER").
			anyRequest().authenticated();
		
		http
		.authorizeRequests().
	/*	antMatchers("/**").*/
		antMatchers(PUBLIC_MATCHERS).
		permitAll().anyRequest().authenticated();

		http
			.csrf().disable().cors().disable()
			.formLogin().failureUrl("/login?error")
			.defaultSuccessUrl("/")
			.loginPage("/login").permitAll()
			.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
			.and()
			.rememberMe();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
	}

}
