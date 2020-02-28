package mainhandler;

import java.util.Objects;

import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import generation.ContextHandler;
import setting.PreferenceHandler;
import setting.Settings;

/**
 * Handles to basic logic of plugin
 * 
 * 1. Loading preferences 2. Loading models 3. Execute functionality 4. Save models
 * 
 * @author Thomas Lieb
 *
 */
public class MainHandler {

    public void execute(String dataPath) {
        Objects.requireNonNull(dataPath);

        Settings settings = PreferenceHandler.getSettingsFromPreferences();

        ModelHandler modelloader = new ModelHandler(new ModelAbstraction(dataPath));
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
