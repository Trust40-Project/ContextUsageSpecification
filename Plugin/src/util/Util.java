package util;

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
	
	public static String getCurrentDir() {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot wsr = ws.getRoot();
		for(IProject p : wsr.getProjects()) {
			return p.getLocation().toString();
		}
		return "";
	}

	public static String getPathrepositorymodel() {
		return pathRepositoryModel;
	}

	public static String getPathassembly() {
		return pathAssembly;
	}

	public static String getPathusagemodel() {
		return pathUsageModel;
	}

	public static String getPathdataprocessing() {
		return pathDataprocessing;
	}
}
