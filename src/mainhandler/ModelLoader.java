package mainhandler;

import java.io.File;
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

public class ModelLoader {
    private String pathDataprocessing;
    private ResourceSet resourceSet;
    private Registry resourceRegistry;

    public ModelLoader(final String pathDataprocessing) {
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
        Resource resourceData = loadResource(this.resourceSet, this.pathDataprocessing);
        return (DataSpecification) resourceData.getContents().get(0);
    }
    
    //TODO put directly in other function?
    private Resource loadResource(final ResourceSet resourceSet, final String path) {
        return resourceSet.getResource(URI.createFileURI(path), true);
    }
}
