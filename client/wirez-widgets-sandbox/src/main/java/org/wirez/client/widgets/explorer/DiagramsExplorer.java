package org.wirez.client.widgets.explorer;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.client.mvp.UberView;
import org.wirez.client.widgets.event.LoadDiagramEvent;
import org.wirez.client.widgets.explorer.item.DiagramLinkedGroupItem;
import org.wirez.core.client.service.ClientDiagramServices;
import org.wirez.core.client.service.ClientRuntimeError;
import org.wirez.core.client.service.ServiceCallback;
import org.wirez.core.client.util.WirezClientLogger;
import org.wirez.core.lookup.LookupManager;
import org.wirez.core.lookup.diagram.DiagramLookupRequest;
import org.wirez.core.lookup.diagram.DiagramLookupRequestImpl;
import org.wirez.core.lookup.diagram.DiagramRepresentation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
public class DiagramsExplorer implements IsWidget {

    private static Logger LOGGER = Logger.getLogger(DiagramsExplorer.class.getName());
    
    public interface View extends UberView<DiagramsExplorer> {

        View showEmpty();

        View add( IsWidget diagramRepresentationItem );
        
        View clear();
        
    }
    
    public interface ClickCallback {
        
        void onClick( String uuid );
        
    }

    ClientDiagramServices clientDiagramServices;
    SyncBeanManager beanManager;
    Event<LoadDiagramEvent> loadDiagramEventEvent;
    View view;

    private final List<DiagramExplorerItem> items = new LinkedList<>();
    private ClickCallback clickCallback;

    private DiagramExplorerItem createNewLinkedGroupItem() {
        return beanManager.lookupBean( DiagramLinkedGroupItem.class ).newInstance();
    }

    @Inject
    public DiagramsExplorer(final ClientDiagramServices clientDiagramServices, 
                            final SyncBeanManager beanManager,
                            final Event<LoadDiagramEvent> loadDiagramEventEvent, 
                            final View view) {
        this.clientDiagramServices = clientDiagramServices;
        this.beanManager = beanManager;
        this.loadDiagramEventEvent = loadDiagramEventEvent;
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
    
    public void show( final ClickCallback clickCallback) {
        this.clickCallback = clickCallback;

        // Notify some processing starts.
        fireProcessingStarted();
        
        clear();
        
        final DiagramLookupRequest request = new DiagramLookupRequestImpl.Builder().build();
        
        clientDiagramServices.lookup( request, new ServiceCallback<LookupManager.LookupResponse<DiagramRepresentation>>() {
            @Override
            public void onSuccess(final LookupManager.LookupResponse<DiagramRepresentation> response) {

                final List<DiagramRepresentation> items = response.getResults();

                if ( null == items || items.isEmpty() ) {

                    view.showEmpty();

                } else {

                    for (final DiagramRepresentation diagram : items) {

                        addEntry( diagram );
                    }

                }

                // Notify some processing ends.
                fireProcessingCompleted();

            }

            @Override
            public void onError(final ClientRuntimeError error) {
                showError( error );
            }
        });
        
    }
    
    public void clear() {
        items.clear();
        view.clear();
    }

    private void addEntry(final DiagramRepresentation diagramRepresentation) {
        final DiagramExplorerItem item = createNewLinkedGroupItem();
        view.add( item.getView() );
        items.add( item );
        item.show(diagramRepresentation, () -> {
            if ( null != clickCallback ) {
                final String itemUUID = diagramRepresentation.getUUID();
                setItemActive( itemUUID );
                clickCallback.onClick( itemUUID );
            }
        });
    }
    
    private void setItemActive( final String itemUUID ) {
        for ( final DiagramExplorerItem item : items ) {
            item.setActive( item.getDiagramUUID().equals( itemUUID ) );
        }
    }

    private void fireProcessingStarted() {
        // TODO
    }

    private void fireProcessingCompleted() {
        // TODO
    }

    private void showError( final ClientRuntimeError error ) {
        final String message = WirezClientLogger.getErrorMessage(error);
        showError( message );
    }

    private void showError( final String error ) {
        fireProcessingCompleted();
        log( Level.SEVERE, error);
    }

    private void log(final Level level, final String message) {
        if ( LogConfiguration.loggingIsEnabled() ) {
            LOGGER.log(level, message);
        }
    }

}
