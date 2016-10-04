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

package org.wirez.client.widgets.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Button;

public class CanvasWizardView extends Composite implements CanvasWizard.View {


    interface ViewBinder extends UiBinder<Widget, CanvasWizardView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );
    
    @UiField
    FlowPanel screenPanel;
    
    @UiField
    Button backButton;

    @UiField
    Button nextButton;
    
    private CanvasWizard presenter;
    
    @Override
    public void init(final CanvasWizard presenter) {
        this.presenter = presenter;
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public CanvasWizard.View setScreenWidget(final IsWidget widget) {
        screenPanel.clear();
        screenPanel.add(widget);
        return this;
    }

    @Override
    public CanvasWizard.View setBackButtonText(final String caption) {
        backButton.setTitle(caption);
        return this;
    }

    @Override
    public CanvasWizard.View setBackButtonVisible(boolean isEnabled) {
        backButton.setVisible(isEnabled);
        return this;
    }

    @Override
    public CanvasWizard.View setNextButtonText(final String caption) {
        nextButton.setTitle(caption);
        return this;
    }

    @Override
    public CanvasWizard.View setNextButtonVisible(final boolean isEnabled) {
        nextButton.setVisible(isEnabled);
        return this;
    }

    @Override
    public CanvasWizard.View setNextButtonEnabled(boolean isEnabled) {
        nextButton.setEnabled(isEnabled);
        return this;
    }

    @Override
    public CanvasWizard.View clear() {
        screenPanel.clear();
        return this;
    }

    @UiHandler("nextButton")
    public void onNextButtonClick(ClickEvent event) {
        presenter.onNextButtonClick();
    }

    @UiHandler("backButton")
    public void onBackButtonClick(ClickEvent event) {
        presenter.onBackButtonClick();
    }
}
