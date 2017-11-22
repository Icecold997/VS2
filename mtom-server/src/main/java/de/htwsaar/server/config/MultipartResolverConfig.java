package de.htwsaar.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Provides the MultipartResolver beans necessary to use multipart requests/responses.
 */
@Configuration
public class MultipartResolverConfig {

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}

	@Bean
	public CommonsMultipartResolver filterMultipartResolver() {
		final CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		return resolver;
	}


}
