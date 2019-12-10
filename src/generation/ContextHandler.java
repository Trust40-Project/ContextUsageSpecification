package generation;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.impl.CharacteristicContainerImpl;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.Context;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristic;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristicType;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import util.MyLogger;
import util.DataProcessingPrinter;

public class ContextHandler {
    private final DataSpecification dataSpec;
    private final UsageModel usageModel;
    
    public ContextHandler(final DataSpecification dataSpec, final UsageModel usageModel) {
        this.dataSpec = dataSpec;
        this.usageModel = usageModel;
    }

    public void execute() {
    	UsageScenario us = usageModel.getUsageScenario_UsageModel().get(0);
    	ScenarioBehaviour sb = us.getScenarioBehaviour_UsageScenario();
    	MyLogger.info(sb.getEntityName());    
    	
    	EList<Stereotype> stl = StereotypeAPI.getApplicableStereotypes(sb);
    	MyLogger.info("Size:" + stl.size());
    	Stereotype st = stl.get(0);
    	MyLogger.info(st.getName());

    	EList<Stereotype> stl2 = StereotypeAPI.getAppliedStereotypes(sb);
    	MyLogger.info("Size2:" + stl2.size());
    	Stereotype st2 = stl2.get(0);
    	MyLogger.info(st2.getName());
    	 	
    	
    	Collection<EStructuralFeature> list =  StereotypeAPI.getParameters(st2);
    	for (EStructuralFeature esf : list) {
    		String name = esf.getName();
        	MyLogger.info(name);
        	Object obj = StereotypeAPI.getTaggedValue(sb, name, st2.getName());
        	if(obj != null ) {
            	MyLogger.info(obj.getClass().getSimpleName());   
            	CharacteristicContainer cc = (CharacteristicContainerImpl) obj;
    			MyLogger.info("Name:" + cc.getEntityName());
    	    	for (Characteristic c : cc.getOwnedCharacteristics()) {
    				MyLogger.info("Type:" + c.getCharacteristicType());
    				
    				if(c instanceof ContextCharacteristic) {
    			    	for (Context context : ((ContextCharacteristic) c).getContext()) {
    						MyLogger.info("Context:" + context.getEntityName());
    			    	}					
    				}
    	    	}
            	
        	}     else {
        		MyLogger.info("null");
        	}
    	}    	
    }
    
	public void executeDummy() {
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
