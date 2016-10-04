package org.wirez.client.widgets.palette.lienzo;

import com.ait.lienzo.client.core.shape.Group;
import org.wirez.client.lienzo.components.palette.LienzoGlyphsPalette;
import org.wirez.client.lienzo.components.palette.LienzoPalette;
import org.wirez.client.widgets.palette.AbstractPaletteWidget;
import org.wirez.client.widgets.palette.lienzo.view.LienzoPaletteWidgetViewImpl;
import org.wirez.core.client.ShapeManager;
import org.wirez.core.client.components.glyph.ShapeGlyphDragHandler;
import org.wirez.core.client.components.palette.model.definition.DefinitionPaletteItem;
import org.wirez.core.client.components.palette.model.definition.DefinitionsPalette;
import org.wirez.core.client.service.ClientFactoryServices;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Palette Widget that binds with a DefinitionsPalette definition.
 *
 * TODO: Refactor creating the PaletteFactory for this palette type.
 */
@Dependent
public class LienzoDefinitionsPaletteWidgetImpl
        extends AbstractPaletteWidget<DefinitionsPalette, LienzoPaletteWidgetViewImpl>
        implements LienzoDefinitionsPaletteWidget {

    private static final int ICON_SIZE = 30;
    private static final int PADDING = 10;

    LienzoGlyphsPalette lienzoGlyphsPalette;
    ShapeGlyphDragHandler<Group> shapeGlyphDragHandler;

    protected LienzoDefinitionsPaletteWidgetImpl() {
        this( null, null, null, null, null );
    }

    @Inject
    public LienzoDefinitionsPaletteWidgetImpl( final ShapeManager shapeManager,
                                               final ClientFactoryServices clientFactoryServices,
                                               final LienzoGlyphsPalette lienzoGlyphsPalette,
                                               final ShapeGlyphDragHandler<Group> shapeGlyphDragHandler,
                                               final LienzoPaletteWidgetViewImpl view ) {
        super( shapeManager, clientFactoryServices, view );
        this.lienzoGlyphsPalette = lienzoGlyphsPalette;
        this.shapeGlyphDragHandler = shapeGlyphDragHandler;
        this.view = view;
    }

    @PostConstruct
    public void init() {

        view.setPresenter( this );
        view.showEmptyView( true );

        lienzoGlyphsPalette.setExpandable( false );
        lienzoGlyphsPalette.setIconSize( ICON_SIZE );
        lienzoGlyphsPalette.setPadding( PADDING );
        lienzoGlyphsPalette.setLayout( LienzoPalette.Layout.VERTICAL );

    }

    @Override
    protected LienzoDefinitionsPaletteWidgetImpl bind() {

        lienzoGlyphsPalette.bind( paletteDefinition );

        // TODO: View's panel size.
        view.show( lienzoGlyphsPalette.getView(), maxWidth, 900 );

        lienzoGlyphsPalette.onItemMouseDown(( id, mouseX, mouseY, itemX, itemY) -> {

            view.showDragProxy( id, mouseX, mouseY );

            return true;

        });

        return this;

    }

    @Override
    protected String getPaletteItemId( int index ) {
        final DefinitionPaletteItem item = getItem( index, paletteDefinition );
        return null != item ? item.getId() : null;
    }

    @Override
    public double getIconSize() {
        return ICON_SIZE;
    }

    protected DefinitionPaletteItem getItem(final int index, final DefinitionsPalette paletteDefinition ) {
        return paletteDefinition.getItems().get( index );
    }

}
