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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class LoadDiagramWizardScreenView extends Composite implements LoadDiagramWizardScreen.View {

    interface ViewBinder extends UiBinder<Widget, LoadDiagramWizardScreenView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField
    FlowPanel mainPanel;
    
    LoadDiagramWizardScreen presenter;

    @Override
    public void init(final LoadDiagramWizardScreen presenter) {
        this.presenter = presenter;
        initWidget( uiBinder.createAndBindUi( this ) );
        clear();
    }

    @Override
    public LoadDiagramWizardScreen.View add(final IsWidget view) {
        mainPanel.add( view );
        return this;
    }

    @Override
    public LoadDiagramWizardScreen.View clear() {
        mainPanel.clear();
        return this;
    }

}
