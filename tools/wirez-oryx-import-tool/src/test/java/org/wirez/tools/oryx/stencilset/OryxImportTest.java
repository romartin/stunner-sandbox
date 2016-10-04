package org.wirez.tools.oryx.stencilset;

import org.apache.velocity.VelocityContext;
import org.wirez.tools.oryx.stencilset.model.Property;
import org.wirez.tools.oryx.stencilset.model.StencilSet;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OryxImportTest {

    protected static final String BPMN_STENCILSET = "org/wirez/tools/oryx/stencilset/bpmn_stencilset.json";
    protected static final String BPMN_STENCILSET_MINIMAL = "org/wirez/tools/oryx/stencilset/bpmn_stencilset_minimal.json";
    // protected static final String OUTPUT_PATH = "/home/romartin/development/wirez/wirez/wirez-sets/wirez-bpmn/wirez-bpmn-api/src/main/java/generated";
    protected static final String OUTPUT_PATH = "/home/romartin/development/wirez/wirez/wirez-tools/wirez-oryx-import-tool/aaaa";

    public static void main(String[] args) {

        OryxImportTest test = new OryxImportTest();
        
        try {

            test.testStencilSetGen();
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void testStencilSetGen() throws IOException {
        StencilSetCodeGenerator generator = new StencilSetCodeGenerator();

        File outputPath = new File(OUTPUT_PATH);

        if ( outputPath.exists() ) {
            removeDirectory(outputPath);
        }

        outputPath.mkdirs();

        StencilSet stencilSet = unmarshall(BPMN_STENCILSET);

        generator.generate(outputPath, "BPMN", "org.roger600.bpmn.generated", stencilSet);

    }

    public void testUnmarshall() {
        StencilSet stencilSet = unmarshall(BPMN_STENCILSET);
        System.out.println(stencilSet);
    }
    
    private StencilSet unmarshall(String file) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        String json = getString(is);
        StencilSet stencilSet = new StencilSetUnmarshaller().unmarshall(json);
        return stencilSet;
    }

    public static void removeDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    private static String getString(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
    
}
