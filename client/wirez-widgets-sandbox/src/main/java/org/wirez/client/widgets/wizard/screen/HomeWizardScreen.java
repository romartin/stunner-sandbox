package org.wirez.client.widgets.wizard.screen;

import com.google.gwt.user.client.ui.Widget;
import org.uberfire.client.mvp.UberView;
import org.wirez.client.widgets.wizard.BaseWizardScreen;
import org.wirez.client.widgets.wizard.CanvasWizardScreen;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class HomeWizardScreen extends BaseWizardScreen implements CanvasWizardScreen {

    public interface View extends UberView<HomeWizardScreen> {
        View clear();
    }
    
    LoadDiagramWizardScreen loadDiagramWizardScreen;
    NewDiagramWizardScreen newDiagramWizardScreen;
    View view;
    
    @Inject
    public HomeWizardScreen(final LoadDiagramWizardScreen loadDiagramWizardScreen, 
                            final NewDiagramWizardScreen newDiagramWizardScreen, 
                            final View view) {
        this.loadDiagramWizardScreen = loadDiagramWizardScreen;
        this.newDiagramWizardScreen = newDiagramWizardScreen;
        this.view = view;
    }

    @PostConstruct
    public void init() {
        view.init(this);
    }

    @Override
    public String getNextButtonText() {
        return null;
    }

    @Override
    public String getBackButtonText() {
        return null;
    }

    @Override
    public Callback getCallback() {
        return null;
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }

    void onLoadButtonClick() {
        wizard.navigate(loadDiagramWizardScreen);
    }

    void onNewButtonClick() {
        wizard.navigate(newDiagramWizardScreen);
    }
    
}
