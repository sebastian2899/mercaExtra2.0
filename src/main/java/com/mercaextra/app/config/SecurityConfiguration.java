package com.mercaextra.app.config;

import com.mercaextra.app.security.*;
import com.mercaextra.app.security.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import tech.jhipster.config.JHipsterProperties;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JHipsterProperties jHipsterProperties;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(
        TokenProvider tokenProvider,
        CorsFilter corsFilter,
        JHipsterProperties jHipsterProperties,
        SecurityProblemSupport problemSupport
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/**")
            .antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
        .and()
            .headers()
            .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
        .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
        .and()
            .permissionsPolicy().policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
        .and()
            .frameOptions()
            .deny()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/api/authenticate").permitAll()
        .antMatchers("/api/register").permitAll()
        .antMatchers("/api/*/producto-filtros/*").permitAll()
        .antMatchers("/api/*/productos-categoria/*").permitAll()
        .antMatchers("/api/*/productos-filtros-categoria/*").permitAll()
        .antMatchers(HttpMethod.GET,"/api/categoria-productos/**").permitAll()
        .antMatchers("/api/activate").permitAll()
        .antMatchers("/api/account/reset-password/init").permitAll()
        .antMatchers("/api/account/reset-password/finish").permitAll()
        .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)  
        .antMatchers("/api/logout-productos").permitAll()
        .antMatchers("/api/cajas/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/api/compras/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/api/empleados/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/api/egresos/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/api/itemFacturaVentas/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/api/notificacions/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/api/proveedors/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers(HttpMethod.GET,"/api/domiciliarios/**").authenticated()
        .antMatchers(HttpMethod.GET,"/api/productos/**").permitAll()
        .antMatchers("/api/productos-similares/**").permitAll()
        .antMatchers("/api/another-similar-products/**").permitAll()
        .antMatchers(HttpMethod.PUT,"/api/pedidos/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers(HttpMethod.DELETE,"/api/pedidos/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers(HttpMethod.POST,"api/productos/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers(HttpMethod.PUT,"api/productos/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers(HttpMethod.DELETE,"api/productos/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .antMatchers("/api/**/discount-products-aviable").permitAll()
        .antMatchers("/api/**").authenticated()        
        .antMatchers("/management/health").permitAll()
        .antMatchers("/management/health/**").permitAll()
        .antMatchers("/management/info").permitAll()
        .antMatchers("/management/prometheus").permitAll()
        .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .httpBasic()
        .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
