package org.wirez.client.widgets.wizard;

public abstract class BaseWizardScreen implements CanvasWizardScreen {

    protected CanvasWizard wizard;

    @Override
    public CanvasWizardScreen show(final CanvasWizard wizard) {
        this.wizard = wizard;
        doShow();
        return this;
    }
    
    protected void doShow() {

    }

    @Override
    public String getNextButtonText() {
        return "Next";
    }

    @Override
    public String getBackButtonText() {
        return "Back";
    }

}
