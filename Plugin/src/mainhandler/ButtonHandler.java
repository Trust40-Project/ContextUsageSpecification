package mainhandler;

import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;

import generation.ContextHandler;
import util.MyLogger;
import util.Util;

public class ButtonHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        Objects.requireNonNull(event);
        
        MyLogger.info("ButtonHandler-Start");
        MyLogger.disable();

    	//TODO do properly, currently use active workspace, only 1 project allowed
        //final String currentPath = Util.getCurrentDir();        
        //MyLogger.info(currentPath);
        
        //Call Mainhandler with datapath
        //new MainHandler().execute(currentPath);
        
        for (String path : Util.getAllDir()) {
        	MyLogger.info2(path);
        	new MainHandler().execute(path);
		}

        MyLogger.enable();
        MyLogger.info("ButtonHandler-End");
        
        return null;
	}
}
