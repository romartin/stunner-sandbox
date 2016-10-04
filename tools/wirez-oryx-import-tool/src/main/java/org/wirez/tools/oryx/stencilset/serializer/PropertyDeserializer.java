package org.wirez.tools.oryx.stencilset.serializer;

import com.google.gson.*;
import org.wirez.tools.oryx.stencilset.model.Property;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class PropertyDeserializer implements JsonDeserializer<Property> {

    @Override
    public Property deserialize(JsonElement jsonElement,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Property result = null;

        if (type.equals(Property.class)) {
            JsonObject propertyObject = jsonElement.getAsJsonObject();

            if (propertyObject != null) {

                result = new Property();

                Set<Map.Entry<String, JsonElement>> _fields = propertyObject.entrySet();
                for (Map.Entry<String, JsonElement> field : _fields) {
                    String fieldName = field.getKey();
                    JsonElement fieldValue = field.getValue();

                    if ("id".equals(fieldName)) {

                        result.setId(fieldValue.getAsString());

                    } else if ("type".equals(fieldName)) {

                        result.setType(fieldValue.getAsString());

                    } else if (fieldName.startsWith("description")) {
                        String lang = "";
                        int langIdx = fieldName.lastIndexOf("_");
                        if (langIdx > -1) {
                            lang = fieldName.substring(langIdx + 1, fieldName.length());
                        }
                        result.getDescriptions().put(lang, fieldValue.getAsString());
                    }  else if ("title".equals(fieldName)) {

                        result.setTitle(fieldValue.getAsString());

                    }  else if ("value".equals(fieldName)) {

                        result.setValue(fieldValue.getAsString());

                    }  else if ("length".equals(fieldName)) {

                        result.setLength(fieldValue.getAsString());

                    }  else if ("readonly".equals(fieldName)) {

                        result.setReadonly(fieldValue.getAsBoolean());

                    }  else if ("optional".equals(fieldName)) {

                        result.setOptional(fieldValue.getAsBoolean());

                    } 
                }

            }
        }

        return result;
    }

}
