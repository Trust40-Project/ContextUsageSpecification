package tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import generation.ContextHandler;
import mainhandler.ModelHandler;
import util.MyLogger;
import util.Util;

public class usecase1 {

	@Test
	public void test() {
      final String currentPath = Util.getCurrentDir();        
      MyLogger.info(currentPath);
      
      String dataPath = "C:\\Users\\T440s\\Praktikum\\UseCasesTechnicalReport\\UC-ContextUsageSpecification\\usecase1";

      ModelHandler modelloader = new ModelHandler(dataPath);
      DataSpecification dataSpec = modelloader.loadDataSpecification();        
	UsageModel usageModel = modelloader.loadUsageModel();    
		Repository repo = modelloader.loadRepositoryModel();
		System system = modelloader.loadAssemblyModel();
              
      final ContextHandler ch = new ContextHandler(dataSpec, usageModel, repo, system);        
      ch.execute();
		
		fail("Not yet implemented");
	}

}
