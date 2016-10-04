package org.wirez.tools.oryx.stencilset.template.builder;

import org.apache.velocity.VelocityContext;
import org.wirez.tools.oryx.stencilset.StencilSetCodeGenerator;
import org.wirez.tools.oryx.stencilset.template.Pojo;
import org.wirez.tools.oryx.stencilset.template.VelocityTemplateBuilder;

import java.util.Collection;

public class PropertyModelBuilder extends VelocityTemplateBuilder {

    private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/PropertyBuilder.vm";
    private final StencilSetCodeGenerator stencilSetCodeGenerator;

    public PropertyModelBuilder(StencilSetCodeGenerator stencilSetCodeGenerator) {
        this.stencilSetCodeGenerator = stencilSetCodeGenerator;
    }

    public String build(String thePackage, String className) {
        VelocityContext context = new VelocityContext();

        context.put("thePackage", thePackage);
        context.put("className", className);
        Collection<Pojo> propertyPojos = stencilSetCodeGenerator.getProperties().values();
        context.put("properties", propertyPojos);
        
        return process( context, stencilSetCodeGenerator.getVelocityEngine(), TEMPLATE );
    }
    
}
