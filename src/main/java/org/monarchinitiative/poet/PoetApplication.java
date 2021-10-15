package org.monarchinitiative.poet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@SpringBootApplication
public class PoetApplication {
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(PoetApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**/*");
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler(env.getProperty("api.client").split(","))
						.addResourceLocations("classpath:/static/")
						.resourceChain(true)
						.addResolver(new PathResourceResolver() {
							@Override
							protected Resource getResource(String resourcePath, Resource location) throws IOException {
								Resource requestedResource = location.createRelative(resourcePath);
								return requestedResource.exists() && requestedResource.isReadable() ?
										requestedResource : new ClassPathResource("/static/index.html");
							}
						});
				registry.setOrder(Integer.MAX_VALUE);
			}
		};
	}
}
