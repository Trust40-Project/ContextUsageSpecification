package mainhandler;

import setting.PreferenceHandler;

/**
 * Abstracts paths to the 4 different models
 * 
 * Needed to enable different handling in tests
 * 
 * @author Thomas Lieb
 *
 */
public class ModelAbstraction {
    private String projectPath;

    public ModelAbstraction(String path) {
        this.projectPath = path;
    }

    public String getDataprocessingPath() {
        String name = PreferenceHandler.getPathdataprocessing();
        String path = projectPath + "/" + name;
        return path;
    }

    public String getUsageModelPath() {
        String name = PreferenceHandler.getPathusagemodel();
        String path = projectPath + "/" + name;
        return path;
    }

    public String getRepositoryModelPath() {
        String name = PreferenceHandler.getPathrepositorymodel();
        String path = projectPath + "/" + name;
        return path;
    }

    public String getAssemblyPath() {
        String name = PreferenceHandler.getPathassembly();
        String path = projectPath + "/" + name;
        return path;
    }
}
