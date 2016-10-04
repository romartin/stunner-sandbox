package org.wirez.core.client.canvas.command.impl;

import org.wirez.core.api.command.AbstractCompositeCommand;
import org.wirez.core.api.command.CommandResult;
import org.wirez.core.client.canvas.command.factory.CanvasCommandFactory;
import org.wirez.core.client.canvas.command.CanvasCommandResult;
import org.wirez.core.client.canvas.command.CanvasCommandViolation;
import org.wirez.core.client.canvas.wires.WiresCanvasHandler;

public abstract class AbstractCanvasCompositeCommand extends AbstractCompositeCommand<WiresCanvasHandler, CanvasCommandViolation> {

    protected CanvasCommandFactory canvasCommandFactory;

    public AbstractCanvasCompositeCommand(final CanvasCommandFactory canvasCommandFactory) {
        this.canvasCommandFactory = canvasCommandFactory;
    }
    
    protected CommandResult<CanvasCommandViolation> buildResult() {
        return new CanvasCommandResult();
    }
}
