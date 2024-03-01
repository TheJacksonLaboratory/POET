package org.monarchinitiative.poet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.io.IOException;

@Configuration
@EnableWebMvc
public class PoetConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST", "PUT", "DELETE", "OPTIONS", "HEAD");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*")
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
