package org.wirez.client.widgets.card;

import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.uberfire.mvp.Command;

public class CardTriggerBuilder {
    
    public static IsWidget buildExchangeTrigger(final String title, final Command clickCallback) {
        final Icon icon = new Icon();
        icon.setType(IconType.EXCHANGE);
        icon.setSize(IconSize.LARGE);
        icon.setTitle( title );
        icon.addClickHandler(clickEvent -> {
            if ( null != clickCallback ) {
                clickCallback.execute();
            }
        });
        return icon;
    }

    
}
