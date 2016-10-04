package org.wirez.core.api.command;

import java.util.LinkedList;
import java.util.List;

// TODO: Check all rules from all commands before executing them.
public abstract class AbstractCompositeCommand<T, V> implements CompositeCommand<T, V> {
    
    protected final List<Command<T, V>> commands = new LinkedList<>();

    public AbstractCompositeCommand<T,V> addCommand(final Command<T, V> command) {
        commands.add(command);
        return this;
    }

    protected abstract CommandResult<V> buildResult();
    
    @Override
    public CommandResult<V> allow(final T context) {
        return allowAll(context);
    }

    protected CommandResult<V> allowAll(final T context) {
        final List<CommandResult<V>> results = new LinkedList<>();
        for ( final Command<T, V> command : commands ) {
            final CommandResult<V> violations = command.allow( context );
            results.add(violations);
        }

        return buildResult(results);
    }

    @Override
    public CommandResult<V> execute(final T context) {
        final List<CommandResult<V>> results = new LinkedList<>();
        for ( final Command<T, V> command : commands ) {
            final CommandResult<V> violations = command.execute( context );
            results.add(violations);
        }

        return buildResult(results);
    }

    @Override
    public CommandResult<V> undo(final T context) {
        final List<CommandResult<V>> results = new LinkedList<>();
        for ( final Command<T, V> command : commands ) {
            final CommandResult<V> violations = command.undo( context );
            results.add(violations);
        }

        return buildResult(results);
    }
    
    
    protected CommandResult<V> buildResult( final List<CommandResult<V>> violations ) {
        if ( null != violations ) {
            final CommandResult<V> result = buildResult();
            for (final CommandResult<V> violation : violations) {
                Iterable<V> violationsIter = violation.getViolations();
                for (V ruleViolation : violationsIter) {
                    result.addViolation(ruleViolation);
                }
            }
            
            return result;
        }
        
        return null;
    }
    
}
