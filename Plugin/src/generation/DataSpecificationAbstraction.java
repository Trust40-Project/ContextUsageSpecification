package generation;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicsFactory;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.RelatedCharacteristics;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.ProcessingFactory;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.Context;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristic;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextFactory;

import util.MyLogger;

public class DataSpecificationAbstraction {
    private final DataSpecification dataSpec;

    public DataSpecificationAbstraction(final DataSpecification dataSpec) {
        this.dataSpec = dataSpec;
    }

    public DataSpecification getDataSpec() {
        return dataSpec;
    }

    public CharacteristicContainer getCharacteristicContainerForDataProcessingContainer(DataProcessingContainer dpc) {
        CharacteristicContainer cc = null;
        for (RelatedCharacteristics rc : dataSpec.getRelatedCharacteristics()) {
            if (rc.getRelatedEntity() == dpc) {
                cc = rc.getCharacteristics();
                break;
            }

            if (rc.getRelatedEntity().getId().equalsIgnoreCase(dpc.getId())) {
                cc = rc.getCharacteristics();
                break;
            }
        }
        return cc;
    }

    public CharacteristicContainer getCharacteristicContainerByName(String name) {
        for (CharacteristicContainer cc : dataSpec.getCharacteristicContainer()) {
            if (cc.getEntityName().equalsIgnoreCase(name)) {
                return cc;
            }
        }
        return null;
    }

    public EList<ContextCharacteristic> getContextCharacteristic(CharacteristicContainer cc) {
        EList<ContextCharacteristic> list = new BasicEList<>();
        for (Characteristic c : cc.getOwnedCharacteristics()) {
            if (c instanceof ContextCharacteristic) {
                list.add((ContextCharacteristic) c);
            }
        }
        return list;
    }

    public Context getContextByName(CharacteristicContainer cc, String name) {
        for (ContextCharacteristic c : getContextCharacteristic(cc)) {
            for (Context context : c.getContext()) {
                if (context.getEntityName().equalsIgnoreCase(name)) {
                    return context;
                }
            }
        }
        return null;
    }

    public void applyContext(ContextCharacteristic applyTo, ContextCharacteristic applyFrom) {
        applyTo.getContext().addAll(applyFrom.getContext());
        MyLogger.info2("Contexts applied:" + applyFrom.getContext().toString());
        MyLogger.info2("Contexts after:" + applyTo.getContext().toString());
    }

    public void createContextCharacteristic(CharacteristicContainer container,
            ContextCharacteristic characteristicData) {

        ContextCharacteristic newContextCharacteristic = ContextFactory.eINSTANCE.createContextCharacteristic();

        newContextCharacteristic.setCharacteristicType(characteristicData.getCharacteristicType());

        applyContext(newContextCharacteristic, characteristicData);

        container.getOwnedCharacteristics().add(newContextCharacteristic);
    }

    public DataProcessingContainer createNewCharacteristicPairForInternalAction(String name) {
        // Create new CharacteristicContainer
        CharacteristicContainer newCC = CharacteristicsFactory.eINSTANCE.createCharacteristicContainer();
        newCC.setEntityName(name);
        dataSpec.getCharacteristicContainer().add(newCC);

        // Create new DataContainer
        DataProcessingContainer newDPC = ProcessingFactory.eINSTANCE.createDataProcessingContainer();
        newDPC.setEntityName(name);
        dataSpec.getDataProcessingContainers().add(newDPC);

        // Create new RelatedCharacteristics and set attributes accordingly
        RelatedCharacteristics newRC = CharacteristicsFactory.eINSTANCE.createRelatedCharacteristics();
        newRC.setEntityName(name);
        newRC.setCharacteristics(newCC);
        newRC.setRelatedEntity(newDPC);
        dataSpec.getRelatedCharacteristics().add(newRC);

        return newDPC;
    }
}
