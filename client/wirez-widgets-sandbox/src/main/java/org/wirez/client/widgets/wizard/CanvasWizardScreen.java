package org.wirez.client.widgets.wizard;

import com.google.gwt.user.client.ui.IsWidget;

public interface CanvasWizardScreen extends IsWidget {
    
    interface Callback {
        
        void onNextButtonClick();

        void onBackButtonClick();
        
    }
    
    String getNextButtonText();

    String getBackButtonText();

    Callback getCallback();

    CanvasWizardScreen show(CanvasWizard wizard);
    
}