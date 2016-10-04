package org.wirez.bpmn.definition.adapter.morph;

import org.wirez.bpmn.definition.BaseEndEvent;
import org.wirez.bpmn.definition.BaseStartEvent;
import org.wirez.bpmn.definition.NoneTask;
import org.wirez.bpmn.definition.StartNoneEvent;
import org.wirez.core.definition.morph.BindableMorphDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// TODO: This class has to be generated from the morphing annotations.

public class StartEventsMorphDefinition extends BindableMorphDefinition {

    private static final  Map<Class<?>, Collection<Class<?>>> DOMAIN_MORPHS =
            new HashMap<Class<?>, Collection<Class<?>>>( 1 ) {{
                put( BaseStartEvent.class, new ArrayList<Class<?>>( 1 ) {{
                    add( StartNoneEvent.class );
                    add( BaseEndEvent.class );
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
