package org.wirez.tools.oryx.stencilset.template.model;

import org.wirez.tools.oryx.stencilset.template.Pojo;
import org.wirez.tools.oryx.stencilset.template.TemplateResult;

import java.util.Map;

public class StencilTemplateResult extends TemplateResult {
    private final Map<String, PropertyValueDef> defaultPropertyValues;
    
    public StencilTemplateResult(String result, Pojo pojo, Map<String, PropertyValueDef> defaultPropertyValues) {
        super(result, pojo);
        this.defaultPropertyValues = defaultPropertyValues;
    }
    
    public Map<String, PropertyValueDef> getDefaultPropertyValues() {
        return defaultPropertyValues;
    }
}
