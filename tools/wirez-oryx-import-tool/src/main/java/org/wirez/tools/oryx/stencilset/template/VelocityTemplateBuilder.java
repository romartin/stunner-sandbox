package org.wirez.tools.oryx.stencilset.template;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.wirez.tools.oryx.stencilset.StencilSetCodeGenerator;

import java.io.StringWriter;

public abstract  class VelocityTemplateBuilder {
    
    protected String process(VelocityContext velocityContext, VelocityEngine engine, String template) {

        StringWriter writer = new StringWriter();
        engine.mergeTemplate( template, StencilSetCodeGenerator.ENCODING, velocityContext, writer );

        // Return the result
        return writer.toString();
    }
}
