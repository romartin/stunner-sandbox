package org.wirez.tools.oryx.stencilset.template;

import org.wirez.tools.oryx.stencilset.model.Property;
import org.wirez.tools.oryx.stencilset.model.PropertyPackage;
import org.wirez.tools.oryx.stencilset.model.Stencil;
import org.wirez.tools.oryx.stencilset.model.StencilSet;
import org.wirez.tools.oryx.stencilset.template.model.PropertyBuilder;
import org.wirez.tools.oryx.stencilset.template.model.PropertyPackageBuilder;
import org.wirez.tools.oryx.stencilset.template.model.StencilBuilder;
import org.wirez.tools.oryx.stencilset.template.model.StencilSetBuilder;

public class TemplateBuilderFactory {
    
    private static final TemplateBuilderFactory INSTANCE = new TemplateBuilderFactory();
    
    public static TemplateBuilderFactory getInstance() {
        return INSTANCE;
    }
    
    public <T> TemplateBuilder<T, StencilSetGeneratorContext> getTemplateBuilder(Class<T> clazz) {

        TemplateBuilder<T, StencilSetGeneratorContext> result = null;

        if ( clazz.equals(StencilSet.class) ) {
            return (TemplateBuilder<T, StencilSetGeneratorContext>) new StencilSetBuilder();
        }
        
        if ( clazz.equals(Stencil.class) ) {
            return (TemplateBuilder<T, StencilSetGeneratorContext>) new StencilBuilder();
        }
        
        if ( clazz.equals(PropertyPackage.class) ) {
            return (TemplateBuilder<T, StencilSetGeneratorContext>) new PropertyPackageBuilder();
        }
        
        if ( clazz.equals(Property.class) ) {
            return (TemplateBuilder<T, StencilSetGeneratorContext>) new PropertyBuilder();
        }
        
        return result;
    }
    
}
