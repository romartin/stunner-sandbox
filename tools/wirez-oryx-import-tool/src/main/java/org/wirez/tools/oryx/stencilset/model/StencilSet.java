package org.wirez.tools.oryx.stencilset.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StencilSet {
    
    private String title;
    private String namespace;
    private final Map<String, String> descriptions = new HashMap<>();
    private final Collection<PropertyPackage> propertyPackages = new ArrayList<>();
    private final Collection<Stencil> stencils = new ArrayList<>();
    private final Collection<Rule> cardinalityRules = new ArrayList<>();
    private final Collection<Rule> connectionRules = new ArrayList<>();
    private final Collection<Rule> containmentRules = new ArrayList<>();

    public StencilSet() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, String> getDescriptions() {
        return descriptions;
    }

    public Collection<PropertyPackage> getPropertyPackages() {
        return propertyPackages;
    }

    public Collection<Stencil> getStencils() {
        return stencils;
    }

    public Collection<Rule> getCardinalityRules() {
        return cardinalityRules;
    }

    public Collection<Rule> getConnectionRules() {
        return connectionRules;
    }

    public Collection<Rule> getContainmentRules() {
        return containmentRules;
    }
}

