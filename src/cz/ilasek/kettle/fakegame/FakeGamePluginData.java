/*
 *
 *
 */

package cz.ilasek.kettle.fakegame;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * 
 * 
 * @author Ivo Lasek
 */
public class FakeGamePluginData extends BaseStepData implements StepDataInterface
{
	private RowMetaInterface outputRowMeta;
	
    public void setOutputRowMeta(RowMetaInterface rmi)
    {
        outputRowMeta = rmi;
    }
    
    /**
     * Get the meta data for the output format
     *
     * @return a <code>RowMetaInterface</code> value
     */    
    public RowMetaInterface getOutputRowMeta()
    {
        return outputRowMeta;
    }
}
