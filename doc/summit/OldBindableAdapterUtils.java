package org.wirez.core.definition.adapter.binding;

import org.jboss.errai.databinding.client.BindableProxy;
import org.jboss.errai.databinding.client.HasProperties;
import org.jboss.errai.databinding.client.NonExistingPropertyException;
import org.jboss.errai.databinding.client.PropertyType;
import org.jboss.errai.databinding.client.api.DataBinder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BindableAdapterUtils {

    private static Logger LOGGER = Logger.getLogger(BindableAdapterUtils.class.getName());

    public static final String SHAPE_SET_SUFFIX = "ShapeSet";

    public static String getDefinitionId( final Class<?> pojoClass ) {
        return getGenericClassName( pojoClass );
    }

    public static String getDefinitionSetId( final Class<?> pojoClass ) {
        return getGenericClassName(pojoClass);
    }

    public static String getPropertySetId( final Class<?> pojoClass ) {
        return getGenericClassName(pojoClass);
    }

    public static String getPropertyId( final Class<?> pojoClass ) {
        return getGenericClassName(pojoClass);
    }

    public static String getShapeSetId( final Class<?> defSetClass ) {
        final String id = getGenericClassId( defSetClass );
        return id + SHAPE_SET_SUFFIX;

    }

    public static String getGenericClassName(final Class<?> pojoClass ) {
        final Class<?> clazz = handleBindableProxyClass( pojoClass );
        return clazz.getName();
    }

    private static String getGenericClassId(final Class<?> pojoClass ) {
        final Class<?> clazz = handleBindableProxyClass( pojoClass );
        return clazz.getSimpleName();
    }

    // When porting bindable models from backend, they objects are proxied by errai databinder classes.
    // This does not happens if the model is created on client side.
    // TODO: Deep into this... check pere's forms callback, as it's where this happens.
    public static Class<?> handleBindableProxyClass(final Class<?> pojoClass) {

        if ( pojoClass.getName().startsWith("org.jboss.errai.databinding") ) {
            return pojoClass.getSuperclass();
        }

        return pojoClass;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance( final Class<?> pojoType ) {

        if ( null != pojoType ) {

            return (T) DataBinder.forType( pojoType ).getModel();

        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T clone( final T pojo ) {

        if ( null != pojo ) {

            final BindableProxy proxy = (BindableProxy) DataBinder.forModel (pojo ).getModel();

            return (T) proxy.deepUnwrap();

        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R merge(final T source,
                                 final R target) {

        if ( null != source ) {

            final HasProperties hasProperties = (HasProperties) DataBinder.forModel( source ).getModel();

            if ( null != hasProperties ) {

                final Map<String, PropertyType> propertyTypeMap = hasProperties.getBeanProperties();

                if ( null != propertyTypeMap && !propertyTypeMap.isEmpty() ) {

                    final HasProperties targetProperties = (HasProperties) DataBinder.forModel( target ).getModel();

                    for ( final Map.Entry<String, PropertyType> entry : propertyTypeMap.entrySet() ) {

                        final String pId = entry.getKey();

                        try {

                            targetProperties.set( pId, hasProperties.get( pId ) );

                        } catch ( NonExistingPropertyException exception ) {

                            // Just skip it, Go to next property.

                            LOGGER.log( Level.INFO, "BindableAdapterUtils#merge - Skipping merge property [" + pId + "]" );

                        }

                    }

                    return (R) target;

                }

            }

        }

        return null;
    }


    public static  <T> Collection<Class<?>> toClassCollection( final Iterable<T> source ) {

        if ( null != source && source.iterator().hasNext() ) {

            final LinkedList<Class<?>> result = new LinkedList<>();

            for ( final Object sourceObject : source ) {

                result.add( sourceObject.getClass() );

            }

            return result;

        }

        return null;

    }

}
