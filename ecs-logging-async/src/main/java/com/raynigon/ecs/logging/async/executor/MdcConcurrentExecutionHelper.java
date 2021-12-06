package com.raynigon.ecs.logging.async.executor;

import org.slf4j.MDC;

import java.util.Map;

public class MdcConcurrentExecutionHelper {

    /**
     * Invoked before running a task.
     *
     * @param newValue the new MDC context
     * @return the old MDC context
     */
    public static Map<String, String> beforeExecution(Map<String, String> newValue)
    {
        Map<String, String> previous = MDC.getCopyOfContextMap();
        if (newValue == null)
            MDC.clear();
        else
            MDC.setContextMap(newValue);
        return previous;
    }

    /**
     * Invoked after running a task.
     *
     * @param oldValue the old MDC context
     */
    public static void afterExecution(Map<String, String> oldValue)
    {
        if (oldValue == null)
            MDC.clear();
        else
            MDC.setContextMap(oldValue);
    }
}
