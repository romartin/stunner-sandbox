/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wirez.client.widgets.wizard;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.uberfire.client.mvp.UberView;
import org.wirez.client.widgets.wizard.screen.HomeWizardScreen;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class CanvasWizard implements IsWidget {

    public interface View extends UberView<CanvasWizard> {

        View setScreenWidget(IsWidget widget);
        
        View setBackButtonText(String caption);

        View setBackButtonVisible(boolean isEnabled);

        View setNextButtonText(String caption);
        
        View setNextButtonVisible(boolean isEnabled);

        View setNextButtonEnabled(boolean isEnabled);
        
        View clear();
        
    }

    HomeWizardScreen homeWizardScreen;
    View view;
    
    private CanvasWizardScreen.Callback screenCallback;

    @Inject
    public CanvasWizard(final HomeWizardScreen homeWizardScreen, 
                        final View view) {
        this.homeWizardScreen = homeWizardScreen;
        this.view = view;
    }
    
    @PostConstruct
    public void init() {
        view.init(this);
        navigate(homeWizardScreen);
    }
    
    private void updateScreenViews(final CanvasWizardScreen screen) {
        view.clear();
        screenCallback = screen.getCallback();
        view.setNextButtonText(screen.getNextButtonText() != null ? screen.getNextButtonText() : "");
        view.setNextButtonVisible(screen.getNextButtonText() != null);
        view.setBackButtonText(screen.getBackButtonText() != null ? screen.getBackButtonText() : "");
        view.setBackButtonVisible(screen.getBackButtonText() != null);
        view.setScreenWidget(screen);
        screen.show(this);
    }
    
    public void setNextButtonEnabled(boolean isEnabled) {
        view.setNextButtonEnabled( isEnabled );
    }

    @Override
    public Widget asWidget() {
        return view.asWidget();
    }
    
    public void navigate(final CanvasWizardScreen screen) {
        updateScreenViews(screen);
    }
    
    public void clear() {
        navigate(homeWizardScreen);
    }


    void onNextButtonClick() {
        assert screenCallback != null;
        screenCallback.onNextButtonClick();
    }

    void onBackButtonClick() {
        assert screenCallback != null;
        screenCallback.onBackButtonClick();
    }
    
}
