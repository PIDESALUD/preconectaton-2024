package pe.gob.minsa.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import pe.gob.minsa.bean.AuditFilter;


@Configuration
@ComponentScan({"pe.gob.minsa.*"})
@PropertySource(value= {"classpath:application.properties"})
public class AppConfig {


    @Bean
	public FilterRegistrationBean<AuditFilter> foo(final AuditFilter filter) {
		final FilterRegistrationBean<AuditFilter> registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns("/servicio/v1.0/*");
		registrationBean.setOrder(1);

		return registrationBean;
	}
    
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
