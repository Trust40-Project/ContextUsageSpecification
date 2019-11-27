package mainhandler;

import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.ui.PlatformUI;
import org.palladiosimulator.pcm.dataprocessing.dataprocessing.DataSpecification;

import util.Util;

public class MainHandler extends AbstractHandler {
	//TODO needed?
    //public static final boolean IS_ECLIPSE_RUNNING = Platform.isRunning();
    
	//TODO use diffenret logger
    public static final Logger LOGGER = PlatformUI.getWorkbench().getService(Logger.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        Objects.requireNonNull(event);
        
        LOGGER.info("test");
        
        final String dataPath = Util.getDataprocessingPath();        
        LOGGER.info(dataPath);
        
        final String id = Util.getIdOfModel(dataPath);        
        LOGGER.info(id);
        
        ModelLoader modelloader = new ModelLoader(dataPath);
        DataSpecification dataContainer = modelloader.loadDataSpecification();
        
		return null;
	}
}
