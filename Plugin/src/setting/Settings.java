package setting;

public class Settings {
    private boolean createContextCharacteristic;
    private ContextMaster contextMaster;
    private boolean applyStereotype;
    private boolean saveChanges;

    public Settings(boolean createContextCharacteristic, ContextMaster contextMaster, boolean applyStereotype,
            boolean saveChanges) {
        super();
        this.createContextCharacteristic = createContextCharacteristic;
        this.contextMaster = contextMaster;
        this.applyStereotype = applyStereotype;
        this.saveChanges = saveChanges;
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

    public boolean isSaveChanges() {
        return saveChanges;
    }
}
