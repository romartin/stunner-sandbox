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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Button;

public class HomeWizardScreenView extends Composite implements HomeWizardScreen.View {

    interface ViewBinder extends UiBinder<Widget, HomeWizardScreenView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField
    Button newDiagramButton;

    @UiField
    Button loadDiagramButton;

    HomeWizardScreen presenter;

    @Override
    public void init(final HomeWizardScreen presenter) {
        this.presenter = presenter;
        initWidget( uiBinder.createAndBindUi( this ) );
        
        newDiagramButton.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                newDiagramButton.getElement().getStyle().setBorderWidth(5, Style.Unit.PX);
                presenter.onNewButtonClick();
            }
        }, ClickEvent.getType());

        loadDiagramButton.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                loadDiagramButton.getElement().getStyle().setBorderWidth(5, Style.Unit.PX);
                presenter.onLoadButtonClick();
            }
        }, ClickEvent.getType());
    }

    @Override
    public HomeWizardScreen.View clear() {
        newDiagramButton.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        loadDiagramButton.getElement().getStyle().setBorderWidth(0, Style.Unit.PX);
        return this;
    }

}
