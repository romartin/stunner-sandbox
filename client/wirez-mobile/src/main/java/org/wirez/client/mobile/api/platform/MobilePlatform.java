package org.wirez.client.mobile.api.platform;

import org.wirez.client.mobile.session.impl.MobileFullSession;
import org.wirez.client.mobile.session.impl.MobileReadOnlySession;
import org.wirez.core.client.api.platform.ClientPlatform;
import org.wirez.core.client.session.impl.DefaultCanvasFullSession;
import org.wirez.core.client.session.impl.DefaultCanvasReadOnlySession;
import org.wirez.core.client.session.impl.DefaultCanvasSessionProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class MobilePlatform implements ClientPlatform, DefaultCanvasSessionProducer {
    
    private static final String[] USER_AGENTS = {""};

    Instance<MobileReadOnlySession> readOnlySessions;
    Instance<MobileFullSession> fullSessions;

    @Inject
    public MobilePlatform( final Instance<MobileReadOnlySession> readOnlySessions, 
                           final Instance<MobileFullSession> fullSessions) {
        this.readOnlySessions = readOnlySessions;
        this.fullSessions = fullSessions;
    }

    @Override
    public boolean supports( final String platform ) {
        return null != platform && ( platform.contains( "arm" ) || platform.contains("iOS") );
    }

    @Override
    public String[] getUserAgents() {
        return USER_AGENTS;
    }

    @Override
    public DefaultCanvasReadOnlySession newReadOnlySession() {
        return readOnlySessions.get();
    }

    @Override
    public DefaultCanvasFullSession newFullSession() {
        return fullSessions.get();
    }
    
}
