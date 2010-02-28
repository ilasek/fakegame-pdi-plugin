package cz.ilasek.kettle.fakegame;

import java.util.List;
import java.util.Map;

import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
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
import org.pentaho.di.trans.step.StepIOMetaInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.mergejoin.MergeJoinMeta;
import org.w3c.dom.Node;

/*
 * Created on 02-jun-2003
 *
 */

public class FakeGamePluginMeta extends BaseStepMeta implements StepMetaInterface
{
    private static Class<?> PKG = MergeJoinMeta.class; // for i18n purposes, needed by Translator2!!   $NON-NLS-1$
    
//	private ValueMetaAndData value;
    
    private final ValueMetaInterface resultMeta = new ValueMeta("Evaluation res", ValueMetaInterface.TYPE_NUMBER);
	
	public FakeGamePluginMeta()
	{
		super(); // allocate BaseStepInfo
		
        resultMeta.setLength(12);
        resultMeta.setPrecision(4);
        resultMeta.setConversionMask("0.00");
	}

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
	 * Prida do zahlavi sloupec pro vysledek resultu.
	 */
	public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space)
	{
	    resultMeta.setOrigin(origin);
	    r.addValueMeta(resultMeta);
	}

	public Object clone()
	{
		Object retval = super.clone();
		return retval;
	}

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

	public void setDefault()
	{
//		value = new ValueMetaAndData( new ValueMeta("MojeHodnota", ValueMetaInterface.TYPE_NUMBER), new Double(0) );
//		value.getValueMeta().setLength(12);
//        value.getValueMeta().setPrecision(4);
	}

	public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String,Counter> counters) throws KettleException
	{
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
	}
	
//	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name)
//	{
//		return new FakeGamePluginDialog(shell, meta, transMeta, name);
//	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp)
	{
		return new FakeGamePlugin(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

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
    
    /**
     * Returns the Input/Output metadata for this step.
     * The generator step only produces output, does not accept input!
     */
    public StepIOMetaInterface getStepIOMeta() {
        if (ioMeta==null) {

            ioMeta = super.getStepIOMeta();
        
//            ioMeta.addStream(new Stream(StreamType.INFO, BaseMessages.getString(PKG, "FakeGamePluginMeta.InfoStream.FirstStream.Description")));
//            ioMeta.addStream(new Stream(StreamType.INFO, BaseMessages.getString(PKG, "FakeGamePluginMeta.InfoStream.SecondStream.Description")));
        }
        
        return ioMeta;
    }    
}
