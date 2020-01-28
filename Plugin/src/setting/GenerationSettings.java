package setting;

public class GenerationSettings {
    private boolean createContextCharacteristic;
    private ContextMaster contextMaster;

    public GenerationSettings(boolean createContextCharacteristic, ContextMaster contextMaster) {
        super();
        this.createContextCharacteristic = createContextCharacteristic;
        this.contextMaster = contextMaster;
    }

    public boolean isCreateContextCharacteristic() {
        return createContextCharacteristic;
    }

    public ContextMaster getContextMaster() {
        return contextMaster;
    }
}
