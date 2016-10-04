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

import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.types.Shadow;
import com.ait.lienzo.shared.core.types.Color;
import com.ait.lienzo.shared.core.types.ColorName;
import org.wirez.bpmn.api.Task;
import org.wirez.bpmn.definition.property.Height;
import org.wirez.bpmn.definition.property.Width;
import org.wirez.core.api.graph.Edge;
import org.wirez.core.api.graph.Node;
import org.wirez.core.api.graph.content.view.View;
import org.wirez.core.api.util.ElementUtils;
import org.wirez.core.client.canvas.CanvasHandler;
import org.wirez.core.client.shape.mutation.HasSizeMutation;
import org.wirez.core.client.shape.mutation.MutationContext;

import java.util.ArrayList;
import java.util.Collection;

public class TaskShape extends BPMNBasicShape<Task> implements HasSizeMutation {

    private static final Shadow SHADOW = new Shadow(Color.fromColorString(Task.COLOR).setA(0.80), 10, -10, -10);

    private Group taskTypeIcon;
    private Rectangle decorator;
    
    public TaskShape(WiresManager manager) {
        super(new MultiPath().rect(0, 0, Task.WIDTH, Task.HEIGHT), manager);
        
        init();
    }

    private void init() {
        decorator = new Rectangle(Task.WIDTH, Task.HEIGHT).setX(0).setY(0).setFillAlpha(0).setStrokeAlpha(0);
        this.addChild(decorator, WiresLayoutContainer.Layout.CENTER, 
                getDecoratorCoordinate( Task.WIDTH ), 
                getDecoratorCoordinate( Task.HEIGHT ) );
        taskTypeIcon = taskTypeUser();
        this.addChild(taskTypeIcon, WiresLayoutContainer.Layout.TOP);
    }

    @Override
    public Collection<Shape> getDecorators() {
        return new ArrayList<Shape>() {{
            add( decorator );
        }};
    }

    @Override
    public void applyElementProperties(Node<View<Task>, Edge> element, CanvasHandler wirezCanvas, MutationContext mutationContext) {
        super.applyElementProperties(element, wirezCanvas, mutationContext);
        
        // Size.
        _applySize(element, mutationContext);
        
    }

    protected TaskShape _applySize(final Node<View<Task>, Edge> element, MutationContext mutationContext) {
        final Width widthProperty  = (Width) ElementUtils.getProperty(element, Width.ID);
        final Height heightProperty  = (Height) ElementUtils.getProperty(element, Height.ID);
        final Double width = widthProperty.getValue();
        final Double height = heightProperty.getValue();
        applySize(width, height, mutationContext);
        ElementUtils.updateBounds(width, height, element.getContent());
        return this;
    }

    @Override
    public String toString() {
        return "TaskShape{}";
    }

    @Override
    public void applySize(final double width, final double height, final MutationContext mutationContext) {
        final double x = getPath().getX();
        final double y = getPath().getY();
        getPath().clear().rect(x, y, width, height);
        decorator.setWidth(width);
        decorator.setHeight(height);
        this.moveChild(decorator, getDecoratorCoordinate(width), getDecoratorCoordinate(height));
    }

    private Group taskTypeUser() {

        final String p1 = "M0.585,24.167h24.083v-7.833c0,0-2.333-3.917-7.083-5.167h-9.25\n" +
                "\t\t\tc-4.417,1.333-7.833,5.75-7.833,5.75L0.585,24.167z";

        final String p2 = "M 6 20 L 6 24";

        final String p3 = "M 20 20 L 20 24";

        // TODO: Circle

        final String p4 = "M8.043,7.083c0,0,2.814-2.426,5.376-1.807s4.624-0.693,4.624-0.693\n" +
                "\t\t\tc0.25,1.688,0.042,3.75-1.458,5.584c0,0,1.083,0.75,1.083,1.5s0.125,1.875-1,3s-5.5,1.25-6.75,0S8.668,12.834,8.668,12\n" +
                "\t\t\ts0.583-1.25,1.25-1.917C8.835,9.5,7.419,7.708,8.043,7.083z";

        final Group userTypeGroup = new Group();

        final SVGPath svgP1 = createSVGPath(p1);
        userTypeGroup.add(svgP1);
        final SVGPath svgP2 = createSVGPath(p2);
        userTypeGroup.add(svgP2);
        final SVGPath svgP3 = createSVGPath(p3);
        userTypeGroup.add(svgP3);
        final SVGPath svgP4 = createSVGPath(p4);
        userTypeGroup.add(svgP4);

        return userTypeGroup;
    }
    
    private SVGPath createSVGPath(String path) {
        return new SVGPath(path)
                .setStrokeColor(ColorName.BLACK)
                .setDraggable(false);
    }
    
    private double getDecoratorCoordinate(final double c) {
        return - ( c / 2 );
    }
}
