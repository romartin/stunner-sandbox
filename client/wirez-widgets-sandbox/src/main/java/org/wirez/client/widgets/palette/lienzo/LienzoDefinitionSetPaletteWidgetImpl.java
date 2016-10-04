package org.wirez.client.widgets.palette.lienzo;

import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.wirez.client.lienzo.components.palette.LienzoDefinitionSetFlatPalette;
import org.wirez.client.lienzo.components.palette.LienzoGlyphItemsPalette;
import org.wirez.client.lienzo.components.palette.LienzoGlyphsHoverPalette;
import org.wirez.client.lienzo.components.palette.LienzoPalette;
import org.wirez.client.widgets.palette.AbstractPaletteWidget;
import org.wirez.client.widgets.palette.lienzo.view.LienzoPaletteWidgetFloatingView;
import org.wirez.client.widgets.palette.lienzo.view.LienzoPaletteWidgetViewImpl;
import org.wirez.core.client.ShapeManager;
import org.wirez.core.client.components.glyph.GlyphTooltip;
import org.wirez.core.client.components.palette.ClientPaletteUtils;
import org.wirez.core.client.components.palette.model.GlyphPaletteItem;
import org.wirez.core.client.components.palette.model.HasPaletteItems;
import org.wirez.core.client.components.palette.model.definition.DefinitionSetPalette;
import org.wirez.core.client.service.ClientFactoryServices;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

// TODO: Refactor creating the PaletteFactory for this palette type.

