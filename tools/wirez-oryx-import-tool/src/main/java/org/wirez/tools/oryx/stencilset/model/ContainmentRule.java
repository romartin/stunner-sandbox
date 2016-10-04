package org.wirez.tools.oryx.stencilset.model;

public class ContainmentRule implements Rule {

    private String role;
    private String[] contains;

    public ContainmentRule() {
    }

    @Override
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String[] getContains() {
        return contains;
    }

    public void setContains(String[] contains) {
        this.contains = contains;
    }
    
}
