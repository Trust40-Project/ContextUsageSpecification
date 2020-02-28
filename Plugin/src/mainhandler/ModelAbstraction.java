package mainhandler;

import setting.PreferenceHandler;

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
