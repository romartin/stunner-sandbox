package org.wirez.tools.oryx.stencilset.template.builder;

import org.apache.velocity.VelocityContext;
import org.wirez.tools.oryx.stencilset.StencilSetCodeGenerator;
import org.wirez.tools.oryx.stencilset.model.Property;
import org.wirez.tools.oryx.stencilset.model.PropertyPackage;
import org.wirez.tools.oryx.stencilset.template.Pojo;
import org.wirez.tools.oryx.stencilset.template.VelocityTemplateBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DefinitionSetModelBuilder extends VelocityTemplateBuilder {

    private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/DefinitionSetBuilder.vm";
    private final StencilSetCodeGenerator stencilSetCodeGenerator;

    public DefinitionSetModelBuilder(StencilSetCodeGenerator stencilSetCodeGenerator) {
        this.stencilSetCodeGenerator = stencilSetCodeGenerator;
    }

    public String build(String thePackage, String className, String definitionBuilderClassName) {
        VelocityContext context = new VelocityContext();

        Pojo stencilSetPojo = stencilSetCodeGenerator.getStencilSetPojo();
        
        context.put("thePackage", thePackage);
        context.put("className", className);
        context.put("definitionBuilderClassName", definitionBuilderClassName);
        context.put("definitionSetClassName", stencilSetPojo.getClassName());

        Collection<Pojo> definitionPojos = stencilSetCodeGenerator.getStencils().values();
        context.put("definitions", definitionPojos);
        
        return process( context, stencilSetCodeGenerator.getVelocityEngine(), TEMPLATE );
    }
    
}
