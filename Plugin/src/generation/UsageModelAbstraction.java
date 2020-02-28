package generation;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import util.Logger;

/**
 * Abstraction for handling related to UsageModel
 * 
 * @author Thomas Lieb
 *
 */
public class UsageModelAbstraction {
    private final UsageModel usageModel;
    private final DataSpecificationAbstraction dataSpecAbs;

    public UsageModelAbstraction(final UsageModel usageModel, DataSpecificationAbstraction dataSpecAbs) {
        this.usageModel = usageModel;
        this.dataSpecAbs = dataSpecAbs;
    }

    private ScenarioBehaviour getScenarioBehaviour() {
        // TODO select correct model, or make loop
        UsageScenario us = usageModel.getUsageScenario_UsageModel().get(0);
        ScenarioBehaviour sb = us.getScenarioBehaviour_UsageScenario();
        return sb;
    }

    public CharacteristicContainer getAppliedCharacterizableContainer() {
        CharacteristicContainer containerCharacterizable = null;

        ScenarioBehaviour scenarioBehaviour = getScenarioBehaviour();
        Logger.infoDetailed(scenarioBehaviour.getEntityName());

        // Iterate applied stereotypes, look for Characterizable
        for (Stereotype stereotype : StereotypeAPI.getAppliedStereotypes(scenarioBehaviour)) {
            Logger.infoDetailed(stereotype.getName());

            if ((stereotype.getName().equals("Characterizable"))) {
                for (EStructuralFeature structuralFeature : StereotypeAPI.getParameters(stereotype)) {
                    String name = structuralFeature.getName();
                    Object obj = StereotypeAPI.getTaggedValue(scenarioBehaviour, name, stereotype.getName());
                    if (obj instanceof CharacteristicContainer) {
                        containerCharacterizable = (CharacteristicContainer) obj;
                    } else {
                        Logger.error("CharacteristicContainer not selected");
                    }
                }
            }
        }
        return containerCharacterizable;
    }

    public EList<EntryLevelSystemCall> getListOfEntryLevelSystemCalls() {
        EList<EntryLevelSystemCall> list = new BasicEList<>();
        for (AbstractUserAction abstractUserAction : getScenarioBehaviour().getActions_ScenarioBehaviour()) {
            if (abstractUserAction instanceof EntryLevelSystemCall) {
                list.add((EntryLevelSystemCall) abstractUserAction);
            }
        }
        return list;
    }

    public CharacteristicContainer getAppliedContainer(EntryLevelSystemCall systemCall) {
        CharacteristicContainer chracteristicContainer = null;

        // Iterate applied stereotypes, look for
        for (Stereotype stereotype : StereotypeAPI.getAppliedStereotypes(systemCall)) {

            if ((stereotype.getName().equals("DataProcessingSpecification"))) {

                for (EStructuralFeature structuralFeature : StereotypeAPI.getParameters(stereotype)) {
                    String name = structuralFeature.getName();
                    Object obj = StereotypeAPI.getTaggedValue(systemCall, name, stereotype.getName());
                    if (obj instanceof DataProcessingContainer) {
                        DataProcessingContainer dpc = (DataProcessingContainer) obj;
                        chracteristicContainer = dataSpecAbs.getCharacteristicContainerForDataProcessingContainer(dpc);
                        if (chracteristicContainer == null) {
                            Logger.error("DataProcessingContainer(" + dpc.getEntityName()
                                    + ") couldn't be matched to CharacteristicContainer");
                        }
                    } else {
                        Logger.error("CharacteristicContainer not selected");
                    }
                }
            }
        }

        return chracteristicContainer;
    }
}
