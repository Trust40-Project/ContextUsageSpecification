package generation;

import java.util.Collection;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characteristic;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicType;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicTypeContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.Characterizable;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.RelatedCharacteristics;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.Context;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristic;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristicType;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import util.MyLogger;
import util.DataProcessingPrinter;

public class ContextHandler {
	private final DataSpecificationAbstraction dataSpecAbs;
    private final UsageModel usageModel;
	private final Repository repo;
	private final System system;
    
    public ContextHandler(final DataSpecification dataSpec, final UsageModel usageModel, final Repository repo, final System system) {
        this.dataSpecAbs = new DataSpecificationAbstraction(dataSpec);
        this.usageModel = usageModel;
        this.repo = repo;
        this.system = system;
    }

    public void execute() {
    	//TODO select correct model, or make loop
    	UsageScenario us = usageModel.getUsageScenario_UsageModel().get(0);
    	ScenarioBehaviour sb = us.getScenarioBehaviour_UsageScenario();
    	MyLogger.info(sb.getEntityName());    
    	
    	//Iterate applied stereotypes, look for Characterizable
    	for (Stereotype stereotype : StereotypeAPI.getAppliedStereotypes(sb)) {
        	MyLogger.info(stereotype.getName());    
        	
        	//TODO proper cast or check to Characterizable
			if((stereotype.getName().equals("Characterizable"))) {
		    	for (EStructuralFeature esf : StereotypeAPI.getParameters(stereotype)) {
		    		String name = esf.getName();
		        	Object obj = StereotypeAPI.getTaggedValue(sb, name, stereotype.getName());
		        	if(obj instanceof CharacteristicContainer ) {
		            	CharacteristicContainer cc = (CharacteristicContainer) obj;
		    	    	
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
		    	    	
		    	    	applyContextToAllSystemCalls(cc, listOfSystemCalls);
		            	
		        	} else {
		        		MyLogger.error("CharacteristicContainer not selected");
		        	}
		    	}   
			} else {
        		MyLogger.error("Stereotype Characterizable not applied");
			}			
		}
    }
    
    public void applyContextToAllSystemCalls(CharacteristicContainer cc, EList<EntryLevelSystemCall> listOfSystemCalls) {   
		MyLogger.info("\nAppling Context to all methods");    	
    	for (EntryLevelSystemCall elsc : listOfSystemCalls) {	
    		MyLogger.info(elsc.getEntityName());     
    		OperationProvidedRole opr = elsc.getProvidedRole_EntryLevelSystemCall();
    		MyLogger.info(opr.getEntityName());    
    		OperationSignature op = elsc.getOperationSignature__EntryLevelSystemCall();
    		MyLogger.info(op.getEntityName());           		
    		
        	//Find Component by iterating connectors, check outer role with system call
    		//Still pass operation signature to know which function is called
        	for (Connector c : system.getConnectors__ComposedStructure()) {
        		MyLogger.info(c.getEntityName());
        		if(c instanceof ProvidedDelegationConnector) {
        			ProvidedDelegationConnector pdc = (ProvidedDelegationConnector) c;
        			if(pdc.getOuterProvidedRole_ProvidedDelegationConnector() == opr) {
                		MyLogger.info(pdc.getAssemblyContext_ProvidedDelegationConnector().getEntityName());
                		AssemblyContext ac = pdc.getAssemblyContext_ProvidedDelegationConnector();
                		RepositoryComponent rc = ac.getEncapsulatedComponent__AssemblyContext();
                		MyLogger.info(rc.getEntityName());
                		if(rc instanceof BasicComponent) {
                    		applyContextsToBasicComponent((BasicComponent)rc, op, cc);
                		}           else {
                			//TODO other cases
                    		MyLogger.error("TODO!!!");
                		}
        			}
        			else {
                		MyLogger.info("WRONG");
        			}
        		} else {
            		MyLogger.error("TODO2!!!");        			
        		}
        	}
    	}
    }
    
    //Seach for operationSignature in BasicComponent, and apply Contexts of CharacteristicsContainer
    public void applyContextsToBasicComponent(BasicComponent bc, OperationSignature op, CharacteristicContainer umcc) {		
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

        		    	for (Stereotype stereotype : StereotypeAPI.getAppliedStereotypes(ia)) {
            		    	MyLogger.info(stereotype.getName());
            	        	//TODO proper cast or check to DataProcessingSpecification
            				if((stereotype.getName().equals("DataProcessingSpecification"))) {
                		    	Collection<EStructuralFeature> list =  StereotypeAPI.getParameters(stereotype);
                		    	for (EStructuralFeature esf : list) {

                		    		String name = esf.getName();
                		        	MyLogger.info(name);
                		        	Object obj = StereotypeAPI.getTaggedValue(ia, name, stereotype.getName());
                		        	if(obj != null ) {
                		            	MyLogger.info(obj.getClass().getSimpleName());   
                		            	DataProcessingContainer dpc = (DataProcessingContainer) obj;
                		            	applyContexts(dpc, umcc);
                		        	}
                		        	else {
                                		MyLogger.error("Stereotype applied put no dataprocessing container selected!");
                		        	}
                		    	}
            				}
        		    	}
        			}    
        		}
        		
    		}
    	}
    }
    
    //Apply Contexts to CharacteristicContainer related to dpc, from reference CharacteristicContainer cc
	public void applyContexts(DataProcessingContainer dpc, CharacteristicContainer cc) {
		MyLogger.info("\nApply Context");   
		new DataProcessingPrinter(dataSpecAbs.getDataSpec()).printDataProcessing();
		
		//Get cc from dpc
		CharacteristicContainer cc2 = dataSpecAbs.getCharacteristicContainerForDataProcessingContainer(dpc);
		
		//Iterate all context, apply each to dpc
    	for (ContextCharacteristic c : dataSpecAbs.getContextCharacteristic(cc)) {
        	for (ContextCharacteristic c2 : dataSpecAbs.getContextCharacteristic(cc2)) {
        		//TODO add filter for "correct" contexttype?
        		c2.getContext().addAll(c.getContext());
        		MyLogger.info2("Apply:" + cc.getEntityName() + " to " + cc2.getEntityName());
        		MyLogger.info2("Contexts applied:" + c.getContext().toString());
        		MyLogger.info2("Contexts after:" + c2.getContext().toString());
        	}
    	}    	
		new DataProcessingPrinter(dataSpecAbs.getDataSpec()).printDataProcessing();
	}
}
