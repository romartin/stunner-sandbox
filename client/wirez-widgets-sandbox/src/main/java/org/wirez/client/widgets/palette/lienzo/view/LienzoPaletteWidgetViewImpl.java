package org.wirez.client.widgets.palette.lienzo.view;

import com.ait.lienzo.client.core.shape.Group;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.wirez.core.client.components.glyph.ShapeGlyphDragHandler;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class LienzoPaletteWidgetViewImpl extends AbstractLienzoPaletteWidgetView {

    interface ViewBinder extends UiBinder<Widget, LienzoPaletteWidgetViewImpl> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField
    FlowPanel mainPanel;

    @UiField
    FlowPanel noCanvasPanel;

    @UiField
    SimplePanel palettePanel;

    @UiField
    FlowPanel paletteContainer;

    @Inject
    public LienzoPaletteWidgetViewImpl( final ShapeGlyphDragHandler<Group> shapeGlyphDragHandler ) {
        super( shapeGlyphDragHandler );
    }

    @PostConstruct
    public void init() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    protected Panel getParentPanel() {
        return palettePanel;
    }

    @Override
    public void showEmptyView( final boolean visible ) {
        super.showEmptyView( visible );
        noCanvasPanel.setVisible( visible );
    }

    @Override
    public void setBackgroundColor( final String color ) {
        super.setBackgroundColor( color );
        mainPanel.getElement().getStyle().setBackgroundColor( color );
    }

    @Override
    public void setMarginTop( final int mTop ) {
        super.setMarginTop( mTop );
        paletteContainer.getElement().getStyle().setMarginTop( mTop, Style.Unit.PX );
    }

}
