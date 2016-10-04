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

package org.wirez.bpmn.client;

import com.ait.lienzo.client.core.shape.AbstractDirectionalMultiPointShape;
import com.ait.lienzo.client.core.shape.Decorator;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import org.wirez.bpmn.definition.property.general.BgColor;
import org.wirez.bpmn.definition.property.general.BorderColor;
import org.wirez.bpmn.definition.property.general.BorderSize;
import org.wirez.core.api.definition.Definition;
import org.wirez.core.api.graph.Edge;
import org.wirez.core.api.graph.Node;
import org.wirez.core.api.graph.content.view.View;
import org.wirez.core.api.util.ElementUtils;
import org.wirez.core.client.canvas.CanvasHandler;
import org.wirez.core.client.shape.impl.BaseConnector;
import org.wirez.core.client.shape.mutation.MutationContext;

public abstract class BPMNBasicConnector<W extends Definition> 
        extends BaseConnector<W> {


    public BPMNBasicConnector(final AbstractDirectionalMultiPointShape<?> line, 
                              final Decorator<?> head, 
                              final Decorator<?> tail, 
                              final WiresManager manager) {
        super(line, head, tail, manager);
    }

    @Override
    public void applyElementProperties(Edge<View<W>, Node> element, CanvasHandler canvasHandler, MutationContext mutationContext) {
        super.applyElementProperties(element, canvasHandler, mutationContext);

        // Fill color.
        _applyFillColor(element);

        // Apply border styles.
        _applyBorders(element);

    }

    protected BPMNBasicConnector<W> _applyFillColor(Edge<View<W>, Node> element) {
        final BgColor bgColor = (BgColor) ElementUtils.getProperty(element, BgColor.ID);
        final String color = bgColor.getValue();
        if (color != null && color.trim().length() > 0) {
            getShape().setFillColor(color);
        }
        return this;
    }

    protected BPMNBasicConnector<W> _applyBorders(Edge<View<W>, Node> element) {
        final BorderColor borderColor  = (BorderColor) ElementUtils.getProperty(element, BorderColor.ID);
        final BorderSize borderSize = (BorderSize) ElementUtils.getProperty(element, BorderSize.ID);
        final String color = borderColor.getValue();
        final Double width = borderSize.getValue();
        if (color != null && color.trim().length() > 0) {
            getShape().setStrokeColor(color);
        }
        if (width != null) {
            getShape().setStrokeWidth(width);
        }
        return this;
    }

}
