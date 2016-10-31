/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.client.widgets.explorer.command.item;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.kie.workbench.common.stunner.core.command.Command;
import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.logging.Logger;

public abstract class AbstractCommandItem<C extends Command, V extends AbstractCommandItem.View> implements IsWidget {

    private static Logger LOGGER = Logger.getLogger( AbstractCommandItem.class.getName() );

    public interface View<P> extends UberView<P> {

        View setText( String text );

        View clear();
    }

    private final V view;
    private C command;

    public AbstractCommandItem( final V view ) {
        this.view = view;
    }

    @PostConstruct
    @SuppressWarnings( "unchecked" )
    public void init() {
        view.init( this );
    }

    public void show( final C command ) {
        this.command = command;
        view.setText( command.toString() );
    }

    public void clear() {
        view.clear();
        this.command = null;
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    public V getView() {
        return view;
    }

    public C getCommand() {
        return command;
    }
}
