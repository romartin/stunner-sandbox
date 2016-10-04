package org.wirez.core.factory.diagram;

import org.wirez.core.diagram.Diagram;
import org.wirez.core.factory.Factory;

public interface TypeDiagramFactory<D extends Diagram> extends DiagramFactory<D> {

    boolean accepts( Class<?> type );

    D buildByType( DiagramBuilder<Class<?>> builder );

}
