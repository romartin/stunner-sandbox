package org.wirez.tools.oryx.stencilset.template.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.wirez.tools.oryx.stencilset.StencilSetCodeGenerator;
import org.wirez.tools.oryx.stencilset.model.*;
import org.wirez.tools.oryx.stencilset.template.Pojo;
import org.wirez.tools.oryx.stencilset.template.StencilSetGeneratorContext;
import org.wirez.tools.oryx.stencilset.template.TemplateResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class StencilSetBuilder extends AbstractTemplateBuilder<StencilSet> {

    private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/StencilSet.vm";

    @Override
    public String getPackage() {
        return null;
    }

    @Override
    public boolean accepts(StencilSet pojo) {
        return true;
    }

    @Override
    public TemplateResult build(StencilSetGeneratorContext generatorContext, StencilSet pojo) {
        if ( null == pojo || null == generatorContext ) throw new NullPointerException();

        String name = generatorContext.getGenerator().getDefinitionSetName();
        String className = StencilSetCodeGenerator.getStencilSetClassName( name );
        String id = StringUtils.uncapitalize(name);
        String pkg = generatorContext.getGenerator().getOutputPathName( getPackage() );
        Pojo resultPojo = new Pojo( className , pkg, id );

        VelocityContext context = generatorContext.getVelocityContext();
        context.put( "pojo", resultPojo);

        context.put( "domain", pojo.getNamespace());
        context.put( "description", pojo.getDescriptions().get(""));

        // Generate property packages.
        StencilSetGeneratorContext newContext = generatorContext.getGenerator().createContext();
        generatorContext.getGenerator().generatePropertyPackages( pojo.getPropertyPackages(), newContext );

        // Generate stencils.
        newContext = generatorContext.getGenerator().createContext();
        Collection<Pojo> generatedStencils = generatorContext.getGenerator().generateStencils( pojo.getStencils(), newContext );
        context.put( "stencils", generatedStencils);

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
        
        String result = process(context, generatorContext.getEngine(), TEMPLATE);
        
        return new TemplateResult( result, resultPojo );
    }

    private Collection<String> buildRules(StencilSetGeneratorContext generatorContext, StencilSet pojo) {

        Collection<String> result = new LinkedList<>();
        StencilSet stencilSet = generatorContext.getGenerator().getModel();
        

        Collection<Rule> rules = stencilSet.getCardinalityRules();
        if ( null != rules && !rules.isEmpty() ) {
            CardinalityRuleBuilder builder = new CardinalityRuleBuilder( generatorContext.getEngine() );
            for ( Rule rule : rules ) {
                String content = builder.build((CardinalityRule) rule);
                result.add( content );
            }
        }

        return result;
    }

    public class CardinalityEdgePojo {
        private final String type;
        private final String edge;
        private final String max;

        public CardinalityEdgePojo(String type, String edge, String max) {
            this.type = type;
            this.edge = edge;
            this.max = max;
        }

        public String getType() {
            return type;
        }

        public String getEdge() {
            return edge;
        }

        public String getMax() {
            return max;
        }
    }

    private class CardinalityRuleBuilder {

        private static final String TEMPLATE = "org/wirez/tools/oryx/stencilset/template/CardinalityRule.vm";
        private final VelocityEngine engine;

        public CardinalityRuleBuilder(VelocityEngine engine) {
            this.engine = engine;
        }

        public String build(CardinalityRule rule) {
            VelocityContext context = new VelocityContext();
            context.put("role", rule.getRole());
            context.put("min", "0");

            Collection<CardinalityEdgePojo> edgePojos = new LinkedList<>();

            List<CardinalityEdgeRule> incoming = rule.getIncomingEdgesRules();
            if ( null != incoming && !incoming.isEmpty() ) {
                for ( CardinalityEdgeRule inEdge : incoming ) {
                    edgePojos.add( new CardinalityEdgePojo( "EdgeType.INCOMING", inEdge.getRole(), Integer.toString(inEdge.getMax()) ) );
                }
            }
            List<CardinalityEdgeRule> outgoing = rule.getOutgoingEdgesRules();
            if ( null != outgoing && !outgoing.isEmpty() ) {
                for ( CardinalityEdgeRule outEdge : outgoing ) {
                    edgePojos.add( new CardinalityEdgePojo( "EdgeType.OUTGOING", outEdge.getRole(), Integer.toString(outEdge.getMax()) ) );
                }
            }

            context.put("edges", edgePojos);

            return process(context, engine, TEMPLATE);
        }

    }
    
}
