package org.wirez.core.client.canvas.loading;

import com.google.gwt.logging.client.LogConfiguration;
import org.wirez.core.client.canvas.AbstractCanvas;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
public class CanvasLoadingObserver {

    private static Logger LOGGER = Logger.getLogger( CanvasLoadingObserver.class.getName() );

    public interface ObserverCallback {

        void showLoading();

        void hideLoading();

    }

    private final Event<CanvasLoadingStartedResponse> loadingResponseEvent;
    private ObserverCallback callback;
    private String canvasUUID;

    protected CanvasLoadingObserver() {
        this.loadingResponseEvent = null;
        this.callback = null;
        this.canvasUUID = null;
    }

    @Inject
    public CanvasLoadingObserver( final Event<CanvasLoadingStartedResponse> responseEvent ) {
        this.loadingResponseEvent = responseEvent;
        this.callback = null;
        this.canvasUUID = null;
    }

    public void registerObserver( final AbstractCanvas canvas,
                                  final ObserverCallback callback ) {
        this.canvasUUID = canvas.getUUID();
        this.callback = callback;

    }

    private void fireLoadingStartedResponse( final String uuid ) {

        log( "Executing showLoading..." );
        this.callback.showLoading();

        log( "Fire canvas loading started response [" + uuid + "]" );
        loadingResponseEvent.fire( new CanvasLoadingStartedResponse( uuid ) );


    }

    private void onCanvasLoadingStartedRequest( @Observes CanvasLoadingStartedRequest request) {

        final String uuid = request.getRequestUUID();

        if ( checkEventContext( uuid ) ) {

            log( "Received canvas loading started request [" + uuid + "]" );

            fireLoadingStartedResponse( uuid );

        }


    }

    private void onCanvasLoadingCompleted( @Observes CanvasLoadingCompleted request) {

        final String uuid = request.getRequestUUID();

        if ( checkEventContext( uuid ) ) {

            log( "Received canvas loading completed [" + uuid + "]" );

            log( "Executing hideLoading..." );
            this.callback.hideLoading();

        }

    }

    private boolean checkEventContext( final String uuid ) {
        return null != callback && null != canvasUUID && canvasUUID.equals( uuid );
    }

    private static void log( final String message ) {
        if ( LogConfiguration.loggingIsEnabled() ) {
            LOGGER.log( Level.SEVERE, message );
        }
    }

}
