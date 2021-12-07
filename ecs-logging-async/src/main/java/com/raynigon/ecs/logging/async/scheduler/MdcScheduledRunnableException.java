package com.raynigon.ecs.logging.async.scheduler;

import java.util.Map;


public class MdcScheduledRunnableException extends RuntimeException {

    private final Map<String, String> mdcTags;

    public MdcScheduledRunnableException(Map<String, String> mdcTags, Throwable t) {
        super(t);
        this.mdcTags = mdcTags;
    }

    public Map<String, String> getMdcTags() {
        return mdcTags;
    }
}
