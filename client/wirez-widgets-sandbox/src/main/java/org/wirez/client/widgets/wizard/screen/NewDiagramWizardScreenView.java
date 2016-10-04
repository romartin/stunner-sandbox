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

package org.wirez.client.widgets.wizard.screen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Popover;
import org.gwtbootstrap3.client.ui.constants.Placement;
import org.uberfire.mvp.Command;

public class NewDiagramWizardScreenView extends Composite implements NewDiagramWizardScreen.View {

    interface ViewBinder extends UiBinder<Widget, NewDiagramWizardScreenView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField
    Heading heading;

    @UiField
    FlowPanel emptyViewPanel;

    @UiField
    HorizontalPanel itemsPanel;

    NewDiagramWizardScreen presenter;

    @Override
    public void init(final NewDiagramWizardScreen presenter) {
        this.presenter = presenter;
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public NewDiagramWizardScreen.View setHeading(final String title) {
        heading.setText(title);
        return this;
    }

    @Override
    public NewDiagramWizardScreen.View addItem(final String name,
                                     final String description,
                                     final SafeUri thumbnailUri,
                                     final boolean isSelected,
                                     final Command clickHandler) {

        final VerticalPanel panel = new VerticalPanel();
        panel.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        panel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        panel.getElement().getStyle().setMargin(25, Style.Unit.PX);
        panel.setHeight("100%");
        
        
        final org.gwtbootstrap3.client.ui.Image image = new org.gwtbootstrap3.client.ui.Image(thumbnailUri);
        image.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        final double alpha = isSelected ? 1 : 0.2;
        image.getElement().getStyle().setPadding(25, Style.Unit.PX);
        image.getElement().setAttribute("style", "filter: alpha(opacity=5);opacity: " + alpha);
        image.addClickHandler(event -> clickHandler.execute());

        final Popover popover = new Popover();
        popover.setTitle( name );
        popover.setContent( description );
        popover.setWidget(image);
        popover.setContainer("body");
        popover.setPlacement(Placement.BOTTOM);
        popover.setShowDelayMs(1000);

        final HTML label = new HTML(name);
        final HorizontalPanel labelPanel = new HorizontalPanel();
        labelPanel.setWidth("100%");
        labelPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        labelPanel.add(label);

        // Mouse over / out handlers.
        panel.addDomHandler(event -> {
            panel.getElement().getStyle().setBackgroundColor("#f2f2f2");
            label.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        }, MouseOverEvent.getType());
        panel.addDomHandler(event -> {
            panel.getElement().getStyle().setBackgroundColor("#FFFFFF");
            label.getElement().getStyle().setFontWeight(Style.FontWeight.NORMAL);
        }, MouseOutEvent.getType());
        
        panel.add(popover);
        panel.add(labelPanel);
        itemsPanel.add(panel);

        return this;
    }

    @Override
    public NewDiagramWizardScreen.View showEmptyView() {
        emptyViewPanel.setVisible(true);
        return this;
    }

    @Override
    public NewDiagramWizardScreen.View clear() {
        emptyViewPanel.setVisible(false);
        itemsPanel.clear();
        return this;
    }

}
