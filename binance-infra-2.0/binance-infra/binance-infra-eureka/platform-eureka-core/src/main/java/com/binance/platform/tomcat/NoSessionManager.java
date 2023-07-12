package com.binance.platform.tomcat;

import java.io.IOException;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Session;
import org.apache.catalina.session.ManagerBase;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoSessionManager extends ManagerBase implements Lifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoSessionManager.class);

    @Override
    protected synchronized void startInternal() throws LifecycleException {
        super.startInternal();
        try {
            load();
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            t.printStackTrace();
        }
        setState(LifecycleState.STARTING);
    }

    @Override
    protected synchronized void stopInternal() throws LifecycleException {
        setState(LifecycleState.STOPPING);
        try {
            unload();
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            t.printStackTrace();
        }
        super.stopInternal();
    }

    @Override
    public void load() throws ClassNotFoundException, IOException {
        LOGGER.info("####################################################################################");
        LOGGER.info("#");
        LOGGER.info("# Tomcat");
        LOGGER.info("#");
        LOGGER.info("HttpSession Close, if you want open it , you can set spring.application.enablesession=true");
        LOGGER.info("#");
        LOGGER.info("####################################################################################");
      
    }

    @Override
    public void unload() throws IOException {
        LOGGER.info("####################################################################################");
        LOGGER.info("#");
        LOGGER.info("# Tomcat");
        LOGGER.info("#");
        LOGGER.info("HttpSession Close, if you want open it , you can set spring.application.enablesession=true");
        LOGGER.info("#");
        LOGGER.info("####################################################################################");
    }

    @Override
    public Session createSession(String sessionId) {
        return null;
    }

    @Override
    public Session createEmptySession() {
        return null;
    }

    @Override
    public void add(Session session) {}

    @Override
    public Session findSession(String id) throws IOException {
        return null;
    }

    @Override
    public Session[] findSessions() {
        return null;
    }

    @Override
    public void processExpires() {}

}
