package generation;

import java.util.Collection;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.ContextCharacteristic;
import org.palladiosimulator.pcm.repository.BasicComponent;
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
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import setting.GenerationSettings;
import util.DataProcessingPrinter;
import util.MyLogger;

public class ContextHandler {
    private final GenerationSettings settings;
    private final DataSpecificationAbstraction dataSpecAbs;
    private final UsageModelAbstraction usageModelAbs;
    private final Repository repo;
    private final AssemblyAbstraction assemblyAbs;

    public ContextHandler(final GenerationSettings settings, final DataSpecification dataSpec,
            final UsageModel usageModel, final Repository repo, final System system) {
        this.settings = settings;
        this.dataSpecAbs = new DataSpecificationAbstraction(dataSpec);
        this.usageModelAbs = new UsageModelAbstraction(usageModel);
        this.repo = repo;
        this.assemblyAbs = new AssemblyAbstraction(system);
    }

    public void execute() {
        applyContextToAllSystemCalls();
    }

    public void applyContextToAllSystemCalls() {
        MyLogger.info("\nAppling Context to all methods");

        CharacteristicContainer containerCharacterizable = usageModelAbs.getAppliedCharacterizableContainer();

        for (EntryLevelSystemCall systemCall : usageModelAbs.getListOfEntryLevelSystemCalls()) {
            CharacteristicContainer containerDataProcessing = usageModelAbs.getAppliedContainer(systemCall);

            switch (settings.getContextMaster()) {
            case Characterizable:
                if (containerCharacterizable != null) {
                    applyContextToSystemCall(systemCall, containerCharacterizable);
                } else {
                    // TODO leave loop as well or throw exception
                    MyLogger.error("Stereotype Characterizable not applied");
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
            }
        }
    }

    public void applyContextToSystemCall(EntryLevelSystemCall elsc, CharacteristicContainer containerToApply) {
        MyLogger.info("\nAppling Context to SystemCall");
        MyLogger.info(elsc.getEntityName());
        OperationProvidedRole opr = elsc.getProvidedRole_EntryLevelSystemCall();
        MyLogger.info(opr.getEntityName());
        OperationSignature op = elsc.getOperationSignature__EntryLevelSystemCall();
        MyLogger.info(op.getEntityName());

        // Find Component by iterating connectors, check outer role with system call
        // Still pass operation signature to know which function is called
        for (ProvidedDelegationConnector connector : assemblyAbs.getListOfProvidedDelegationConnector()) {
            OperationProvidedRole opr2 = connector.getOuterProvidedRole_ProvidedDelegationConnector();

            if (assemblyAbs.isOperationProvidedRoleMatch(opr, opr2)) {
                MyLogger.info(connector.getAssemblyContext_ProvidedDelegationConnector().getEntityName());
                AssemblyContext ac = connector.getAssemblyContext_ProvidedDelegationConnector();
                RepositoryComponent rc = ac.getEncapsulatedComponent__AssemblyContext();
                MyLogger.info(rc.getEntityName());
                if (rc instanceof BasicComponent) {
                    applyContextsToBasicComponent(ac, (BasicComponent) rc, op, containerToApply);
                } else {
                    // TODO other cases
                    MyLogger.error("TODO!!!");
                }
            }
        }
    }

    // Seach for operationSignature in BasicComponent, and apply Contexts of
    // CharacteristicsContainer
    public void applyContextsToBasicComponent(AssemblyContext bcac, BasicComponent bc, OperationSignature op,
            CharacteristicContainer umcc) {
        for (ServiceEffectSpecification seff : bc.getServiceEffectSpecifications__BasicComponent()) {
            MyLogger.info(seff.getDescribedService__SEFF().getEntityName());
            if (seff.getDescribedService__SEFF() == op) {
                MyLogger.info("MATCH");

                // TODO instance of. other cases allowed?
                ResourceDemandingSEFF rdSeff = (ResourceDemandingSEFF) seff;

                // Get all internal actions, and check applied data processing
                for (AbstractAction aa : rdSeff.getSteps_Behaviour()) {
                    if (aa instanceof InternalAction) {
                        applyContextsToInternalCall((InternalAction) aa, umcc);
                    } else if (aa instanceof ExternalCallAction) {
                        applyContextsToExternalCall((ExternalCallAction) aa, bcac, umcc);
                    }
                }
            }
        }
    }

    public void applyContextsToExternalCall(ExternalCallAction eca, AssemblyContext bcac,
            CharacteristicContainer umcc) {
        MyLogger.info(eca.getEntityName());
        MyLogger.info(eca.getCalledService_ExternalService().getEntityName());
        OperationSignature op2 = eca.getCalledService_ExternalService();
        OperationRequiredRole orr = eca.getRole_ExternalService();
        MyLogger.info(orr.getEntityName());
        MyLogger.info(orr.getRequiredInterface__OperationRequiredRole().getEntityName());

        for (AssemblyConnector connector : assemblyAbs.getListOfAssemblyConnectors()) {
            MyLogger.info(connector.getEntityName());
            AssemblyConnector ac = (AssemblyConnector) connector;
            AssemblyContext acProvide = ac.getProvidingAssemblyContext_AssemblyConnector();
            AssemblyContext acRequire = ac.getRequiringAssemblyContext_AssemblyConnector();
            if (acRequire.equals(bcac)) {
                OperationRequiredRole orr2 = ac.getRequiredRole_AssemblyConnector();
                if (orr2.equals(orr)) {
                    RepositoryComponent rc = acProvide.getEncapsulatedComponent__AssemblyContext();
                    MyLogger.info(rc.getEntityName());
                    if (rc instanceof BasicComponent) {
                        applyContextsToBasicComponent(acProvide, (BasicComponent) rc, op2, umcc);
                    }
                }
            }
        }
    }

    public void applyContextsToInternalCall(InternalAction ia, CharacteristicContainer umcc) {
        for (Stereotype stereotype : StereotypeAPI.getAppliedStereotypes(ia)) {
            MyLogger.info(stereotype.getName());
            // TODO proper cast or check to DataProcessingSpecification
            if ((stereotype.getName().equals("DataProcessingSpecification"))) {
                Collection<EStructuralFeature> list = StereotypeAPI.getParameters(stereotype);
                for (EStructuralFeature esf : list) {

                    String name = esf.getName();
                    MyLogger.info(name);
                    Object obj = StereotypeAPI.getTaggedValue(ia, name, stereotype.getName());
                    if (obj != null) {
                        MyLogger.info(obj.getClass().getSimpleName());
                        DataProcessingContainer dpc = (DataProcessingContainer) obj;
                        applyContexts(dpc, umcc);
                    } else {
                        MyLogger.error("Stereotype applied put no dataprocessing container selected!");
                    }
                }
            }
        }
    }

    // Apply Contexts to CharacteristicContainer related to dpc, from reference
    // CharacteristicContainer cc
    public void applyContexts(DataProcessingContainer dpc, CharacteristicContainer cc) {
        MyLogger.info("\nApply Context");
        new DataProcessingPrinter(dataSpecAbs.getDataSpec()).printDataProcessing();

        // Get cc from dpc
        CharacteristicContainer cc2 = dataSpecAbs.getCharacteristicContainerForDataProcessingContainer(dpc);

        if (cc2 == null) {
            MyLogger.error("DataProcessingContainer(" + dpc.getEntityName()
                    + ") couldn't be matched to CharacteristicContainer");
        }

        // Iterate all context, apply each to dpc
        for (ContextCharacteristic c : dataSpecAbs.getContextCharacteristic(cc)) {
            boolean contextApplied = false;
            for (ContextCharacteristic c2 : dataSpecAbs.getContextCharacteristic(cc2)) {
                // TODO anoter compare issue
                // if (c.getCharacteristicType() == c2.getCharacteristicType()) {
                if (c.getCharacteristicType().getId().equalsIgnoreCase(c2.getCharacteristicType().getId())) {
                    MyLogger.info2("Apply:" + cc.getEntityName() + " to " + cc2.getEntityName());
                    dataSpecAbs.applyContext(c2, c);
                    contextApplied = true;
                }
            }

            // Context wasn't applied because no matching contexttype found -> create context type
            if (!contextApplied) {
                if (settings.isCreateContextCharacteristic()) {
                    MyLogger.info2("CREATE NEW CONTEXTCONTAINER");
                    MyLogger.info("Before:" + cc2.getOwnedCharacteristics().size());
                    dataSpecAbs.createContextCharacteristic(cc2, c);
                    MyLogger.info("After:" + cc2.getOwnedCharacteristics().size());
                }
            }
        }
        new DataProcessingPrinter(dataSpecAbs.getDataSpec()).printDataProcessing();
    }
}
