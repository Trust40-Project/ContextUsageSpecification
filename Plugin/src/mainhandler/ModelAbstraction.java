package mainhandler;

import setting.PreferenceHandler;
import util.MyLogger;

public class ModelAbstraction {
    private String projectPath;

    public ModelAbstraction(String path) {
        this.projectPath = path;
    }

    public String getDataprocessingPath() {
        String name = PreferenceHandler.getPathdataprocessing();
        String path = projectPath + "/" + name;
        MyLogger.info2(path);
        return path;
    }

    public String getUsageModelPath() {
        String name = PreferenceHandler.getPathusagemodel();
        String path = projectPath + "/" + name;
        MyLogger.info2(path);
        return path;
    }

    public String getRepositoryModelPath() {
        String name = PreferenceHandler.getPathrepositorymodel();
        String path = projectPath + "/" + name;
        MyLogger.info2(path);
        return path;
    }

    public String getAssemblyPath() {
        String name = PreferenceHandler.getPathassembly();
        String path = projectPath + "/" + name;
        MyLogger.info2(path);
        return path;
    }
}
