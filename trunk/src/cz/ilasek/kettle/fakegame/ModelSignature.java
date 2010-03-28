package cz.ilasek.kettle.fakegame;

public class ModelSignature {
    private final String name;
    private final Class<?> outputType;
    
    public ModelSignature(String name, Class<?> outputType)
    {
        this.name = name;
        this.outputType = outputType;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the outputType
     */
    public Class<?> getOutputType() {
        return outputType;
    }

}
