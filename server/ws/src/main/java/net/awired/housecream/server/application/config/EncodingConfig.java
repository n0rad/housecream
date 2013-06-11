package net.awired.housecream.server.application.config;

import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

@Configuration
public class EncodingConfig {

    @Bean(name = "objectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(new JacksonAnnotationIntrospector(),
                new JaxbAnnotationIntrospector()));
        return objectMapper;
    }

    @Bean(name = "jaxbProvider")
    public JAXBElementProvider<Object> jAXBElementProvider() {
        return new JAXBElementProvider<Object>();
    }

    @Bean(name = "jsonProvider")
    public JacksonJsonProvider jacksonJsonProvider() {
        return new JacksonJsonProvider(objectMapper());
    }

}
