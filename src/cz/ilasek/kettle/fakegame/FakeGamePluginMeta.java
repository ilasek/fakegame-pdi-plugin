package cz.ilasek.kettle.fakegame;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

/*
 * Created on 02-jun-2003
 *
 */

public class FakeGamePluginMeta extends BaseStepMeta implements StepMetaInterface
{
    private ModelsFacade evaluableSet;
    private String modelsFileName;
    private String serializedModels;

	public FakeGamePluginMeta()
	{
		super(); // allocate BaseStepInfo
	}
	
	public ModelsFacade getModels()
	{
	    return evaluableSet;
	}
	
	public String getModelFileName()
	{
	    return modelsFileName;
	}
	
	public String getSerializedModels()
	{
	    return serializedModels;
	}
	
	public void setModelsFileName(String modelsFileName) throws KettleStepException
	{
	    this.modelsFileName = modelsFileName;
	    if (modelsFileName != null)
	    {
    	    StringBuilder serializedModel = new StringBuilder();
    	    
            try {
                FileInputStream modelFile = new FileInputStream(new File(modelsFileName));
    	    
        	    int len;
        	    byte [] readBytes = new byte[512];
        	    while ((len = modelFile.read(readBytes)) > 0)
        	    {
        	        serializedModel.append(new String(readBytes,  0, len));
        	    }
        	    setSerializedModels(serializedModel.toString());
            } catch (FileNotFoundException e) {
                throw new KettleStepException("File " + modelsFileName + " not found.");
            } catch (IOException e) {
                throw new KettleStepException("Problem reading file " + modelsFileName);
            }
	    } else {
	        setSerializedModels(null);
	    }
	}
	
	public void setSerializedModels(String serializedModels) throws KettleStepException
	{
	    this.serializedModels = serializedModels;
	    
	    if (serializedModels != null)
	    {
            ModelsFacade models = new ModelsFacade();
//            models.loadModels(new DataInputStream(new ByteArrayInputStream(serializedModels.getBytes())));
            models.loadClassifiers(new DataInputStream(new ByteArrayInputStream(serializedModels.getBytes())));

            setModels(models);
	    } 
	    else 
	    {
	        setModels(null);
	    }
	}
	
	private void setModels(ModelsFacade evaluableSet)
	{
	    this.evaluableSet = evaluableSet;
	}

	// TODO
	public String getXML()
	{
		String retval = "";
		
//		retval+="    <values>"+Const.CR;
//		if (value!=null)
//		{
//			retval+=value.getXML();
//		}
//		retval+="      </values>"+Const.CR;

		return retval;
	}

	  /**
	   * Generates row meta data to represent
	   * the fields output by this step
	   *
	   * @param row the meta data for the output produced
	   * @param origin the name of the step to be used as the origin
	   * @param info The input rows metadata that enters the step through 
	   * the specified channels in the same order as in method getInfoSteps(). 
	   * The step metadata can then choose what to do with it: ignore it or not.
	   * @param nextStep if this is a non-null value, it's the next step in 
	   * the transformation. The one who's asking, the step where the data is 
	   * targetted towards.
	   * @param space not sure what this is :-)
	   * @exception KettleStepException if an error occurs
	   */
	public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
	{
	    ValueMetaInterface resultMeta = new ValueMeta("Evaluation result", ValueMetaInterface.TYPE_NUMBER);
	    
	    resultMeta.setLength(12);
        resultMeta.setPrecision(4);
        resultMeta.setConversionMask("0.00");
	    resultMeta.setOrigin(origin);
	    
	    r.addValueMeta(resultMeta);
	}

	// TODO
	public Object clone()
	{
		Object retval = super.clone();
		return retval;
	}

