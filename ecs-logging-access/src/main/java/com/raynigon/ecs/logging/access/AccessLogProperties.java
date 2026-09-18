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

    private int bodySizeLimit = 10_000;

    /**
     * When {@code exportBody} is enabled, {@code multipart/form-data} requests are, by
     * default, still excluded from body export. Capturing the request body for such
     * requests requires reading the raw input stream before the servlet container has
     * parsed it into parts, which both prevents the container's own multipart parsing
     * from running afterwards (causing request failures) and would only yield a mix of
     * MIME boundaries, part headers and raw binary file bytes rather than anything
     * useful to log. Set to {@code false} to opt back into the legacy, unsafe behaviour.
     */
    private boolean excludeMultipartBody = true;
}
