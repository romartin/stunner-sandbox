package org.wirez.client.lienzo.components.palette.impl;

import org.wirez.client.lienzo.components.palette.AbstractLienzoGlyphItemsPalette;
import org.wirez.client.lienzo.components.palette.LienzoDefinitionsCategoryPalette;
import org.wirez.client.lienzo.components.palette.view.LienzoHoverPaletteView;
import org.wirez.client.lienzo.components.palette.view.element.*;
import org.wirez.core.client.ShapeManager;
import org.wirez.core.client.components.glyph.DefinitionGlyphTooltip;
import org.wirez.core.client.components.palette.ClientPaletteUtils;
import org.wirez.core.client.components.palette.model.GlyphPaletteItem;
import org.wirez.core.client.components.palette.model.HasPaletteItems;
import org.wirez.core.client.components.palette.model.definition.DefinitionPaletteCategory;
import org.wirez.core.client.components.palette.model.definition.DefinitionPaletteGroup;
import org.wirez.core.client.components.palette.model.definition.DefinitionPaletteItem;
import org.wirez.core.client.components.palette.view.PaletteGrid;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

@Dependent
public class LienzoDefinitionsCategoryPaletteImpl
    extends AbstractLienzoGlyphItemsPalette<LienzoHoverPaletteView>
    implements LienzoDefinitionsCategoryPalette {

    protected LienzoDefinitionsCategoryPaletteImpl() {
        this( null, null, null );
    }

    @Inject
    public LienzoDefinitionsCategoryPaletteImpl(final ShapeManager shapeManager,
                                                final DefinitionGlyphTooltip definitionGlyphTooltip,
                                                final LienzoHoverPaletteView view ) {

        super( shapeManager, definitionGlyphTooltip, view );

    }

    @PostConstruct
    public void init() {
        super.doInit();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doBind() {

        final DefinitionPaletteCategory category = (DefinitionPaletteCategory) paletteDefinition;

        final PaletteGrid grid = getGrid();

        // The category title and glyph.
        addGlyphItemIntoView( category, grid );

        // Separator.
        addSeparatorIntoView( grid );

        super.doBind();

    }


    @Override
    protected void addGlyphItemIntoView( final GlyphPaletteItem item,
                                    final PaletteGrid grid ) {


        if ( item instanceof DefinitionPaletteGroup ) {

            // The group of items for this category.
            addPaletteGroupIntoView( (DefinitionPaletteGroup) item, grid );

        } else {

            // The glyph item for this category.
            super.addGlyphItemIntoView( item, grid );

        }

        // Separator.
        addSeparatorIntoView( grid );

    }

    @SuppressWarnings("unchecked")
    protected void addPaletteGroupIntoView( final DefinitionPaletteGroup group,
                                            final PaletteGrid grid ) {

        // The group title.
        addTextIntoView( group.getTitle(), grid );

        final List<DefinitionPaletteItem> items = group.getItems();

        if ( null != items && !items.isEmpty() ) {

            for ( final DefinitionPaletteItem item : items ) {

                super.addGlyphItemIntoView( item, grid );

            }

        }

    }

    protected void addTextIntoView( final String text,
                                    final PaletteGrid grid ) {

        final LienzoTextPaletteElementView separatorPaletteTextView =
                new LienzoTextPaletteElementViewImpl( text, "Verdana", 10 );

        addElementIntoView( separatorPaletteTextView );
    }

    protected void addSeparatorIntoView( final PaletteGrid grid ) {

        final LienzoSeparatorPaletteElementView separatorPaletteElementView =
                new LienzoSeparatorPaletteElementViewImpl( grid.getIconSize(), grid.getIconSize() );

        addElementIntoView( separatorPaletteElementView );
    }

    protected void addElementIntoView( final LienzoPaletteElementView paletteElementView ) {

        itemViews.add( paletteElementView );
        view.add( paletteElementView );

    }

    @Override
    @SuppressWarnings("unchecked")
    public double[] computePaletteSize() {

        final DefinitionPaletteCategory category = (DefinitionPaletteCategory) paletteDefinition;

        // Category title & separator item views.
        int size = 2;

        final List<DefinitionPaletteItem> items = category.getItems();

        int textLength = 0;

        if ( null != items && !items.isEmpty() ) {

            for ( final DefinitionPaletteItem item : items ) {

                if ( item instanceof HasPaletteItems ) {

                    final List<DefinitionPaletteItem> subItems = ((HasPaletteItems) item).getItems();

                    final int subItemsSize = null != subItems ? subItems.size() : 0;

                    final String _l = ClientPaletteUtils.getLongestText( (HasPaletteItems) item );

                    if ( _l != null && _l.length() > textLength ) {

                        textLength = _l.length();

                    }

                    // Group title & separator view & group item views.
                    size+= 2 + subItemsSize;

                } else {

                    // Item title & separator view.
                    size+=2;

                }

            }

        }

        final PaletteGrid grid = getGrid();

        return ClientPaletteUtils.computeSizeForVerticalLayout( size, grid.getIconSize(), grid.getPadding(), textLength );

    }

}
