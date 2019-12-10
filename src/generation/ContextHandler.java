package generation;

import java.util.Objects;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.EnumCharacteristicLiteral;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Enumeration;

import util.MyLogger;

public class ContextHandler {
    private final String id;
    private final DataSpecification dataContainer;
	private final MyLogger logger;

    public ContextHandler(final String id, final DataSpecification dataSpec, MyLogger logger) {
        this.id = Objects.requireNonNull(id);
        this.dataContainer = dataSpec;
        this.logger = logger;
    }

	public void printInfo() {        
		Characteristic myC = null;
		CharacteristicType myT = null;
		
        logger.info(dataContainer.toString());
        
        logger.info("Char: " + dataContainer.getCharacteristicContainer().size());
    	for (CharacteristicContainer cc : dataContainer.getCharacteristicContainer()) {
			logger.info("Name:" + cc.getEntityName());
			logger.info("Id:" + cc.getId());
	    	for (Characteristic c : cc.getOwnedCharacteristics()) {
				logger.info("Type:" + c.getCharacteristicType());
				logger.info("Id:" + c.getId());
				
				myC= c;
	    	}
		}
    	
    	logger.info("");
    	
        logger.info("CharType: " + dataContainer.getCharacteristicTypeContainers().size());
    	for (CharacteristicTypeContainer ctc : dataContainer.getCharacteristicTypeContainers()) {

			logger.info("Id: " + ctc.getId());
			logger.info("Characteristics: " + ctc.getCharacteristicTypes().size());	
        	for (CharacteristicType ct : ctc.getCharacteristicTypes()) {
				logger.info(ct.getEntityName());		
				logger.info("Id: " + ct.getId());	
				
				myT = ct;
			}	
        	
			logger.info("Enumerations: " + ctc.getEnumerations().size());	
        	for (Enumeration enumer : ctc.getEnumerations()) {
				logger.info(enumer.getEntityName());	
	        	for (EnumCharacteristicLiteral lit : enumer.getLiterals()) {
					logger.info("Name: " + lit.getEntityName());	
					logger.info("Id: " + lit.getId());	
				}
			}		
		}  

    	logger.info("");

		logger.info("OldType:" + myC.getCharacteristicType());
		logger.info("SetType:" + myT);		
    	myC.setCharacteristicType(myT);  
		logger.info("NewType:" + myC.getCharacteristicType());  	    	
	}
}
