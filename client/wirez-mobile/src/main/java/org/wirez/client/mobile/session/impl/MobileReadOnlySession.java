package org.wirez.client.mobile.session.impl;

import org.wirez.client.mobile.api.platform.Mobile;
import org.wirez.core.client.canvas.AbstractCanvas;
import org.wirez.core.client.canvas.AbstractCanvasHandler;
import org.wirez.core.client.canvas.controls.pan.PanControl;
import org.wirez.core.client.canvas.controls.select.SelectionControl;
import org.wirez.core.client.canvas.controls.zoom.Wheel;
import org.wirez.core.client.canvas.controls.zoom.ZoomControl;
import org.wirez.core.client.session.impl.AbstractReadOnlySession;
import org.wirez.core.client.shape.Shape;
import org.wirez.core.graph.Element;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class MobileReadOnlySession extends AbstractReadOnlySession {

    @Inject
    public MobileReadOnlySession(final AbstractCanvas canvas,
                                 final AbstractCanvasHandler canvasHandler,
                                 final @Mobile SelectionControl<AbstractCanvasHandler, Element> selectionControl,
                                 final @Wheel ZoomControl<AbstractCanvas> zoomControl,
                                 final PanControl<AbstractCanvas> panControl) {
        
        super( canvas, canvasHandler, selectionControl, zoomControl, panControl );
        
    }

    @Override
    public String toString() {
        return "MobileReadOnlySession";
    }
    
}
