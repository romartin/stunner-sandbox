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

import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.shape.wires.WiresLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.LinearGradient;
import org.wirez.bpmn.definition.property.general.*;
import org.wirez.core.api.definition.Definition;
import org.wirez.core.api.graph.Edge;
import org.wirez.core.api.graph.Node;
import org.wirez.core.api.graph.content.view.View;
import org.wirez.core.api.util.ElementUtils;
import org.wirez.core.client.HasDecorators;
import org.wirez.core.client.canvas.CanvasHandler;
import org.wirez.core.client.shape.impl.BaseShape;
import org.wirez.core.client.shape.mutation.MutationContext;
import org.wirez.core.client.util.ShapeUtils;

public abstract class BPMNBasicShape<W extends Definition> 
        extends BaseShape<W>
        implements HasDecorators {

    public BPMNBasicShape(final MultiPath path, 
                          final WiresManager manager) {
        super(path, new WiresLayoutContainer(), manager);
    }

    @Override
    public void applyElementProperties(Node<View<W>, Edge> element, CanvasHandler wirezCanvas, MutationContext mutationContext) {
        super.applyElementProperties(element, wirezCanvas, mutationContext);

        // Fill color.
        _applyFillColor(element);

        // Apply border styles.
        _applyBorders(element);

        // Apply font styles.
        _applyFont(element);
        
    }

    protected BPMNBasicShape<W> _applyFillColor(Node<View<W>, Edge> element) {
        final BgColor bgColor = (BgColor) ElementUtils.getProperty(element, BgColor.ID);
        final String color = bgColor.getValue();
        if (color != null && color.trim().length() > 0) {
            final BoundingBox bb = getShape().getBoundingBox();
            final double w = bb.getWidth();
            final double h = bb.getHeight();
            final LinearGradient gradient = ShapeUtils.getLinearGradient(color, "#FFFFFF", w, h);
            getShape().setFillGradient(gradient);
        }
        return this;
    }

    protected BPMNBasicShape<W> _applyBorders(Node<View<W>, Edge> element) {
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

    protected BPMNBasicShape<W> _applyFont(Node<View<W>, Edge> element) {
        final Text text = super.getText();
        if ( null != text ) {
            final FontFamily fontFamily = (FontFamily) ElementUtils.getProperty(element, FontFamily.ID);
            final FontColor fontColor  = (FontColor) ElementUtils.getProperty(element, FontColor.ID);
            final FontSize fontSize = (FontSize) ElementUtils.getProperty(element, FontSize.ID);
            final FontBorderSize fontBorderSize = (FontBorderSize) ElementUtils.getProperty(element, FontBorderSize.ID);
            final String family = fontFamily.getValue();
            final String color = fontColor.getValue();
            final Double size = fontSize.getValue();
            final Double borderSize = fontBorderSize.getValue();
            if (family != null && family.trim().length() > 0) {
                text.setFontFamily(family);
            }
            if (color != null && color.trim().length() > 0) {
                text.setStrokeColor(color);
            }
            if (size != null && size > 0) {
                text.setFontSize(size);
            }
            if (borderSize != null && borderSize > 0) {
                text.setStrokeWidth(borderSize);
            }

            // Center the text on the parent using the bb calculation.
            final BoundingBox bb = text.getBoundingBox();
            final double bbw = bb.getWidth();
            final double bbh = bb.getHeight();
            this.moveChild(text, - ( bbw / 2 ), - ( bbh / 2 ) );
            text.moveToTop();
        }

        return this;
    }
    
}
