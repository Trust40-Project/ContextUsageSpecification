package mainhandler;

import util.Util;

public class ModelAbstraction {

	private String projectPath;
	
	public ModelAbstraction(String path) {
		this.projectPath = path;
	}

	//TODO add functionality	
    public String getDataprocessingPath() {
    	return projectPath + Util.getPathdataprocessing();
    }
	public String getUsageModelPath() {
		return projectPath + Util.getPathusagemodel();
	}
	public String getRepositoryModelPath() {
		return projectPath + Util.getPathrepositorymodel();
	}
	public String getAssemblyPath() {
		return projectPath + Util.getPathassembly();
	}
}
