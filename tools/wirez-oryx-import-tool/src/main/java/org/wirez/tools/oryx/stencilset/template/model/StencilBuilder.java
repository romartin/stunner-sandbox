package org.wirez.tools.oryx.stencilset.template.model;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.wirez.tools.oryx.stencilset.StencilSetCodeGenerator;
import org.wirez.tools.oryx.stencilset.model.*;
import org.wirez.tools.oryx.stencilset.template.Pojo;
import org.wirez.tools.oryx.stencilset.template.StencilSetGeneratorContext;
import org.wirez.tools.oryx.stencilset.template.TemplateResult;

import java.util.*;

public class StencilBuilder extends AbstractTemplateBuilder<Stencil> {

    private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/Stencil.vm";

    @Override
    public String getPackage() {
        return null;
    }

    @Override
    public boolean accepts(Stencil pojo) {
        return true;
    }

    @Override
    public TemplateResult build(StencilSetGeneratorContext generatorContext, Stencil pojo) {
        if ( null == pojo || null == generatorContext ) throw new NullPointerException();

        String className = StencilSetCodeGenerator.getStencilClassName( pojo.getId() );
        String pkg = generatorContext.getGenerator().getOutputPathName( getPackage() );
        Pojo resultPojo = new Pojo( className , pkg, pojo.getId() );

        VelocityContext context = generatorContext.getVelocityContext();
        context.put( "pojo", resultPojo);

        context.put( "graphElementClass", getGraphElementClass( pojo ) );

        context.put( "category", pojo.getGroups()[0]);
        context.put( "title", pojo.getTitle());
        context.put( "description", pojo.getDescriptions().get(""));
        Collection<String> roles = Arrays.asList(pojo.getRoles());
        context.put( "roles", roles);

        String rulesStr = "";
        Collection<String> rules = buildRules( generatorContext, pojo );
        if ( null != rules ) {
            StringBuilder rulesContent = new StringBuilder();
            for ( String ruleContent : rules ) {
                rulesContent.append( ruleContent ).append("\n");
            }
            rulesStr = rulesContent.toString();
        }

        context.put( "rules", rulesStr );

        // Generate property package class members.
        Collection<Pojo> propertyPacks = getPropertyPackPojos( generatorContext, pojo );
        context.put( "propertyPacks", propertyPacks);
        
        // Generate dependant properties.
        StencilSetGeneratorContext newContext = generatorContext.getGenerator().createContext();
        Collection<Pojo> generatedProperties = generatorContext.getGenerator().generateProperties( pojo.getProperties(), newContext );
        
        Collection<Pojo> stencilProperties = new LinkedList<>();
        for ( Pojo generatedProperty : generatedProperties ) {
            String pId = generatedProperty.getId();
            Pojo[] pp = generatorContext.getGenerator().getProperty(pojo, pId);
            if ( pp != null && pp.length == 1 ) {
                stencilProperties.add( pp[0] );
            }
        }
        context.put( "properties", stencilProperties);

        Map<String, PropertyValueDef> defaultPropertyValues = getDefaultPropertyValues( generatorContext, pojo );
        context.put( "defaultPropertyValues", defaultPropertyValues);

        String result = process(context, generatorContext.getEngine(), TEMPLATE);
        
        return new StencilTemplateResult( result, resultPojo, defaultPropertyValues );
    }
    
    private Map<String, PropertyValueDef> getDefaultPropertyValues(StencilSetGeneratorContext generatorContext, Stencil pojo) {
        Map<String, PropertyValueDef> defaultValues = new HashMap<>();
        
        Collection<String> propertyPojos = generatorContext.getGenerator().getProperties().keySet();
        Collection<Property> properties = pojo.getProperties();
        for ( Property property : properties ) {
            if ( propertyPojos.contains( property.getId() ) ) {
                PropertyValueDef valueDef = PropertyBuilder.getValue( property );
                if ( null != valueDef ) {
                    defaultValues.put( property.getId().toUpperCase() , valueDef );
                }
            }
        }
        
        return defaultValues;
    }
    
    private Collection<String> buildRules(StencilSetGeneratorContext generatorContext, Stencil pojo) {

        Collection<String> result = new LinkedList<>();
        StencilSet stencilSet = generatorContext.getGenerator().getModel();
        Collection<Rule> rules = stencilSet.getContainmentRules();
        if ( null != rules && !rules.isEmpty() ) {
            ContainmentRuleBuilder builder = new ContainmentRuleBuilder( generatorContext.getEngine() );
            for ( Rule rule : rules ) {
                if ( rule.getRole().equalsIgnoreCase(pojo.getId())) {
                    String content = builder.build((ContainmentRule) rule);
                    result.add( content );
                }
            }
        }

        rules = stencilSet.getConnectionRules();
        if ( null != rules && !rules.isEmpty() ) {
            ConnectionRuleBuilder builder = new ConnectionRuleBuilder( generatorContext.getEngine() );
            for ( Rule rule : rules ) {
                if ( rule.getRole().equalsIgnoreCase(pojo.getId())) {
                    String content = builder.build((ConnectionRule) rule);
                    result.add( content );
                }
            }
        }
        
        return result;
    }
    
    private Collection<Pojo> getPropertyPackPojos(StencilSetGeneratorContext generatorContext, Stencil pojo) {
        String[] propertyPackIds = pojo.getPropertyPackages();
        if ( null != propertyPackIds && propertyPackIds.length > 0 ) {
            List<Pojo> result = new ArrayList<>();
            Map<String, Pojo> pojosMap = generatorContext.getGenerator().getPropertyPackages();
            for ( String propertyPackId : propertyPackIds ) {
                if ( pojosMap.containsKey( propertyPackId ) ) {
                    result.add( pojosMap.get( propertyPackId ) );
                }
            }
            return result;
        }
        
        return null;
    }
    
    private String getGraphElementClass(Stencil pojo) {
        String type = pojo.getType();
        
        if ( "node".equals( type  ) ) {
            return "Node.class";
        } else if ( "edge".equals( type  ) ) {
            return "Edge.class";
        }
        
        throw new RuntimeException("Unknown graph element type for stencil [" + pojo.getId() + "]");
    }
    
    private class ContainmentRuleBuilder {

        private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/ContainmentRule.vm";
        private final VelocityEngine engine;

        public ContainmentRuleBuilder(VelocityEngine engine) {
            this.engine = engine;
        }

        public String build(ContainmentRule rule) {
            VelocityContext context = new VelocityContext();
            context.put( "roles", Arrays.asList(rule.getContains()) );
            return process(context, engine, TEMPLATE);
        }
        
    }

    private class ConnectionRuleBuilder {

        private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/ConnectionRule.vm";
        private final VelocityEngine engine;

        public ConnectionRuleBuilder(VelocityEngine engine) {
            this.engine = engine;
        }

        public String build(ConnectionRule rule) {
            VelocityContext context = new VelocityContext();
            context.put( "connections", rule.getEntries());
            return process(context, engine, TEMPLATE);
        }

    }
    
}
