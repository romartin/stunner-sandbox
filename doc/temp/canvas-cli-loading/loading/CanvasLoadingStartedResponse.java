package org.wirez.core.client.canvas.loading;

import org.jboss.errai.common.client.api.annotations.NonPortable;

@NonPortable
public class CanvasLoadingStartedResponse {

    private final String requestUUID;

    public CanvasLoadingStartedResponse( final String requestUUID ) {
        this.requestUUID = requestUUID;
    }

    public String getRequestUUID() {
        return requestUUID;
    }

}
