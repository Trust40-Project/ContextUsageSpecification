package mainhandler;

import java.util.Objects;

import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import generation.ContextHandler;
import setting.ContextMaster;
import setting.GenerationSettings;

public class MainHandler {

    public void execute(String dataPath) {
        Objects.requireNonNull(dataPath);

        GenerationSettings settings = new GenerationSettings(true, ContextMaster.Combined, true, false);

        ModelHandler modelloader = new ModelHandler(new ModelAbstraction(dataPath, true));
        DataSpecification dataSpec = modelloader.loadDataSpecification();
        UsageModel usageModel = modelloader.loadUsageModel();
        Repository repo = modelloader.loadRepositoryModel();
        System system = modelloader.loadAssemblyModel();

        if (settings.isSaveChanges()) {
            modelloader.trackModifications();
        }

        final ContextHandler ch = new ContextHandler(settings, dataSpec, usageModel, repo, system);
        ch.execute();

        modelloader.saveDataSpecification();
        modelloader.saveRepositoryModel();
    }
}
