/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wirez.client.widgets.canvas.preview;

import com.ait.lienzo.client.core.mediator.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.wirez.client.lienzo.LienzoLayer;
import org.wirez.client.widgets.canvas.FocusableLienzoPanelView;
import org.wirez.core.client.canvas.Layer;

import java.util.Iterator;

public class CanvasPreviewView extends Composite implements CanvasPreview.View {

    interface ViewBinder extends UiBinder<Widget, CanvasPreviewView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    
    @UiField
    FlowPanel mainPanel;

    CanvasPreview presenter;
    private FocusableLienzoPanelView panel;
    private com.ait.lienzo.client.core.shape.Layer canvasLayer = new com.ait.lienzo.client.core.shape.Layer();

    @Override
    public void init(final CanvasPreview presenter) {
        this.presenter = presenter;
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public CanvasPreview.View show(final Layer layer,
                                   final int width,
                                   final int height) {

        final LienzoLayer lienzoLayer = ( LienzoLayer ) layer;

        panel = new FocusableLienzoPanelView( width, height );

        mainPanel.add(panel);

        panel.add( canvasLayer );

        canvasLayer.getContext().scale(0.2, 0.2);

        com.ait.lienzo.client.core.shape.Layer replicatingDragLayer = panel.getViewport().getDragLayer();
        replicatingDragLayer.getContext().scale(0.2, 0.2);

        // TODO: lienzoLayer.getLienzoLayer().setReplicatedLayer(  canvasLayer );

        return this;
    }

    @Override
    public CanvasPreview.View addMediator( final MediatorType type,
                                           final MediatorEventFilter eventFilter ) {
        final Mediators mediators = canvasLayer.getViewport().getMediators();

        final IEventFilter iEventFilter = null != eventFilter ?  translate( eventFilter ) : null;

        final IEventFilter[] filters = null != iEventFilter ? new IEventFilter[] { iEventFilter } : null;

        final IMediator mediator = isZoomMediator( type ) ?
                createMouseWheelZoomMediator( filters ) : createMousePanMediator( filters );

        mediators.push( mediator );

        return this;
    }

    @Override
    public CanvasPreview.View clear() {
        destroyLienzoStuff();
        mainPanel.clear();
        return this;
    }

    private void destroyLienzoStuff() {

        if ( null != canvasLayer && null != canvasLayer.getViewport() ) {

            final Mediators mediators = canvasLayer.getViewport().getMediators();

            if ( null != mediators && mediators.iterator().hasNext() ) {

                final Iterator<IMediator> mediatorIterator = mediators.iterator();

                while ( mediatorIterator.hasNext() ) {

                    final IMediator mediator = mediatorIterator.next();

                    mediators.remove( mediator );

                }

            }

            canvasLayer.clear();
            canvasLayer.removeFromParent();

        }

        if ( null != panel ) {

            panel.clear();
            panel.removeFromParent();
            panel = null;
        }

    }

    private IEventFilter translate( final MediatorEventFilter eventFilter ) {

        switch ( eventFilter )  {

            case CTROL:
                return EventFilter.CONTROL;

            case SHIFT:
                return EventFilter.SHIFT;

            case META:
                return EventFilter.META;

            case ALT:
                return EventFilter.ALT;

        }

        return null;
    }

    private MouseWheelZoomMediator createMouseWheelZoomMediator( final IEventFilter[] filters ) {

        if ( null != filters ) {

            return new MouseWheelZoomMediator( filters );

        }

        return new MouseWheelZoomMediator();

    }

    private MousePanMediator createMousePanMediator( final IEventFilter[] filters ) {

        if ( null != filters ) {

            return new MousePanMediator( filters );

        }

        return new MousePanMediator();

    }

    private boolean isZoomMediator( final MediatorType type ) {
        return MediatorType.MOUSE_ZOOM_WHEEL.equals( type );
    }

    private boolean isPanMediator( final MediatorType type ) {
        return MediatorType.MOUSE_PAN.equals( type );
    }

}
