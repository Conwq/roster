package ru.sergey.patseev.jwtservicespringbootstrarter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.sergey.patseev.jwtservicespringbootstrarter.service.JwtService;

/**
 * Configuration class for JWT Service.
 */
@Configuration
@ComponentScan("ru.sergey.patseev.jwtservicespringbootstrarter")
@PropertySource("classpath:application.yml")
public class JwtServiceConfig {

	/**
	 * Bean for JWT Service.
	 *
	 * @return Instance of JwtService
	 */
	@Bean
	public JwtService jwtService() {
		return new JwtService();
	}
}