@Dependent
public class LienzoDefinitionSetPaletteWidgetImpl
        extends AbstractPaletteWidget<DefinitionSetPalette, LienzoPaletteWidgetViewImpl>
        implements LienzoDefinitionSetPaletteWidget {

    private static final int ICON_SIZE = 30;
    private static final int PADDING = 10;

    SyncBeanManager beanManager;

    LienzoPaletteWidgetFloatingView floatingView;

    private LienzoDefinitionSetFlatPalette mainPalette;
    private LienzoGlyphsHoverPalette glyphsFloatingPalette;

    protected LienzoDefinitionSetPaletteWidgetImpl() {
        this( null, null, null, null, null );
    }

    @Inject
    public LienzoDefinitionSetPaletteWidgetImpl( final ShapeManager shapeManager,
                                                 final ClientFactoryServices clientFactoryServices,
                                                 final LienzoPaletteWidgetViewImpl view,
                                                 final SyncBeanManager beanManager,
                                                 final LienzoPaletteWidgetFloatingView floatingView) {
        super(shapeManager, clientFactoryServices, view);
        this.beanManager = beanManager;
        this.floatingView = floatingView;
    }

    @PostConstruct
    public void init() {

        this.mainPalette = beanManager.lookupBean( LienzoDefinitionSetFlatPalette.class ).newInstance();
        this.glyphsFloatingPalette = beanManager.lookupBean( LienzoGlyphsHoverPalette.class ).newInstance();

        view.setPresenter( this );
        view.showEmptyView( true );
        floatingView.setPresenter( this );

        mainPalette.setExpandable( false );
        mainPalette.setLayout( LienzoPalette.Layout.VERTICAL );
        updateMainPaletteIconsSize();
        mainPalette.onShowGlyTooltip( mainPaletteGlyphTooltipCallback );

        glyphsFloatingPalette.setExpandable( false );
        glyphsFloatingPalette.setIconSize( ICON_SIZE );
        glyphsFloatingPalette.setPadding( PADDING );
        glyphsFloatingPalette.setLayout( LienzoPalette.Layout.HORIZONTAL );
        glyphsFloatingPalette.onClose( floatingPaletteCloseCallback );
        glyphsFloatingPalette.onShowGlyTooltip( floatingPaletteGlyphTooltipCallback );

    }


    @Override
    public void unbind() {

        super.unbind();

        floatingView.clear();

    }

    @Override
    public double getIconSize() {
        return ICON_SIZE;
    }

    public double getPadding() {
        return PADDING ;
    }

    private final LienzoGlyphItemsPalette.GlyphTooltipCallback floatingPaletteGlyphTooltipCallback =
            (glyphTooltip, item, mouseX, mouseY, itemX, itemY) -> {

                final int[] mainPaletteSize = getMainPaletteSize();

                final double pX = mainPaletteSize[0] + itemX;
                final double pY = itemY + floatingView.getAbsoluteTop() + ( getIconSize() / 2 ) - getPadding();

                glyphTooltip.showTooltip( item.getDefinitionId(), pX, pY, GlyphTooltip.Direction.WEST );

                return false;

            };

    private final LienzoGlyphItemsPalette.GlyphTooltipCallback mainPaletteGlyphTooltipCallback =
            (glyphTooltip, item, mouseX, mouseY, itemX, itemY) -> {

                final int[] mainPaletteSize = getMainPaletteSize();

                final double pX = mainPaletteSize[0] + itemX - ( getIconSize() / 2 );
                final double pY = itemY + getViewAbsoluteTop() + ( getIconSize() / 2 );

                glyphTooltip.showTooltip( item.getDefinitionId(), pX, pY, GlyphTooltip.Direction.WEST );

                floatingView.clear();

                return false;

            };

    private final CloseCallback floatingPaletteCloseCallback = new CloseCallback() {

        @Override
        public boolean onClose() {

            floatingView.hidePopup();
            return true;

        }

    };

    @Override
    protected void beforeBind() {
        super.beforeBind();

        floatingView.clear();

    }

    @Override
    @SuppressWarnings("unchecked")
    protected LienzoDefinitionSetPaletteWidgetImpl bind() {

        mainPalette.bind( paletteDefinition );

        final int[] mainPaletteSize = getMainPaletteSize();

        view.show( mainPalette.getView(), mainPaletteSize[0], mainPaletteSize[1] );

        mainPalette.onItemMouseDown(( id, mouseX, mouseY, itemX, itemY) -> {

            final GlyphPaletteItem item = getPaletteItem( id, getMainPaletteItems() );

            view.showDragProxy( item.getDefinitionId(), mouseX, mouseY );

            return !hasPaletteItems( item );

        });

        mainPalette.onItemHover( this::showFloatingPalette );

        return this;

    }

    @SuppressWarnings("unchecked")
    private boolean showFloatingPalette( final String id,
                                         final double x,
                                         final double y,
                                         final double itemX,
                                         final double itemY ) {

        final GlyphPaletteItem item = getPaletteItem( id, getMainPaletteItems() );

        if ( hasPaletteItems( item ) ) {

            final HasPaletteItems<GlyphPaletteItem> hasPaletteItems =
                    (HasPaletteItems<GlyphPaletteItem>) item;

            glyphsFloatingPalette.bind( hasPaletteItems );

            final int[] mainPaletteSize = getMainPaletteSize();

            final double pX = mainPaletteSize[0] - ( getPadding() * 1.5 ) - getIconSize();
            final double pY = itemY + getViewAbsoluteTop() - ( getPadding() * 2 ) + ( getIconSize() / 2 );

            floatingView
                    .setX( pX )
                    .setY( pY );

            // TODO: Let the floating panel use same size as the palette view's one.
            floatingView.show( glyphsFloatingPalette.getView() );

            glyphsFloatingPalette.onItemMouseDown( (id1, mouseX, mouseY, itemX1, itemY1) -> {

                final GlyphPaletteItem item1 = getPaletteItem( id1, hasPaletteItems.getItems() );

                if ( !hasPaletteItems( item1 ) ) {

                    view.showDragProxy( item1.getDefinitionId(), mouseX, mouseY );

                }

                floatingView.hidePopup();

                return true;

            } );

            floatingView.showPopup();

            return false;

        }

        return true;

    }

    @Override
    protected void  doDestroy() {

        super.doDestroy();

        floatingView.destroy();

    }

    @Override
    protected String getPaletteItemId( final int index ) {
        final GlyphPaletteItem item = getMainPaletteItem( index );
        return null != item ? item.getId() : null;
    }


    private int[] getMainPaletteSize() {

        double width = 0;
        double height = 0;

        if ( null != paletteDefinition ) {

            final List<? extends GlyphPaletteItem> items = getMainPaletteItems();
            final int itemsSize = null != items ? items.size() : 0;

            final double[] mainPaletteSize =
                    ClientPaletteUtils.computeSizeForVerticalLayout( itemsSize, _getIconSize(), _getPadding(), 0 );

            width = mainPaletteSize[0];
            height = mainPaletteSize[1];
        }

        return new int[] { (int) width, (int) height };
    }

    private boolean hasPaletteItems(final GlyphPaletteItem item ) {
        return item instanceof HasPaletteItems;
    }

    private void updateMainPaletteIconsSize() {

        mainPalette.setIconSize( _getIconSize() );
        mainPalette.setPadding( _getPadding() );

    }

    private List<GlyphPaletteItem> getMainPaletteItems() {
        return mainPalette.getItems();
    }

    private GlyphPaletteItem getMainPaletteItem( final int index ) {
        return mainPalette.getItem( index );
    }

    private GlyphPaletteItem getPaletteItem( final String id, final List<GlyphPaletteItem> items ) {

        if ( null != items && !items.isEmpty() ) {

            for ( final GlyphPaletteItem item : items ) {

                if ( item.getId().equals( id ) ) {

                    return item;

                }

            }

        }

        return null;
    }

    private double getViewAbsoluteTop() {
        return view.getAbsoluteTop();
    }

    private int _getIconSize() {

        return (int) getIconSize();

    }

    private int _getPadding() {

        return (int) getPadding();

    }

}
