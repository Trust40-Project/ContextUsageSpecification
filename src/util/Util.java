package util;

import java.io.File;

//TODO refactor name
public class Util {
	private final static String pathDataprocessing = "E:\\Praktikum\\ContextUsageSpecificationUseCase\\usecase1\\My.dataprocessing";
	private final static String pathUsageModel = "E:\\Praktikum\\ContextUsageSpecificationUseCase\\usecase1\\newUsageModel.usagemodel";
	private final static String pathRepositoryModel = "E:\\Praktikum\\ContextUsageSpecificationUseCase\\usecase1\\newRepository.repository";
	
	//TODO add functionality
    public static String getDataprocessingPath() {
    	return pathDataprocessing;
    }
	public static String getUsageModelPath() {
		return pathUsageModel;
	}
	public static String getRepositoryModelPath() {
		return pathRepositoryModel;
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
