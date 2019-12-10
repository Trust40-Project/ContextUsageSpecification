package util;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.ui.PlatformUI;

public class MyLogger {
	//TODO use diffenret logger
    //public static final Logger LOGGER = PlatformUI.getWorkbench().getService(Logger.class);

	private static MyLogger instance = null;	

	public static MyLogger getInstance() {
		if(instance == null) {
			instance = new MyLogger();
		}
		return instance;
	}
	
	public static void info(String string) {
		getInstance().println(string);
	}
	
	private void println(String string) {
		//LOGGER.info(string);
		System.out.println(string);
	}
}
