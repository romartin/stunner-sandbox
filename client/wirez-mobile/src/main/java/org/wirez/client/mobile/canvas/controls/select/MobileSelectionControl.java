package org.wirez.client.mobile.canvas.controls.select;

import com.google.gwt.core.client.GWT;
import org.wirez.client.mobile.api.platform.Mobile;
import org.wirez.core.client.animation.AnimationFactory;
import org.wirez.core.client.canvas.controls.select.AbstractSelectionControl;
import org.wirez.core.client.canvas.event.selection.CanvasClearSelectionEvent;
import org.wirez.core.client.canvas.event.selection.CanvasElementSelectedEvent;
import org.wirez.core.client.shape.Shape;
import org.wirez.core.client.shape.view.HasEventHandlers;
import org.wirez.core.client.shape.view.ShapeView;
import org.wirez.core.client.shape.view.event.TouchEvent;
import org.wirez.core.client.shape.view.event.TouchHandler;
import org.wirez.core.client.shape.view.event.ViewEventType;
import org.wirez.core.graph.Element;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Dependent
@Mobile
public final class MobileSelectionControl extends AbstractSelectionControl {

    @Inject
    public MobileSelectionControl(final Event<CanvasElementSelectedEvent> elementSelectedEventEvent,
                                  final Event<CanvasClearSelectionEvent> clearSelectionEventEvent ) {

        super( elementSelectedEventEvent, clearSelectionEventEvent );

    }

    /*
        **************************************************************
        *               CANVAS CONTROL METHODS
        ***************************************************************
     */


    @Override
    public void register( final Element element,
                          final Shape shape ) {

        // Selection handling.
        final ShapeView shapeView = shape.getShapeView();
        
        if ( shapeView instanceof HasEventHandlers ) {

            final HasEventHandlers hasEventHandlers = (HasEventHandlers) shapeView;

            // Touch event.
            final TouchHandler touchHandler = new TouchHandler() {
                @Override
                public void start( final TouchEvent event ) {

                    log( "TouchStart", event );

                    final boolean isSelected = isSelected( element );

                    MobileSelectionControl.super.handleElementSelection( element, isSelected, !event.isShiftKeyDown() );
                    
                }

                @Override
                public void move( final TouchEvent event ) {

                    log( "TouchMove", event );
                    
                }

                @Override
                public void end( final TouchEvent event ) {

                    log( "TouchEnd", event );
                    
                }

                @Override
                public void cancel( final TouchEvent event ) {

                    log( "TouchCancel", event );
                    
                }

                @Override
                public void handle( final TouchEvent event ) {

                    log( "TouchHandle", event );
                    
                }
            };
            
            hasEventHandlers.addHandler( ViewEventType.TOUCH, touchHandler );

            registerHandler( shape.getUUID(), touchHandler );
            
            
        }
        
    }
    
    private void log( final String prefix, final TouchEvent event ) {

        GWT.log( prefix + " -> Touch [" + event.getTouchX() + ", " + event.getTouchY() + "] " );
        
        
    }
    
}
