package generation;

public class GenerationSettings {
    private boolean createContextCharacteristic;

    public GenerationSettings(boolean createContextCharacteristic) {
        super();
        this.createContextCharacteristic = createContextCharacteristic;
    }

    public boolean isCreateContextCharacteristic() {
        return createContextCharacteristic;
    }

    public void setCreateContextCharacteristic(boolean createContextCharacteristic) {
        this.createContextCharacteristic = createContextCharacteristic;
    }
}
