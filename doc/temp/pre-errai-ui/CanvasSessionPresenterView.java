package org.wirez.client.widgets.session.presenter.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.client.ui.Row;
import org.wirez.client.widgets.session.presenter.CanvasSessionPresenter;

import javax.enterprise.context.Dependent;

@Dependent
public class CanvasSessionPresenterView extends Composite implements CanvasSessionPresenter.View {

    interface ViewBinder extends UiBinder<Widget, CanvasSessionPresenterView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField
    FlowPanel loadingPanel;

    @UiField
    HorizontalPanel toolbarPanel;
    
    @UiField
    FlowPanel canvasPanel;
    
    public CanvasSessionPresenterView() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public CanvasSessionPresenter.View setToolbar(final IsWidget widget) {
        toolbarPanel.clear();
        toolbarPanel.add( widget );
        return this;
    }

    @Override
    public CanvasSessionPresenter.View setCanvas(final IsWidget widget) {
        canvasPanel.clear();
        canvasPanel.add( widget );
        return this;
    }

    @Override
    public CanvasSessionPresenter.View setLoading( final boolean loading ) {
        loadingPanel.setVisible( loading );
        return this;
    }

    @Override
    public void destroy() {
        this.removeFromParent();
    }

}
