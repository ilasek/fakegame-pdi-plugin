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

/**
 * Class serves as a unified interface for classifiers and models from FAKE GAME.
 * 
 * @author Ivo Lasek
 *
 */
public class ModelsFacade {
    /** 
     * Signals that there is no suitable mappings of the inputs in transformation
     * to the inputs ofg models.
     */
    public final Integer NO_MAPPING_FOUND = null;
    
    private final Class<?> CLASSIFIER_OUTPUT_TYPE = Integer.class;
    private final Class<?> CLASSIFIER_PROBABILITY_OUTPUT_TYPE = Double.class;
    private final Class<?> MODEL_OUTPUT_TYPE = Double.class;
    
    /**
     * Classifiers hidden behind this facade.
     */
    private List<ConnectableClassifier> classifiers = new ArrayList<ConnectableClassifier>();
    /**
     * Models hidden behind this facade.
     */
    private List<ConnectableModel> models = new ArrayList<ConnectableModel>();
    
    /**
     * Show output probabilities as the output of classifiers or not (show the 
     * class instead).
     */
    private boolean showOutputProbabilities = false;
    
    /**
     * Inputs required by models and classifiers.
     */
    private ArrayList<String> inputs = new ArrayList<String>();
    
    /**
     * Generated mappings of the inputs.
     */
    private Integer[] mappings;
    
    private Logger logger = Logger.getLogger(ModelsFacade.class);

    /**
     * Loads serialized classifiers.
     * 
     * @param config Stream containing serialized classifiers.
     */
    public void loadClassifiers(DataInputStream config)
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.setClassLoader(Classifiers.class.getClassLoader());
        classifiers = (ArrayList<ConnectableClassifier>) xstream.fromXML(config);
        
        loadInputs();
    }
    
    /**
     * Loads serialized models.
     * 
     * @param config Stream containing serialized models.
     */    
    public void loadModels(DataInputStream config)
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.setClassLoader(Classifiers.class.getClassLoader());
        models = (ArrayList<ConnectableModel>) xstream.fromXML(config);
        
        loadInputs();
    }
    
    /**
     * Maps input of models to inputs coming from transformation to the plugin.
     * 
     * @param incommingHeader Inputs coming to the plugin. 
     */
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
    
    /**
     * Transform the inputs of plugin into a map, where key is a name of column
     * and value is its position.
     *  
     * @param incommingHeader
     * @return
     */
    public static Map<String, Integer> formHeaderMap(List<ValueMetaInterface> incommingHeader)
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int i = 0;
        for (ValueMetaInterface valueMeta : incommingHeader)
        {
            map.put(valueMeta.getName(), i);
            i++;
        }
        
        return map;
    }
    
    /**
     * Loads inputs of the models. Kind of input is determined by the first model 
     * or classifier. Inputs of all the classifiers must thus be the same. 
     * 
     * @return
     */
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
    
    /**
     * Returns position of models input in the incoming row.
     * 
     * @param inputIndex Index of a model input. 
     * @return Corresponding index in an incoming row. 
     */
    public Integer mapInputToRow(int inputIndex)
    {
        return mappings[inputIndex];
    }
    
    public ArrayList<String> getInputs()
    {
        return inputs;
    }
    
    /**
     * Returns names and types of the outputs of all stored models and classifiers.
     * @return
     */
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
    
    /**
     * Evaluates the input based on stored model.
     * 
     * @param inputRow
     * @param rowMeta
     * @return
     * @throws KettleValueException
     */
    public Object[] evaluate(Object[] inputRow, RowMetaInterface rowMeta)
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
                evaluationResult[i] = model.getOutput(translateRow(inputRow, rowMeta));

                i++;
            }            
        }
        
        return evaluationResult;
    }
    
    /**
     * Prepares row coming to the plugin for evaluation using a FAKE GAME model.
     * 
     * @param inputRow
     * @param rowMeta
     * @return
     */
    private double[] translateRow(Object[] inputRow, RowMetaInterface rowMeta)
    {
        double[] translatedRow = new double[mappings.length];
        
        for (int i = 0; i < mappings.length; i++)
        {
            int rowIndex = mapInputToRow(i);
            try {
                translatedRow[i] = rowMeta.getNumber(inputRow, rowIndex);
            } catch (KettleValueException e)
            {
                translatedRow[i] = Double.NaN;
            }
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