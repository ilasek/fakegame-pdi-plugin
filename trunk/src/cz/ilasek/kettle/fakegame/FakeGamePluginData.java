/*
 *
 *
 */

package cz.ilasek.kettle.fakegame;

import org.pentaho.di.core.RowSet;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * 
 * 
 * @author Matt
 * @since  24-mrt-2005
 */
public class FakeGamePluginData extends BaseStepData implements StepDataInterface
{
	private RowMetaInterface outputRowMeta;
	
	public RowSet learnRowSet;
	public RowSet workingRowset;
	
	public Object[] learnRow;
	public Object[] workingRow;

    public FakeGamePluginData()
	{
		super();
	}
    
    public void setOutputRowMeta(FakeGamePluginMeta meta, RowMetaInterface inputRowMeta,
            String stepName, VariableSpace space)
    {
//        outputRowMeta = inputRowMeta.clone();
        outputRowMeta = new RowMeta();
        meta.getFields(outputRowMeta, stepName, null, null, space);        
    }
    
    public RowMetaInterface getOutputRowMeta()
    {
        return outputRowMeta;
    }
}
