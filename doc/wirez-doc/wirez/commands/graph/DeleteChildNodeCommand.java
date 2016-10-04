package org.wirez.core.api.graph.command.impl;

import org.uberfire.commons.validation.PortablePreconditions;
import org.wirez.core.api.command.CommandResult;
import org.wirez.core.api.graph.Graph;
import org.wirez.core.api.graph.Node;
import org.wirez.core.api.graph.command.GraphCommandResult;
import org.wirez.core.api.graph.command.factory.GraphCommandFactory;
import org.wirez.core.api.rule.RuleManager;
import org.wirez.core.api.rule.RuleViolation;

import java.util.Collection;

public class DeleteChildNodeCommand extends AbstractGraphCompositeCommand {

    private Graph target;
    private Node parent;
    private Node candidate;
    
    public DeleteChildNodeCommand(final GraphCommandFactory commandFactory,
                                  final Graph target,
                                  final Node parent,
                                  final Node candidate) {
        super(commandFactory);
        this.target = PortablePreconditions.checkNotNull( "target",
                target );
        this.parent = PortablePreconditions.checkNotNull( "parent",
                parent );
        this.candidate = PortablePreconditions.checkNotNull( "candidate",
                candidate );
        initCommands();
    }
    
    private void initCommands() {
        this.addCommand( commandFactory.DELETE_CHILD_EDGE( parent, candidate) )
            .addCommand( commandFactory.DELETE_NODE( target, candidate ) );
    }

    @Override
    public String toString() {
        return "DeleteChildNodeCommand [parent=" + parent.getUUID() + ", candidate=" + candidate.getUUID() + "]";
    }

    @Override
    public CommandResult<RuleViolation> allow(RuleManager context) {
        return check( context );
    }

    private CommandResult<RuleViolation> check(final RuleManager ruleManager) {

        boolean isNodeInGraph = false;
        for ( Object node : target.nodes() ) {
            if ( node.equals( candidate ) ) {
                isNodeInGraph = true;
                break;
            }
        }

        GraphCommandResult results;
        if ( isNodeInGraph ) {
            final Collection<RuleViolation> cardinalityRuleViolations = (Collection<RuleViolation>) ruleManager.checkCardinality( target, candidate, RuleManager.Operation.DELETE).violations();
            results = new GraphCommandResult(cardinalityRuleViolations);
        } else {
            results = new GraphCommandResult();
            results.setType(CommandResult.Type.ERROR);
            results.setMessage("Node was not present in Graph and hence was not deleted");
        }

        return results;
    }
}
