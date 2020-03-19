package generation;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.ComposedStructure;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.core.composition.RequiredDelegationConnector;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristic;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.CompositeComponent;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import setting.Settings;
import util.DataProcessingPrinter;
import util.Logger;

/**
 * Contains all logic / functionality
 * 
 * Needs the 4 different models as input, calls the different abstraction classes for specific logic
 * 
 * @author Thomas Lieb
 *
 */
public class ContextHandler {
    private final Settings settings;
    private final DataSpecificationAbstraction dataSpecAbs;
    private final UsageModelAbstraction usageModelAbs;
    private final Repository repo;
    private final AssemblyAbstraction assemblyAbs;

    /**
     * Constructor
     * 
     * @param settings
     * @param dataSpec
     * @param usageModel
     * @param repo
     * @param system
     */
    public ContextHandler(final Settings settings, final DataSpecification dataSpec, final UsageModel usageModel,
            final Repository repo, final System system) {
        this.settings = settings;
        this.dataSpecAbs = new DataSpecificationAbstraction(dataSpec);
        this.usageModelAbs = new UsageModelAbstraction(usageModel, dataSpecAbs);
        this.repo = repo;
        this.assemblyAbs = new AssemblyAbstraction(system);
    }

    /**
     * Entrypoint for mainhandler
     */
    public void execute() {
    	for(ScenarioBehaviour scenarioBehaviour : usageModelAbs.getListofScenarioBehaviour()) {
            applyContextToAllSystemCalls(scenarioBehaviour);
    	}
    }

    /**
     * Iterate all SystemCalls, call applyContextToSystemCall for each one with matching
     * characteristicContainer according to settings
     * @param scenarioBehaviour 
     */
    private void applyContextToAllSystemCalls(ScenarioBehaviour scenarioBehaviour) {
        Logger.infoDetailed("\nAppling Context to all methods");

        CharacteristicContainer containerCharacterizable = usageModelAbs.getAppliedCharacterizableContainer(scenarioBehaviour);

        for (EntryLevelSystemCall systemCall : usageModelAbs.getListOfEntryLevelSystemCalls(scenarioBehaviour)) {
            CharacteristicContainer containerDataProcessing = usageModelAbs.getAppliedContainer(systemCall);

            // Depending on ContextMaster a different characteristicContainer is used to be applied
            switch (settings.getContextMaster()) {
            case Characterizable:
                if (containerCharacterizable != null) {
                    applyContextToSystemCall(systemCall, containerCharacterizable);
                }
                break;
            case DataProcessing:
                if (containerDataProcessing != null) {
                    applyContextToSystemCall(systemCall, containerDataProcessing);
                }
                break;
            case Combined:
                if (containerDataProcessing != null) {
                    applyContextToSystemCall(systemCall, containerDataProcessing);
                } else {
                    if (containerCharacterizable != null) {
                        applyContextToSystemCall(systemCall, containerCharacterizable);
                    }
                }
                break;
            default:
                break;
            }
        }
    }

    /**
     * Applies context from containerToApply to BasicComponent called by entryLevelSystemCall
     * 
     * @param entryLevelSystemCall
     * @param containerToApply
     */
    private void applyContextToSystemCall(EntryLevelSystemCall entryLevelSystemCall,
            CharacteristicContainer containerToApply) {
        Logger.infoDetailed("\nAppling Context to SystemCall: " + entryLevelSystemCall.getEntityName());
        Logger.infoDetailed(entryLevelSystemCall.getEntityName());
        OperationProvidedRole opr = entryLevelSystemCall.getProvidedRole_EntryLevelSystemCall();
        Logger.infoDetailed(opr.getEntityName());
        OperationSignature op = entryLevelSystemCall.getOperationSignature__EntryLevelSystemCall();
        Logger.infoDetailed(op.getEntityName());

        EList<AssemblyContext> hierarchy = new BasicEList<AssemblyContext>();
        applyContextsToComposedStructure(assemblyAbs.getSystem(), hierarchy, opr,op,containerToApply);
    }
    
    private void applyContextsToComposedStructure(ComposedStructure composedStructure,
            EList<AssemblyContext> hierarchy, OperationProvidedRole operationProvidedRole, 
            OperationSignature operationSignature, 
            CharacteristicContainer containerToApply) {
        
    	// Find Component by iterating connectors, 
    	// Check outer role with passed operationProvidedRole
        // If match, pass inner role 
        for (ProvidedDelegationConnector connector : assemblyAbs.getListOfProvidedDelegationConnector(composedStructure)) {
            OperationProvidedRole outerRole = connector.getOuterProvidedRole_ProvidedDelegationConnector();
            OperationProvidedRole innerRole = connector.getInnerProvidedRole_ProvidedDelegationConnector();

            if (assemblyAbs.isOperationProvidedRoleMatch(operationProvidedRole, outerRole)) {
                Logger.infoDetailed(connector.getAssemblyContext_ProvidedDelegationConnector().getEntityName());
                AssemblyContext ac = connector.getAssemblyContext_ProvidedDelegationConnector();
                RepositoryComponent rc = ac.getEncapsulatedComponent__AssemblyContext();
                Logger.infoDetailed(rc.getEntityName());

                //If child component is same as parent or already contained in hierarchy--> endless loop
                if(!rc.getId().equalsIgnoreCase(composedStructure.getId()) && !hierarchy.contains(ac)) {
                    EList<AssemblyContext> copy = new BasicEList<AssemblyContext>();
                    copy.addAll(hierarchy);
                    copy.add(ac);
                    applyContextToRepositoryComponent(rc, copy, innerRole, operationSignature, containerToApply);
                } else {
                	Logger.info("Error in component(" + composedStructure.getId() + "): Recursion without end");
                }
            }
        }
    }
    	 

