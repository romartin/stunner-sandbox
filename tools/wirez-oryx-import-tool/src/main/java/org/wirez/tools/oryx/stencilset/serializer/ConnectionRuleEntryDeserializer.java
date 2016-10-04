package org.wirez.tools.oryx.stencilset.serializer;

import com.google.gson.*;
import org.wirez.tools.oryx.stencilset.model.ConnectionRuleEntry;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class ConnectionRuleEntryDeserializer implements JsonDeserializer<ConnectionRuleEntry> {

    @Override
    public ConnectionRuleEntry deserialize(JsonElement jsonElement,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ConnectionRuleEntry result = null;

        if (type.equals(ConnectionRuleEntry.class)) {
            JsonObject ruleObject = jsonElement.getAsJsonObject();

            if (ruleObject != null) {

                result = new ConnectionRuleEntry();

                Set<Map.Entry<String, JsonElement>> _fields = ruleObject.entrySet();
                for (Map.Entry<String, JsonElement> field : _fields) {
                    String fieldName = field.getKey();
                    JsonElement fieldValue = field.getValue();

                    if ("from".equals(fieldName)) {

                        result.setFrom(fieldValue.getAsString());

                    } else if ("to".equals(fieldName)) {

                        String[] roles = Utils.parseStringArray( fieldValue );
                        result.setTo( roles );

                    } 
                }

            }
        }

        return result;
    }

}
