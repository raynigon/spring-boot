package com.raynigon.ecs.logging.access;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ConfigurationProperties("raynigon.logging.access")
public class AccessLogProperties {

    private List<String> excludeEndpoints = new ArrayList<>();

    private boolean exportBody = false;
}
