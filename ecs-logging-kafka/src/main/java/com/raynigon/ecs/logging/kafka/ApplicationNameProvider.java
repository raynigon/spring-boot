package com.raynigon.ecs.logging.kafka;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ApplicationNameProvider {

    private static String sApplicationName = "unknown";

    @Value("${spring.application.name}")
    private String applicationName;

    @PostConstruct
    void init(){
        sApplicationName = applicationName;
    }

    public static String getApplicationName(){
        return sApplicationName;
    }
}
