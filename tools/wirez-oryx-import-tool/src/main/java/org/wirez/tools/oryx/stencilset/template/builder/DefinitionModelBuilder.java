package org.wirez.tools.oryx.stencilset.template.builder;

import org.apache.velocity.VelocityContext;
import org.wirez.tools.oryx.stencilset.StencilSetCodeGenerator;
import org.wirez.tools.oryx.stencilset.model.Property;
import org.wirez.tools.oryx.stencilset.model.PropertyPackage;
import org.wirez.tools.oryx.stencilset.model.Stencil;
import org.wirez.tools.oryx.stencilset.template.Pojo;
import org.wirez.tools.oryx.stencilset.template.VelocityTemplateBuilder;
import org.wirez.tools.oryx.stencilset.template.model.PropertyValueDef;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DefinitionModelBuilder extends VelocityTemplateBuilder {

    private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/DefinitionBuilder.vm";
    private final StencilSetCodeGenerator stencilSetCodeGenerator;

    public DefinitionModelBuilder(StencilSetCodeGenerator stencilSetCodeGenerator) {
        this.stencilSetCodeGenerator = stencilSetCodeGenerator;
    }

    public String build(String thePackage, String className, String propertySetBuilderClassName, String propertyBuilderClassName) {
        VelocityContext context = new VelocityContext();

        context.put("thePackage", thePackage);
        context.put("className", className);
        context.put("propertySetBuilderClassName", propertySetBuilderClassName);
        context.put("propertyBuilderClassName", propertyBuilderClassName);

        Collection<Pojo> stencilPojos = stencilSetCodeGenerator.getStencils().values();
        context.put("stencils", stencilPojos);

        Map<String, Collection<Pojo>> propertyPacksByStencil = new HashMap<>();
        Map<String, Collection<Pojo>> propertiesByStencil = new HashMap<>();
        for ( Pojo stencilPojo : stencilPojos ) {
            Stencil stencil = getStencil( stencilPojo.getId() );

            // Property sets by stencil.
            String[] propPackIds = stencil.getPropertyPackages();
            Collection<Pojo> propsSetByStencil = new LinkedList<>();
            for ( String propPackId : propPackIds ) {
                Pojo propPackPojo = stencilSetCodeGenerator.getPropertyPackages().get( propPackId );
                if ( null != propPackPojo ) {
                    propsSetByStencil.add( propPackPojo );
                }
            }
            propertyPacksByStencil.put( stencil.getId() , propsSetByStencil );
            
            // Properties by stencil.
            Collection<Property> properties = stencil.getProperties();
            Collection<Pojo> propsByStencil = new LinkedList<>();
            for ( Property property : properties ) {
                String pId = property.getId();
                Pojo propPackPojo = stencilSetCodeGenerator.getProperties().get( pId );
                if ( null != propPackPojo ) {
                    propsByStencil.add( propPackPojo );
                }
            }
            propertiesByStencil.put( stencil.getId() , propsByStencil );
            
        }
        
        context.put("propertyPacksByStencil", propertyPacksByStencil);
        context.put("propertiesByStencil", propertiesByStencil);

        Map<String, String> initsByStencil = new HashMap<>();
        Map<String, Map<String, PropertyValueDef>> stencilDefaultValues = stencilSetCodeGenerator.getStencilDefaultValues();
        for ( Pojo stencilPojo : stencilPojos ) {
            
            String id = stencilPojo.getId();
            Map<String, PropertyValueDef> defValues = stencilDefaultValues.get( id );
            StringBuilder content = new StringBuilder();
            if ( null != defValues ) {
                Stencil stencil = getStencil( id );
                for ( Map.Entry<String, PropertyValueDef> defValuesEntry : defValues.entrySet() ) {
                    String pId = defValuesEntry.getKey().toLowerCase();
                    PropertyValueDef pValue = defValuesEntry.getValue();
                    
                    Pojo[] pp = stencilSetCodeGenerator.getProperty(stencil, pId);
                    
                    if ( null != pp && pp.length == 1 ) {
                        content.append("o.get").append(pp[0].getClassName()).append("().setValue(")
                                .append(stencilPojo.getClassName()).append(".").append(pId.toUpperCase()).append(");\n");
                    } else if ( null != pp && pp.length == 2 ) {
                        content.append("o.get").append(pp[0].getClassName()).append("().get")
                                .append(pp[1].getClassName()).append("().setValue(").append(stencilPojo.getClassName())
                                .append(".").append(pId.toUpperCase()).append(");\n");
                    }
                    
                }

                initsByStencil.put( stencil.getId()  , content.toString() );
            }
            
            
        }
        context.put("initsByStencil", initsByStencil);
        
        
        return process( context, stencilSetCodeGenerator.getVelocityEngine(), TEMPLATE );
    }
    
    
    
    private Stencil getStencil(String id) {
        Collection<Stencil> packages = stencilSetCodeGenerator.getModel().getStencils();
        if ( !packages.isEmpty() ) {
            for ( Stencil p : packages ) {
                if ( p.getId().equals(id) ) {
                    return p;
                }
            }
        }
        return null;
    }
    
    
    
}
