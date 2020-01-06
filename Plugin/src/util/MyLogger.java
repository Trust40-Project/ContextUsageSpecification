package util;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.ui.PlatformUI;

public class MyLogger {
	//TODO use diffenret logger
    //public static final Logger LOGGER = PlatformUI.getWorkbench().getService(Logger.class);

	private static MyLogger instance = null;
	private static boolean isActive = true;

	public static MyLogger getInstance() {
		if(instance == null) {
			instance = new MyLogger();
		}
		return instance;
	}
	
	public static void error(String string) {
		getInstance().println(string);
	}
	
	public static void info(String string) {
		getInstance().println(string);
	}
	private void println(String string) {
		//LOGGER.info(string);
		if(isActive) System.out.println(string);
	}
	
	public static void info2(String string) {
		getInstance().println2(string);		
	}
	private void println2(String string) {
		//LOGGER.info(string);
		System.out.println(string);
	}	
	
	public static void disable() {isActive = false;};
	public static void enable() {isActive = true;};
}
