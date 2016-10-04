package org.wirez.core.client.canvas.loading;

import org.jboss.errai.common.client.api.annotations.NonPortable;

@NonPortable
public class CanvasLoadingStartedRequest {

    private final String requestUUID;

    public CanvasLoadingStartedRequest( final String requestUUID ) {
        this.requestUUID = requestUUID;
    }

    public String getRequestUUID() {
        return requestUUID;
    }

}