	// TODO
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String,Counter> counters)
		throws KettleXMLException
	{
//		try
//		{
//			value = new ValueMetaAndData();
//			
//			Node valnode  = XMLHandler.getSubNode(stepnode, "values", "value");
//			if (valnode!=null)
//			{
//				System.out.println("reading value in "+valnode);
//				value.loadXML(valnode);
//			}
//		}
//		catch(Exception e)
//		{
//			throw new KettleXMLException("Unable to read step info from XML node", e);
//		}
	}

    /**
     * Check for equality
     * 
     * @param obj
     *            an <code>Object</code> to compare with
     * @return true if equal to the supplied object
     */
    public boolean equals(Object obj) {
        if (obj != null && (obj.getClass().equals(this.getClass()))) {
            FakeGamePluginMeta m = (FakeGamePluginMeta) obj;
            return (getXML() == m.getXML());
        }

        return false;
    }	
	
    /**
     * Hash code method
     * 
     * @return the hash code for this object
     */
    public int hashCode() {
        return getXML().hashCode();
    }	
	
	public void setDefault()
	{
	    modelsFileName = null;
	}

	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String,Counter> counters) throws KettleException
	{
	    // TODO
//		try
//		{
//			String name      =      rep.getStepAttributeString (id_step, 0, "value_name");
//			String typedesc  =      rep.getStepAttributeString (id_step, 0, "value_type");
//			String text      =      rep.getStepAttributeString (id_step, 0, "value_text");
//			boolean isnull   =      rep.getStepAttributeBoolean(id_step, 0, "value_null");
//			int length       = (int)rep.getStepAttributeInteger(id_step, 0, "value_length");
//			int precision    = (int)rep.getStepAttributeInteger(id_step, 0, "value_precision");
//			
//			int type = ValueMeta.getType(typedesc);
//			value = new ValueMetaAndData(new ValueMeta(name, type), null);
//			value.getValueMeta().setLength(length);
//            value.getValueMeta().setPrecision(precision);
//			
//			if (isnull) 
//			{
//				value.setValueData(null);
//			}
//			else
//			{
//                ValueMetaInterface stringMeta = new ValueMeta(name, ValueMetaInterface.TYPE_STRING);
//				if (type!=ValueMetaInterface.TYPE_STRING) text=ValueDataUtil.trim(text);
//				value.setValueData( value.getValueMeta().convertData(stringMeta, text));
//			}
//		}
//		catch(KettleDatabaseException dbe)
//		{
//			throw new KettleException("error reading step with id_step="+id_step+" from the repository", dbe);
//		}
//		catch(Exception e)
//		{
//			throw new KettleException("Unexpected error reading step with id_step="+id_step+" from the repository", e);
//		}
	}
	

	public void saveRep(Repository rep, long id_transformation, long id_step) throws KettleException
	{
	    // TODO
//		try
//		{
//			rep.saveStepAttribute(id_transformation, id_step, "value_name", value.getValueMeta().getName());
//			rep.saveStepAttribute(id_transformation, id_step, 0, "value_type",      value.getValueMeta().getTypeDesc());
//			rep.saveStepAttribute(id_transformation, id_step, 0, "value_text",      value.getValueMeta().getString(value.getValueData()));
//			rep.saveStepAttribute(id_transformation, id_step, 0, "value_null",      value.getValueMeta().isNull(value.getValueData()));
//			rep.saveStepAttribute(id_transformation, id_step, 0, "value_length",    value.getValueMeta().getLength());
//			rep.saveStepAttribute(id_transformation, id_step, 0, "value_precision", value.getValueMeta().getPrecision());
//		}
//		catch(KettleDatabaseException dbe)
//		{
//			throw new KettleException("Unable to save step information to the repository, id_step="+id_step, dbe);
//		}
	}

    /**
     * Check the settings of this step and put findings in a remarks list.
     * 
     * @param remarks
     *            the list to put the remarks in. see
     *            <code>org.pentaho.di.core.CheckResult</code>
     * @param transmeta
     *            the transform meta data
     * @param stepMeta
     *            the step meta data
     * @param prev
     *            the fields coming from a previous step
     * @param input
     *            the input step names
     * @param output
     *            the output step names
     * @param info
     *            the fields that are used as information by the step
     */	
	public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info)
	{
		CheckResult cr;
		if (prev==null || prev.size()==0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!", stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is connected to previous one, receiving "+prev.size()+" fields", stepMeta);
			remarks.add(cr);
		}
		
		// See if we have input streams leading to this step!
		if (input.length>0)
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta);
			remarks.add(cr);
		}
		else
		{
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);
			remarks.add(cr);
		}
		
	    if (evaluableSet == null) {
            cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "Step does not have access to a usable evaluableSet!", stepMeta);
            remarks.add(cr);
	    }		
	}

    /**
     * Get the UI for this step.
     *
     * @param shell a <code>Shell</code> value
     * @param meta a <code>StepMetaInterface</code> value
     * @param transMeta a <code>TransMeta</code> value
     * @param name a <code>String</code> value
     * @return a <code>StepDialogInterface</code> value
     */	
	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name)
	{
		return new FakeGamePluginDialog(shell, meta, transMeta, name);
	}

    /**
     * Get the executing step, needed by Trans to launch a step.
     *
     * @param stepMeta the step info
     * @param stepDataInterface the step data interface linked 
     * to this step. Here the step can store temporary data, 
     * database connections, etc.
     * @param cnr the copy number to get.
     * @param tr the transformation info.
     * @param trans the launching transformation
     * @return a <code>StepInterface</code> value
     */	
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp)
	{
		return new FakeGamePlugin(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}
    
    /**
     * Get a new instance of the appropriate data class. This 
     * data class implements the StepDataInterface. It basically 
     * contains the persisting data that needs to live on, even 
     * if a worker thread is terminated.
     *
     * @return a <code>StepDataInterface</code> value
     */	
	public StepDataInterface getStepData()
	{
		return new FakeGamePluginData();
	}

    @Override
    public void readRep(Repository arg0, ObjectId arg1,
            List<DatabaseMeta> arg2, Map<String, Counter> arg3)
            throws KettleException {
        // TODO To implement
    }

    @Override
    public void saveRep(Repository arg0, ObjectId arg1, ObjectId arg2)
            throws KettleException {
        // TODO to implement
    }
}
