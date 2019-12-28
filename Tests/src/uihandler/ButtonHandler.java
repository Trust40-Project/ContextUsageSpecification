package uihandler;


import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import mainhandler.MainHandler;
import util.MyLogger;
import util.Util;

public class ButtonHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        Objects.requireNonNull(event);
        
        MyLogger.info("Tests-Start");

    	//TODO do properly, currently use active workspace, only 1 project allowed
        final String currentPath = Util.getCurrentDir();        
        MyLogger.info(currentPath);
        
        //Call Mainhandler with datapath
        new MainHandler().execute(currentPath);

        MyLogger.info("Tests-End");
        
        return null;
	}
}
