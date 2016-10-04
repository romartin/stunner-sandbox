package org.wirez.client.widgets.explorer;

import com.google.gwt.user.client.ui.IsWidget;
import org.uberfire.mvp.Command;
import org.wirez.core.lookup.diagram.DiagramRepresentation;

public interface DiagramExplorerItem {

    void show( DiagramRepresentation diagramRepresentation, Command showCallback );

    void setActive( boolean active );

    String getDiagramUUID();

    IsWidget getView();

}
