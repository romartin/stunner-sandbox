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

package org.wirez.client.widgets.explorer.item;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.LinkedGroupItemText;

// TODO: Use thumbnail image.
public class DiagramLinkedGroupItemView extends Composite implements DiagramLinkedGroupItem.View {

    private static final String IMAGE_SIZE = "35px";

    interface ViewBinder extends UiBinder<Widget, DiagramLinkedGroupItemView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField
    LinkedGroupItem item;

    @UiField
    Heading heading;

    @UiField
    LinkedGroupItemText subHeading;

    DiagramLinkedGroupItem presenter;

    @Override
    public void init(final DiagramLinkedGroupItem presenter) {
        this.presenter = presenter;
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public DiagramLinkedGroupItem.View show( final String uuid,
                                             final String title,
                                             final String path,
                                             final SafeUri thumbUri) {

        heading.setText( title );
        subHeading.setText( uuid );
        item.setActive( false );
        item.addClickHandler(clickEvent -> {
            presenter.onClick();
        });
        return this;
    }

    @Override
    public DiagramLinkedGroupItem.View setActive( final boolean isActive) {
        item.setActive( isActive );
        return this;
    }

    private Image createThumbImage( final SafeUri thumbUri ) {

        final Image image = new Image(thumbUri);
        image.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        image.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        image.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        image.setSize(IMAGE_SIZE, IMAGE_SIZE);
        
        return image;
    }

}
