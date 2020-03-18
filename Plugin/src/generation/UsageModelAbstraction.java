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

    public EList<ScenarioBehaviour> getListofScenarioBehaviour() {
        EList<ScenarioBehaviour> list = new BasicEList<>();
        for(UsageScenario usageScenario : usageModel.getUsageScenario_UsageModel()) {
        	ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        	list.add(scenarioBehaviour);
        }
        return list;
    }

    public CharacteristicContainer getAppliedCharacterizableContainer(ScenarioBehaviour scenarioBehaviour) {
        Logger.infoDetailed(scenarioBehaviour.getEntityName());

        CharacteristicContainer containerCharacterizable = MdsdAbstraction
                .getCharacteristicContainerFromStereotype(scenarioBehaviour);

        return containerCharacterizable;
    }

    public EList<EntryLevelSystemCall> getListOfEntryLevelSystemCalls(ScenarioBehaviour scenarioBehaviour) {
        EList<EntryLevelSystemCall> list = new BasicEList<>();
        for (AbstractUserAction abstractUserAction : scenarioBehaviour.getActions_ScenarioBehaviour()) {
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
