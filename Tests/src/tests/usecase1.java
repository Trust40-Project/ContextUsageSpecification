package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.characteristics.CharacteristicContainer;
import org.palladiosimulator.pcm.dataprocessing.dynamicextension.context.Context;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import generation.ContextHandler;
import generation.DataSpecificationAbstraction;
import mainhandler.ModelHandler;
import util.MyLogger;
import util.Util;

public class usecase1 {
	//usecase1 applies context of systemcall to method
	//TODO restructure

	@Test
	public void test() {
      final String currentPath = Util.getCurrentDir();        
      MyLogger.info(currentPath);
      
      String dataPath = currentPath + "\\..\\" + "usecase1"; 
      MyLogger.info(dataPath);

      ModelHandler modelloader = new ModelHandler(dataPath);
      DataSpecification dataSpec = modelloader.loadDataSpecification();        
	UsageModel usageModel = modelloader.loadUsageModel();    
		Repository repo = modelloader.loadRepositoryModel();
		System system = modelloader.loadAssemblyModel();
        
		final ContextHandler ch = new ContextHandler(dataSpec, usageModel, repo, system);    
		DataSpecificationAbstraction dataSpecAbs = new DataSpecificationAbstraction(dataSpec);
		
		CharacteristicContainer sys = dataSpecAbs.getCharacteristicContainerByName("SystemCall1");
		CharacteristicContainer method = dataSpecAbs.getCharacteristicContainerByName("method1");      
		assertNotNull(sys);
		assertNotNull(method);

		Context c_sys = dataSpecAbs.getContextByName(sys, "high");  
		Context c_method = dataSpecAbs.getContextByName(method, "aname");   
		Context c_method2 = dataSpecAbs.getContextByName(method, "high");     
		assertNotNull(c_sys);
		assertNotNull(c_method);
		assertNull(c_method2);
		
		ch.execute();		

		c_sys = dataSpecAbs.getContextByName(sys, "high");  
		c_method = dataSpecAbs.getContextByName(method, "aname");    
		c_method2 = dataSpecAbs.getContextByName(method, "high");  
		assertNotNull(c_sys);
		assertNotNull(c_method);
		assertNotNull(c_method2);
		
	}

}
