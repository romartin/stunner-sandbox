package org.wirez.tools.oryx.stencilset.serializer;

import com.google.gson.*;
import org.wirez.tools.oryx.stencilset.model.CardinalityEdgeRule;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class CardinalityEdgeRuleDeserializer implements JsonDeserializer<CardinalityEdgeRule> {

    @Override
    public CardinalityEdgeRule deserialize(JsonElement jsonElement,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        CardinalityEdgeRule result = null;

        if (type.equals(CardinalityEdgeRule.class)) {
            JsonObject ruleObject = jsonElement.getAsJsonObject();

            if (ruleObject != null) {

                result = new CardinalityEdgeRule();

                Set<Map.Entry<String, JsonElement>> _fields = ruleObject.entrySet();
                for (Map.Entry<String, JsonElement> field : _fields) {
                    String fieldName = field.getKey();
                    JsonElement fieldValue = field.getValue();

                    if ("role".equals(fieldName)) {

                        result.setRole(fieldValue.getAsString());

                    } else if ("maximum".equals(fieldName)) {

                        result.setMax(fieldValue.getAsInt());

                    } 
                }

            }
        }

        return result;
    }

}
