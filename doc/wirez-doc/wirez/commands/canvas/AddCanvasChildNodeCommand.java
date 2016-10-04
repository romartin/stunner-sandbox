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
import org.wirez.core.api.graph.Node;
import org.wirez.core.api.graph.command.GraphCommandFactory;
import org.wirez.core.api.graph.impl.DefaultGraph;
import org.wirez.core.api.rule.RuleManager;
import org.wirez.core.client.canvas.command.BaseCanvasCommand;
import org.wirez.core.client.canvas.command.CanvasCommand;
import org.wirez.core.client.canvas.wires.WiresCanvasHandler;
import org.wirez.core.client.shape.factory.ShapeFactory;

/**
 * A Command to add a DefaultNode to a Graph and add the corresponding canvas shapes.
 */
public class AddCanvasChildNodeCommand extends BaseCanvasCommand {

    Node parent;
    Node candidate;
    ShapeFactory factory;

    public AddCanvasChildNodeCommand(final GraphCommandFactory commandFactory, final Node parent, final Node candidate, final ShapeFactory factory ) {
        super(commandFactory);
        this.parent = parent;
        this.candidate = candidate;
        this.factory = factory;
    }

    @Override
    public Command getCommand() {
        return commandFactory.addChildNodeCommand((DefaultGraph) canvasHandler.getGraphHandler().getGraph(), parent, candidate);
    }

    @Override
    public CanvasCommand apply() {
        ( (WiresCanvasHandler) canvasHandler).addChild(parent, candidate);
        ( (WiresCanvasHandler) canvasHandler).applyElementMutation(candidate);
        return this;
    }

    @Override
    public CommandResult execute(final RuleManager ruleManager) {
        return super.execute(ruleManager);
    }

    @Override
    public String toString() {
        return "AddCanvasChildNodeCommand [parent=" + parent + ", candidate=" + candidate.getUUID() + ", factory=" + factory + "]";
    }


}