    /**
     * Handling to differentiate between different repositoryComponent types
     * 
     * Parameters only needed for calls to different sub-functions
     * 
     * @param repositoryComponent
     * @param hierarchy
     * @param operationProvidedRole 
     * @param operationSignature
     * @param containerToApply
     */
    private void applyContextToRepositoryComponent(RepositoryComponent repositoryComponent,
            EList<AssemblyContext> hierarchy, OperationProvidedRole operationProvidedRole, OperationSignature operationSignature,
            CharacteristicContainer containerToApply) {
    	
        if (repositoryComponent instanceof BasicComponent) {
            applyContextsToBasicComponent((BasicComponent) repositoryComponent, hierarchy, operationSignature,
                    containerToApply);
        } 
        else if (repositoryComponent instanceof CompositeComponent) {
            applyContextsToComposedStructure((CompositeComponent) repositoryComponent, hierarchy, operationProvidedRole, operationSignature, containerToApply);
        }        
        else {
            // TODO other cases
            Logger.error("TODO!!!");
        }

    }

    /**
     * Applies Context to internal and external actions in this basicComponent
     * 
     * Internal actions need to match operationSignature
     * 
     * External Actions need assemblyContext to find correct called other components
     * 
     * @param basicComponent
     * @param hierarchy
     * @param operationSignature
     * @param containerToApply
     */
    private void applyContextsToBasicComponent(BasicComponent basicComponent, EList<AssemblyContext> hierarchy, 
            OperationSignature operationSignature, CharacteristicContainer containerToApply) {
        for (ServiceEffectSpecification seff : basicComponent.getServiceEffectSpecifications__BasicComponent()) {
            Logger.infoDetailed(seff.getDescribedService__SEFF().getEntityName());
            if (seff.getDescribedService__SEFF() == operationSignature) {
                Logger.infoDetailed("SEFF matched");

                if(seff instanceof ResourceDemandingSEFF) {
                    ResourceDemandingSEFF rdSeff = (ResourceDemandingSEFF) seff;

                    // Get all internal actions, and check applied data processing
                    for (AbstractAction action : rdSeff.getSteps_Behaviour()) {
                        if (action instanceof InternalAction) {
                            // Name for eventually newly created containers
                            String name = basicComponent.getEntityName() + "_" + operationSignature.getEntityName();

                            applyContextsToInternalCall((InternalAction) action, containerToApply, name);
                        } else if (action instanceof ExternalCallAction) {
                            applyContextsToExternalCall((ExternalCallAction) action, hierarchy, containerToApply);
                        }
                    }
                }
                else {
                }
            }
        }
    }

    /**
     * Applies context to externalAction
     * 
     * External actions calls another (basic) component. Find match with assemblyContext, and then
     * call applyContextsToBasicComponent for that component
     * 
     * @param externalAction
     * @param hierarchy
     * @param containerToApply
     */
    private void applyContextsToExternalCall(ExternalCallAction externalAction, EList<AssemblyContext> hierarchy, CharacteristicContainer containerToApply) {
        Logger.infoDetailed(externalAction.getEntityName());
        Logger.infoDetailed(externalAction.getCalledService_ExternalService().getEntityName());
        OperationSignature externalSignature = externalAction.getCalledService_ExternalService();
        OperationRequiredRole requiredRole = externalAction.getRole_ExternalService();
        Logger.infoDetailed(requiredRole.getEntityName());
        Logger.infoDetailed(requiredRole.getRequiredInterface__OperationRequiredRole().getEntityName());

        searchForMatchingExternalComponent(hierarchy, requiredRole, externalSignature,containerToApply);
    }
    
