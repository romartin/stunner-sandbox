/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.shared.core.types.ColorName;
import org.wirez.bpmn.api.ParallelGateway;
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

public class ParallelGatewayShape extends BPMNBasicShape<ParallelGateway> implements HasRadiusMutation {

    protected RegularPolygon decorator;
    protected Group gwTypeIcon;

    public ParallelGatewayShape(WiresManager manager) {
        super(new MultiPath()
                .M(0 ,ParallelGateway.RADIUS)
                .L(ParallelGateway.RADIUS, 0)
                .L(ParallelGateway.RADIUS * 2, ParallelGateway.RADIUS)
                .L(ParallelGateway.RADIUS, ( ParallelGateway.RADIUS * 2) )
                .Z()
                .setStrokeColor(ColorName.BLACK)
                .setFillColor(ParallelGateway.COLOR),
                manager);
        ;
        init();
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
        return getPath();
    }

    protected void init() {
        final double radius = ParallelGateway.RADIUS;
        
        decorator = new RegularPolygon(4, radius)
                .setStrokeWidth(0)
                .setStrokeAlpha(0)
                .setFillAlpha(0)
                .setStrokeAlpha(0);

        gwTypeIcon = new Group();
        final double[] gwTypeIconSize = updateGwTypeIcon(radius);
        
        this.addChild(decorator, WiresLayoutContainer.Layout.CENTER);
        this.addChild(gwTypeIcon, WiresLayoutContainer.Layout.CENTER, 
                - ( gwTypeIconSize[0] / 2 ), - ( gwTypeIconSize[1] / 2 ) );
    }
    
    private double[] updateGwTypeIcon(final double radius) {
        gwTypeIcon.removeAll();

        final double lineSize = radius / 2;
        final double lineAlpha = 0.8;
        Line hLine = new Line( 0 , 0, lineSize, 0).setY(lineSize / 2);
        hLine.setStrokeWidth(2);
        hLine.setStrokeAlpha(lineAlpha);
        Line vLine = new Line(0, 0, 0, lineSize).setX(lineSize / 2);
        vLine.setStrokeWidth(2);
        vLine.setStrokeAlpha(lineAlpha);
        gwTypeIcon.add(hLine);
        gwTypeIcon.add(vLine);
        final BoundingBox bb = gwTypeIcon.getBoundingBox();
        return new double[] {bb.getWidth(), bb.getHeight()};
    }

    public void applyElementProperties(Node<View<ParallelGateway>, Edge> element, CanvasHandler wirezCanvas, MutationContext mutationContext) {
        super.applyElementProperties(element, wirezCanvas, mutationContext);

        // Radius.
        _applyRadius(element, mutationContext);
        
    }

    protected ParallelGatewayShape _applyRadius(final Node<View<ParallelGateway>, Edge> element, final MutationContext mutationContext) {
        final Radius radiusProperty  = (Radius) ElementUtils.getProperty(element, Radius.ID);
        final Double radius = radiusProperty.getValue();
        if ( null != radius ) {
            applyRadius(radius, mutationContext);
            ElementUtils.updateBounds(radius, element.getContent());
        }
        return this;
    }

    @Override
    public void applyRadius(final double radius, final MutationContext mutationContext) {
        if (radius > 0) {
            // _applyParallelGatewayRadius(polygon, radius);
            _applyParallelGatewayRadius(decorator, radius);
            updateGwTypeIcon(radius);
        }
    }
    

    protected ParallelGatewayShape _applyParallelGatewayRadius(final RegularPolygon polygon, final double radius) {
        polygon.setRadius(radius);
        return this;
    }


    @Override
    public String toString() {
        return "ParallelGatewayShape{}";
    }
    
}
