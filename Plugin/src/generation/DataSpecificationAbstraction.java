package generation;

import java.util.Iterator;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.RelatedCharacteristics;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.Context;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristic;

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
    		if(rc.getRelatedEntity() == dpc) {    			
    			cc = rc.getCharacteristics(); 
    			//TODO more than 1 possible? for each match
    			break;
    		}
    	}
    	//TODO catch null
		return cc;
	}
	
	public CharacteristicContainer getCharacteristicContainerByName(String name) {
    	for (CharacteristicContainer cc : dataSpec.getCharacteristicContainer()) {
    		if(cc.getEntityName().equalsIgnoreCase(name))
    		{
    			return cc;
    		}
    	}
		return null;		
	}

	public EList<ContextCharacteristic> getContextCharacteristic(CharacteristicContainer cc) {		
		EList<ContextCharacteristic> list = new BasicEList<>();
		for (Characteristic c : cc.getOwnedCharacteristics()) { 		
			if(c instanceof ContextCharacteristic) {
				list.add((ContextCharacteristic) c);
			}
    	}
		return list;
	}
}
