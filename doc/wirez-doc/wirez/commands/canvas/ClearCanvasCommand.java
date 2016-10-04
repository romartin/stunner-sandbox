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
import org.wirez.core.api.graph.command.GraphCommandFactory;
import org.wirez.core.api.graph.impl.DefaultGraph;
import org.wirez.core.api.rule.RuleManager;
import org.wirez.core.client.canvas.command.BaseCanvasCommand;
import org.wirez.core.client.canvas.command.CanvasCommand;
import org.wirez.core.client.canvas.wires.WiresCanvasHandler;

/**
 * A Command to clear a canvas and all the graph's elements..
 */
public class ClearCanvasCommand extends BaseCanvasCommand {

    public ClearCanvasCommand(final GraphCommandFactory commandFactory) {
        super(commandFactory);

    }

    @Override
    protected Command getCommand() {
        return commandFactory.clearGraphCommand( (DefaultGraph) canvasHandler.getGraphHandler().getGraph() );
    }

    @Override
    public CanvasCommand apply() {
        final WiresCanvasHandler baseCanvasHandler = (WiresCanvasHandler) canvasHandler;
        baseCanvasHandler.clear();
        return this;
    }

    @Override
    public CommandResult execute(final RuleManager ruleManager) {
        return super.execute(ruleManager);
    }

    @Override
    public CommandResult undo(RuleManager ruleManager) {
        super.undo(ruleManager);
        throw new UnsupportedOperationException("Clear canvas not implemented yet.");
    }

    @Override
    public String toString() {
        return "ClearCanvasCommand [canvas=" + canvasHandler.getSettings().getUUID() + "]";
    }

}
