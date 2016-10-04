package org.wirez.tools.oryx.stencilset.model;

import java.util.ArrayList;
import java.util.List;

public class ConnectionRule implements Rule {
    
    private String role;
    private final List<ConnectionRuleEntry> entries = new ArrayList<>();

    public ConnectionRule() {
    }

    @Override
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<ConnectionRuleEntry> getEntries() {
        return entries;
    }
    
}
