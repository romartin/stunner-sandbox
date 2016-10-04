package org.wirez.tools.oryx.stencilset.serializer;

import com.google.gson.*;
import org.wirez.tools.oryx.stencilset.model.CardinalityEdgeRule;
import org.wirez.tools.oryx.stencilset.model.CardinalityRule;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CardinalityRuleDeserializer implements JsonDeserializer<CardinalityRule> {

    @Override
    public CardinalityRule deserialize(JsonElement jsonElement,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        CardinalityRule result = null;

        if (type.equals(CardinalityRule.class)) {
            JsonObject ruleObject = jsonElement.getAsJsonObject();

            if (ruleObject != null) {

                result = new CardinalityRule();

                Set<Map.Entry<String, JsonElement>> _fields = ruleObject.entrySet();
                for (Map.Entry<String, JsonElement> field : _fields) {
                    String fieldName = field.getKey();
                    JsonElement fieldValue = field.getValue();

                    if ("role".equals(fieldName)) {

                        result.setRole(fieldValue.getAsString());

                    } else if ( "incomingEdges".equals(fieldName) && fieldValue.isJsonArray() ) {

                        Iterator fieldValueArrayIt = ((JsonArray)fieldValue).iterator();
                        while (fieldValueArrayIt.hasNext()) {

                            JsonElement element = (JsonElement) fieldValueArrayIt.next();
                            CardinalityEdgeRule  entry = jsonDeserializationContext.deserialize(element, CardinalityEdgeRule.class);
                            result.getIncomingEdgesRules().add( entry );

                        }

                    } else if ( "outgoingEdges".equals(fieldName) && fieldValue.isJsonArray() ) {

                        Iterator fieldValueArrayIt = ((JsonArray)fieldValue).iterator();
                        while (fieldValueArrayIt.hasNext()) {

                            JsonElement element = (JsonElement) fieldValueArrayIt.next();
                            CardinalityEdgeRule  entry = jsonDeserializationContext.deserialize(element, CardinalityEdgeRule.class);
                            result.getOutgoingEdgesRules().add( entry );

                        }

                    }
                    
                    
                }

            }
        }

        return result;
    }

}
