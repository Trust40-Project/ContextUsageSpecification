package generation;

import java.util.Collection;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.RelatedCharacteristics;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.impl.CharacteristicContainerImpl;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.Context;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristic;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristicType;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingInternalBehaviour;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import util.MyLogger;
import util.DataProcessingPrinter;

public class ContextHandler {
    private final DataSpecification dataSpec;
    private final UsageModel usageModel;
	private final Repository repo;
    
    public ContextHandler(final DataSpecification dataSpec, final UsageModel usageModel, final Repository repo) {
        this.dataSpec = dataSpec;
        this.usageModel = usageModel;
        this.repo = repo;
    }

    public void execute() {
    	UsageScenario us = usageModel.getUsageScenario_UsageModel().get(0);
    	ScenarioBehaviour sb = us.getScenarioBehaviour_UsageScenario();
    	MyLogger.info(sb.getEntityName());    
    	
    	//TODO catch exception no stereotype applied
    	//TODO get correct applied stereotype with instance of, also for all other similar cases
    	EList<Stereotype> stl = StereotypeAPI.getApplicableStereotypes(sb);
    	MyLogger.info("Size:" + stl.size());
    	Stereotype st = stl.get(0);
    	MyLogger.info(st.getName());

    	EList<Stereotype> stl2 = StereotypeAPI.getAppliedStereotypes(sb);
    	MyLogger.info("Size2:" + stl2.size());
    	Stereotype st2 = stl2.get(0);
    	MyLogger.info(st2.getName());
    	
    	//Get list of all operations called inside usage model   
    	EList<EntryLevelSystemCall> listOfSystemCalls = new BasicEList<>();
    	for (AbstractUserAction aue : sb.getActions_ScenarioBehaviour()) {
			MyLogger.info("ActionName:" + aue.getEntityName());    		
			MyLogger.info("Class:" + aue.getClass());
			if(aue instanceof EntryLevelSystemCall) {
				EntryLevelSystemCall elsc = (EntryLevelSystemCall) aue;
				listOfSystemCalls.add(elsc);
			}
    	}
    	 	
    	//Get cc of usage model 
    	CharacteristicContainer umcc = null;
    	
    	Collection<EStructuralFeature> list =  StereotypeAPI.getParameters(st2);
    	for (EStructuralFeature esf : list) {
    		String name = esf.getName();
        	MyLogger.info(name);
        	Object obj = StereotypeAPI.getTaggedValue(sb, name, st2.getName());
        	if(obj != null ) {
            	MyLogger.info(obj.getClass().getSimpleName());   
            	CharacteristicContainer cc = (CharacteristicContainer) obj;
    			MyLogger.info("Name:" + cc.getEntityName());
    	    	for (Characteristic c : cc.getOwnedCharacteristics()) {
    				MyLogger.info("Type:" + c.getCharacteristicType());
    				
    				if(c instanceof ContextCharacteristic) {
    			    	for (Context context : ((ContextCharacteristic) c).getContext()) {
    						MyLogger.info("Context:" + context.getEntityName());
    			    	}					
    				}
    	    	}
    	    	
    	    	umcc = cc;
            	
        	}     else {
        		MyLogger.info("null");
        	}
    	}    
    	
    	
    	if(umcc != null) {    		
    		MyLogger.info("\nAppling Context to all methods");    	
        	for (EntryLevelSystemCall elsc : listOfSystemCalls) {	
        		MyLogger.info(elsc.getEntityName());     
        		OperationSignature op = elsc.getOperationSignature__EntryLevelSystemCall();
        		MyLogger.info(op.getEntityName());   
        		
        		//Find all matching internal actions
        		//First find matching operation signature in basic component
            	for (RepositoryComponent rc : repo.getComponents__Repository()) {
                	for (ProvidedRole pr : rc.getProvidedRoles_InterfaceProvidingEntity()) {
                		InterfaceProvidingEntity ipe = pr.getProvidingEntity_ProvidedRole();  
                		MyLogger.info(ipe.getEntityName());     
                		
                		//TODO instance of
                		BasicComponent bc = (BasicComponent) ipe;
                		
                    	for (ServiceEffectSpecification seff : bc.getServiceEffectSpecifications__BasicComponent()) {
                    		MyLogger.info(seff.getDescribedService__SEFF().getEntityName());   
                    		if(seff.getDescribedService__SEFF() == op) {
                        		MyLogger.info("MATCH");     
                        		
                        		//TODO instance of. other cases allowed?
                        		ResourceDemandingSEFF rdSeff = (ResourceDemandingSEFF) seff;
                        		
                        		//Get all internal actions, and check applied data processing
                        		for (AbstractAction aa : rdSeff.getSteps_Behaviour()) {
                        			if(aa instanceof InternalAction) {
                        				InternalAction ia = (InternalAction) aa;

                        		    	EList<Stereotype> stl3 = StereotypeAPI.getAppliedStereotypes(ia);
                        		    	Stereotype st3 = stl3.get(0);
                        		    	MyLogger.info(st3.getName());
                        		    	Collection<EStructuralFeature> list3 =  StereotypeAPI.getParameters(st3);
                        		    	for (EStructuralFeature esf : list3) {

                        		    		String name = esf.getName();
                        		        	MyLogger.info(name);
                        		        	Object obj = StereotypeAPI.getTaggedValue(ia, name, st3.getName());
                        		        	if(obj != null ) {
                        		            	MyLogger.info(obj.getClass().getSimpleName());   
                        		            	DataProcessingContainer dpc = (DataProcessingContainer) obj;
                        		            	applyContexts(dpc, umcc);
                        		        	}
                        		    	}
                        			}    
                        		}
                        		
                    		}
                    	}
                	}    
            	}
        	}
    	}
    }
    
	public void applyContexts(DataProcessingContainer dpc, CharacteristicContainer cc) {
		MyLogger.info("\nApply Context");   
		new DataProcessingPrinter(dataSpec).printDataProcessing();
		
		//Get cc from dpc
		CharacteristicContainer cc2 = null;
    	for (RelatedCharacteristics rc : dataSpec.getRelatedCharacteristics()) {
    		if(rc.getRelatedEntity() == dpc) {    			
    			cc2 = rc.getCharacteristics(); 
    			//TODO more than 1 possible? for each match
    			break;
    		}
    	}

		//Iterate all context, apply each to dpc
    	for (Characteristic c : cc.getOwnedCharacteristics()) {
    		//TODO get correct characteristic (what is correct?)    		
			if(c instanceof ContextCharacteristic) {
				EList<Context> cl =((ContextCharacteristic) c).getContext();	
				
				
				
				//TODO handle properly	
				if(cc2.getOwnedCharacteristics().get(0) instanceof ContextCharacteristic) {
					MyLogger.info("MATCH2"); 
					ContextCharacteristic ccc = (ContextCharacteristic) cc2.getOwnedCharacteristics().get(0);
					ccc.getContext().addAll(cl);
				}				
			}
    	}    	
		new DataProcessingPrinter(dataSpec).printDataProcessing();
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
