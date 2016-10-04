package org.wirez.tools.oryx.stencilset.template;

public class Pojo {

    private final String className;
    private final String pkg;
    private final String id;

    public Pojo(String className, String pkg, String id) {
        this.className = className;
        this.pkg = pkg;
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public String getPkg() {
        return pkg;
    }

    public String getId() {
        return id;
    }
}
