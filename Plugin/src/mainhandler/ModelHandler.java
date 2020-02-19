package mainhandler;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.modelversioning.emfprofile.registry.IProfileRegistry;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataprocessingPackage;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.DynamicextensionPackage;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

public class ModelHandler {
    private ResourceSet resourceSet;
    private Resource resourceDataProcessing;
    private Resource resourceRepository;
    private ModelAbstraction model;

    public ModelHandler(final ModelAbstraction model) {
        this.model = model;
        this.resourceSet = new ResourceSetImpl();

        // Needed to load MDSD profiles from beginning
        IProfileRegistry.eINSTANCE.getClass();

        DataprocessingPackage.eINSTANCE.eClass();
        DynamicextensionPackage.eINSTANCE.eClass();
        RepositoryPackage.eINSTANCE.eClass();
        UsagemodelPackage.eINSTANCE.eClass();
        SystemPackage.eINSTANCE.eClass();

        Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;

        final Map<String, Object> map = resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());
        this.resourceSet.setResourceFactoryRegistry(resourceRegistry);
    }

    public void trackModifications() {
        resourceDataProcessing.setTrackingModification(true);
        resourceRepository.setTrackingModification(true);
    }

    public DataSpecification loadDataSpecification() {
        resourceDataProcessing = loadResource(this.resourceSet, model.getDataprocessingPath());

        // TODO Throw exception if > 1 or cast fails
        return (DataSpecification) resourceDataProcessing.getContents().get(0);
    }

    public void saveDataSpecification() {
        if (resourceDataProcessing.isModified()) {
            try {
                resourceDataProcessing.save(null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public UsageModel loadUsageModel() {
        Resource resourceData = loadResource(this.resourceSet, model.getUsageModelPath());

        // TODO Throw exception if > 1 or cast fails
        return (UsageModel) resourceData.getContents().get(0);
    }

    public Repository loadRepositoryModel() {
        resourceRepository = loadResource(this.resourceSet, model.getRepositoryModelPath());

        // TODO Throw exception if > 1 or cast fails
        return (Repository) resourceRepository.getContents().get(0);
    }

    public void saveRepositoryModel() {
        if (resourceRepository.isModified()) {
            try {
                resourceRepository.save(null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public System loadAssemblyModel() {
        Resource resourceData = loadResource(this.resourceSet, model.getAssemblyPath());

        // TODO Throw exception if > 1 or cast fails
        return (System) resourceData.getContents().get(0);
    }

    private Resource loadResource(final ResourceSet resourceSet, final String path) {
        return resourceSet.getResource(URI.createFileURI(path), true);
    }
}
