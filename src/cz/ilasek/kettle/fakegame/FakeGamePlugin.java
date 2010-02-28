package cz.ilasek.kettle.fakegame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.steps.mergejoin.MergeJoinMeta;

/*
 * Created on 2-jun-2003
 *
 */

public class FakeGamePlugin extends BaseStep implements StepInterface 
{
    private static final File TMP_FILE = new File("plugins/steps/FakeGamePlugin/tmp/data.txt");
    private static Class<?> PKG = MergeJoinMeta.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$
    
    private FakeGamePluginData data;
    private FakeGamePluginMeta meta;
    private FileWriter fileWriter;
    

    public FakeGamePlugin(StepMeta s, StepDataInterface stepDataInterface,
            int copyNr, TransMeta transMeta, Trans trans) 
    {
        super(s, stepDataInterface, copyNr, transMeta, trans);
    }

    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi)
            throws KettleException {
        meta = (FakeGamePluginMeta) smi;
        data = (FakeGamePluginData) sdi;

//        Object[] row = getRow(); // get row, blocks when needed!
//
//        if (row == null) // no more input to be expected...
//        {
//            try {
//                fileWriter.close();
//            } catch (IOException e) {
//                System.err.println("Unable to close file " + TMP_FILE);
//                e.printStackTrace();
//            }
//            
//            classifyData();
//            setOutputDone();
//            return false;
//        }

        ////////////////////////////
        // Zpracovani dvou streamu
        if (first) 
        {
            List<StreamInterface> infoStreams = meta.getStepIOMeta().getInfoStreams();
            data.learnRowSet = findInputRowSet(infoStreams.get(0).getStepname());
            if (data.learnRowSet==null) {
                throw new KettleException( BaseMessages.getString(PKG, "FakeGamePlugin.Exception.UnableToFindSpecifiedStep", infoStreams.get(0).getStepname()) );
            }
            data.workingRowset = findInputRowSet(infoStreams.get(1).getStepname());
            if (data.workingRowset==null) {
                throw new KettleException( BaseMessages.getString(PKG, "FakeGamePlugin.Exception.UnableToFindSpecifiedStep", infoStreams.get(1).getStepname()) );
            }
            
//            writeLineIntoFile(getInputRowMeta().getFieldNames());
            first = false;
//            data.setOutputRowMeta(meta,  getInputRowMeta(), getStepname(), this);
        }
        //////////////////////////////
        
        data.learnRow = getRowFrom(data.learnRowSet);
        
        if (data.learnRow == null)
        {
            data.workingRow = getRowFrom(data.workingRowset);
            if (data.workingRow == null)
            {
                setOutputDone();
                return false;
            }
        }

        ///////////////////////////////
        // Cteni dat ulozenych v radce
        RowMetaInterface learnRowMeta = data.learnRowSet.getRowMeta();
        ValueMetaInterface v = learnRowMeta.getValueMeta(0);
        Object valueData = data.learnRow[0];
        String rowCol1 = v.getString(valueData);
        ///////////////////////////////
//        writeLineIntoFile(row);

        addResultToRow(0.1);
        
        return true;
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
            logBasic("Finished, processing " + linesRead + " rows");
            markStop();
        }
    }

    /**
     * Prida ohodnoceni instance do vysledne radky.
     * 
     * @param row
     *            Vstupni radka reprezentujici instanci.
     * @param result
     *            Vysledek ohodnoceni teto instance.
     * @throws KettleStepException
     */
    private void addResultToRow(Double result)
            throws KettleStepException {
        
        Object[] outputRow = new Object[1];
        outputRow[0] = result;
        
        putRow(data.getOutputRowMeta(), outputRow);
    }
}
