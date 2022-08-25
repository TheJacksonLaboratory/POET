package org.monarchinitiative.poet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import java.io.IOException;

@Configuration
public class PoetConfiguration {

    @Autowired
    private Environment env;

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

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/{spring:(?!assets)\\w+}")
                        .setViewName("forward:/");
                registry.addViewController("/**/{spring:\\w+}")
                        .setViewName("forward:/");
                registry.addViewController("/{spring:(?!assets)\\w+}/**{spring:?!(\\.js|\\.css)$}")
                        .setViewName("forward:/");
            }
        };
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.monarchinitiative.model.responses.chebi");
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setDefaultUri("https://www.ebi.ac.uk/webservices/chebi/2.0/webservice?wsdl");
        template.setMarshaller(marshaller());
        template.setUnmarshaller(marshaller());

        return template;
    }
}
