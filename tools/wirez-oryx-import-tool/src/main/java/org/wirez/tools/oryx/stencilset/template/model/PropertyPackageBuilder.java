package org.wirez.tools.oryx.stencilset.template.model;

import org.apache.velocity.VelocityContext;
import org.wirez.tools.oryx.stencilset.StencilSetCodeGenerator;
import org.wirez.tools.oryx.stencilset.model.PropertyPackage;
import org.wirez.tools.oryx.stencilset.template.Pojo;
import org.wirez.tools.oryx.stencilset.template.StencilSetGeneratorContext;
import org.wirez.tools.oryx.stencilset.template.TemplateResult;

import java.util.Collection;

public class PropertyPackageBuilder extends AbstractTemplateBuilder<PropertyPackage> {

    private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/PropertyPackage.vm";

    @Override
    public String getPackage() {
        return "property";
    }

    @Override
    public boolean accepts(PropertyPackage pojo) {
        return true;
    }

    @Override
    public TemplateResult build(StencilSetGeneratorContext generatorContext, PropertyPackage pojo) {
        if ( null == pojo || null == generatorContext ) throw new NullPointerException();

        String className = StencilSetCodeGenerator.getPropertyPackClassName( pojo.getName() );
        String pkg = generatorContext.getGenerator().getOutputPathName( getPackage() );
        Pojo resultPojo = new Pojo( className , pkg, pojo.getName() );

        VelocityContext context = generatorContext.getVelocityContext();
        context.put( "pojo", resultPojo);
        
        // Generate properties.
        StencilSetGeneratorContext newContext = generatorContext.getGenerator().createContext();
        Collection<Pojo> generatedProperties = generatorContext.getGenerator().generateProperties( pojo.getProperties(), newContext );
        context.put( "properties", generatedProperties);
        
        String result = process(context, generatorContext.getEngine(), TEMPLATE);
        
        return new TemplateResult( result, resultPojo );
    }
    
}
