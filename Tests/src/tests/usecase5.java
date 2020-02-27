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
import util.MyLogger;
import util.Util;

public class usecase5 {
    // usecase1 applies context of systemcall to method
    // TODO restructure

    @Test
    public void test() {
        final String currentPath = Util.getCurrentDir();
        MyLogger.info(currentPath);

        String dataPath = currentPath + "\\..\\" + "usecase5";
        MyLogger.info(dataPath);

        Settings settings = new Settings(true, ContextMaster.Characterizable, false, false);

        ModelHandler modelloader = new ModelHandler(new ModelAbstraction(dataPath));
        DataSpecification dataSpec = modelloader.loadDataSpecification();
        UsageModel usageModel = modelloader.loadUsageModel();
        Repository repo = modelloader.loadRepositoryModel();
        System system = modelloader.loadAssemblyModel();

        final ContextHandler ch = new ContextHandler(settings, dataSpec, usageModel, repo, system);
        DataSpecificationAbstraction dataSpecAbs = new DataSpecificationAbstraction(dataSpec);

        CharacteristicContainer sys = dataSpecAbs.getCharacteristicContainerByName("SystemCall1");
        CharacteristicContainer method = dataSpecAbs.getCharacteristicContainerByName("method1");
        CharacteristicContainer method2_bc2 = dataSpecAbs.getCharacteristicContainerByName("method2_BC2");
        CharacteristicContainer method2_bc3 = dataSpecAbs.getCharacteristicContainerByName("method2_BC3");
        assertNotNull(sys);
        assertNotNull(method);
        assertNotNull(method2_bc2);
        assertNotNull(method2_bc3);

        Context c_sys = dataSpecAbs.getContextByName(sys, "PUBLIC");
        Context c_method = dataSpecAbs.getContextByName(method, "PUBLIC");
        Context c_method2_bc2 = dataSpecAbs.getContextByName(method2_bc2, "PUBLIC");
        Context c_method2_bc3 = dataSpecAbs.getContextByName(method2_bc3, "PUBLIC");
        assertNotNull(c_sys);
        assertNull(c_method);
        assertNull(c_method2_bc2);
        assertNull(c_method2_bc3);

        ch.execute();

        c_sys = dataSpecAbs.getContextByName(sys, "PUBLIC");
        c_method = dataSpecAbs.getContextByName(method, "PUBLIC");
        c_method2_bc2 = dataSpecAbs.getContextByName(method2_bc2, "PUBLIC");
        c_method2_bc3 = dataSpecAbs.getContextByName(method2_bc3, "PUBLIC");
        assertNotNull(c_sys);
        assertNotNull(c_method);
        assertNull(c_method2_bc2);
        assertNotNull(c_method2_bc3);
    }
}
