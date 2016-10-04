package org.wirez.tools.oryx.stencilset.template.model;

public class PropertyValueDef {

    private final String className;
    private final String type;
    private final String value;

    public PropertyValueDef(String className, String type, String value) {
        this.className = className;
        this.type = type;
        this.value = value;
    }

    public String getClassName() {
        return className;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
}
