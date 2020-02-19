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
import mainhandler.ModelAbstraction;
import mainhandler.ModelHandler;
import setting.ContextMaster;
import setting.GenerationSettings;
import util.MyLogger;
import util.Util;

public class usecase4 {
    // usecase1 applies context of systemcall to method
    // TODO restructure

    @Test
    public void test() {
        final String currentPath = Util.getCurrentDir();
        MyLogger.info(currentPath);

        String dataPath = currentPath + "\\..\\" + "usecase4";
        MyLogger.info(dataPath);

        // TODO :Usecase for false
        GenerationSettings settings = new GenerationSettings(true, ContextMaster.Characterizable, false, false);

        ModelHandler modelloader = new ModelHandler(new ModelAbstraction(dataPath, true));
        DataSpecification dataSpec = modelloader.loadDataSpecification();
        UsageModel usageModel = modelloader.loadUsageModel();
        Repository repo = modelloader.loadRepositoryModel();
        System system = modelloader.loadAssemblyModel();

        final ContextHandler ch = new ContextHandler(settings, dataSpec, usageModel, repo, system);
        DataSpecificationAbstraction dataSpecAbs = new DataSpecificationAbstraction(dataSpec);

        CharacteristicContainer sys = dataSpecAbs.getCharacteristicContainerByName("SystemCall1");
        CharacteristicContainer method = dataSpecAbs.getCharacteristicContainerByName("method1");
        assertNotNull(sys);
        assertNotNull(method);

        Context c_sys = dataSpecAbs.getContextByName(sys, "PUBLIC");
        Context c_method = dataSpecAbs.getContextByName(method, "PUBLIC");
        assertNotNull(c_sys);
        assertNull(c_method);

        ch.execute();

        c_sys = dataSpecAbs.getContextByName(sys, "PUBLIC");
        c_method = dataSpecAbs.getContextByName(method, "PUBLIC");
        assertNotNull(c_sys);
        assertNotNull(c_method);
    }
}
