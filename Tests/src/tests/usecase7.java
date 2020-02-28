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
import setting.Settings;
import util.Logger;
import util.Util;

public class usecase7 {
    // usecase1 applies context of systemcall to method
    // TODO restructure

    @Test
    public void test() {
        final String currentPath = Util.getCurrentDir();
        Logger.infoDetailed(currentPath);

        String dataPath = currentPath + "\\..\\" + "usecase7";
        Logger.infoDetailed(dataPath);

        Settings settings = new Settings(true, ContextMaster.Characterizable, true, false);

        ModelHandler modelloader = new ModelHandler(new ModelAbstraction(dataPath));
        DataSpecification dataSpec = modelloader.loadDataSpecification();
        UsageModel usageModel = modelloader.loadUsageModel();
        Repository repo = modelloader.loadRepositoryModel();
        System system = modelloader.loadAssemblyModel();

        final ContextHandler ch = new ContextHandler(settings, dataSpec, usageModel, repo, system);
        DataSpecificationAbstraction dataSpecAbs = new DataSpecificationAbstraction(dataSpec);

        CharacteristicContainer sys = dataSpecAbs.getCharacteristicContainerByName("SystemCall1");
        CharacteristicContainer method = dataSpecAbs.getCharacteristicContainerByName("BasicComponent1_method1");
        assertNotNull(sys);
        assertNull(method);

        Context c_sys = dataSpecAbs.getContextByName(sys, "PUBLIC");
        assertNotNull(c_sys);

        ch.execute();

        method = dataSpecAbs.getCharacteristicContainerByName("BasicComponent1_method1");
        assertNotNull(method);

        c_sys = dataSpecAbs.getContextByName(sys, "PUBLIC");
        Context c_method = dataSpecAbs.getContextByName(method, "PUBLIC");
        assertNotNull(c_sys);
        assertNotNull(c_method);
    }
}
