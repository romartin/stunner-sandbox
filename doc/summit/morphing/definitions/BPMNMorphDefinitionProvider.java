package org.wirez.bpmn.definition.adapter.morph;

import org.wirez.core.definition.morph.MorphDefinition;
import org.wirez.core.definition.morph.MorphDefinitionProvider;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// TODO: This class has to be generated from the morphing annotations.

@ApplicationScoped
public class BPMNMorphDefinitionProvider implements MorphDefinitionProvider {

    private static final List<MorphDefinition> MORPH_DEFINITIONS =
            new ArrayList<MorphDefinition>() {{
                add( new ActivitiesMorphDefinition() );
                add( new TaskMorphDefinition() );
                add( new SubprocessMorphDefinition() );
                add( new StartEventsMorphDefinition() );
                add( new EndEventsMorphDefinition() );
            }};

    @Override
    public Collection<MorphDefinition> getMorphDefinitions() {
        return MORPH_DEFINITIONS;
    }

}
