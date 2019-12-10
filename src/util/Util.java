package util;

import java.io.File;

//TODO refactor name
public class Util {
	private final static String pathDataprocessing= "E:\\Praktikum\\ContextUsageSpecificationUseCase\\usecase1\\My.dataprocessing";
	//private final static String pathDataprocessing= "C:\\Users\\Thomas\\workspace-palladiobench\\__test\\My.dataprocessing";
	
	//TODO add functionality
    public static String getDataprocessingPath() {
    	return pathDataprocessing;
    }

	//TODO describe what happens here
    public static String getIdOfModel(final String pathDataprocessing) {
        final StringBuilder id = new StringBuilder();
        
        final char dirDelim = File.separatorChar;
        boolean add = false;
        boolean done = false;
        for (int i = pathDataprocessing.length() - 1; i >= 0; i--) {
            final char now = pathDataprocessing.charAt(i);
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
