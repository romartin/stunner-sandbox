package org.wirez.tools.oryx.stencilset.template;

public class TemplateResult {
    
    private final String result;
    private final Pojo pojo;

    public TemplateResult(String result, Pojo pojo) {
        this.result = result;
        this.pojo = pojo;
    }

    public String getResult() {
        return result;
    }

    public Pojo getPojo() {
        return pojo;
    }
    
}
