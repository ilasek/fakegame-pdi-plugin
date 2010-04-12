/**
 * 
 */
package cz.ilasek.kettle.fakegame;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

/**
 * Test set for plugin class {@link FakeGamePlugin}.
 * 
 * @author Ivo Lasek
 *
 */
public class FakeGamePluginTest {
    
    private static final String TRANSFORMATION_FILE = "resources/test/transformation.ktr";
    
    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePlugin#run()}.
     * @throws KettleException 
     */
    @Test
    public void testRun() throws KettleException {
        KettleEnvironment.init();
        TransMeta transMeta = new TransMeta(TRANSFORMATION_FILE);
        Trans trans = new Trans(transMeta);

        trans.execute(null);
        trans.waitUntilFinished();
        
        assertEquals(0, trans.getResult().getExitStatus());
        List<RowMetaAndData> rows = trans.getResult().getRows();
        assertEquals(new Long(0), rows.get(0).getInteger(7));
        assertEquals("ClassifierModel", rows.get(0).getRowMeta().getFieldNames()[7]);
    }

}
