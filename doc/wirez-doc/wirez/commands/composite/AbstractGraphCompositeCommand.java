package org.wirez.core.api.graph.command.impl;

import org.wirez.core.api.command.AbstractCompositeCommand;
import org.wirez.core.api.command.CommandResult;
import org.wirez.core.api.graph.command.factory.GraphCommandFactory;
import org.wirez.core.api.graph.command.GraphCommandResult;
import org.wirez.core.api.rule.RuleManager;
import org.wirez.core.api.rule.RuleViolation;

public abstract class AbstractGraphCompositeCommand extends AbstractCompositeCommand<RuleManager, RuleViolation> {

    protected GraphCommandFactory commandFactory;

    public AbstractGraphCompositeCommand(GraphCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    @Override
    protected CommandResult<RuleViolation> buildResult() {
        return new GraphCommandResult();
    }
}
