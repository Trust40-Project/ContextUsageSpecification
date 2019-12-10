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
        
        final String dataPath = Util.getDataprocessingPath();        
        MyLogger.info(dataPath);
        final String id = Util.getIdOfModel(dataPath);        
        MyLogger.info(id);
        
        //Call Mainhandler with datapath
        new MainHandler().execute(dataPath);

        MyLogger.info("ButtonHandler-End");
        
        return null;
	}
}
