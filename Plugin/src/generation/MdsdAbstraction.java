package generation;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.api.ProfileAPI;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.processing.DataProcessingContainer;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;

import util.Logger;

/**
 * Abstraction for handling related to StereotypeApi / ProfileApi
 * 
 * @author Thomas Lieb
 *
 */
public class MdsdAbstraction {

    private static String stereotypeNameDataSpec = "DataProcessingSpecification";
    private static String stereotypeNameCharacterizable = "Characterizable";

    public static boolean isDataProcessingStereotypeApplied(EObject object) {
        for (Stereotype stereotype : StereotypeAPI.getAppliedStereotypes(object)) {
            Logger.infoDetailed(stereotype.getName());
            if ((stereotype.getName().equals(stereotypeNameDataSpec))) {
                return true;
            }
        }
        return false;
    }

    public static DataProcessingContainer getDataProcessingFromStereotype(EObject object) {
        DataProcessingContainer container = null;

        for (Stereotype stereotype : StereotypeAPI.getAppliedStereotypes(object)) {
            Logger.infoDetailed(stereotype.getName());
            if ((stereotype.getName().equals(stereotypeNameDataSpec))) {

                Collection<EStructuralFeature> list = StereotypeAPI.getParameters(stereotype);
                for (EStructuralFeature esf : list) {
                    String name = esf.getName();
                    Logger.infoDetailed(name);
                    Object obj = StereotypeAPI.getTaggedValue(object, name, stereotype.getName());
                    if (obj instanceof DataProcessingContainer) {
                        container = (DataProcessingContainer) obj;
                        return container;
                    } else {
                        Logger.error("Stereotype applied put no dataprocessing container selected!");
                    }
                }
            }
        }
        return container;
    }

    public static CharacteristicContainer getCharacteristicContainerFromStereotype(
            ScenarioBehaviour scenarioBehaviour) {
        // Iterate applied stereotypes, look for Characterizable
        for (Stereotype stereotype : StereotypeAPI.getAppliedStereotypes(scenarioBehaviour)) {
            Logger.infoDetailed(stereotype.getName());

            if ((stereotype.getName().equals(stereotypeNameCharacterizable))) {
                for (EStructuralFeature structuralFeature : StereotypeAPI.getParameters(stereotype)) {
                    String name = structuralFeature.getName();
                    Object obj = StereotypeAPI.getTaggedValue(scenarioBehaviour, name, stereotype.getName());
                    if (obj instanceof CharacteristicContainer) {
                        return (CharacteristicContainer) obj;
                    } else {
                        Logger.error("CharacteristicContainer not selected");
                    }
                }
            }
        }
        return null;
    }

    public static void applyStereoTypeToInternalAction(InternalAction internalAction, DataProcessingContainer newDPC) {
        // Make sure Profile is applied on this resource / in repository
        if (!ProfileAPI.isProfileApplied(internalAction.eResource(), "DataProcessing")) {
            ProfileAPI.applyProfile(internalAction.eResource(), "DataProcessing");
        }

        // Apply stereotype to internal action
        StereotypeAPI.applyStereotype(internalAction, "DataProcessingSpecification");
        StereotypeAPI.setTaggedValue(internalAction, newDPC, "DataProcessingSpecification", "dataProcessingContainer");
    }
}
