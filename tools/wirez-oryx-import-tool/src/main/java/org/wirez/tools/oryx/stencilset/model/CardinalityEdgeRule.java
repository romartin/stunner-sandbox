package org.wirez.tools.oryx.stencilset.model;

public class CardinalityEdgeRule implements Rule {
    
    private String role;
    private int max;

    public CardinalityEdgeRule() {
    }

    @Override
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    
}
