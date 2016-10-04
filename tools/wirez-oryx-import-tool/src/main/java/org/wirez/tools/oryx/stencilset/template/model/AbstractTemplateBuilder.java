package org.wirez.tools.oryx.stencilset.template.model;

import org.wirez.tools.oryx.stencilset.template.StencilSetGeneratorContext;
import org.wirez.tools.oryx.stencilset.template.TemplateBuilder;
import org.wirez.tools.oryx.stencilset.template.VelocityTemplateBuilder;

public abstract class AbstractTemplateBuilder<T> extends VelocityTemplateBuilder implements TemplateBuilder<T, StencilSetGeneratorContext> {

    protected String getDefaultPackage(StencilSetGeneratorContext generatorContext) {
        return generatorContext.getGenerator().getOutputPathName( getPackage() );
    }
    
}
