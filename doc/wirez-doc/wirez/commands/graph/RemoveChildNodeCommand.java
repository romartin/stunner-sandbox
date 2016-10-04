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

package org.wirez.core.api.graph.command.impl;

import org.uberfire.commons.validation.PortablePreconditions;
import org.wirez.core.api.command.CommandResult;
import org.wirez.core.api.graph.Edge;
import org.wirez.core.api.graph.Node;
import org.wirez.core.api.graph.command.GraphCommandFactory;
import org.wirez.core.api.graph.command.GraphCommandResult;
import org.wirez.core.api.graph.content.ParentChildRelationship;
import org.wirez.core.api.graph.impl.DefaultGraph;
import org.wirez.core.api.rule.RuleManager;
import org.wirez.core.api.rule.RuleViolation;

import java.util.*;

/**
 * A Command to remove the parent of a DefaultNode
 */
public class RemoveChildNodeCommand extends AbstractCommand {

    private DefaultGraph target;
    private Node oldParent;
    private Node candidate;

    public RemoveChildNodeCommand(final GraphCommandFactory commandFactory,
                                  final DefaultGraph target,
                                  final Node oldParent,
                                  final Node candidate ) {
        super(commandFactory);
        this.target = PortablePreconditions.checkNotNull( "target",
                target );
        this.oldParent = PortablePreconditions.checkNotNull( "oldParent",
                oldParent );
        this.candidate = PortablePreconditions.checkNotNull( "candidate",
                                                             candidate );
    }
    
    @Override
    public CommandResult<RuleViolation> allow(final RuleManager ruleManager) {
        return check(ruleManager);
    }

    @Override
    public CommandResult<RuleViolation> execute(final RuleManager ruleManager) {
        final CommandResult<RuleViolation> results = check(ruleManager);
        if ( !results.getType().equals( CommandResult.Type.ERROR ) ) {
            
            final List<Edge> oldParentOutEdges = oldParent.getOutEdges();
            DeleteEdgeCommand deleteEdgeCommand = null;
            if ( null != oldParentOutEdges && !oldParentOutEdges.isEmpty() ) {
                for (final Edge oldParentEdge : oldParentOutEdges) {
                    if ( oldParentEdge.getContent() instanceof ParentChildRelationship ) {
                        final Node oldParentChild = oldParentEdge.getTargetNode();
                        if ( null != oldParentChild && oldParentChild.equals(candidate) ) {
                            deleteEdgeCommand = new DeleteEdgeCommand(commandFactory, target, oldParentEdge);
                            break;
                        }
                    }
                }
            }
            
            if ( null != deleteEdgeCommand ) {
                deleteEdgeCommand.execute(ruleManager);
            }
            
        }
        return results;
    }
    
    private CommandResult<RuleViolation> check(final RuleManager ruleManager) {
        final Collection<RuleViolation> containmentRuleViolations = (Collection<RuleViolation>) ruleManager.checkContainment( target, candidate).violations();
        final Collection<RuleViolation> cardinalityRuleViolations = (Collection<RuleViolation>) ruleManager.checkCardinality( target, candidate, RuleManager.Operation.ADD).violations();
        final Collection<RuleViolation> violations = new LinkedList<RuleViolation>();
        violations.addAll(containmentRuleViolations);
        violations.addAll(cardinalityRuleViolations);
        return new GraphCommandResult(violations);
    }

    @Override
    public CommandResult<RuleViolation> undo(RuleManager ruleManager) {
        // TODO
        return null;
    }

    @Override
    public String toString() {
        return "RemoveChildNodeCommand [graph=" + target.getUUID() + ", candidate=" + candidate.getUUID() + "]";
    }
}
