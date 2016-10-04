package org.wirez.client.widgets.wizard.screen;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.uberfire.client.mvp.UberView;
import org.wirez.client.widgets.event.LoadDiagramEvent;
import org.wirez.client.widgets.explorer.DiagramsExplorer;
import org.wirez.client.widgets.wizard.BaseWizardScreen;
import org.wirez.client.widgets.wizard.CanvasWizardScreen;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
public class LoadDiagramWizardScreen extends BaseWizardScreen implements CanvasWizardScreen {

    private static Logger LOGGER = Logger.getLogger(LoadDiagramWizardScreen.class.getName());

    public interface View extends UberView<LoadDiagramWizardScreen> {
        
        View add(IsWidget explorerView);
        
        View clear();
        
    }

    DiagramsExplorer diagramsExplorer;
    Event<LoadDiagramEvent> loadDiagramEventEvent;
    View view;
    
    private String uuid;
    
    @Inject
    public LoadDiagramWizardScreen(final DiagramsExplorer diagramsExplorer,
                                   final Event<LoadDiagramEvent> loadDiagramEventEvent, 
                                   final View view) {
        this.diagramsExplorer = diagramsExplorer;
        this.loadDiagramEventEvent = loadDiagramEventEvent;
        this.view = view;
    }

    @PostConstruct
    public void init() {
        view.init(this);
        view.add( diagramsExplorer.asWidget() );
    }
    
    @Override
    public String getNextButtonText() {
        return "Load";
    }

    @Override
    protected void doShow() {
        super.doShow();

        clear();

        wizard.setNextButtonEnabled(false);

        diagramsExplorer.show(uuid1 -> {
            
            LoadDiagramWizardScreen.this.uuid = uuid1;
            
            wizard.setNextButtonEnabled(true);
            
        });
        
    }

    @Override
    public Callback getCallback() {
        return new Callback() {
            
            @Override
            public void onNextButtonClick() {

                // Clear to home screen.
                wizard.clear();

                // Fire the load diagram event for others.
                loadDiagramEventEvent.fire(new LoadDiagramEvent( LoadDiagramWizardScreen.this.uuid ));
                
            }

            @Override
            public void onBackButtonClick() {
                wizard.clear();
            }
            
        };
    }
    
    public void clear() {
        this.uuid = null;
        wizard.setNextButtonEnabled(false);
        diagramsExplorer.clear();
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    private void log(final Level level, final String message) {
        if ( LogConfiguration.loggingIsEnabled() ) {
            LOGGER.log(level, message);
        }
    }
    
}
