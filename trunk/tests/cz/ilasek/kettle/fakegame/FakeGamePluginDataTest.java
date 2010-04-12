/**
 * 
 */
package cz.ilasek.kettle.fakegame;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.row.RowMeta;

/**
 * @author ivo
 *
 */
public class FakeGamePluginDataTest {

    private RowMeta meta;
    private FakeGamePluginData data;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        meta = new RowMeta();
        data = new FakeGamePluginData();
    }

    /**
     * Test method for {@link cz.ilasek.kettle.fakegame.FakeGamePluginData#setOutputRowMeta(org.pentaho.di.core.row.RowMetaInterface)}.
     */
    @Test
    public void testSetOutputRowMeta() {
        data.setOutputRowMeta(meta);
        assertEquals(meta, data.getOutputRowMeta());
    }
}
