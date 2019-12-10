package generation;

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

import sun.java2d.pipe.hw.ContextCapabilities;
import util.MyLogger;
import util.DataProcessingPrinter;

public class ContextHandler {
    private final DataSpecification dataSpec;
    public ContextHandler(final DataSpecification dataSpec) {
        this.dataSpec = dataSpec;
    }

	public void execute() {
		new DataProcessingPrinter(dataSpec).printDataProcessing();
				
		//TODO iterate	
		CharacteristicContainer cc = dataSpec.getCharacteristicContainer().get(0);
		Characteristic characteristic = cc.getOwnedCharacteristics().get(0);
		CharacteristicTypeContainer ctc = dataSpec.getCharacteristicTypeContainers().get(0);
		CharacteristicType ct = ctc.getCharacteristicTypes().get(1);
		
		if(characteristic instanceof ContextCharacteristic) {
			MyLogger.info("ContextCharacteristic");
			ContextCharacteristic conc = (ContextCharacteristic) characteristic;
			
			if(ct instanceof ContextCharacteristicType)
			{
				MyLogger.info("ContextCharacteristicType");
				conc.getContext().addAll(((ContextCharacteristicType) ct).getContext());
			}
		}

		new DataProcessingPrinter(dataSpec).printDataProcessing();
	}
}
