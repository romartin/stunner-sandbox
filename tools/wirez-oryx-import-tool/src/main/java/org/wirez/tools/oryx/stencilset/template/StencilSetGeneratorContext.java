package org.wirez.tools.oryx.stencilset.template;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.wirez.tools.oryx.stencilset.StencilSetCodeGenerator;

import java.io.File;

public class StencilSetGeneratorContext {
    
    private final StencilSetCodeGenerator generator;
    private final VelocityContext velocityContext;
    private final VelocityEngine engine;
    private final String basePackage;

    public StencilSetGeneratorContext(StencilSetCodeGenerator generator, 
                                      VelocityContext velocityContext,
                                      VelocityEngine engine,
                                      String basePackage) {
        this.generator = generator;
        this.velocityContext = velocityContext;
        this.engine = engine;
        this.basePackage = basePackage;
    }
    
    public StencilSetCodeGenerator getGenerator() {
        return generator;
    }

    public VelocityContext getVelocityContext() {
        return velocityContext;
    }

    public VelocityEngine getEngine() {
        return engine;
    }

    public String getBasePackage() {
        return basePackage;
    }
    
}
