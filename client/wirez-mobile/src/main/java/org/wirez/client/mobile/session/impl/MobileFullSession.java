package org.wirez.client.mobile.session.impl;

import org.wirez.client.mobile.api.platform.Mobile;
import org.wirez.core.client.canvas.AbstractCanvas;
import org.wirez.core.client.canvas.AbstractCanvasHandler;
import org.wirez.core.client.canvas.controls.resize.ResizeControl;
import org.wirez.core.client.command.CanvasCommandManager;
import org.wirez.core.client.canvas.controls.actions.CanvasNameEditionControl;
import org.wirez.core.client.canvas.controls.actions.CanvasSaveControl;
import org.wirez.core.client.canvas.controls.actions.CanvasValidationControl;
import org.wirez.core.client.canvas.controls.builder.ElementBuilderControl;
import org.wirez.core.client.canvas.controls.builder.impl.Observer;
import org.wirez.core.client.canvas.controls.connection.ConnectionAcceptorControl;
import org.wirez.core.client.canvas.controls.containment.ContainmentAcceptorControl;
import org.wirez.core.client.canvas.controls.docking.DockingAcceptorControl;
import org.wirez.core.client.canvas.controls.drag.DragControl;
import org.wirez.core.client.canvas.controls.palette.CanvasPaletteControl;
import org.wirez.core.client.canvas.controls.pan.PanControl;
import org.wirez.core.client.canvas.controls.select.SelectionControl;
import org.wirez.core.client.canvas.controls.toolbox.ToolboxControl;
import org.wirez.core.client.canvas.controls.zoom.Wheel;
import org.wirez.core.client.canvas.controls.zoom.ZoomControl;
import org.wirez.core.client.session.impl.AbstractFullSession;
import org.wirez.core.graph.Element;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class MobileFullSession extends AbstractFullSession {

    @Inject
    public MobileFullSession(final AbstractCanvas canvas,
                             final AbstractCanvasHandler canvasHandler,
                             final ResizeControl<AbstractCanvasHandler, Element> resizeControl,
                             final CanvasValidationControl<AbstractCanvasHandler> canvasValidationControl,
                             final CanvasSaveControl<AbstractCanvasHandler> canvasSaveControl,
                             final CanvasPaletteControl<AbstractCanvasHandler> canvasPaletteControl,
                             final CanvasCommandManager<AbstractCanvasHandler> canvasCommandManager,
                             final ConnectionAcceptorControl<AbstractCanvasHandler> connectionAcceptorControl,
                             final ContainmentAcceptorControl<AbstractCanvasHandler> containmentAcceptorControl,
                             final DockingAcceptorControl<AbstractCanvasHandler> dockingAcceptorControl,
                             final @Mobile SelectionControl<AbstractCanvasHandler, Element> selectionControl,
                             final CanvasNameEditionControl<AbstractCanvasHandler, Element> canvasNameEditionControl,
                             final DragControl<AbstractCanvasHandler, Element> dragControl,
                             final ToolboxControl<AbstractCanvasHandler, Element> toolboxControl,
                             final @Observer  ElementBuilderControl<AbstractCanvasHandler> builderControl,
                             final @Wheel ZoomControl<AbstractCanvas> zoomControl,
                             final PanControl<AbstractCanvas> panControl) {
        
        super( canvas, canvasHandler, resizeControl, canvasValidationControl, canvasSaveControl, canvasPaletteControl,
                selectionControl, zoomControl, panControl, canvasCommandManager, connectionAcceptorControl,
                containmentAcceptorControl, dockingAcceptorControl, canvasNameEditionControl,  dragControl,
                toolboxControl, builderControl );
        
    }

    @Override
    public String toString() {
        return "MobileFullSession";
    }
}
