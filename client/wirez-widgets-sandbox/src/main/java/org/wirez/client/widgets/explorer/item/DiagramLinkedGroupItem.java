package org.wirez.client.widgets.explorer.item;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.Command;
import org.wirez.client.widgets.explorer.DiagramExplorerItem;
import org.wirez.core.client.ShapeManager;
import org.wirez.core.client.ShapeSet;
import org.wirez.core.client.util.ShapeUtils;
import org.wirez.core.lookup.diagram.DiagramRepresentation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
public class DiagramLinkedGroupItem implements IsWidget, DiagramExplorerItem {

    private static Logger LOGGER = Logger.getLogger(DiagramLinkedGroupItem.class.getName());

    public interface View extends UberView<DiagramLinkedGroupItem> {

        View show( String uuid, String title, String path, SafeUri thumbUri );
        
        View setActive( boolean isActive );
        
    }

    ShapeManager shapeManager;
    View view;
    
    private String uuid;
    private Command callback;

    @Inject
    public DiagramLinkedGroupItem( final ShapeManager shapeManager,
                                   final View view) {
        this.shapeManager = shapeManager;
        this.view = view;
    }

    @PostConstruct
    public void init() {
        view.init(this);
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    @Override
    public IsWidget getView() {
        return asWidget();
    }

    public void show(final DiagramRepresentation diagramRepresentation, final Command callback ) {
        this.callback = callback;
        
        this.uuid = diagramRepresentation.getUUID();
        final Collection<ShapeSet> shapeSets = shapeManager.getShapeSets();
        final ShapeSet shapeSet = ShapeUtils.getShapeSet(shapeSets, diagramRepresentation.getShapeSetId());
        final String defSetId = shapeSet.getDefinitionSetId();
        final SafeUri thumbUri = shapeManager.getThumbnail( defSetId );
        
        view.show( diagramRepresentation.getUUID(), diagramRepresentation.getTitle(), diagramRepresentation.getVFSPath(), null );
    }
    
    public void setActive( final boolean isActive ) {
        view.setActive( isActive );
    }

    public String getDiagramUUID() {
        return uuid;
    }

    void onClick() {
        if ( null != callback ) {
            callback.execute();
        }
    }

    private void log(final Level level, final String message) {
        if ( LogConfiguration.loggingIsEnabled() ) {
            LOGGER.log(level, message);
        }
    }

}
