package org.wirez.bpmn.definition.adapter.morph;

import org.wirez.bpmn.definition.*;
import org.wirez.core.definition.morph.BindableMorphDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// TODO: This class has to be generated from the morphing annotations.

public class EndEventsMorphDefinition extends BindableMorphDefinition {

    private static final  Map<Class<?>, Collection<Class<?>>> DOMAIN_MORPHS =
            new HashMap<Class<?>, Collection<Class<?>>>( 1 ) {{
                put( BaseEndEvent.class, new ArrayList<Class<?>>( 1 ) {{
                    add( EndNoneEvent.class );
                    add( EndTerminateEvent.class );
                    add( BaseStartEvent.class );
                }} );
            }};

    @Override
    protected Class<?> getDefaultType() {
        return NoneTask.class;
    }

    @Override
    protected Map<Class<?>, Collection<Class<?>>> getDomainMorphs() {
        return DOMAIN_MORPHS;
    }

}
