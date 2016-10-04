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

package org.wirez.core.client.canvas.wires;

import com.ait.lienzo.client.core.event.NodeMouseClickEvent;
import com.ait.lienzo.client.core.event.NodeMouseClickHandler;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.ui.IsWidget;
import org.wirez.core.api.command.Command;
import org.wirez.core.api.command.CommandManager;
import org.wirez.core.api.command.CommandResults;
import org.wirez.core.api.command.DefaultCommandManager;
import org.wirez.core.api.definition.Definition;
import org.wirez.core.api.event.NotificationEvent;
import org.wirez.core.api.graph.Element;
import org.wirez.core.api.graph.Graph;
import org.wirez.core.api.graph.Node;
import org.wirez.core.api.graph.content.ViewContent;
import org.wirez.core.api.graph.impl.DefaultGraph;
import org.wirez.core.api.rule.DefaultRuleManager;
import org.wirez.core.api.rule.RuleManager;
import org.wirez.core.api.rule.RuleViolation;
import org.wirez.core.client.Shape;
import org.wirez.core.client.canvas.CanvasHandler;
import org.wirez.core.client.canvas.CanvasListener;
import org.wirez.core.client.canvas.settings.CanvasSettings;
import org.wirez.core.client.canvas.command.CanvasCommand;
import org.wirez.core.client.canvas.command.CanvasCommandViolation;
import org.wirez.core.client.canvas.control.SelectionManager;
import org.wirez.core.client.control.*;
import org.wirez.core.client.shape.factory.ShapeFactory;
import org.wirez.core.client.shape.factory.control.HasShapeControlFactories;
import org.wirez.core.client.shape.factory.control.ShapeControlFactory;
import org.wirez.core.client.shape.mutation.*;
import org.wirez.core.client.notification.CanvasCommandAllowedNotification;
import org.wirez.core.client.notification.CanvasCommandExecutionNotification;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Implement SelectionManager<Element>
public abstract class WiresCanvasHandler 
        implements CanvasHandler<WiresCanvas>, 
                    CommandManager<WiresCanvasHandler, CanvasCommandViolation> {

    private static Logger LOGGER = Logger.getLogger("org.wirez.core.client.canvas.impl.WiresCanvasHandler");
    
    protected Event<NotificationEvent> notificationEvent;
    protected CommandManager<RuleManager, RuleViolation> graphCommandManager;
    protected DefaultRuleManager ruleManager;
    protected CanvasSettings settings;
    protected WiresCanvas canvas;
    protected Graph<?, ? extends Node> graph;
    protected Collection<CanvasListener> listeners = new LinkedList<CanvasListener>();

    @Inject
    public WiresCanvasHandler(final Event<NotificationEvent> notificationEvent,
                              final CommandManager<RuleManager, RuleViolation> graphCommandManager,
                              final @Named( "default" ) DefaultRuleManager ruleManager) {
        this.notificationEvent = notificationEvent;
        this.graphCommandManager = graphCommandManager;
        this.ruleManager = ruleManager;
    }

    @Override
    public CanvasHandler initialize(final WiresCanvas canvas, final CanvasSettings settings) {
        this.settings = settings;
        this.canvas = canvas;
        this.graph = settings.getGraph();
        return this;
    }

    public CommandManager<RuleManager, RuleViolation> getGraphCommandManager() {
        return graphCommandManager;
    }

    @Override
    public CanvasSettings getSettings() {
        return settings;
    }

    @Override
    public WiresCanvas getCanvas() {
        return canvas;
    }

    /*
        ***************************************************************************************
        * Shape/element handling
        ***************************************************************************************
     */

    public void register(final ShapeFactory factory, final Element candidate) {
        assert factory != null && candidate != null;
        
        final Object content = candidate.getContent();
        assert content instanceof ViewContent;
        
        final Definition wirez = ( (ViewContent) candidate.getContent()).getDefinition();
        final Shape shape = factory.build(wirez, this);

        // Set the same identifier as the graph element's one.
        shape.setId(candidate.getUUID());

        // Selection handling.
        if (canvas instanceof SelectionManager) {
            final SelectionManager<Shape> selectionManager = (SelectionManager<Shape>) canvas;
            shape.getShape().addNodeMouseClickHandler(new NodeMouseClickHandler() {
                @Override
                public void onNodeMouseClick(final NodeMouseClickEvent nodeMouseClickEvent) {

                    final boolean isSelected = selectionManager.isSelected(shape);

                    if (!nodeMouseClickEvent.isShiftKeyDown()) {
                        selectionManager.clearSelection();
                    }

                    if (isSelected) {
                        log(Level.FINE, "Deselect [shape=" + shape.getId() + "]");
                        selectionManager.deselect(shape);
                    } else {
                        log(Level.FINE, "Select [shape=" + shape.getId() + "]");
                        selectionManager.select(shape);
                    }

                }
            });

        }

        // Shape controls.
        if (factory instanceof HasShapeControlFactories) {

            final Collection<ShapeControlFactory<?, ?>> factories = ((HasShapeControlFactories) factory).getFactories();
            for (ShapeControlFactory controlFactory : factories) {
                ShapeControl control = controlFactory.build(shape);

                // Some controls needs to add elements on the DOM.
                if (control instanceof IsWidget) {
                    final IsWidget controlWidget = (IsWidget) control;
                    canvas.addControl(controlWidget);
                }
                
                // Controls that execute commands on the canvas require the canvas handler instance.
                if (control instanceof HasCanvasHandler) {
                    final HasCanvasHandler hasCanvasHandler = (HasCanvasHandler) control;
                    hasCanvasHandler.setCanvasHandler(this);
                            
                }
                
                // Enable the stateful control.
                control.enable(shape, candidate);
                
            }

        }
        
        // Add the shapes on canvas and fire events.
        canvas.addShape(shape);
        canvas.draw();
        afterElementAdded(candidate);
    }

    public void applyElementMutation(final Element candidate) {
        final Shape shape = canvas.getShape(candidate.getUUID());
        if (shape instanceof HasMutation) {

            final HasMutation hasMutation = (HasMutation) shape;

            if (hasMutation.accepts(MutationType.STATIC)) {

                MutationContext context = new StaticMutationContext();

                if (shape instanceof HasGraphElementMutation) {
                    final HasGraphElementMutation hasGraphElement = (HasGraphElementMutation) shape;
                    hasGraphElement.applyElementPosition(candidate, this, context);
                    hasGraphElement.applyElementProperties(candidate, this, context);
                    afterElementUpdated(candidate, hasGraphElement);
                }

            }

        }
    }
    
    public void addChild(final Element parent, final Element child) {
        assert parent != null && child != null;

        final WiresShape parentShape = (WiresShape) canvas.getShape(parent.getUUID());
        final WiresShape childShape = (WiresShape) canvas.getShape(child.getUUID());
        parentShape.add(childShape);
    }
    

    public void updateElementPosition(final Element element) {
        final Shape shape = canvas.getShape(element.getUUID());

        final HasGraphElementMutation shapeMutation = (HasGraphElementMutation) shape;
        final MutationContext context = new StaticMutationContext();
        shapeMutation.applyElementPosition(element, this, context);
        canvas.draw();
        afterElementUpdated(element, shapeMutation);
    }

    public void updateElementProperties(final Element element) {
        final Shape shape = canvas.getShape(element.getUUID());

        final HasGraphElementMutation shapeMutation = (HasGraphElementMutation) shape;
        final MutationContext context = new StaticMutationContext();
        shapeMutation.applyElementProperties(element, this, context);
        canvas.draw();
        afterElementUpdated(element, shapeMutation);
    }

    public void deregister(final Element element) {

        final Shape shape = canvas.getShape(element.getUUID());
        // TODO: Delete connector connections to the node being deleted?
        canvas.deleteShape(shape);
        canvas.draw();
        afterElementDeleted(element);

    }
    
    public void clear() {
        canvas.clear();
        canvas.draw();
        fireCanvasClear();
    }
    
    /*
        ***************************************************************************************
        * Listeners handling
        ***************************************************************************************
     */

    @Override
    public CanvasHandler addListener(final CanvasListener listener) {
        assert listener != null;
        listeners.add(listener);
        return this;
    }
    
    protected void afterElementAdded(final Element element) {

        fireElementAdded(element);
        
    }

    protected void afterElementDeleted(final Element element) {

        fireElementDeleted(element);
    }

    protected void afterElementUpdated(final Element element, final HasGraphElementMutation elementMutation) {

        elementMutation.afterMutations(getBaseCanvas());
        
        fireElementUpdated(element);
    }

    public void fireElementAdded(final Element element) {
        for (final CanvasListener listener : listeners) {
            listener.onElementAdded(element);
        }
    }

    public void fireElementDeleted(final Element element) {
        for (final CanvasListener listener : listeners) {
            listener.onElementDeleted(element);
        }
    }

    public void fireElementUpdated(final Element element) {
        for (final CanvasListener listener : listeners) {
            listener.onElementModified(element);
        }
    }

    public void fireCanvasClear() {
        for (final CanvasListener listener : listeners) {
            listener.onClear();
        }
    }
    
    public void removeListener(final CanvasListener listener) {
        listeners.remove(listener);
    }
    
    /*
        ***************************************************************************************
        * Command handling
        ***************************************************************************************
     */

    @Override
    public boolean allow(WiresCanvasHandler context, Command<WiresCanvasHandler, CanvasCommandViolation>... command) {
        return false;
    }

    @Override
    public CommandResults<CanvasCommandViolation> execute(WiresCanvasHandler context, Command<WiresCanvasHandler, CanvasCommandViolation>... command) {
        return null;
    }

    @Override
    public CommandResults<CanvasCommandViolation> undo(WiresCanvasHandler context) {
        return null;
    }

    public boolean allow(WiresCanvasHandler context, Command<WiresCanvasHandler, CanvasCommandViolation>... command) {
        return false;
    }

    public CommandResults<CanvasCommandViolation> execute(WiresCanvasHandler context, Command<WiresCanvasHandler, CanvasCommandViolation>... command) {
        return null;
    }

    public CommandResults<CanvasCommandViolation> undo(WiresCanvasHandler context) {
        return null;
    }
    
    
    
    
    

    @Override
    public boolean allow(final CanvasCommand command) {
        return this.allow(ruleManager, command);
    }

    @Override
    public CommandResults execute(final CanvasCommand... commands) {
        return this.execute(ruleManager, commands);
    }

    @Override
    public CommandResults undo() {
        return this.undo(ruleManager);
    }

    @Override
    public boolean allow(final RuleManager ruleManager,
                         final CanvasCommand command) {
        command.setCanvas(this);
        boolean isAllowed = commandManager.allow(ruleManager, command);
        fireCommandAllowedNotification(command, isAllowed);
        return isAllowed;
    }
    
    @Override
    public CommandResults execute(final RuleManager ruleManager,
                                  final CanvasCommand... commands) {

        // Initialize commands.
        for (final CanvasCommand command : commands) {
            command.setCanvas(this);
        }

        // If multiple commands, execute them in batch.
        CommandResults results = commandManager.execute(ruleManager, commands);

        // TODO: Check errors.
        final boolean hasErrors = false;
        if (!hasErrors) {
            // Update canvas state.
            for (final CanvasCommand command : commands) {
                command.apply();
                fireCommandExecutionNotification(command, results);
            }
        }

        return results;
    }

    @Override
    public CommandResults undo(final RuleManager ruleManager) {
        return commandManager.undo(ruleManager);
    }

    protected void fireCommandAllowedNotification(final CanvasCommand command, final boolean isAllowed) {
        final CanvasCommandAllowedNotification notification = new CanvasCommandAllowedNotification(getSettings().getTitle(), command, isAllowed);
        notificationEvent.fire(new NotificationEvent(notification));
    }

    protected void fireCommandExecutionNotification(final CanvasCommand command, final CommandResults results) {
        final CanvasCommandExecutionNotification notification = new CanvasCommandExecutionNotification(getSettings().getTitle(), command, results);
        notificationEvent.fire(new NotificationEvent(notification));
    }
    
    protected WiresCanvas getBaseCanvas() {
        return (WiresCanvas) canvas;
    }

    private void log(final Level level, final String message) {
        if ( LogConfiguration.loggingIsEnabled() ) {
            LOGGER.log(level, message);
        }
    }
    
}
