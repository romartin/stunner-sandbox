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

package org.kie.workbench.common.stunner.client.widgets.explorer.command;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.kie.workbench.common.stunner.client.widgets.explorer.command.item.AbstractCommandItem;
import org.kie.workbench.common.stunner.client.widgets.explorer.command.item.CanvasCommandItem;
import org.kie.workbench.common.stunner.core.command.Command;
import org.kie.workbench.common.stunner.core.registry.command.CommandRegistry;
import org.uberfire.client.mvp.UberView;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractCommandsExplorer<C extends Command,
        I extends AbstractCommandItem<C, ?>,
        V extends AbstractCommandsExplorer.View> implements IsWidget {

    private static Logger LOGGER = Logger.getLogger( AbstractCommandsExplorer.class.getName() );

    public interface View<P> extends UberView<P> {

        View add( IsWidget item );

        View clear();
    }

    private final V view;
    private CommandRegistry<C> registry;
    private final List<I> items = new LinkedList<>();

    public AbstractCommandsExplorer( final V view ) {
        this.view = view;
    }

    protected abstract I newItemInstance( C command );

    @PostConstruct
    @SuppressWarnings( "unchecked" )
    public void init() {
        view.init( this );
    }

    public void show( final CommandRegistry<C> commandRegistry ) {
        this.registry = commandRegistry;
        final Iterable<Iterable<C>> allCommands = commandRegistry.getCommandHistory();
        if ( null != allCommands ) {
            allCommands.forEach( batchCommand -> {
                batchCommand.forEach( command -> {
                    // TODO: Create a BatchCanvacCommandItem?
                    final I item = newItemInstance( command );
                    items.add( item );
                    view.add( item.asWidget() );
                } );
            } );
        }
    }

    public void clear() {
        this.items.clear();
        this.registry = null;
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    public V getView() {
        return view;
    }

    public CommandRegistry<C> getRegistry() {
        return registry;
    }

}
