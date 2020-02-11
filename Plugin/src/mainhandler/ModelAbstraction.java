package mainhandler;

import util.MyLogger;

public class ModelAbstraction {
    private String projectPath;
    private boolean useDefault;

    public ModelAbstraction(String path, boolean useDefault) {
        this.projectPath = path;
        this.useDefault = useDefault;
    }

    public String getDataprocessingPath() {
        String name = PreferenceHandler.getPathdataprocessing();
        if (useDefault)
            name = "My.dataprocessing";
        String path = projectPath + "/" + name;
        MyLogger.info2(path);
        return path;
    }

    public String getUsageModelPath() {
        String name = PreferenceHandler.getPathusagemodel();
        if (useDefault)
            name = "newUsageModel.usagemodel";

        String path = projectPath + "/" + name;
        MyLogger.info2(path);
        return path;
    }

    public String getRepositoryModelPath() {
        String name = PreferenceHandler.getPathrepositorymodel();
        if (useDefault)
            name = "newRepository.repository";

        String path = projectPath + "/" + name;
        MyLogger.info2(path);
        return path;
    }

    public String getAssemblyPath() {
        String name = PreferenceHandler.getPathassembly();
        if (useDefault)
            name = "newAssembly.system";

        String path = projectPath + "/" + name;
        MyLogger.info2(path);
        return path;
    }
}
