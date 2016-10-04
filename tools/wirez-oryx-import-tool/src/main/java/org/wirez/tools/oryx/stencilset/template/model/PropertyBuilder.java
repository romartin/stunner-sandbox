package org.wirez.tools.oryx.stencilset.template.model;

import org.apache.velocity.VelocityContext;
import org.wirez.tools.oryx.stencilset.StencilSetCodeGenerator;
import org.wirez.tools.oryx.stencilset.model.Property;
import org.wirez.tools.oryx.stencilset.template.Pojo;
import org.wirez.tools.oryx.stencilset.template.StencilSetGeneratorContext;
import org.wirez.tools.oryx.stencilset.template.TemplateResult;

public class PropertyBuilder extends AbstractTemplateBuilder<Property> {

    private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/Property.vm";

    @Override
    public String getPackage() {
        return "property";
    }

    @Override
    public boolean accepts(Property pojo) {
        return getValue( pojo ) != null;
    }

    @Override
    public TemplateResult build(StencilSetGeneratorContext generatorContext, Property pojo) {
        if ( null == pojo || null == generatorContext ) throw new NullPointerException();

        String className = StencilSetCodeGenerator.getPropertyClassName( pojo.getId() );
        String pkg = generatorContext.getGenerator().getOutputPathName( getPackage() );
        Pojo resultPojo = new Pojo( className , pkg, pojo.getId() );
        
        VelocityContext context = generatorContext.getVelocityContext();
        context.put( "property", pojo);
        context.put( "pojo", resultPojo);
        context.put( "propertyDescription", pojo.getDescriptions().get(""));
        PropertyValueDef value = getValue( pojo );
        context.put( "propValue", value);
        
        String content = process(context, generatorContext.getEngine(), TEMPLATE);
        
        return new TemplateResult( content, resultPojo );
    }
    
    public static PropertyValueDef getValue(Property pojo) {
        
        String id = pojo.getId();
        String type = pojo.getType();
        
        // Defaults
        String propClassName = "String";
        String propType = "StringType()";
        String propValue = "\"" + pojo.getValue() + "\"" ;
        
        if ( "String".equals(type) ) {
            
        } else if ( "Text".equals(type) ) {

        } else if ( "Color".equals(type) ) {

            propType = "ColorType()";
            
        } else if ( "Float".equals(type) ) {

            propClassName = "Double";
            propType = "DoubleType()";
            propValue = pojo.getValue() + "d";
            
        } else if ( "Boolean".equals(type) ) {

            propClassName = "Boolean";
            propType = "BooleanType()";
            propValue = pojo.getValue();
            
        } else if ( "Integer".equals(type) ) {

            propClassName = "Integer";
            propType = "IntegerType()";
            propValue = pojo.getValue();
            
        } else {
            
            return  null;
            
        }
        
        return new PropertyValueDef( propClassName, propType, propValue );
    }
    
    
}
