package org.wirez.tools.oryx.stencilset.model;

public class ConnectionRuleEntry {
    
    private String from;
    private String[] to;

    public ConnectionRuleEntry() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }
    
}
