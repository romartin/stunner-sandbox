package org.wirez.core.factory.diagram;

import org.wirez.core.graph.Graph;

public interface DiagramBuilder<T> {

    DiagramBuilder<T> forDefinitionSet( T definitionSet );

    DiagramBuilder<T> forShapeSet( String id );

    DiagramBuilder<T> withTitle( String title );

    DiagramBuilder<T> withGraph( Graph graoh);

}
