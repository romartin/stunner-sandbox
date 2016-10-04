package org.wirez.tools.oryx.stencilset.serializer;

import com.google.gson.*;
import org.wirez.tools.oryx.stencilset.model.*;

import java.lang.reflect.Type;
import java.util.*;

public class StencilSetDeserializer implements JsonDeserializer<StencilSet> {

    @Override
    public StencilSet deserialize(JsonElement jsonElement, 
                                  Type type, 
                                  JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        StencilSet result = null;

        if ( type.equals(StencilSet.class) ) {
            JsonObject stencilSetObject = jsonElement.getAsJsonObject();

            if ( stencilSetObject != null ) {

                result = new StencilSet();
                
                Set<Map.Entry<String, JsonElement>> _fields = stencilSetObject.entrySet();
                for (Map.Entry<String, JsonElement> field : _fields) {
                    String fieldName = field.getKey();
                    JsonElement fieldValue = field.getValue();
                    
                    if ( "title".equals(fieldName) ) {
                        
                        result.setTitle( fieldValue.getAsString() );
                                
                    } else if ( "namespace".equals(fieldName) ) {

                        result.setNamespace( fieldValue.getAsString() );
                        
                    } else if ( fieldName.startsWith("description") ) {
                        String lang = "";
                        int langIdx = fieldName.lastIndexOf("_");
                        if ( langIdx > -1 ) {
                            lang = fieldName.substring( langIdx + 1, fieldName.length() );
                        }
                        result.getDescriptions().put( lang, fieldValue.getAsString() );
                    } else if ( "propertyPackages".equals(fieldName) && fieldValue.isJsonArray() ) {

                        Iterator fieldValueArrayIt = ((JsonArray)fieldValue).iterator();
                        while (fieldValueArrayIt.hasNext()) {

                            JsonElement element = (JsonElement) fieldValueArrayIt.next();
                            PropertyPackage propertyPackage = jsonDeserializationContext.deserialize(element, PropertyPackage.class);
                            result.getPropertyPackages().add( propertyPackage );

                        }

                    } else if ( "stencils".equals(fieldName) && fieldValue.isJsonArray() ) {

                        Iterator fieldValueArrayIt = ((JsonArray)fieldValue).iterator();
                        while (fieldValueArrayIt.hasNext()) {

                            JsonElement element = (JsonElement) fieldValueArrayIt.next();
                            Stencil stencil = jsonDeserializationContext.deserialize(element, Stencil.class);
                            result.getStencils().add( stencil );

                        }

                    } else if ( "rules".equals(fieldName) && fieldValue.isJsonObject() ) {
                        JsonObject rulesObject = fieldValue.getAsJsonObject();
                        
                        // Cardinality rules.
                        List<Rule> cardinalityRules = parseRules(jsonDeserializationContext, rulesObject, "cardinalityRules", CardinalityRule.class); 
                        result.getCardinalityRules().addAll( cardinalityRules );

                        // Connection rules.
                        List<Rule> connectionRules = parseRules(jsonDeserializationContext, rulesObject, "connectionRules", ConnectionRule.class);
                        result.getConnectionRules().addAll( connectionRules );

                        // Containment rules.
                        List<Rule> containmentRules = parseRules(jsonDeserializationContext, rulesObject, "containmentRules", ContainmentRule.class);
                        result.getContainmentRules().addAll( containmentRules );

                    }
                    
                }
                
            }
            
        }
        
        return result;
    }
    
    private List<Rule> parseRules(JsonDeserializationContext jsonDeserializationContext,
                                  JsonObject rulesObject, 
                                  String fieldName, 
                                  Class clazz) {
        List<Rule> result = null;
        
        JsonArray cardinalityRulesArray = rulesObject.get(fieldName).getAsJsonArray();
        
        if ( null != cardinalityRulesArray ) {
            
            result = new ArrayList<>();
            
            Iterator cardinalityRulesArrayIt = cardinalityRulesArray.iterator();
            while (cardinalityRulesArrayIt.hasNext()) {
                JsonElement element = (JsonElement) cardinalityRulesArrayIt.next();
                Rule rule = jsonDeserializationContext.deserialize(element, clazz);
                result.add( rule );
            }
            
        }
        
        return result;
    }
    
}
