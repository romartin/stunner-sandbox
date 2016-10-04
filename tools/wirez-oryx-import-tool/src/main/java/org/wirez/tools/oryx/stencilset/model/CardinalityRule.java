package org.wirez.tools.oryx.stencilset.model;

import java.util.ArrayList;
import java.util.List;

public class CardinalityRule implements Rule {

    private String role;
    private final List<CardinalityEdgeRule> incomingEdgesRules = new ArrayList<>();
    private final List<CardinalityEdgeRule> outgoingEdgesRules = new ArrayList<>();

    public CardinalityRule() {
    }

    @Override
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<CardinalityEdgeRule> getIncomingEdgesRules() {
        return incomingEdgesRules;
    }

    public List<CardinalityEdgeRule> getOutgoingEdgesRules() {
        return outgoingEdgesRules;
    }
}
