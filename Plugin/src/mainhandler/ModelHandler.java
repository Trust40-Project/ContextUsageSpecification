package mainhandler;

import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataprocessingPackage;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.DynamicextensionPackage;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

import util.Util;

public class ModelHandler {
    private String path;
    private ResourceSet resourceSet;
    private Resource resourceData;

    public ModelHandler(final String path) {
		this.path = path;
        this.resourceSet = new ResourceSetImpl();
        
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
    
    public DataSpecification loadDataSpecification() {
        resourceData = loadResource(this.resourceSet, Util.getDataprocessingPath());
        
        //resourceData.setTrackingModification(true);
        
        //TODO Throw exception if > 1 or cast fails
        return (DataSpecification) resourceData.getContents().get(0);
    }
    
    public void saveDataSpecification() {
    	if(resourceData.isModified()) {
        	try {
    			resourceData.save(null);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }

	public UsageModel loadUsageModel() {
        resourceData = loadResource(this.resourceSet, Util.getUsageModelPath());

        //TODO Throw exception if > 1 or cast fails
		return (UsageModel) resourceData.getContents().get(0);
	}  

	public Repository loadRepositoryModel() {
        resourceData = loadResource(this.resourceSet, Util.getRepositoryModelPath());

        //TODO Throw exception if > 1 or cast fails
		return (Repository) resourceData.getContents().get(0);
	} 

	public System loadAssemblyModel() {
        resourceData = loadResource(this.resourceSet, Util.getAssemblyPath());

        //TODO Throw exception if > 1 or cast fails
		return (System) resourceData.getContents().get(0);
	} 
	
    private Resource loadResource(final ResourceSet resourceSet, final String path) {
        return resourceSet.getResource(URI.createFileURI(path), true);
    }
}