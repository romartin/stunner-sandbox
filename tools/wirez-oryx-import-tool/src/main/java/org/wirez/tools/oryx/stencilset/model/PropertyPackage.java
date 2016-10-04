package org.wirez.tools.oryx.stencilset.model;

import java.util.ArrayList;
import java.util.Collection;

public class PropertyPackage {
    
    private String name;
    private final Collection<Property> properties = new ArrayList<>();

    public PropertyPackage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Property> getProperties() {
        return properties;
    }
}
