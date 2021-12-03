package com.raynigon.ecs.logging.access;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@ConfigurationProperties("raynigon.logging.access")
public class AccessLogProperties {

    private final List<String> excludeEndpoints = new ArrayList<>();

    private final boolean exportBody = false;
}