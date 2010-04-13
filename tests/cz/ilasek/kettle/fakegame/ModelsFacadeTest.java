/**
 * 
 */
package cz.ilasek.kettle.fakegame;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;

/**
 * @author Ivo Lasek
 *
 */
public class ModelsFacadeTest {
    
    private static final String CLASS_FILE = "resources/test/iris-classify-poly3.net";
    private static final String MODEL_FILE = "resources/test/iris-poly5.net";    
    
    private ModelsFacade modelsFacade;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        modelsFacade = new ModelsFacade();
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#loadClassifiers(java.io.DataInputStream)}.
     * @throws FileNotFoundException 
     */
    @Test
    public void testLoadClassifiers() throws FileNotFoundException {
        DataInputStream input = new DataInputStream(new FileInputStream(CLASS_FILE));
        modelsFacade.loadClassifiers(input);
        List<ModelSignature> signatures = modelsFacade.getModelsSignatures();
        assertEquals(1, signatures.size());
        assertEquals("ClassifierModel", signatures.get(0).getName());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#loadModels(java.io.DataInputStream)}.
     * @throws FileNotFoundException 
     */
    @Test
    public void testLoadModels() throws FileNotFoundException {
        DataInputStream input = new DataInputStream(new FileInputStream(MODEL_FILE));
        modelsFacade.loadModels(input);
        List<ModelSignature> signatures = modelsFacade.getModelsSignatures();
        assertEquals(3, signatures.size());
        assertEquals("PolynomialModel_Iris-setosa", signatures.get(0).getName());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#getModelsSignatures()}.
     * @throws FileNotFoundException 
     */
    @Test
    public void testGetModelsSignatures() throws FileNotFoundException {
        DataInputStream input = new DataInputStream(new FileInputStream(CLASS_FILE));
        modelsFacade.loadClassifiers(input);
        List<ModelSignature> signatures = modelsFacade.getModelsSignatures();
        assertEquals(1, signatures.size());        
        modelsFacade.setShowOutputProbabilities(true);
        signatures = modelsFacade.getModelsSignatures();
        assertEquals(3, signatures.size());
    }
    
    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#isShowOutputProbabilities()}.
     */
    @Test
    public void testIsShowOutputProbabilities() {
        assertFalse(modelsFacade.isShowOutputProbabilities());
        modelsFacade.setShowOutputProbabilities(true);
        assertTrue(modelsFacade.isShowOutputProbabilities());
    }
    
    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#evaluate(java.lang.Object[], org.pentaho.di.core.row.RowMetaInterface)}.
     * @throws KettleValueException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testEvaluate() throws KettleValueException, FileNotFoundException {
        DataInputStream input = new DataInputStream(new FileInputStream(CLASS_FILE));
        modelsFacade.loadClassifiers(input);
        
        Object[] inputRow = { 1d, 1d, 1d, 1d };
        RowMeta rowMeta = new RowMeta();
        rowMeta.addValueMeta(new ValueMeta("sepal_width", ValueMetaInterface.TYPE_NUMBER));
        rowMeta.addValueMeta(new ValueMeta("sepal_length", ValueMetaInterface.TYPE_NUMBER));
        rowMeta.addValueMeta(new ValueMeta("petal_width", ValueMetaInterface.TYPE_NUMBER));
        rowMeta.addValueMeta(new ValueMeta("petal_length", ValueMetaInterface.TYPE_NUMBER));
        
        modelsFacade.generateMappings(rowMeta.getValueMetaList());
        modelsFacade.setShowOutputProbabilities(true);
        Object[] evaluation = modelsFacade.evaluate(inputRow, rowMeta);
        assertTrue(0.5d < (Double) evaluation[0]);
        
        ModelsFacade models = new ModelsFacade();
        DataInputStream input2 = new DataInputStream(new FileInputStream(MODEL_FILE));
        models.loadModels(input2);
        models.generateMappings(rowMeta.getValueMetaList());
        Object[] evaluation2 = models.evaluate(inputRow, rowMeta);
        assertEquals(3, evaluation2.length);
        assertTrue(1d <= (Double) evaluation2[0]);
        assertTrue(0d >= (Double) evaluation2[1]);
        assertTrue(0d >= (Double) evaluation2[2]);
    }
    
//    /**
//     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#generateMappings(java.util.List)}.
//     */
//    @Test
//    public void testGenerateMappings() {
//        modelsFacade.generateMappings(incommingHeader)
//    }
//
//    /**
//     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#formHeaderMap(java.util.List)}.
//     */
//    @Test
//    public void testFormHeaderMap() {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#mapInputToRow(int)}.
//     */
//    @Test
//    public void testMapInputToRow() {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#getInputs()}.
//     */
//    @Test
//    public void testGetInputs() {
//        fail("Not yet implemented");
//    }
//
//
//
//    /**
//     * Test method for {@link cz.ilasek.kettle.fakegame.ModelsFacade#setShowOutputProbabilities(boolean)}.
//     */
//    @Test
//    public void testSetShowOutputProbabilities() {
//        fail("Not yet implemented");
//    }
//

}
