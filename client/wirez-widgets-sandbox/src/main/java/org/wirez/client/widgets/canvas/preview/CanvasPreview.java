package org.wirez.client.widgets.canvas.preview;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.uberfire.client.mvp.UberView;
import org.wirez.core.client.canvas.Canvas;
import org.wirez.core.client.canvas.Layer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
TODO: CanvasView's layer must be added into the DOM AFTER the replicated layer from this widget's view has been created and
 added into DOM's as well -> fire event for this.

Comments and usage from Mark
*****************************
I setup a replicating layer system, for fast mirroring at the raw Context2D level.
https://github.com/ahome-it/lienzo-core/pull/121

You will need to replicate both the drag layer and any other areas you wan to replicate.
Setting up is a little hackish, but this shows setting a simple 200, 200 zoomed out mirror.
The impl within is fairly clean and self contained.

    LienzoPanel panel = new LienzoPanel(200, 200);
    RootPanel.get().add(panel);

    Layer replicatingLayer = new Layer();
    panel.add(replicatingLayer);


    replicatingLayer.getCanvasElement();
    replicatingLayer.getContext().scale(0.2, 0.2);

    Layer replicatingDragLayer = panel.getViewport().getDragLayer();
    replicatingDragLayer.getContext().scale(0.2, 0.2);

    panel = new LienzoPanel( new Viewport(1000, 1000, replicatingDragLayer) );
    RootPanel.get().add(panel);

    layer = new Layer();
    layer.setReplicatedLayer(replicatingLayer);
    panel.add( layer );

You can see the video here:
http://screencast.com/t/P0iLObFQo

I think the overlay replication is not quite yet, so that needs fixing still.
Also I think it would be good to be able to draw an additional rectangle, that represents the zoomed in viewport
of the source against the zoomed out but scaled replication.


 */
@Dependent
public class CanvasPreview implements IsWidget {

    private static Logger LOGGER = Logger.getLogger(CanvasPreview.class.getName());

    public interface View extends UberView<CanvasPreview> {

        enum MediatorType {

            MOUSE_PAN, MOUSE_ZOOM_WHEEL;

        }

        enum MediatorEventFilter {

            CTROL, META, SHIFT, ALT;

        }

        View show( Layer layer, int width, int height );

        View addMediator( MediatorType type, MediatorEventFilter eventFilter );

        View clear();
        
    }

    View view;
    
    @Inject
    public CanvasPreview(final View view) {
        this.view = view;
    }

    @PostConstruct
    public void init() {
        view.init(this);
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    public void show( final Canvas<?> canvas,
                      final int width,
                      final int height ) {
        clear();
        
        if ( null != canvas ) {
            
            final Layer layer = canvas.getLayer();

            view.show( layer, width, height );

            /*view.addMediator( View.MediatorType.MOUSE_PAN, null );
            view.addMediator( View.MediatorType.MOUSE_ZOOM_WHEEL, View.MediatorEventFilter.CTROL );*/

        }

    }

    public void clear() {
        view.clear();
    }

    private void log(final Level level, final String message) {
        if ( LogConfiguration.loggingIsEnabled() ) {
            LOGGER.log(level, message);
        }
    }

}
