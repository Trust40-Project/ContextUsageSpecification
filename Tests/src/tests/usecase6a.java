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

public class usecase6a {
    // usecase1 applies context of systemcall to method
    // TODO restructure

    @Test
    public void test() {
        final String currentPath = Util.getCurrentDir();
        Logger.infoDetailed(currentPath);

        String dataPath = currentPath + "\\..\\" + "usecase6";
        Logger.infoDetailed(dataPath);

        Settings settings = new Settings(false, ContextMaster.Characterizable, false, false);

        ModelHandler modelloader = new ModelHandler(new ModelAbstraction(dataPath));
        DataSpecification dataSpec = modelloader.loadDataSpecification();
        UsageModel usageModel = modelloader.loadUsageModel();
        Repository repo = modelloader.loadRepositoryModel();
        System system = modelloader.loadAssemblyModel();

        final ContextHandler ch = new ContextHandler(settings, dataSpec, usageModel, repo, system);
        DataSpecificationAbstraction dataSpecAbs = new DataSpecificationAbstraction(dataSpec);

        CharacteristicContainer character = dataSpecAbs.getCharacteristicContainerByName("Characterizable");
        CharacteristicContainer sys = dataSpecAbs.getCharacteristicContainerByName("SystemCall1");
        CharacteristicContainer method = dataSpecAbs.getCharacteristicContainerByName("method1");
        CharacteristicContainer method2 = dataSpecAbs.getCharacteristicContainerByName("method2");
        assertNotNull(character);
        assertNotNull(sys);
        assertNotNull(method);
        assertNotNull(method2);

        Context c_char = dataSpecAbs.getContextByName(character, "PUBLIC");
        Context c_sys = dataSpecAbs.getContextByName(sys, "PUBLIC");
        Context c_method = dataSpecAbs.getContextByName(method, "PUBLIC");
        Context c_method2 = dataSpecAbs.getContextByName(method2, "PUBLIC");
        assertNotNull(c_char);
        assertNull(c_sys);
        assertNull(c_method);
        assertNull(c_method2);
        Context c_char_2 = dataSpecAbs.getContextByName(character, "SECRET");
        Context c_sys_2 = dataSpecAbs.getContextByName(sys, "SECRET");
        Context c_method_2 = dataSpecAbs.getContextByName(method, "SECRET");
        Context c_method2_2 = dataSpecAbs.getContextByName(method2, "SECRET");
        assertNull(c_char_2);
        assertNotNull(c_sys_2);
        assertNull(c_method_2);
        assertNull(c_method2_2);

        ch.execute();

        c_char = dataSpecAbs.getContextByName(character, "PUBLIC");
        c_sys = dataSpecAbs.getContextByName(sys, "PUBLIC");
        c_method = dataSpecAbs.getContextByName(method, "PUBLIC");
        c_method2 = dataSpecAbs.getContextByName(method2, "PUBLIC");
        assertNotNull(c_char);
        assertNull(c_sys);
        assertNotNull(c_method);
        assertNotNull(c_method2);
        c_char_2 = dataSpecAbs.getContextByName(character, "SECRET");
        c_sys_2 = dataSpecAbs.getContextByName(sys, "SECRET");
        c_method_2 = dataSpecAbs.getContextByName(method, "SECRET");
        c_method2_2 = dataSpecAbs.getContextByName(method2, "SECRET");
        assertNull(c_char_2);
        assertNotNull(c_sys_2);
        assertNull(c_method_2);
        assertNull(c_method2_2);
    }
}
