/**
 * 
 */
package cz.ilasek.kettle.fakegame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.LongObjectId;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryCapabilities;
import org.pentaho.di.repository.RepositoryMeta;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test set for class {@link FakeGamePluginMeta}.
 * @author Ivo Lasek
 *
 */
public class FakeGamePluginMetaTest {
    private static final String CLASS_FILE = "resources/test/iris-classify-poly3.net";
    private static final String MODEL_FILE = "resources/test/iris-poly5.net";
    
    private static final String TRANSFORMATION_FILE = "resources/test/transformation.ktr";
    
    private FakeGamePluginMeta classMeta;
    private FakeGamePluginMeta modelMeta;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        classMeta = new FakeGamePluginMeta();
        modelMeta = new FakeGamePluginMeta();
        
        classMeta.setModelsFileName(CLASS_FILE, new TransMeta());
        modelMeta.setModelsFileName(MODEL_FILE, new TransMeta());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#hashCode()}.
     * @throws KettleStepException 
     */
    @Test
    public void testHashCode() throws KettleStepException {
        FakeGamePluginMeta tmpMeta = new FakeGamePluginMeta();
        
        tmpMeta.setModelsFileName(CLASS_FILE, new TransMeta());
        assertEquals(tmpMeta.hashCode(), classMeta.hashCode());
        tmpMeta.setModelsFileName(MODEL_FILE, new TransMeta());
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
        tmpMeta.setModelsFileName(CLASS_FILE, new TransMeta());
        
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
        tmpMeta.setModelsFileName(MODEL_FILE, new TransMeta());
        assertEquals(tmpMeta.getSerializedModels(), modelMeta.getSerializedModels());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#setModelsFileName(java.lang.String)}.
     * @throws KettleStepException 
     */
    @Test
    public void testSetModelsFileName() throws KettleStepException {
        assertFalse(classMeta.hashCode() == modelMeta.hashCode());
        classMeta.setModelsFileName(MODEL_FILE, new TransMeta());
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
     * @throws KettleStepException 
     */
    @Test
    public void testEqualsObject() throws KettleStepException {
        assertFalse(classMeta.equals(modelMeta));
        FakeGamePluginMeta tmpMeta =  new FakeGamePluginMeta();
        tmpMeta.setModelsFileName(CLASS_FILE, new TransMeta());
        assertTrue(classMeta.equals(tmpMeta));
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#setDefault()}.
     */
    @Test
    public void testSetDefault() {
        classMeta.setDefault();
        FakeGamePluginMeta tmpMeta = new FakeGamePluginMeta();
        assertTrue(classMeta.equals(tmpMeta));
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#setShowOutputProbabilities(boolean)}.
     */
    @Test
    public void testSetShowOutputProbabilities() {
        classMeta.setShowOutputProbabilities(true);
        assertTrue(classMeta.isShowOutputProbabilities());
        classMeta.setShowOutputProbabilities(false);
        assertFalse(classMeta.isShowOutputProbabilities());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#isShowOutputProbabilities()}.
     */
    @Test
    public void testIsShowOutputProbabilities() {
        classMeta.setShowOutputProbabilities(true);
        assertTrue(classMeta.isShowOutputProbabilities());
        classMeta.setShowOutputProbabilities(false);
        assertFalse(classMeta.isShowOutputProbabilities());
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#check(java.util.List, org.pentaho.di.trans.TransMeta, org.pentaho.di.trans.step.StepMeta, org.pentaho.di.core.row.RowMetaInterface, java.lang.String[], java.lang.String[], org.pentaho.di.core.row.RowMetaInterface)}.
     */
    @Test
    public void testCheck() {
        String[] input = new String[2]; 
        List<CheckResultInterface> results = new LinkedList<CheckResultInterface>();
        modelMeta.check(results, new TransMeta(), new StepMeta(), null, input, null, null);
        
        assertEquals("Not receiving any fields from previous steps!", results.get(0).getText());
        assertTrue(results.size() == 2);
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getStep(org.pentaho.di.trans.step.StepMeta, org.pentaho.di.trans.step.StepDataInterface, int, org.pentaho.di.trans.TransMeta, org.pentaho.di.trans.Trans)}.
     * @throws KettleException 
     */
    @Test
    public void testGetStep() throws KettleException {
        KettleEnvironment.init();
        TransMeta transMeta = new TransMeta(TRANSFORMATION_FILE);
        Trans trans = new Trans(transMeta);
        trans.prepareExecution(null);
        StepInterface step = trans.findRunThread("FakeGame Plugin");
        
        FakeGamePlugin plugin = (FakeGamePlugin) classMeta.getStep(step.getStepMeta(), new FakeGamePluginData(), 0, transMeta, trans);
        assertFalse(plugin == null);
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#getStepData()}.
     */
    @Test
    public void testGetStepData() {
        StepDataInterface data = classMeta.getStepData();
        assertFalse(data == null);
        assertTrue(data instanceof StepDataInterface);
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#readRep(org.pentaho.di.repository.Repository, org.pentaho.di.repository.ObjectId, java.util.List, java.util.Map)}.
     * @throws KettleException 
     */
    @Test
    public void testReadRep() throws KettleException {
        ObjectId idStep = new LongObjectId(1l);
        classMeta.readRep(new KettleFileRepository(), idStep, null, null);
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginMeta#saveRep(org.pentaho.di.repository.Repository, org.pentaho.di.repository.ObjectId, org.pentaho.di.repository.ObjectId)}.
     * @throws KettleException 
     */
    @Test
    public void testSaveRep() throws KettleException {
        KettleFileRepository repository = new KettleFileRepository();
        ObjectId idStep = new LongObjectId(1l);
        ObjectId idTrans = new LongObjectId(2l);
        classMeta.saveRep(repository, idStep, idTrans);
    }

}
