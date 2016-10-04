package org.wirez.core.client.canvas.loading;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.Timer;
import org.uberfire.mvp.Command;
import org.wirez.core.client.canvas.AbstractCanvas;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
public class CanvasLoadingDispatcher {

    private static Logger LOGGER = Logger.getLogger( CanvasLoadingDispatcher.class.getName() );
    private static final int TIMEOUT_DURATION = 150;

    private final Event<CanvasLoadingStartedRequest> loadingStartedRequestEvent;
    private final Event<CanvasLoadingCompleted> loadingCompletedRequestEvent;
    private String requestUUID;
    private Command callback;
    private Timer timeout;

    protected CanvasLoadingDispatcher() {
        this.loadingStartedRequestEvent = null;
        this.loadingCompletedRequestEvent = null;
        this.requestUUID = null;
        this.timeout = null;
    }

    @Inject
    public CanvasLoadingDispatcher( final Event<CanvasLoadingStartedRequest> requestEvent,
                                    final Event<CanvasLoadingCompleted> loadingCompletedRequestEvent) {
        this.loadingStartedRequestEvent = requestEvent;
        this.loadingCompletedRequestEvent = loadingCompletedRequestEvent;
        this.requestUUID = null;
        this.timeout = null;
    }

    public void fireLoadingStarts( final AbstractCanvas canvas,
                             final Command callback ) {

        if ( null != this.requestUUID ) {

            throw new UnsupportedOperationException( "Only one loading request soported at same time." );
        }

        clearTimeout();

        this.requestUUID = getRequestUUID( canvas );
        this.callback = callback;

        this.timeout = new Timer() {
            @Override
            public void run() {
                log( "Timeout fired for canvas loading started request [" + CanvasLoadingDispatcher.this.requestUUID + "]" );
                processCanvasResponse();
            }
        };

        timeout.schedule( TIMEOUT_DURATION );

        log( "Fire canvas loading started request [" + this.requestUUID + "]" );
        loadingStartedRequestEvent.fire( new CanvasLoadingStartedRequest( this.requestUUID ) );

    }

    public void fireLoadingCompletes( final AbstractCanvas canvas ) {


        log( "Fire canvas loading complete request [" + this.requestUUID + "]" );

        loadingCompletedRequestEvent.fire( new CanvasLoadingCompleted( getRequestUUID( canvas ) ) );


    }

    private String getRequestUUID( final AbstractCanvas canvas ) {
        return canvas.getUUID();
    }

    private void clearTimeout() {

        if ( null != this.timeout ) {

            log( "Clearing timeout..." );
            if ( this.timeout.isRunning() ) {
                this.timeout.cancel();
            }

            this.timeout = null;
        }

    }

    private void processCanvasResponse() {

        clearTimeout();

        if ( null != this.callback ) {

            log( "Executing loading dispatcher callback..." );
            this.callback.execute();

        }


        this.requestUUID = null;
    }

    private void onCanvasLoadingResponse( @Observes CanvasLoadingStartedResponse response ) {

        if ( null != this.requestUUID && this.requestUUID.equals( response.getRequestUUID() ) ) {

            log( "Received canvas loading started response [" + response.getRequestUUID() + "]" );

            processCanvasResponse();

        }

    }

    private static void log( final String message ) {
        if ( LogConfiguration.loggingIsEnabled() ) {
            LOGGER.log( Level.SEVERE, message );
        }
    }

}
