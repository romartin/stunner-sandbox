package org.wirez.tools.oryx.stencilset.serializer;

import com.google.gson.*;
import org.wirez.tools.oryx.stencilset.model.ConnectionRule;
import org.wirez.tools.oryx.stencilset.model.ConnectionRuleEntry;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ConnectionRuleDeserializer implements JsonDeserializer<ConnectionRule> {

    @Override
    public ConnectionRule deserialize(JsonElement jsonElement,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ConnectionRule result = null;

        if (type.equals(ConnectionRule.class)) {
            JsonObject ruleObject = jsonElement.getAsJsonObject();

            if (ruleObject != null) {

                result = new ConnectionRule();

                Set<Map.Entry<String, JsonElement>> _fields = ruleObject.entrySet();
                for (Map.Entry<String, JsonElement> field : _fields) {
                    String fieldName = field.getKey();
                    JsonElement fieldValue = field.getValue();

                    if ("role".equals(fieldName)) {

                        result.setRole(fieldValue.getAsString());

                    } else if ( "connects".equals(fieldName) && fieldValue.isJsonArray() ) {

                        Iterator fieldValueArrayIt = ((JsonArray)fieldValue).iterator();
                        while (fieldValueArrayIt.hasNext()) {

                            JsonElement element = (JsonElement) fieldValueArrayIt.next();
                            ConnectionRuleEntry entry = jsonDeserializationContext.deserialize(element, ConnectionRuleEntry.class);
                            result.getEntries().add( entry );

                        }

                    }
                    
                }

            }
        }

        return result;
    }

}
