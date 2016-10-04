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

import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Ring;
import com.ait.lienzo.client.core.shape.Shape;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import org.wirez.bpmn.api.EndTerminateEvent;
import org.wirez.bpmn.definition.property.Radius;
import org.wirez.core.api.graph.Edge;
import org.wirez.core.api.graph.Node;
import org.wirez.core.api.graph.content.view.View;
import org.wirez.core.api.util.ElementUtils;
import org.wirez.core.client.canvas.CanvasHandler;
import org.wirez.core.client.shape.mutation.HasRadiusMutation;
import org.wirez.core.client.shape.mutation.MutationContext;

import java.util.ArrayList;
import java.util.Collection;

public class EndTerminateEventShape extends BPMNBasicShape<EndTerminateEvent> implements HasRadiusMutation {

    protected Circle circle;
    protected Ring ring;
    protected Circle decorator;
    
    public EndTerminateEventShape(WiresManager manager) {
        super(new MultiPath().rect(0,0, EndTerminateEvent.RADIUS * 2, EndTerminateEvent.RADIUS * 2)
                .setFillAlpha(0.001)
                .setStrokeAlpha(0), 
                manager);
        init();
    }

    protected void init() {
        final double radius = EndTerminateEvent.RADIUS;
        circle = new Circle(radius).setX(radius).setY(radius);
        this.addChild(circle, WiresLayoutContainer.Layout.CENTER);
        final Double[] rr = getRingRadius(radius);
        ring = new Ring(rr[0], rr[1]).setX(radius).setY(radius).setFillColor(EndTerminateEvent.RING_COLOR);
        this.addChild(ring, WiresLayoutContainer.Layout.CENTER);
        decorator = new Circle(radius).setX(radius).setY(radius).setFillAlpha(0).setStrokeAlpha(0);
        this.addChild(decorator, WiresLayoutContainer.Layout.CENTER);
    }

    @Override
    protected WiresLayoutContainer.Layout getTextPosition() {
        return WiresLayoutContainer.Layout.BOTTOM;
    }

    @Override
    public Collection<Shape> getDecorators() {
        return new ArrayList<Shape>() {{
            add( decorator );
        }};
    }

    @Override
    public Shape getShape() {
        return circle;
    }

    @Override
    public void applyElementProperties(Node<View<EndTerminateEvent>, Edge> element, CanvasHandler wirezCanvas, MutationContext mutationContext) {
        super.applyElementProperties(element, wirezCanvas, mutationContext);

        // Radius.
        _applyRadius(element, mutationContext);
    }

    @Override
    public void applyRadius(double radius, MutationContext mutationContext) {
        if (radius > 0) {
            circle.setRadius(radius);
            final Double[] rr = getRingRadius(radius);
            ring.setInnerRadius(rr[0]);
            ring.setOuterRadius(rr[1]);
            decorator.setRadius(radius);
        }
    }

    protected EndTerminateEventShape _applyRadius(final Node<View<EndTerminateEvent>, Edge> element, MutationContext mutationContext) {
        final Radius radiusProperty  = (Radius) ElementUtils.getProperty(element, Radius.ID);
        final Double radius = radiusProperty.getValue();
        if ( null != radius ) {
            applyRadius(radius, mutationContext);
            ElementUtils.updateBounds(radius, element.getContent());
        }
        return this;
    }
    
    public static Double[] getRingRadius(final double radius) {
        final double r = radius / 8;
        final double inner = r * 6;
        final double outer = inner + r;
        
        return new Double[] { inner, outer};
    }

    @Override
    public String toString() {
        return "EndTerminateEventShape{}";
    }

    
}
