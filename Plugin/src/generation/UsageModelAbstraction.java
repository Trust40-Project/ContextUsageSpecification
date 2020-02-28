package generation;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
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
        ScenarioBehaviour scenarioBehaviour = getScenarioBehaviour();
        Logger.infoDetailed(scenarioBehaviour.getEntityName());

        CharacteristicContainer containerCharacterizable = MdsdAbstraction
                .getCharacteristicContainerFromStereotype(scenarioBehaviour);

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

        if (MdsdAbstraction.isDataProcessingStereotypeApplied(systemCall)) {
            DataProcessingContainer dpc = MdsdAbstraction.getDataProcessingFromStereotype(systemCall);

            chracteristicContainer = dataSpecAbs.getCharacteristicContainerForDataProcessingContainer(dpc);

            if (chracteristicContainer == null) {
                Logger.error("DataProcessingContainer(" + dpc.getEntityName()
                        + ") couldn't be matched to CharacteristicContainer");
            }
        }

        return chracteristicContainer;
    }
}
