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

import util.MyLogger;

public class ModelHandler {
    private String pathDataprocessing;
    private ResourceSet resourceSet;
    private Registry resourceRegistry;

    //TODO any other resources needed? dynExt?
    private Resource resourceData;

    public ModelHandler(final String pathDataprocessing) {
		this.pathDataprocessing = pathDataprocessing;
        this.resourceSet = new ResourceSetImpl();
        
        DataprocessingPackage.eINSTANCE.eClass();
        DynamicextensionPackage.eINSTANCE.eClass();
        this.resourceRegistry = Resource.Factory.Registry.INSTANCE;
        
        final Map<String, Object> map = this.resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());
        this.resourceSet.setResourceFactoryRegistry(this.resourceRegistry);
    }
    
    public DataSpecification loadDataSpecification() {
        resourceData = loadResource(this.resourceSet, this.pathDataprocessing);
        
        //resourceData.setTrackingModification(true);
        
        //Throw exception if > 1
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
    
    //TODO put directly in other function?
    private Resource loadResource(final ResourceSet resourceSet, final String path) {
        return resourceSet.getResource(URI.createFileURI(path), true);
    }  
}
