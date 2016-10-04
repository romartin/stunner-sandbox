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

package org.wirez.core.client.canvas.command.impl;

import org.wirez.core.api.command.Command;
import org.wirez.core.api.command.CommandResult;
import org.wirez.core.api.command.DefaultCommandResult;
import org.wirez.core.api.graph.Element;
import org.wirez.core.api.graph.command.GraphCommandFactory;
import org.wirez.core.api.rule.RuleManager;
import org.wirez.core.api.rule.RuleViolation;
import org.wirez.core.client.canvas.CanvasHandler;
import org.wirez.core.client.canvas.command.CanvasCommand;
import org.wirez.core.client.canvas.command.WiresCanvasCommandManager;
import org.wirez.core.client.canvas.wires.WiresCanvasHandler;

import java.util.LinkedList;
import java.util.List;

public class CompositeElementCanvasCommand implements CanvasCommand {

    CanvasHandler canvasHandler;
    List<Command> commands = new LinkedList<Command>();
    final Element element;
    boolean applyElementPosition = false;
    boolean applyElementProperties = false;
    
    public CompositeElementCanvasCommand(final GraphCommandFactory commandFactory, final Element element) {
        this.element = element;
    }

    @Override
    public CanvasCommand setCanvas(final CanvasHandler canvasHandler) {
        this.canvasHandler = canvasHandler;
        return this;
    }

    public CompositeElementCanvasCommand doApplyElementPosition() {
        this.applyElementPosition = true;
        return this;
    }

    public CompositeElementCanvasCommand doApplyElementProperties() {
        this.applyElementProperties = true;
        return this;
    }

    @Override
    public CanvasCommand apply() {
        final WiresCanvasHandler wirezCanvas = (WiresCanvasHandler) canvasHandler;
        if (applyElementPosition) {
            wirezCanvas.updateElementPosition(element);
        }
        if (applyElementProperties) {
            wirezCanvas.updateElementProperties(element);
        }
        return this;
    }

    public CompositeElementCanvasCommand add(final Command command) {
        if ( null != command ) {
            commands.add(command);
        }
        return this;
    }
    
    @Override
    public CommandResult allow(final RuleManager ruleManager) {
        final List<RuleViolation> violations = new LinkedList<RuleViolation>();
        for (final Command command : commands) {
            
            CommandResult result = command.allow(ruleManager);
            for (RuleViolation violation : result.getRuleViolations()) {
                violations.add(violation);
            }
        }
        
        return new DefaultCommandResult(violations);
    }

    @Override
    public CommandResult execute(final RuleManager ruleManager) {
        final List<RuleViolation> violations = new LinkedList<RuleViolation>();
        for (final Command command : commands) {

            CommandResult result = command.execute(ruleManager);
            for (RuleViolation violation : result.getRuleViolations()) {
                violations.add(violation);
            }
        }

        return new DefaultCommandResult(violations);
    }

    @Override
    public CommandResult undo(final RuleManager ruleManager) {
        final List<RuleViolation> violations = new LinkedList<RuleViolation>();
        for (final Command command : commands) {

            CommandResult result = null;
            if (command instanceof CanvasCommand) {
                CanvasCommand canvasCommand = (CanvasCommand) command;
                WiresCanvasCommandManager commandManager = (WiresCanvasCommandManager) canvasHandler;
                result = commandManager.execute(ruleManager, canvasCommand).results().iterator().next();
            } else {
                result = command.undo(ruleManager);
            }
            
            for (RuleViolation violation : result.getRuleViolations()) {
                violations.add(violation);
            }
        }

        return new DefaultCommandResult(violations);
    }
}
