package cz.ilasek.kettle.fakegame;

import game.classifiers.Classifiers;
import game.classifiers.ConnectableClassifier;
import game.models.ConnectableModel;
import game.models.OutputProducer;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class ModelsFacade {
    public final Integer NO_MAPPING_FOUND = null;
    
    private final Class<?> CLASSIFIER_OUTPUT_TYPE = Integer.class;
    private final Class<?> CLASSIFIER_PROBABILITY_OUTPUT_TYPE = Double.class;
    private final Class<?> MODEL_OUTPUT_TYPE = Double.class;
    
    private List<ConnectableClassifier> classifiers = new ArrayList<ConnectableClassifier>();
    private List<ConnectableModel> models = new ArrayList<ConnectableModel>();
    
    private boolean showOutputProbabilities = false;
    
    private ArrayList<String> inputs = new ArrayList<String>();
    
    private Integer[] mappings;
    
    private Logger logger = Logger.getLogger(ModelsFacade.class);

    public void loadClassifiers(DataInputStream config)
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.setClassLoader(Classifiers.class.getClassLoader());
        classifiers = (ArrayList<ConnectableClassifier>) xstream.fromXML(config);
        
        loadInputs();
    }
    
    public void loadModels(DataInputStream config)
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.setClassLoader(Classifiers.class.getClassLoader());
        models = (ArrayList<ConnectableModel>) xstream.fromXML(config);
        
        loadInputs();
    }
    
    public void generateMappings(List<ValueMetaInterface> incommingHeader)
    {
        mappings = new Integer[inputs.size()];
        Map<String, Integer> headerMap = formHeaderMap(incommingHeader);
        
        for (int i  = 0; i < inputs.size(); i++)
        {
            Integer indexInHeader = headerMap.get(inputs.get(i));
            mappings[i] = indexInHeader;
        }
    }
    
    public static Map<String, Integer> formHeaderMap(List<ValueMetaInterface> incommingHeader)
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int i = 0;
        for (ValueMetaInterface valueMeta : incommingHeader)
        {
            if (valueMeta.isNumeric())
            {
                map.put(valueMeta.getName(), i);
            }
            i++;
        }
        
        return map;
    }
    
    private ArrayList<String> loadInputs()
    {
        Vector<OutputProducer> inputsVector = new Vector<OutputProducer>();
        
        if (!models.isEmpty())
        {
            inputsVector = models.get(0).getInputs();
        }
        else if (!classifiers.isEmpty())
        {
            inputsVector = classifiers.get(0).getInputs();
        }
        
        if (!inputsVector.isEmpty())
        {
            for (OutputProducer input : inputsVector)
            {
                inputs.add(input.getName());
            }
        }
        
        return inputs;
    }
    
    public Integer mapInputToRow(int inputIndex)
    {
        return mappings[inputIndex];
    }
    
    public ArrayList<String> getInputs()
    {
        return inputs;
    }
    
    public List<ModelSignature> getModelsSignatures()
    {
        List<ModelSignature> modelSignatures = new LinkedList<ModelSignature>();

        for (ConnectableClassifier classifier : classifiers)
        {
            String classifierClass = classifier.getClassifier().getClass().getSimpleName();
            
            if (showOutputProbabilities)
            {
                int outputProbabilitiesCount = classifier.getOutputProbabilities().length;
                for (int i = 0; i < outputProbabilitiesCount; i++)
                {
                    modelSignatures.add(new ModelSignature(classifierClass + "_" + i, CLASSIFIER_PROBABILITY_OUTPUT_TYPE));
                }
            }
            else
                modelSignatures.add(new ModelSignature(classifierClass, CLASSIFIER_OUTPUT_TYPE));
        }
        
        for (ConnectableModel model : models)
        {
            modelSignatures.add(new ModelSignature(
                    model.getModel().getClass().getSimpleName() + "_" + model.getName(), 
                    MODEL_OUTPUT_TYPE));
        }
        
        return modelSignatures;
    }
    
    public Object[] evaluate(Object[] inputRow, RowMetaInterface rowMeta) throws KettleValueException
    {
        int totalResultsCount = getModelsSignatures().size();
        Object[] evaluationResult = new Object[totalResultsCount];
        
        if (totalResultsCount > 0)
        {
            int i = 0;
            for (ConnectableClassifier classifier : classifiers)
            {
                if (showOutputProbabilities)
                {
                    double[] outputProbabilties = classifier.getOutputProbabilities(translateRow(inputRow, rowMeta));
                    for (double outputProbability : outputProbabilties)
                    {
                        evaluationResult[i] = new Double(outputProbability);
                        i++;
                    }
                }
                else
                {
                    evaluationResult[i] = new Integer(classifier.getOutput(translateRow(inputRow, rowMeta))).longValue();
                    i++;
                }
            }
            for (ConnectableModel model : models)
            {
                try {
                    evaluationResult[i] = model.getOutput(translateRow(inputRow, rowMeta));
                } catch (KettleValueException e) {
                    evaluationResult[i] = null;
                    logger.error(Messages.getString("ModelsFacade.Log.KettleValuenError"));
                }
                i++;
            }            
        }
        
        return evaluationResult;
    }
    
    private double[] translateRow(Object[] inputRow, RowMetaInterface rowMeta) throws KettleValueException
    {
        double[] translatedRow = new double[mappings.length];
        
        for (int i = 0; i < mappings.length; i++)
        {
            translatedRow[i] = rowMeta.getNumber(inputRow, mapInputToRow(i));
        }
        
        return translatedRow;
    }

    /**
     * @param showOutputProbabilities the showOutputProbabilities to set
     */
    public void setShowOutputProbabilities(boolean showOutputProbabilities) {
        this.showOutputProbabilities = showOutputProbabilities;
    }

    /**
     * @return the showOutputProbabilities
     */
    public boolean isShowOutputProbabilities() {
        return showOutputProbabilities;
    }
}                                                                                                                                                                         