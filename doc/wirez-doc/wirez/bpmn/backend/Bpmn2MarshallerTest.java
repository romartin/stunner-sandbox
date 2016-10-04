package org.wirez.bpmn.backend.marshall.json;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.DocumentRoot;
import org.eclipse.bpmn2.util.Bpmn2ResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.jboss.drools.DroolsPackage;
import org.jboss.drools.util.DroolsResourceFactoryImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.wirez.bpmn.api.*;
import org.wirez.bpmn.backend.legacy.profile.impl.DefaultProfileImpl;
import org.wirez.bpmn.backend.marshall.json.builder.BPMNGraphObjectBuilderFactory;
import org.wirez.core.api.DefinitionManager;
import org.wirez.core.api.definition.Definition;
import org.wirez.core.api.graph.Element;
import org.wirez.core.api.graph.Graph;
import org.wirez.core.api.graph.command.GraphCommandManager;
import org.wirez.core.api.graph.command.factory.GraphCommandFactoryImpl;
import org.wirez.core.api.rule.EmptyRuleManager;
import org.wirez.core.api.service.definition.DefinitionService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class Bpmn2MarshallerTest {

    @Mock
    GraphFactory<? extends Definition> graphFactory;

    @Mock
    ViewNodeFactory<? extends Definition> nodeFactory;

    @Mock
    ConnectionEdgeFactory<? extends Definition> edgeFactory;

    @Mock
    DefinitionManager definitionManager;

    @Mock
    DefinitionService definitionService;

    @InjectMocks
    BPMNGraphObjectBuilderFactory bpmnWiresFactory;
    
    private ResourceSet resourceSet;
    
    @Before
    public void setup() {

        resourceSet = new ResourceSetImpl();

        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
                ( Resource.Factory.Registry.DEFAULT_EXTENSION,
                  new DroolsResourceFactoryImpl() );
        resourceSet.getPackageRegistry().put
                ( DroolsPackage.eNS_URI,
                  DroolsPackage.eINSTANCE );
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
                .put( Resource.Factory.Registry.DEFAULT_EXTENSION, new Bpmn2ResourceFactoryImpl() );
        resourceSet.getPackageRegistry().put( "http://www.omg.org/spec/BPMN/20100524/MODEL", Bpmn2Package.eINSTANCE );

        
        doAnswer(new Answer<Element>() {
            @Override
            public Element answer(InvocationOnMock invocationOnMock) throws Throwable {
                final String defId = (String) invocationOnMock.getArguments()[0];

                BPMNDefinition bpmnDefinition = null;
                
                /*if ( BPMNDiagram.ID.equals(defId) ) {
                    bpmnDefinition = new BPMNDiagram(
                            new BPMNGeneral(new Name(), new Documentation()),
                            new DiagramSet(new Package(), new Executable())
                    );
                } else if ( StartNoneEvent.ID.equals(defId) ) {
                    bpmnDefinition = new StartNoneEvent(
                            new BPMNGeneral(new Name(), new Documentation()),
                            new BackgroundSet(new BgColor() , new BorderColor(), new BorderSize()),
                            new FontSet(new FontFamily(), new FontColor() , new FontSize() , new FontBorderSize() ),
                            new Radius()
                    );
                } else if ( EndNoneEvent.ID.equals(defId) ) {
                    bpmnDefinition = new EndNoneEvent(
                            new BPMNGeneral(new Name(), new Documentation()),
                            new BackgroundSet(new BgColor() , new BorderColor(), new BorderSize()),
                            new FontSet(new FontFamily(), new FontColor() , new FontSize() , new FontBorderSize() ),
                            new Radius()
                    );
                } else if ( Task.ID.equals(defId) ) {
                    bpmnDefinition = new Task(
                            new BPMNGeneral(new Name(), new Documentation()),
                            new BackgroundSet(new BgColor() , new BorderColor(), new BorderSize()),
                            new FontSet(new FontFamily(), new FontColor() , new FontSize() , new FontBorderSize() ),
                            new Width(),
                            new Height()
                    );
                } else if ( ParallelGateway.ID.equals(defId) ) {
                    bpmnDefinition = new ParallelGateway(
                            new BPMNGeneral(new Name(), new Documentation()),
                            new BackgroundSet(new BgColor() , new BorderColor(), new BorderSize()),
                            new FontSet(new FontFamily(), new FontColor() , new FontSize() , new FontBorderSize() ),
                            new Radius()
                    );
                } else if ( SequenceFlow.ID.equals(defId) ) {
                    bpmnDefinition = new SequenceFlow(
                            new BPMNGeneral(new Name(), new Documentation()),
                            new BackgroundSet(new BgColor() , new BorderColor(), new BorderSize())
                    );
                }*/

                // TODO
                
                return null;
            }
        }).when(definitionService).buildGraphElement(anyString(), anyString());
    }

    @Test
    public void parseGraphFile() throws IOException {
        Definitions definitions = parseDefinitions();
        Bpmn2UnMarshaller parser = new Bpmn2UnMarshaller(bpmnWiresFactory, null, null, new GraphCommandManager(null), 
                new EmptyRuleManager(), new GraphCommandFactoryImpl(null));
        parser.setProfile(new DefaultProfileImpl());
        Graph bpmnGraph = parser.unmarshall(definitions, null);
        // log(bpmnGraphs);
        log("BPMN2 load test finished!");
    }

    protected Definitions parseDefinitions() throws IOException {
        InputStream inputStream = null;
        try {
            log("Starting BPMN2 load test...");
            XMLResource outResource = (XMLResource) resourceSet.createResource( URI.createURI( "inputStream://dummyUriWithValidSuffix.xml" ) );
            inputStream = Bpmn2MarshallerTest.class.getClassLoader().getResourceAsStream( "test.bpmn2" );
            outResource.getDefaultLoadOptions().put( XMLResource.OPTION_ENCODING, "UTF-8" );
            outResource.setEncoding( "UTF-8" );
            Map<String, Object> options = new HashMap<String, Object>();
            options.put( XMLResource.OPTION_ENCODING, "UTF-8" );
            outResource.load( inputStream, options );

            DocumentRoot root = (DocumentRoot) outResource.getContents().get( 0 );
            return root.getDefinitions();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return null;
    }

    // @Test
    public void parseJSONFile() throws IOException {
        /*Definitions definitions = parseDefinitions();
        Bpmn2JsonMarshaller parser = new Bpmn2JsonMarshaller();
        parser.setProfile(new DefaultProfileImpl());
        String json = parser.marshall(definitions, null);
        logger.log(json);
        logger.log("BPMN2 load test finished!");*/
    }
    
    private void log(String message) {
        System.out.println(message);
    }

    private void log(Collection<Graph> graphs) {
        // TODO
    }
}