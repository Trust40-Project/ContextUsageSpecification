package mainhandler;

import java.util.Objects;

import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import generation.ContextHandler;

public class MainHandler {
	//TODO needed?
    //public static final boolean IS_ECLIPSE_RUNNING = Platform.isRunning();
    
	public void execute(String dataPath) {
        Objects.requireNonNull(dataPath);
                        
        ModelHandler modelloader = new ModelHandler(dataPath);
        DataSpecification dataSpec = modelloader.loadDataSpecification();        
		UsageModel usageModel = modelloader.loadUsageModel();    
		Repository repo = modelloader.loadRepositoryModel();
		System system = modelloader.loadAssemblyModel();
                
        final ContextHandler ch = new ContextHandler(dataSpec, usageModel, repo, system);        
        ch.execute();
        
        modelloader.saveDataSpecification();
	}
}
