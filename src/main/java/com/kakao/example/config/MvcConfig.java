package com.kakao.example.config;

import framework.util.annotation.Bean;
import framework.util.annotation.Component;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import static framework.util.annotation.Component.ComponentType.CONFIGURATION;

@Component(type = CONFIGURATION)
public class MvcConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }
}
