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

public class DataProcessingPrinter {
	private DataSpecification dataSpecification;
	
	public DataProcessingPrinter(DataSpecification dataSpecification) {
		this.dataSpecification = dataSpecification;
	}
	
	public void printDataProcessing() {  		
        MyLogger.info("\n" + dataSpecification.toString());      
        printCharacteristicContainer();
        printCharacteristicTypeContainer(); 
        MyLogger.info("");
	}

	private void printCharacteristicContainer() {  
        MyLogger.info("Char: " + dataSpecification.getCharacteristicContainer().size());
    	for (CharacteristicContainer cc : dataSpecification.getCharacteristicContainer()) {
			MyLogger.info("Name:" + cc.getEntityName());
	    	for (Characteristic c : cc.getOwnedCharacteristics()) {
				MyLogger.info("Type:" + c.getCharacteristicType());
				
				if(c instanceof ContextCharacteristic) {
			    	for (Context context : ((ContextCharacteristic) c).getContext()) {
						MyLogger.info("Context:" + context.getEntityName());
			    	}					
				}
	    	}
		}		
	}

	private void printCharacteristicTypeContainer() {      	
        MyLogger.info("CharType: " + dataSpecification.getCharacteristicTypeContainers().size());
    	for (CharacteristicTypeContainer ctc : dataSpecification.getCharacteristicTypeContainers()) {

			MyLogger.info("Characteristics: " + ctc.getCharacteristicTypes().size());	
        	for (CharacteristicType ct : ctc.getCharacteristicTypes()) {
				MyLogger.info(ct.getEntityName());	
				
				if(ct instanceof ContextCharacteristicType) {
					ContextCharacteristicType cct = (ContextCharacteristicType) ct;
			    	for (Context context : (cct.getContext())) {
						MyLogger.info("Context:" + context.getEntityName());
			    	}					
				}
			}	
        	
			MyLogger.info("Enumerations: " + ctc.getEnumerations().size());	
        	for (Enumeration enumer : ctc.getEnumerations()) {
				MyLogger.info(enumer.getEntityName());	
	        	for (EnumCharacteristicLiteral lit : enumer.getLiterals()) {
					MyLogger.info("Name: " + lit.getEntityName());	
				}
			}		
		}  			
	}
}
