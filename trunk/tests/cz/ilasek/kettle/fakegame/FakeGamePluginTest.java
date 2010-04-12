/**
 * 
 */
package cz.ilasek.kettle.fakegame;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;

/**
 * @author ivo
 *
 */
public class FakeGamePluginTest {
    
    private FakeGamePlugin plugin;
    private FakeGamePluginMeta meta;
    private FakeGamePluginData data;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        meta = new FakeGamePluginMeta();
        data = new FakeGamePluginData();
        
        TransMeta transMeta = new TransMeta();
        Trans trans = new Trans();
        StepMeta stepMeta = new StepMeta();
        stepMeta.setName("step name");
        //test
        plugin = new FakeGamePlugin(stepMeta, data, 1, transMeta, trans);
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePlugin#init(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)}.
     */
    @Test
    public void testInit() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePlugin#processRow(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)}.
     */
    @Test
    public void testProcessRow() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePlugin#FakeGamePlugin(org.pentaho.di.trans.step.StepMeta, org.pentaho.di.trans.step.StepDataInterface, int, org.pentaho.di.trans.TransMeta, org.pentaho.di.trans.Trans)}.
     */
    @Test
    public void testFakeGamePlugin() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePlugin#run()}.
     */
    @Test
    public void testRun() {
        fail("Not yet implemented");
    }

}
