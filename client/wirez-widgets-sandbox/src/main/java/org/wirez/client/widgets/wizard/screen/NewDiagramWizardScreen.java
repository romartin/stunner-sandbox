package org.wirez.client.widgets.wizard.screen;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Widget;
import org.uberfire.client.mvp.UberView;
import org.uberfire.mvp.Command;
import org.wirez.client.widgets.event.CreateEmptyDiagramEvent;
import org.wirez.client.widgets.wizard.BaseWizardScreen;
import org.wirez.client.widgets.wizard.CanvasWizardScreen;
import org.wirez.core.client.ShapeManager;
import org.wirez.core.client.ShapeSet;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Collection;

@Dependent
public class NewDiagramWizardScreen extends BaseWizardScreen implements CanvasWizardScreen {

    public interface View extends UberView<NewDiagramWizardScreen> {

        View setHeading(String title);

        View addItem(String name, String description, SafeUri thumbnailUri, boolean isSelected, Command clickHandler);

        View showEmptyView();

        View clear();
        
    }

    ShapeManager shapeManager;
    Event<CreateEmptyDiagramEvent> createEmptyDiagramEventEvent;
    View view;

    @Inject
    public NewDiagramWizardScreen(final ShapeManager shapeManager, 
                                  final Event<CreateEmptyDiagramEvent> createEmptyDiagramEventEvent, 
                                  final View view) {
        this.shapeManager = shapeManager;
        this.createEmptyDiagramEventEvent = createEmptyDiagramEventEvent;
        this.view = view;
    }

    @PostConstruct
    public void init() {
        view.init(this);
        view.setHeading("Choose the type:");
    }

    @Override
    public String getNextButtonText() {
        return null;
    }

    private final Callback callback = new Callback() {
        @Override
        public void onNextButtonClick() {
            
        }

        @Override
        public void onBackButtonClick() {
            wizard.clear();
        }
    };
    
    @Override
    public Callback getCallback() {
        return callback;
    }

    @Override
    protected void doShow() {
        super.doShow();

        view.clear();
        final Collection<ShapeSet> shapeSets = shapeManager.getShapeSets();
        if (shapeSets == null || shapeSets.isEmpty()) {
            view.showEmptyView();
        } else {
            for (final ShapeSet shapeSet : shapeSets) {
                final String uuid = shapeSet.getId();
                final String name = shapeSet.getName();
                final String description = shapeSet.getDescription();
                final String defSetId = shapeSet.getDefinitionSetId();
                final SafeUri thumbUri = shapeManager.getThumbnail( defSetId );
                
                view.addItem(name, description, thumbUri, true,
                        new Command() {
                            @Override
                            public void execute() {
                                NewDiagramWizardScreen.this.onShapeSetSelected(uuid);
                            }
                        });

            }
        }
    }

    public void clear() {
        view.clear();
    }

    private void onShapeSetSelected(final String shapeSetId) {
        createEmptyDiagramEventEvent.fire(new CreateEmptyDiagramEvent(shapeSetId));
        clear();
        doShow();
    }
    
    @Override
    public Widget asWidget() {
        return view.asWidget();
    }
    
}
