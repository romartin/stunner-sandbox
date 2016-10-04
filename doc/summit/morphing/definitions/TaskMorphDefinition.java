package org.wirez.bpmn.definition.adapter.morph;

import org.wirez.bpmn.definition.BaseTask;
import org.wirez.bpmn.definition.NoneTask;
import org.wirez.core.definition.morph.BindablePropertyMorphDefinition;
import org.wirez.core.definition.morph.MorphProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// TODO: This class has to be generated from the morphing annotations.

public class TaskMorphDefinition extends BindablePropertyMorphDefinition {

    private static final Map<Class<?>, Collection<MorphProperty>> PROPERTY_MORPH_DEFINITIONS =
            new HashMap<Class<?>, Collection<MorphProperty>>( 1 ) {{
                put( BaseTask.class, new ArrayList<MorphProperty>( 1 ) {{
                    add( TaskTypeMorphProperty.INSTANCE );
                }} );
            }};

    @Override
    protected Map<Class<?>, Collection<MorphProperty>> getBindableMorphProperties() {
        return PROPERTY_MORPH_DEFINITIONS;
    }

    @Override
    protected Class<?> getDefaultType() {
        return NoneTask.class;
    }

}
