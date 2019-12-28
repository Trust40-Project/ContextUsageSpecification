package mainhandler;

import util.MyLogger;
import util.Util;

public class ModelAbstraction {

	private String projectPath;
	
	public ModelAbstraction(String path) {
		this.projectPath = path;
	}

	//TODO add functionality	
    public String getDataprocessingPath() {
    	MyLogger.info(projectPath + Util.getPathdataprocessing());
    	return projectPath + Util.getPathdataprocessing();
    }
	public String getUsageModelPath() {
    	MyLogger.info(projectPath + Util.getPathusagemodel());
		return projectPath + Util.getPathusagemodel();
	}
	public String getRepositoryModelPath() {
    	MyLogger.info(projectPath + Util.getPathrepositorymodel());
		return projectPath + Util.getPathrepositorymodel();
	}
	public String getAssemblyPath() {
    	MyLogger.info(projectPath + Util.getPathassembly());
		return projectPath + Util.getPathassembly();
	}
}
