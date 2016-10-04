package org.wirez.core.factory.diagram;

import org.wirez.core.diagram.Diagram;
import org.wirez.core.factory.Factory;

public interface DiagramFactory<D extends Diagram> extends Factory<D, DiagramBuilder<String>> {

    boolean accepts( String id );

}
