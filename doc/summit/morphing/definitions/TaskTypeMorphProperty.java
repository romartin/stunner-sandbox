package org.wirez.bpmn.definition.adapter.morph;

import org.wirez.bpmn.definition.BusinessRuleTask;
import org.wirez.bpmn.definition.NoneTask;
import org.wirez.bpmn.definition.ScriptTask;
import org.wirez.bpmn.definition.UserTask;
import org.wirez.bpmn.definition.property.task.TaskType;
import org.wirez.core.definition.morph.BindableMorphProperty;

import java.util.HashMap;
import java.util.Map;

// TODO: This class has to be generated from the morphing annotations.

public class TaskTypeMorphProperty extends BindableMorphProperty<TaskType> {

    static final TaskTypeMorphProperty INSTANCE = new TaskTypeMorphProperty();

    private final Map<Object, Class<?>> MORPH_TARGETS = new HashMap<Object, Class<?>>( 4 ) {{
        put( TaskType.TaskTypes.NONE, NoneTask.class );
        put( TaskType.TaskTypes.USER, UserTask.class );
        put( TaskType.TaskTypes.SCRIPT, ScriptTask.class );
        put( TaskType.TaskTypes.BUSINESS_RULE, BusinessRuleTask.class );
    }};

    @Override
    public Class<?> getPropertyClass() {
        return TaskType.class;
    }

    @Override
    public Map<Object, Class<?>> getMorphTargetClasses() {
        return MORPH_TARGETS;
    }

    @Override
    public Object getValue( final TaskType property ) {
        return property.getValue();
    }

}
