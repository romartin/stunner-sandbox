/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.client.widgets.explorer.command.item;

import org.kie.workbench.common.stunner.core.client.command.CanvasCommand;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.logging.Logger;

@Dependent
public class CanvasCommandItem extends AbstractCommandItem<CanvasCommand<?>, CanvasCommandItem.View> {

    private static Logger LOGGER = Logger.getLogger( CanvasCommandItem.class.getName() );

    public interface View extends AbstractCommandItem.View<CanvasCommandItem> {
    }

    protected CanvasCommandItem() {
        this( null );
    }

    @Inject
    public CanvasCommandItem( final CanvasCommandItem.View view ) {
        super( view );
    }


}
