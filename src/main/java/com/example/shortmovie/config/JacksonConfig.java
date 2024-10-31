package com.example.shortmovie.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 숫자 직렬화를 위한 커스텀 모듈
        SimpleModule module = new SimpleModule();
        module.addSerializer(Number.class, new StdSerializer<Number>(Number.class) {
            @Override
            public void serialize(Number value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider provider) throws IOException {
                gen.writeString(value.toString()); // 숫자를 문자열로 직렬화
            }
        });
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.registerModule(module);
        return objectMapper;
    }
}
