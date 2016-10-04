package org.wirez.tools.oryx.stencilset.template;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public interface TemplateBuilder<T, C> {

    String getPackage();
    
    boolean accepts ( T pojo );

    TemplateResult build( C context, T pojo );
}
