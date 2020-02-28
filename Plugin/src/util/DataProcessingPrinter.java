package util;

import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.EnumCharacteristicLiteral;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Enumeration;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.Context;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristic;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristicType;

/**
 * Helperclass to print DataProcessing Information
 * 
 * @author Thomas Lieb
 *
 */
public class DataProcessingPrinter {
    private DataSpecification dataSpecification;

    public DataProcessingPrinter(DataSpecification dataSpecification) {
        this.dataSpecification = dataSpecification;
    }

    /**
     * Prints information about CharacteristicContainer and CharacteristicTypeContainer
     */
    public void printDataProcessing() {
        Logger.infoDetailed("\n" + dataSpecification.toString());
        printCharacteristicContainer();
        printCharacteristicTypeContainer();
        Logger.infoDetailed("");
    }

    private void printCharacteristicContainer() {
        Logger.infoDetailed("Char: " + dataSpecification.getCharacteristicContainer().size());
        for (CharacteristicContainer cc : dataSpecification.getCharacteristicContainer()) {
            Logger.infoDetailed("Name:" + cc.getEntityName());
            for (Characteristic c : cc.getOwnedCharacteristics()) {
                Logger.infoDetailed("Type:" + c.getCharacteristicType());

                if (c instanceof ContextCharacteristic) {
                    for (Context context : ((ContextCharacteristic) c).getContext()) {
                        Logger.infoDetailed("Context:" + context.getEntityName());
                    }
                }
            }
        }
    }

    private void printCharacteristicTypeContainer() {
        Logger.infoDetailed("CharType: " + dataSpecification.getCharacteristicTypeContainers().size());
        for (CharacteristicTypeContainer ctc : dataSpecification.getCharacteristicTypeContainers()) {

            Logger.infoDetailed("Characteristics: " + ctc.getCharacteristicTypes().size());
            for (CharacteristicType ct : ctc.getCharacteristicTypes()) {
                Logger.infoDetailed(ct.getEntityName());

                if (ct instanceof ContextCharacteristicType) {
                    ContextCharacteristicType cct = (ContextCharacteristicType) ct;
                    for (Context context : (cct.getContext())) {
                        Logger.infoDetailed("Context:" + context.getEntityName());
                    }
                }
            }

            Logger.infoDetailed("Enumerations: " + ctc.getEnumerations().size());
            for (Enumeration enumer : ctc.getEnumerations()) {
                Logger.infoDetailed(enumer.getEntityName());
                for (EnumCharacteristicLiteral lit : enumer.getLiterals()) {
                    Logger.infoDetailed("Name: " + lit.getEntityName());
                }
            }
        }
    }
}
