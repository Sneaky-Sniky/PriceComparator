package com.nikoletta.pricecomparator.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Configuration
public class JacksonConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(sdf);
        mapper.addMixIn(Object.class, IgnoreHibernatePropertiesInJackson.class);
        mapper.findAndRegisterModules();

        return mapper;
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private abstract static class IgnoreHibernatePropertiesInJackson{ }
}