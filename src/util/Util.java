package util;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

//TODO refactor name
public class Util {
	private final static String pathDataprocessing = "/My.dataprocessing";
	private final static String pathUsageModel = "/newUsageModel.usagemodel";
	private final static String pathAssembly = "/newAssembly.system";
	private final static String pathRepositoryModel = "/newRepository.repository";
	
	//TODO add functionality
    public static String getDataprocessingPath() {
    	return getCurrentDir() + pathDataprocessing;
    }
	public static String getUsageModelPath() {
		return getCurrentDir() + pathUsageModel;
	}
	public static String getRepositoryModelPath() {
		return getCurrentDir() + pathRepositoryModel;
	}
	public static String getAssemblyPath() {
		return getCurrentDir() + pathAssembly;
	}
	
	public static String getCurrentDir() {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsr = ws.getRoot();
		for(IProject p : wsr.getProjects()) {
			return p.getLocation().toString();
		}
		return "";
	}
}
