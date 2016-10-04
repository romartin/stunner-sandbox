package org.wirez.tools.oryx.stencilset.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Stencil {
    
    private String type;
    private String id;
    private String title;
    private final Map<String, String> descriptions = new HashMap<>();
    private String[] groups;
    private String[] propertyPackages;
    private String[] roles;
    private final Collection<Property> properties = new ArrayList<>();
    private String view;
    private String icon;

    public Stencil() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, String> getDescriptions() {
        return descriptions;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    public String[] getPropertyPackages() {
        return propertyPackages;
    }

    public void setPropertyPackages(String[] propertyPackages) {
        this.propertyPackages = propertyPackages;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public Collection<Property> getProperties() {
        return properties;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
