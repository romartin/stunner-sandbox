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

public class PropertySetModelBuilder extends VelocityTemplateBuilder {

    private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/PropertySetBuilder.vm";
    private final StencilSetCodeGenerator stencilSetCodeGenerator;

    public PropertySetModelBuilder(StencilSetCodeGenerator stencilSetCodeGenerator) {
        this.stencilSetCodeGenerator = stencilSetCodeGenerator;
    }

    public String build(String thePackage, String className, String propertyBuilderClasName) {
        VelocityContext context = new VelocityContext();

        context.put("thePackage", thePackage);
        context.put("className", className);
        context.put("propertyBuilderClassName", propertyBuilderClasName);

        Map<String, Collection<Pojo>> properties = new HashMap<>();
        Collection<Pojo> propertySetPojos = stencilSetCodeGenerator.getPropertyPackages().values();
        for ( Pojo propertySetPojo : propertySetPojos ) {
            Collection<Pojo> propsByPack = new LinkedList<>();
            PropertyPackage pack = getPropertyPackage( propertySetPojo.getId() );
            assert null != pack;
            Collection<Property> props = pack.getProperties();
            for ( Property p : props ) {
                Pojo pp  = stencilSetCodeGenerator.getProperties().get( p.getId() );
                if ( null != pp ) {
                    propsByPack.add( pp );
                }
            }
            properties.put( propertySetPojo.getId(), propsByPack );
        }
        
        context.put("propertySets", propertySetPojos);
        context.put("propertiesByPack", properties);
        
        return process( context, stencilSetCodeGenerator.getVelocityEngine(), TEMPLATE );
    }
    
    private PropertyPackage getPropertyPackage(String id) {
        Collection<PropertyPackage> packages = stencilSetCodeGenerator.getModel().getPropertyPackages();
        if ( !packages.isEmpty() ) {
            for ( PropertyPackage p : packages ) {
                if ( p.getName().equals(id) ) {
                    return p;
                }
            }
        }
        return null;
    }
    
}
