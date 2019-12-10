package util;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.ui.PlatformUI;

public class MyLogger {
	//TODO use diffenret logger
    public static final Logger LOGGER = PlatformUI.getWorkbench().getService(Logger.class);

	public void info(String string) {
		LOGGER.info(string);		
	}
}
