package org.wirez.tools.oryx.stencilset.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static String[] parseStringArray(JsonElement element) {

        List<String> result = new LinkedList<>();

        if ( element.isJsonArray() ) {

            Iterator fieldValueArrayIt = ((JsonArray)element).iterator();
            while (fieldValueArrayIt.hasNext()) {

                JsonElement arrayElement = (JsonElement) fieldValueArrayIt.next();
                result.add( arrayElement.getAsString() );

            }

            return result.toArray(new String[result.size()]);
        }

        return null;

    }
    
}
