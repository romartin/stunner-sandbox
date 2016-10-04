package org.wirez.tools.oryx.stencilset;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.wirez.tools.oryx.stencilset.model.Property;
import org.wirez.tools.oryx.stencilset.model.PropertyPackage;
import org.wirez.tools.oryx.stencilset.model.Stencil;
import org.wirez.tools.oryx.stencilset.model.StencilSet;
import org.wirez.tools.oryx.stencilset.template.*;
import org.wirez.tools.oryx.stencilset.template.builder.DefinitionModelBuilder;
import org.wirez.tools.oryx.stencilset.template.builder.DefinitionSetModelBuilder;
import org.wirez.tools.oryx.stencilset.template.builder.PropertyModelBuilder;
import org.wirez.tools.oryx.stencilset.template.builder.PropertySetModelBuilder;
import org.wirez.tools.oryx.stencilset.template.model.PropertyValueDef;
import org.wirez.tools.oryx.stencilset.template.model.StencilTemplateResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StencilSetCodeGenerator {

    public static final String ENCODING = "UTF-8";
    private static final String VELOCITY_CONFIG = "org/wirez/tools/oryx/stencilset/template/velocity.properties";

    private VelocityEngine velocityEngine;
    private final TemplateBuilderFactory factory;
    
    private File basePath;
    private String basePackage;
    private String definitionSetName;
    private StencilSet stencilSet;
    private final Map<String, Pojo> propertyPackages = new HashMap<>();
    private final Map<String, Pojo> properties = new HashMap<>();
    private final Map<String, Pojo> stencils = new HashMap<>();
    private final Map<String, Map<String, PropertyValueDef>> stencilDefaultValues = new HashMap<>();
    private Pojo stencilSetPojo = null;
    
    public StencilSetCodeGenerator() {
        this.factory = TemplateBuilderFactory.getInstance();
        init();
    }

    public static String getPropertyPackClassName(String id) {
        return toJavaIdentifier( id ) + "Package";
    }

    public static String getPropertyClassName(String id) {
        return toJavaIdentifier( id );
    }

    public static String getStencilClassName(String id) {
        return toJavaIdentifier( id );
    }

    public static String getStencilSetClassName(String id) {
        return toJavaIdentifier( id ) + "DefinitionSet";
    }

    public StencilSetGeneratorContext createContext() {
        return new StencilSetGeneratorContext(this, new VelocityContext(), velocityEngine, basePackage);
    }
    
    public void generate(File outputPath,
                         String definitionSetName,
                         String basePpackage, 
                         StencilSet stencilSet) {
        
        if ( !outputPath.exists() ) {
            throw new RuntimeException("Output path does not exist! ["  + outputPath.getAbsolutePath() + "]" );
        }

        this.stencilSet = stencilSet;
        this.definitionSetName = definitionSetName;
        this.basePackage = basePpackage;
        this.basePath = outputPath;
        propertyPackages.clear();
        properties.clear();
        stencils.clear();
        
        
        // Rules & Definition Sets.
        StencilSetGeneratorContext generatorContext = createContext();
        this.stencilSetPojo = generateStencilSet( stencilSet, generatorContext );
        
        // ***** POST PROCESSING *********
        
        // Generate model builder classes.
        String pkg = this.basePackage + ".factory";
        
        String propertyModelBuilderClassName = getDefinitionSetName() + "PropertyBuilder";
        generatePropertyModelBuilder(pkg, propertyModelBuilderClassName);

        String propertySetModelBuilderClassName = getDefinitionSetName() + "PropertySetBuilder";
        generatePropertySetModelBuilder( pkg, propertySetModelBuilderClassName, propertyModelBuilderClassName );

        String definitionModelBuilderClassName = getDefinitionSetName() + "DefinitionBuilder";
        generateStencilModelBuilder( pkg, definitionModelBuilderClassName, propertySetModelBuilderClassName, propertyModelBuilderClassName );

        String definitionSetModelBuilderClassName = getDefinitionSetName() + "DefinitionSetBuilder";
        generateStencilSetModelBuilder( pkg, definitionSetModelBuilderClassName, definitionModelBuilderClassName );


        System.out.println("Generated " + properties.size() + " property pojos.");
        System.out.println("Generated " + propertyPackages.size() + " property set pojos.");
        System.out.println("Generated " + stencils.size() + " definition pojos.");
        System.out.println("Generated " + stencilSet.getContainmentRules().size() + " containment rules.");
        System.out.println("Generated " + stencilSet.getConnectionRules().size() + " connection rules.");
        System.out.println("Generated " + stencilSet.getCardinalityRules().size() + " cardinality rules.");
    }

    public Pojo[] getProperty(Stencil stencil, String pId) {
        Collection<Property> properties = stencil.getProperties();
        for ( Property property : properties ) {
            if ( property.getId().equals(pId) ) {
                Pojo propPojo = this.properties.get( pId );
                
                String[] stencilPropPacks = stencil.getPropertyPackages();
                for ( String stencilPropPack : stencilPropPacks ) {
                    PropertyPackage pack = getPropertyPackage(stencilPropPack);
                    if ( pack != null && hasProperty(pack, pId) ) {
                        Pojo packPojo = propertyPackages.get( pack.getName() );
                        return new Pojo[] { packPojo, propPojo};
                    }
                }
                
                return new Pojo[] { propPojo};
            }
        }
        
        return null;
    }

    private PropertyPackage getPropertyPackage(String id) {
        Collection<PropertyPackage> packages = getModel().getPropertyPackages();
        if ( !packages.isEmpty() ) {
            for ( PropertyPackage p : packages ) {
                if ( p.getName().equals(id) ) {
                    return p;
                }
            }
        }
        return null;
    }
    
    private boolean hasProperty(PropertyPackage pack, String propId) {
        Collection<Property> properties = pack.getProperties();
        for ( Property prop : properties ) {
            if ( prop.getId().equals(propId) ) {
                return true;
            }
        }
        return false;
    }

    public void generateStencilSetModelBuilder(String pkg, String className, String definitionBuilderClassName) {
        DefinitionSetModelBuilder builder = new DefinitionSetModelBuilder(this);
        String content = builder.build( pkg, className, definitionBuilderClassName );
        File outputPath = getOutputPath( pkg );
        writeFile( outputPath,  className, content );
    }

    public void generateStencilModelBuilder(String pkg, String className, String propSetBuilderClassName, String propBuilderClassName) {
        DefinitionModelBuilder builder = new DefinitionModelBuilder(this);
        String content = builder.build( pkg, className, propSetBuilderClassName, propBuilderClassName );
        File outputPath = getOutputPath( pkg );
        writeFile( outputPath,  className, content );
    }
    
    public void generatePropertyModelBuilder(String pkg, String className) {
        PropertyModelBuilder builder = new PropertyModelBuilder(this);
        String content = builder.build( pkg, className );
        File outputPath = getOutputPath( pkg );
        writeFile( outputPath,  className, content );
    }

    public void generatePropertySetModelBuilder(String pkg, String className, String propBuilderClassName) {
        PropertySetModelBuilder builder = new PropertySetModelBuilder(this);
        String content = builder.build( pkg, className, propBuilderClassName );
        File outputPath = getOutputPath( pkg );
        writeFile( outputPath,  className, content );
    }

    public Pojo generateStencilSet( StencilSet stencilSet,
                                              StencilSetGeneratorContext generatorContext) {

        TemplateBuilder<StencilSet, StencilSetGeneratorContext> builder = factory.getTemplateBuilder(StencilSet.class);
        
        if ( builder.accepts( stencilSet) ) {
            TemplateResult result = builder.build(generatorContext, stencilSet);
            File outputPath = getOutputPath( result.getPojo().getPkg() );
            writeFile( outputPath,  result.getPojo().getClassName(), result.getResult() );
            return result.getPojo();
        }

        return null;
    }

    public Collection<Pojo> generateStencils( Collection<Stencil> stencils,
                                  StencilSetGeneratorContext generatorContext) {

        // Stencils.
        Collection<Pojo> pojos = new LinkedList<>();
        if ( null != stencils && !stencils.isEmpty() ) {
            TemplateBuilder<Stencil, StencilSetGeneratorContext> builder = factory.getTemplateBuilder(Stencil.class);
            for ( Stencil stencil : stencils ) {
                String id = stencil.getId();
                boolean accepts = builder.accepts( stencil );
                
                if ( !this.stencils.containsKey( id ) &&
                        builder.accepts( stencil ) ) {

                    StencilTemplateResult result = (StencilTemplateResult) builder.build( generatorContext, stencil );
                    Pojo pojo = result.getPojo();
                    File outputPath = getOutputPath( pojo.getPkg() );
                    writeFile( outputPath,  pojo.getClassName(), result.getResult() );

                    this.stencils.put( id, pojo );
                    this.stencilDefaultValues.put( id, result.getDefaultPropertyValues() );
                    
                } 
                
                if ( accepts ) {

                    pojos.add( this.stencils.get( id ) );

                }

            }
        }
        
        return pojos;
    }
    
    public Collection<Pojo> generatePropertyPackages( Collection<PropertyPackage> propertyPackages,
                                          StencilSetGeneratorContext generatorContext) {

        // Property packages.
        Collection<Pojo> pojos = new LinkedList<>();
        if ( null != propertyPackages && !propertyPackages.isEmpty() ) {
            TemplateBuilder<PropertyPackage, StencilSetGeneratorContext> builder = factory.getTemplateBuilder(PropertyPackage.class);
            for ( PropertyPackage propertyPackage : propertyPackages ) {
                String id = propertyPackage.getName();
                boolean accepts = builder.accepts( propertyPackage );
                
                if ( !this.propertyPackages.containsKey( id ) && accepts ) {
                   
                    TemplateResult result = builder.build( generatorContext, propertyPackage );
                    Pojo pojo = result.getPojo();
                    File outputPath = getOutputPath( pojo.getPkg() );
                    writeFile( outputPath,  pojo.getClassName(), result.getResult() );

                    this.propertyPackages.put( id, pojo );
                    
                }
                
                if ( accepts ) {

                    pojos.add( this.properties.get( id ) );

                }
                
            }
        }

        return pojos;
    }

    public Collection<Pojo> generateProperties( Collection<Property> properties,
                                    StencilSetGeneratorContext generatorContext) {

        // Properties.
        Collection<Pojo> pojos = new LinkedList<>();
        if ( null != properties && !properties.isEmpty() ) {
            TemplateBuilder<Property, StencilSetGeneratorContext> builder = factory.getTemplateBuilder(Property.class);
            for ( Property property : properties ) {
                String id = property.getId();
                boolean accepts = builder.accepts( property );
                
                if ( !this.properties.containsKey( id ) && 
                        builder.accepts( property ) ) {

                    TemplateResult result = builder.build( generatorContext, property );
                    Pojo pojo = result.getPojo();
                    File outputPath = getOutputPath( pojo.getPkg() );
                    writeFile( outputPath,  pojo.getClassName(), result.getResult() );

                    this.properties.put( id, pojo );
                    
                }
                
                if ( accepts ) {

                    pojos.add( this.properties.get( id ) );
                    
                }

                
            }
        }
        
        return pojos;
    }

    public Map<String, Pojo> getPropertyPackages() {
        return propertyPackages;
    }

    public Map<String, Pojo> getProperties() {
        return properties;
    }

    public String getDefinitionSetName() {
        return definitionSetName;
    }
    
    public StencilSet getModel() {
        return stencilSet;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public Map<String, Pojo> getStencils() {
        return stencils;
    }

    public Map<String, Map<String, PropertyValueDef>> getStencilDefaultValues() {
        return stencilDefaultValues;
    }

    public Pojo getStencilSetPojo() {
        return stencilSetPojo;
    }

    public String getOutputPathName(String pkg) {
        if ( pkg == null ) {
            return basePackage;
        } else {
            return basePackage + "." + pkg;
        }
    }

    private File getOutputPath(String pkg) {
        if ( pkg == null ) {
            return basePath;
        } else {
            return new File(basePath,  pkg.replaceAll("\\.", File.separator));
        }
    }
    
    
    private void writeFile(File outputPath,
                           String fileName,
                           String content) {
        
        if ( !outputPath.exists() ) {
            outputPath.mkdirs();
        }
        
        File file = new File(outputPath, fileName + ".java");
        
        if ( file.exists() ) {
            boolean delete = file.delete();
            if ( !delete ) {
                throw new RuntimeException("Cannot delete existing file [" + file.getAbsolutePath() + "]");
            }
        }

        try {
            Files.write(Paths.get(file.toURI()), content.getBytes(ENCODING));
        } catch (Exception e) {
            error("Cannot write file [" + file.getAbsolutePath() + "]", e);
        }

    }

   

    private void init() {

        try {
            // Load the velocity properties from the class path
            Properties properties = new Properties();
            properties.load( StencilSetCodeGenerator.class.getClassLoader().getResourceAsStream( VELOCITY_CONFIG ) );

            // Create and initialize the template engine
            velocityEngine = new VelocityEngine( properties );
        
        } catch( Exception e ) {
            error("Error initializing StencilSet code generator.", e);
        }

    }

    private void error(String message, Exception e) {
        System.out.println("[ERROR] " + message);
        e.printStackTrace();
    }
    
    private static String toJavaIdentifier(String id) {
        id = StringUtils.capitalize(id);
        StringBuilder sb = new StringBuilder();
        if(!Character.isJavaIdentifierStart(id.charAt(0))) {
            sb.append("_");
        }
        for (char c : id.toCharArray()) {
            if(!Character.isJavaIdentifierPart(c)) {
                sb.append("_");
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
    
}
