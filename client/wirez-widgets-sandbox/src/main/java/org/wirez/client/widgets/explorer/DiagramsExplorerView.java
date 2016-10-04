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

package org.wirez.client.widgets.explorer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItemText;

public class DiagramsExplorerView extends Composite implements DiagramsExplorer.View {

    interface ViewBinder extends UiBinder<Widget, DiagramsExplorerView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField
    LinkedGroup group;

    DiagramsExplorer presenter;

    @Override
    public void init(final DiagramsExplorer presenter) {
        this.presenter = presenter;
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public DiagramsExplorer.View showEmpty() {
        final LinkedGroupItemText emptyItem = new LinkedGroupItemText( "There are no diagrams");
        group.add( emptyItem );
        return this;
    }

    @Override
    public DiagramsExplorer.View add( final IsWidget diagramRepresentationItem) {
        group.add( diagramRepresentationItem );
        return this;
    }

    @Override
    public DiagramsExplorer.View clear() {
        group.clear();
        return this;
    }

}