    private void searchForMatchingExternalComponent(EList<AssemblyContext> hierarchy,
           OperationRequiredRole requiredRole, OperationSignature externalSignature, CharacteristicContainer containerToApply) {
    	    	
    	AssemblyContext currentContext = hierarchy.get(hierarchy.size() - 1);
    	ComposedStructure cs = currentContext.getParentStructure__AssemblyContext();
    	
        //Search in parent structure for all outgoing connectors from this component and find matching signature        
        for (AssemblyConnector connector : assemblyAbs.getListOfAssemblyConnectors(cs)) {
            Logger.infoDetailed(connector.getEntityName());
            AssemblyContext targetComponent = connector.getProvidingAssemblyContext_AssemblyConnector();
            AssemblyContext sourceComponent = connector.getRequiringAssemblyContext_AssemblyConnector();
            if (sourceComponent.equals(currentContext)) {
                OperationRequiredRole orr = connector.getRequiredRole_AssemblyConnector();
                OperationProvidedRole opr = connector.getProvidedRole_AssemblyConnector();
                if (orr.equals(requiredRole)) {
                    RepositoryComponent rc = targetComponent.getEncapsulatedComponent__AssemblyContext();
                    Logger.infoDetailed(rc.getEntityName());
                    EList<AssemblyContext> copy = new BasicEList<AssemblyContext>();
                    copy.addAll(hierarchy);
                    applyContextToRepositoryComponent(rc, copy, opr, externalSignature, containerToApply);
                }
            }
        }
        
        //if no matching assembly connector found, check if maybe call outside of structure
        for(RequiredDelegationConnector connector : assemblyAbs.getListOfRequiredDelegationConnector(cs)) {
            Logger.infoDetailed(connector.getEntityName());
            AssemblyContext sourceComponent = connector.getAssemblyContext_RequiredDelegationConnector();
            if (sourceComponent.equals(currentContext)) {
                Logger.infoDetailed(sourceComponent.getEntityName());
                
            	OperationRequiredRole innerRole = connector.getInnerRequiredRole_RequiredDelegationConnector();
            	OperationRequiredRole outerRole = connector.getOuterRequiredRole_RequiredDelegationConnector();
                if(assemblyAbs.isOperationRequiredRoleMatch(innerRole, requiredRole)) {
                	//Go 1 level higher
                    EList<AssemblyContext> copy = new BasicEList<AssemblyContext>();
                    copy.addAll(hierarchy);
                    copy.remove(currentContext);
                    
                    //If top level is already reached, stop
                    if(copy.size() > 0) {
                        searchForMatchingExternalComponent(copy, outerRole, externalSignature,containerToApply);
                    }
                }
            }
        }
    }

    /**
     * Applies context to internalAction
     * 
     * Check if stereotype is applied to internalAction.
     * 
     * Call applyContext for container from appliedStereotype and with containerToApply
     * 
     * @param internalAction
     * @param containerToApply
     * @param nameForNewContainers
     */
    private void applyContextsToInternalCall(InternalAction internalAction, CharacteristicContainer containerToApply,
            String nameForNewContainers) {

        if (MdsdAbstraction.isDataProcessingStereotypeApplied(internalAction)) {
            DataProcessingContainer dpc = MdsdAbstraction.getDataProcessingFromStereotype(internalAction);

            CharacteristicContainer containerTarget = dataSpecAbs
                    .getCharacteristicContainerForDataProcessingContainer(dpc);

            if (containerTarget != null) {
                applyContexts(containerTarget, containerToApply);
            } else {
                Logger.error("DataProcessingContainer(" + dpc.getEntityName()
                        + ") couldn't be matched to CharacteristicContainer");
            }
        } else {
            // Check setting if stereotype should be applied and containers be created
            if (settings.isApplyStereotype()) {
                Logger.info("APPLY STEREOTYPE TO " + internalAction.getEntityName());

                DataProcessingContainer newDPC = dataSpecAbs
                        .createNewCharacteristicPairForInternalAction(nameForNewContainers);

                MdsdAbstraction.applyStereoTypeToInternalAction(internalAction, newDPC);

                // Call same function again, since stereotype now applied
                applyContextsToInternalCall(internalAction, containerToApply, nameForNewContainers);
            }
        }
    }

    /**
     * Applies context from one container to the other one.
     * 
     * @param applyTo
     * @param applyFrom
     */
    private void applyContexts(CharacteristicContainer applyTo, CharacteristicContainer applyFrom) {
        Logger.infoDetailed("\nApply Context");
        new DataProcessingPrinter(dataSpecAbs.getDataSpec()).printDataProcessing();

        // Iterate all context, apply each to dpc
        for (ContextCharacteristic c : dataSpecAbs.getContextCharacteristic(applyFrom)) {
            boolean contextApplied = false;
            for (ContextCharacteristic c2 : dataSpecAbs.getContextCharacteristic(applyTo)) {
                // if (c.getCharacteristicType() == c2.getCharacteristicType()) {
                if (c.getCharacteristicType().getId().equalsIgnoreCase(c2.getCharacteristicType().getId())) {
                    Logger.info("Apply:" + applyFrom.getEntityName() + " to " + applyTo.getEntityName());
                    dataSpecAbs.applyContext(c2, c);
                    contextApplied = true;
                }
            }

            // Context wasn't applied because no matching contexttype found -> create context type
            if (!contextApplied) {
                if (settings.isCreateContextCharacteristic()) {
                    Logger.info("CREATE NEW CONTEXTCONTAINER");
                    dataSpecAbs.createContextCharacteristic(applyTo, c);
                }
            }
        }
        new DataProcessingPrinter(dataSpecAbs.getDataSpec()).printDataProcessing();
    }
}
