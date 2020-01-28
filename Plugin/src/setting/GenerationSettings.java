package setting;

public class GenerationSettings {
    private boolean createContextCharacteristic;
    private ContextMaster contextMaster;
    private boolean applyStereotype;

    public GenerationSettings(boolean createContextCharacteristic, ContextMaster contextMaster,
            boolean applyStereotype) {
        super();
        this.createContextCharacteristic = createContextCharacteristic;
        this.contextMaster = contextMaster;
        this.applyStereotype = applyStereotype;
    }

    public boolean isCreateContextCharacteristic() {
        return createContextCharacteristic;
    }

    public ContextMaster getContextMaster() {
        return contextMaster;
    }

    public boolean isApplyStereotype() {
        return applyStereotype;
    }
}
