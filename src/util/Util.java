package util;

import java.io.File;

//TODO refactor name
public class Util {
	private final static String pathProject = "C:\\Users\\T440s\\Praktikum\\ContextUsageSpecificationUseCase\\usecase1\\";
	private final static String pathDataprocessing = "My.dataprocessing";
	private final static String pathUsageModel = "newUsageModel.usagemodel";
	private final static String pathRepositoryModel = "newRepository.repository";
	
	//TODO add functionality
    public static String getDataprocessingPath() {
    	return pathProject + pathDataprocessing;
    }
	public static String getUsageModelPath() {
		return pathProject + pathUsageModel;
	}
	public static String getRepositoryModelPath() {
		return pathProject + pathRepositoryModel;
	}

	//TODO describe what happens here
    public static String getIdOfModel(final String fullPath) {
        final StringBuilder id = new StringBuilder();
        
        final char dirDelim = File.separatorChar;
        boolean add = false;
        boolean done = false;
        for (int i = fullPath.length() - 1; i >= 0; i--) {
            final char now = fullPath.charAt(i);
            if (!done && !add && now == dirDelim) {
                add = true;
            } else if (add && now == dirDelim) {
                add = false;
                done = true;
            }
            
            if (add) {
                id.append(now);
            }
        }
        
        return id.reverse().substring(0, id.length() - 1);
    }
}
