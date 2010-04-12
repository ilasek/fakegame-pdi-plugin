/**
 * 
 */
package cz.ilasek.kettle.fakegame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.xml.XMLHandler;
import org.w3c.dom.Document;

/**
 * @author ivo
 *
 */
public class FakeGamePluginMetaTest {
    private static final String CLASS_FILE = "resources/test/iris-classify-poly3.net";
    private static final String MODEL_FILE = "resources/test/iris-poly5.net";
    
    private FakeGamePluginMeta classMeta;
    private FakeGamePluginMeta modelMeta;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        classMeta = new FakeGamePluginMeta();
        modelMeta = new FakeGamePluginMeta();
        
        classMeta.setModelsFileName(CLASS_FILE);
        modelMeta.setModelsFileName(MODEL_FILE);
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#hashCode()}.
     * @throws KettleStepException 
     */
    @Test
    public void testHashCode() throws KettleStepException {
        FakeGamePluginMeta tmpMeta = new FakeGamePluginMeta();
        
        tmpMeta.setModelsFileName(CLASS_FILE);
        assertEquals(tmpMeta.hashCode(), classMeta.hashCode());
        tmpMeta.setModelsFileName(MODEL_FILE);
        assertFalse(tmpMeta.hashCode()== classMeta.hashCode());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#clone()}.
     */
    @Test
    public void testClone() {
        FakeGamePluginMeta tmpMeta = (FakeGamePluginMeta) classMeta.clone();
        
        assertNotSame(tmpMeta, classMeta);
        assertEquals(tmpMeta.hashCode(), classMeta.hashCode());
        assertFalse(tmpMeta.hashCode()== modelMeta.hashCode());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getXML()}.
     * @throws KettleStepException 
     */
    @Test
    public void testGetXML() throws KettleStepException {
        FakeGamePluginMeta tmpMeta = new FakeGamePluginMeta();
        tmpMeta.setModelsFileName(CLASS_FILE);
        
        assertTrue(tmpMeta.getXML().equals(classMeta.getXML()));
        tmpMeta.setShowOutputProbabilities(true);
        assertFalse(tmpMeta.getXML().equals(classMeta.getXML()));
        classMeta.setShowOutputProbabilities(true);
        assertTrue(tmpMeta.getXML().equals(classMeta.getXML()));
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getFields(org.pentaho.di.core.row.RowMetaInterface, java.lang.String, org.pentaho.di.core.row.RowMetaInterface[], org.pentaho.di.trans.step.StepMeta, org.pentaho.di.core.variables.VariableSpace)}.
     */
    @Test
    public void testGetFields() {
        RowMeta rowMeta = new RowMeta();
        classMeta.getFields(rowMeta, "nowhere", null, null, null);
        assertEquals("ClassifierModel", rowMeta.getFieldNames()[0]);
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getModels()}.
     */
    @Test
    public void testGetModels() {
        assertEquals("PolynomialModel_Iris-setosa", modelMeta.getModels().getModelsSignatures().get(0).getName());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getModelFileName()}.
     */
    @Test
    public void testGetModelFileName() {
        assertEquals(MODEL_FILE, modelMeta.getModelFileName());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getSerializedModels()}.
     * @throws KettleStepException 
     */
    @Test
    public void testGetSerializedModels() throws KettleStepException {
        FakeGamePluginMeta tmpMeta = new FakeGamePluginMeta();
        tmpMeta.setModelsFileName(MODEL_FILE);
        assertEquals(tmpMeta.getSerializedModels(), modelMeta.getSerializedModels());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#setModelsFileName(java.lang.String)}.
     * @throws KettleStepException 
     */
    @Test
    public void testSetModelsFileName() throws KettleStepException {
        assertFalse(classMeta.hashCode() == modelMeta.hashCode());
        classMeta.setModelsFileName(MODEL_FILE);
        assertTrue(classMeta.hashCode() == modelMeta.hashCode());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#setSerializedModels(java.lang.String)}.
     */
    @Test
    public void testSetSerializedModels() {
        assertFalse(classMeta.hashCode() == modelMeta.hashCode());
        classMeta.setSerializedModels(modelMeta.getSerializedModels());
        assertTrue(classMeta.getSerializedModels() == modelMeta.getSerializedModels());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#loadXML(org.w3c.dom.Node, java.util.List, java.util.Map)}.
     * @throws KettleXMLException 
     */
    @Test
    public void testLoadXML() throws KettleXMLException {
        assertFalse(classMeta.hashCode() == modelMeta.hashCode());
        assertFalse(classMeta.isShowOutputProbabilities());
        
        modelMeta.setShowOutputProbabilities(true);
        Document stepnode = XMLHandler.loadXMLString(modelMeta.getXML());

        classMeta.loadXML(stepnode, null, null);
        assertTrue(classMeta.hashCode() == modelMeta.hashCode());
        assertTrue(classMeta.isShowOutputProbabilities());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#setDefault()}.
     */
    @Test
    public void testSetDefault() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#setShowOutputProbabilities(boolean)}.
     */
    @Test
    public void testSetShowOutputProbabilities() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#isShowOutputProbabilities()}.
     */
    @Test
    public void testIsShowOutputProbabilities() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#check(java.util.List, org.pentaho.di.trans.TransMeta, org.pentaho.di.trans.step.StepMeta, org.pentaho.di.core.row.RowMetaInterface, java.lang.String[], java.lang.String[], org.pentaho.di.core.row.RowMetaInterface)}.
     */
    @Test
    public void testCheck() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getDialog(org.eclipse.swt.widgets.Shell, org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.TransMeta, java.lang.String)}.
     */
    @Test
    public void testGetDialog() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getStep(org.pentaho.di.trans.step.StepMeta, org.pentaho.di.trans.step.StepDataInterface, int, org.pentaho.di.trans.TransMeta, org.pentaho.di.trans.Trans)}.
     */
    @Test
    public void testGetStep() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getStepData()}.
     */
    @Test
    public void testGetStepData() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#readRep(org.pentaho.di.repository.Repository, org.pentaho.di.repository.ObjectId, java.util.List, java.util.Map)}.
     */
    @Test
    public void testReadRep() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#saveRep(org.pentaho.di.repository.Repository, org.pentaho.di.repository.ObjectId, org.pentaho.di.repository.ObjectId)}.
     */
    @Test
    public void testSaveRep() {
        fail("Not yet implemented");
    }

}
