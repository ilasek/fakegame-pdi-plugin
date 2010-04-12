package cz.ilasek.kettle.fakegame;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

/*
 * Created on 2-jun-2003
 *
 */

public class FakeGamePlugin extends BaseStep implements StepInterface 
{
    private FakeGamePluginData data;
    private FakeGamePluginMeta meta;

    public FakeGamePlugin(StepMeta s, StepDataInterface stepDataInterface,
            int copyNr, TransMeta transMeta, Trans trans) 
    {
        super(s, stepDataInterface, copyNr, transMeta, trans);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
            throws KettleException {
        meta = (FakeGamePluginMeta) smi;
        data = (FakeGamePluginData) sdi;

        Object[] inputRow = getRow(); // get row, blocks when needed!

        if (inputRow == null) // no more input to be expected...
        {
            setOutputDone();
            return false;
        }

        if (first) 
        {
            firstRow();
        }
        
        Object [] outputRow = evalueateModel(inputRow);
        putRow(data.getOutputRowMeta(), outputRow);
            
        return true;
    }
    
    private void firstRow()
    {
        first = false;
        data.setOutputRowMeta(getInputRowMeta().clone());
        
        meta.getModels().generateMappings(getInputRowMeta().getValueMetaList());
        
        meta.getFields(data.getOutputRowMeta(), getStepname(), null, null, this);        
    }
    
    private Object[] evalueateModel(Object[] inputRow) throws KettleValueException
    {
        Object[] outputRow = RowDataUtil.resizeArray(inputRow, data.getOutputRowMeta().size());
        int resultIndex = getInputRowMeta().size();
        
        Object[] evaluationResults = meta.getModels().evaluate(inputRow, getInputRowMeta());
        
        for (Object result : evaluationResults)
        {
            outputRow[resultIndex] = result;
            resultIndex++;
        }
        
        return outputRow;
    }

    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (FakeGamePluginMeta) smi;
        data = (FakeGamePluginData) sdi;

        return super.init(smi, sdi);
    }

    //
    // Run is were the action happens!
    public void run() {
        logBasic("Starting to run...");
        try {
            while (processRow(meta, data) && !isStopped())
                ;
        } catch (Exception e) {
            logError("Unexpected error : " + e.toString());
            logError(Const.getStackTracker(e));
            setErrors(1);
            stopAll();
        } finally {
            dispose(meta, data);
            logBasic("Finished, processing " + getLinesRead() + " rows");
            markStop();
        }
    }
}
