package org.wirez.tools.oryx.stencilset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.wirez.tools.oryx.stencilset.model.*;
import org.wirez.tools.oryx.stencilset.serializer.*;

public class StencilSetUnmarshaller {
    
    public StencilSet unmarshall(String json) {

        // Configure the GSON deserializers and instances.
        GsonBuilder builder = new GsonBuilder();
        JsonDeserializer<StencilSet> stencilSetJsonDeserializer = new StencilSetDeserializer();
        JsonDeserializer<Stencil> stencilJsonDeserializer = new StencilDeserializer();
        JsonDeserializer<PropertyPackage> propertyPackageJsonDeserializer = new PropertyPackageDeserializer();
        JsonDeserializer<Property> propertyJsonDeserializer = new PropertyDeserializer();
        JsonDeserializer<ContainmentRule> containmentRuleJsonDeserializer = new ContainmentRuleDeserializer();
        JsonDeserializer<ConnectionRuleEntry> connectionRuleEntryJsonDeserializer = new ConnectionRuleEntryDeserializer();
        JsonDeserializer<ConnectionRule> connectionRuleJsonDeserializer = new ConnectionRuleDeserializer();
        JsonDeserializer<CardinalityEdgeRule> cardinalityEdgeRuleJsonDeserializer = new CardinalityEdgeRuleDeserializer();
        JsonDeserializer<CardinalityRule> cardinalityRuleJsonDeserializer = new CardinalityRuleDeserializer();
        builder.registerTypeAdapter(StencilSet.class, stencilSetJsonDeserializer);
        builder.registerTypeAdapter(Stencil.class, stencilJsonDeserializer);
        builder.registerTypeAdapter(PropertyPackage.class, propertyPackageJsonDeserializer);
        builder.registerTypeAdapter(Property.class, propertyJsonDeserializer);
        builder.registerTypeAdapter(ContainmentRule.class, containmentRuleJsonDeserializer);
        builder.registerTypeAdapter(ConnectionRuleEntry.class, connectionRuleEntryJsonDeserializer);
        builder.registerTypeAdapter(ConnectionRule.class, connectionRuleJsonDeserializer);
        builder.registerTypeAdapter(CardinalityEdgeRule.class, cardinalityEdgeRuleJsonDeserializer);
        builder.registerTypeAdapter(CardinalityRule.class, cardinalityRuleJsonDeserializer);

        // Do the work.
        Gson gson = builder.create();
        StencilSet stencilSet = gson.fromJson(json, StencilSet.class);
        
        return stencilSet;
    }

}
