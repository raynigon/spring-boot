package com.raynigon.ecs.logging.access.logback;

import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.BasicStatusManager;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LifeCycleManager;
import ch.qos.logback.core.spi.*;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.ExecutorServiceUtil;
import ch.qos.logback.core.util.StatusPrinter;
import com.raynigon.ecs.logging.access.AccessLogProperties;
import com.raynigon.ecs.logging.access.context.IAccessLogContext;
import com.raynigon.ecs.logging.access.event.EcsAccessEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

@Slf4j
public class AccessLogContext implements IAccessLogContext, LifeCycle, Context, AppenderAttachable<IAccessEvent> {

    private final AccessLogProperties config;
    private final long birthTime;
    private final LogbackLock configurationLock;
    private final LifeCycleManager lifeCycleManager;
    private final StatusManager statusManager;
    private final Map<String, String> properties;
    private final Map<String, Object> objects;
    private final URL configLocation;
    private final AppenderAttachableImpl<IAccessEvent> appenderContainer;
    private final List<ConfigurationEventListener> eventListeners = new ArrayList<>();
    private SequenceNumberGenerator sequenceNumberGenerator;

    private ExecutorService executorService;
    private String contextName;
    private boolean started;
    ;

    public AccessLogContext(AccessLogProperties config) {
        this(config, "access-log", AccessLogContext.class.getResource("/logback-access.xml"), new AppenderAttachableImpl<>());
    }

    public AccessLogContext(AccessLogProperties config, String contextName, URL configLocation, AppenderAttachableImpl<IAccessEvent> appenderContainer) {
        super();
        Objects.requireNonNull(config);
        Objects.requireNonNull(contextName);
        Objects.requireNonNull(configLocation);
        Objects.requireNonNull(appenderContainer);
        this.config = config;
        this.contextName = contextName;
        this.configLocation = configLocation;
        this.appenderContainer = appenderContainer;
        this.birthTime = System.currentTimeMillis();
        this.configurationLock = new LogbackLock();
        this.lifeCycleManager = new LifeCycleManager();
        this.statusManager = new BasicStatusManager();
        this.properties = new HashMap<>();
        this.objects = new HashMap<>();
    }

    @Override
    public AccessLogProperties getConfig() {
        return config;
    }

    public final void appendEvent(EcsAccessEvent event) {
        Objects.requireNonNull(event);
        appenderContainer.appendLoopOnAppenders(event);
    }

    public void start() {
        executorService = ExecutorServiceUtil.newThreadPoolExecutor();

        try {
            JoranConfigurator jc = new JoranConfigurator();
            jc.setContext(this);
            jc.doConfigure(this.configLocation);
        } catch (Throwable e) {
            log.error("Unable to start access log", e);
        }

        StatusPrinter.printInCaseOfErrorsOrWarnings(this);
        started = true;
    }

    public void stop() {
        lifeCycleManager.reset();
        if (executorService != null) {
            ExecutorServiceUtil.shutdown(executorService);
        }
        executorService = null;
        started = false;
    }

    public boolean isStarted() {
        return this.started;
    }

    public String getProperty(String key) {
        Objects.requireNonNull(key);
        return properties.get(key);
    }

    public Object getObject(String key) {
        Objects.requireNonNull(key);
        return objects.get(key);
    }

    public void putProperty(String key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        this.properties.put(key, value);
    }

    public void putObject(String key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        this.objects.put(key, value);
    }

    public Map<String, String> getCopyOfPropertyMap() {
        return Collections.unmodifiableMap(this.properties);
    }

    public StatusManager getStatusManager() {
        return this.statusManager;
    }

    public String getName() {
        return this.contextName;
    }

    public void setName(String value) {
        Objects.requireNonNull(value);
        this.contextName = value;
    }

    public long getBirthTime() {
        return this.birthTime;
    }

    public Object getConfigurationLock() {
        return this.configurationLock;
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public void register(LifeCycle component) {
        this.lifeCycleManager.register(component);
    }

    @Override
    public SequenceNumberGenerator getSequenceNumberGenerator() {
        return this.sequenceNumberGenerator;
    }

    @Override
    public void setSequenceNumberGenerator(SequenceNumberGenerator sequenceNumberGenerator) {
        this.sequenceNumberGenerator = sequenceNumberGenerator;
    }

    @Override
    public void addConfigurationEventListener(ConfigurationEventListener listener) {
        eventListeners.add(listener);
    }

    @Override
    public void fireConfigurationEvent(ConfigurationEvent configurationEvent) {
        eventListeners.forEach((listener) -> listener.listen(configurationEvent));
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        throw new UnsupportedOperationException();
    }

    public void addScheduledFuture(ScheduledFuture scheduledFuture) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public void addAppender(Appender p0) {
        this.appenderContainer.addAppender(p0);
    }

    public void detachAndStopAllAppenders() {
        this.appenderContainer.detachAndStopAllAppenders();
    }

    @SuppressWarnings("unchecked")
    public boolean detachAppender(Appender p0) {
        return this.appenderContainer.detachAppender(p0);
    }

    public boolean detachAppender(String name) {
        return this.appenderContainer.detachAppender(name);
    }

    public Appender<IAccessEvent> getAppender(String name) {
        return this.appenderContainer.getAppender(name);
    }

    @SuppressWarnings("unchecked")
    public boolean isAttached(Appender p0) {
        return this.appenderContainer.isAttached(p0);
    }

    public Iterator<Appender<IAccessEvent>> iteratorForAppenders() {
        return this.appenderContainer.iteratorForAppenders();
    }
}

