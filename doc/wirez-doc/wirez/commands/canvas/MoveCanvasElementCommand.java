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
import org.wirez.core.api.graph.Element;
import org.wirez.core.api.graph.command.GraphCommandFactory;
import org.wirez.core.api.graph.command.impl.UpdateElementPositionCommand;
import org.wirez.core.api.rule.RuleManager;
import org.wirez.core.client.canvas.command.BaseCanvasCommand;
import org.wirez.core.client.canvas.command.CanvasCommand;
import org.wirez.core.client.canvas.wires.WiresCanvasHandler;

/**
 * A Command to update an element's bounds and their corresponding shape positions.
 */
public class MoveCanvasElementCommand extends BaseCanvasCommand implements CanvasCommand {

    Element element;
    Double x;
    Double y;
    
    public MoveCanvasElementCommand(final GraphCommandFactory commandFactory, 
                                    final Element element ,
                                    final Double x,
                                    final Double y) {
        super(commandFactory);
        this.element = element;
        this.x = x;
        this.y = y;
    }

    @Override
    protected Command getCommand() {
        return commandFactory.updateElementPositionCommand(element, x, y);
    }

    @Override
    public CanvasCommand apply() {
        final WiresCanvasHandler wirezCanvas = (WiresCanvasHandler) canvasHandler;
        wirezCanvas.updateElementPosition(element);
        return this;
    }

    @Override
    public CommandResult execute(final RuleManager ruleManager) {
        return super.execute(ruleManager);
    }

    @Override
    public CommandResult undo(final RuleManager ruleManager) {
        super.undo(ruleManager);
        UpdateElementPositionCommand oldCommand = (UpdateElementPositionCommand) command;
        final double oldX = oldCommand.getOldX();
        final double oldY  = oldCommand.getOldY();
        final MoveCanvasElementCommand undoCommand = new MoveCanvasElementCommand( commandFactory, element, oldX, oldY );
        return executeUndoCommand(undoCommand, ruleManager);

    }
    
    @Override
    public String toString() {
        return "MoveCanvasElementCommand [element=" + element.getUUID() + ", x=" + x + ", y=" +  y + "]";
    }
    
    
}
