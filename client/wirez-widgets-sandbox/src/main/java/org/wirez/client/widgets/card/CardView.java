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

package org.wirez.client.widgets.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.card.client.ui.Back;
import org.gwtbootstrap3.extras.card.client.ui.Front;
import org.gwtbootstrap3.extras.card.client.ui.Trigger;
import org.uberfire.mvp.Command;

public class CardView extends Composite implements Card.View {

    interface ViewBinder extends UiBinder<Widget, CardView> {

    }

    private class CustomTrigger extends Trigger {

        public CustomTrigger() {
            this.setType(IconType.EXCHANGE);
            this.setSize(IconSize.LARGE);
            this.setTitle("Switch");
            this.addHandler(event -> {
                if ( null != switchCallback) {
                    switchCallback.execute();
                }

            }, ClickEvent.getType());
        }

    }

    private final CustomTrigger frontTrigger = new CustomTrigger();
    private final CustomTrigger backTrigger = new CustomTrigger();
    
    private class CustomFront extends Front {

        public CustomFront() {
            super();
            final int cs = super.getChildren().size();
            super.remove( cs - 1);
            super.add(frontTrigger);
        }

    }

    private class CustomBack extends Back {

        public CustomBack() {
            super();
            final int cs = super.getChildren().size();
            super.remove( cs - 1);
            super.add(backTrigger);
        }

    }
    
    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    private Command switchCallback;
    
    @UiField
    org.gwtbootstrap3.extras.card.client.ui.Card card;

    @UiField( provided = true )
    org.gwtbootstrap3.extras.card.client.ui.Front cardFront;

    @UiField( provided = true )
    org.gwtbootstrap3.extras.card.client.ui.Back cardBack;

    @UiField
    FlowPanel frontPanel;

    @UiField
    FlowPanel backPanel;
    
    @UiField
    FlowPanel customTriggerPanel;
    
    Card presenter;

    @Override
    public void init(final Card presenter) {
        this.presenter = presenter;
        this.cardFront = new CustomFront();
        this.cardBack = new CustomBack();
        initWidget( uiBinder.createAndBindUi( this ) );
        
        final Style cardStyle = card.getElement().getStyle();
        cardStyle.setWidth(100, Style.Unit.PCT);
        cardStyle.setHeight(100, Style.Unit.PCT);
    }

    @Override
    public Card.View setFlipCallback(final Command flipCallback) {
        this.switchCallback = flipCallback;
        return this;
    }

    @Override
    public Card.View setFront(final IsWidget frontWidget) {
        this.frontPanel.add( frontWidget );
        return this;
    }

    @Override
    public Card.View setBack(final IsWidget backWidget) {
        this.backPanel.add( backWidget );
        return this;
    }

    @Override
    public Card.View clearFront() {
        frontPanel.clear();
        return this;
    }

    @Override
    public Card.View clearBack() {
        backPanel.clear();
        return this;
    }

    @Override
    public Card.View add(final IsWidget widget) {
        customTriggerPanel.add( widget );
        return this;
    }

    @Override
    public Card.View flip() {
        fireClickEvent( frontTrigger );
        return this;
    }
    
    private void fireClickEvent( final HasHandlers trigger ) {
        NativeEvent event = Document.get().createClickEvent( 1, 0, 0, 0, 0, false, false, false, false);
        DomEvent.fireNativeEvent(event, trigger);
    }

}
