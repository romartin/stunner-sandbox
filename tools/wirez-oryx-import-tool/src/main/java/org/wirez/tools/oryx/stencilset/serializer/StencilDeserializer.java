package org.wirez.tools.oryx.stencilset.serializer;

import com.google.gson.*;
import org.wirez.tools.oryx.stencilset.model.Property;
import org.wirez.tools.oryx.stencilset.model.Stencil;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StencilDeserializer implements JsonDeserializer<Stencil> {

    @Override
    public Stencil deserialize(JsonElement jsonElement,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Stencil result = null;

        if (type.equals(Stencil.class)) {
            JsonObject stencilObject = jsonElement.getAsJsonObject();

            if (stencilObject != null) {

                result = new Stencil();

                Set<Map.Entry<String, JsonElement>> _fields = stencilObject.entrySet();
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
                        
                    } else if ("title".equals(fieldName)) {

                        result.setTitle(fieldValue.getAsString());

                    } else if ("groups".equals(fieldName)) {

                        String[] groups = Utils.parseStringArray( fieldValue );
                        result.setGroups( groups );

                    } else if ("roles".equals(fieldName)) {

                        String[] roles = Utils.parseStringArray( fieldValue );
                        result.setRoles( roles );

                    } else if ("propertyPackages".equals(fieldName)) {

                        String[] propertyPackages = Utils.parseStringArray( fieldValue );
                        result.setPropertyPackages( propertyPackages );

                    } else if ("view".equals(fieldName)) {

                        result.setView(fieldValue.getAsString());

                    } else if ("icon".equals(fieldName)) {

                        result.setIcon(fieldValue.getAsString());

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
