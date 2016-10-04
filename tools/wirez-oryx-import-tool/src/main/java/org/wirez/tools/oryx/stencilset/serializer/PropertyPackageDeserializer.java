package org.wirez.tools.oryx.stencilset.serializer;

import com.google.gson.*;
import org.wirez.tools.oryx.stencilset.model.Property;
import org.wirez.tools.oryx.stencilset.model.PropertyPackage;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PropertyPackageDeserializer implements JsonDeserializer<PropertyPackage> {

    @Override
    public PropertyPackage deserialize(JsonElement jsonElement,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        PropertyPackage result = null;

        if (type.equals(PropertyPackage.class)) {
            JsonObject propertyObject = jsonElement.getAsJsonObject();

            if (propertyObject != null) {

                result = new PropertyPackage();

                Set<Map.Entry<String, JsonElement>> _fields = propertyObject.entrySet();
                for (Map.Entry<String, JsonElement> field : _fields) {
                    String fieldName = field.getKey();
                    JsonElement fieldValue = field.getValue();

                    if ( "name".equals(fieldName) ) {

                        result.setName(fieldValue.getAsString());

                    } else if ( "properties".equals(fieldName) && fieldValue.isJsonArray() ) {
                        
                        Iterator fieldValueArrayIt = ((JsonArray)fieldValue).iterator();
                        while (fieldValueArrayIt.hasNext()) {
                        
                            JsonElement element = (JsonElement) fieldValueArrayIt.next();
                            Property property = jsonDeserializationContext.deserialize(element, Property.class);
                            result.getProperties().add( property );
                            
                        }

                    }
                }

            }
            
        }

        return result;
    }

}
