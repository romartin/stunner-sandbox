package org.wirez.client.widgets.palette.lienzo.view;

import com.ait.lienzo.client.core.shape.Group;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.wirez.core.client.components.glyph.ShapeGlyphDragHandler;
import org.wirez.core.client.components.palette.view.PaletteView;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

// TODO: Refactor using WidgetFloatingView.

@Dependent
public class LienzoPaletteWidgetFloatingView extends AbstractLienzoPaletteWidgetView {

    interface ViewBinder extends UiBinder<Widget, LienzoPaletteWidgetFloatingView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    private double x;
    private double y;
    private boolean attached;

    @UiField
    SimplePanel mainPanel;

    protected LienzoPaletteWidgetFloatingView() {
        this( null );
    }

    @Inject
    public LienzoPaletteWidgetFloatingView( final ShapeGlyphDragHandler<Group> shapeGlyphDragHandler ) {
        super( shapeGlyphDragHandler );
        this.attached = false;
    }

    @PostConstruct
    public void init() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    protected void beforeShow( final PaletteView paletteView,
                               final int width,
                               final int height ) {
        attach();
        super.beforeShow(paletteView, width, height);
    }

    @Override
    public void destroy() {
        super.destroy();
        detach();
    }

    public LienzoPaletteWidgetFloatingView setX( final double x ) {
        this.x = x;
        return this;
    }

    public LienzoPaletteWidgetFloatingView setY( final double y ) {
        this.y = y;
        return this;
    }

    public void showPopup() {
        this.getElement().getStyle().setLeft( x, Style.Unit.PX );
        this.getElement().getStyle().setTop( y, Style.Unit.PX );
        this.getElement().getStyle().setDisplay( Style.Display.INLINE );
    }

    public  void hidePopup() {
        this.getElement().getStyle().setDisplay( Style.Display.NONE );
    }

    @Override
    protected Panel getParentPanel() {
        return mainPanel;
    }

    private void attach() {

        if ( !attached ) {

            RootPanel.get().add( this );
            this.getElement().getStyle().setPosition( Style.Position.FIXED );
            this.getElement().getStyle().setZIndex( 20 );
            hidePopup();
            attached = true;

        }

    }

    private void detach() {

        if ( attached ) {

            RootPanel.get().remove( this );
            attached = false;
        }

    }

}
