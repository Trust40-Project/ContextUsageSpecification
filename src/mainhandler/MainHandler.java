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

public class MainHandler extends AbstractHandler {
	//TODO needed?
    //public static final boolean IS_ECLIPSE_RUNNING = Platform.isRunning();
    
	private MyLogger logger = new MyLogger();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        Objects.requireNonNull(event);
        
        logger.info("test");
        
        final String dataPath = Util.getDataprocessingPath();        
        logger.info(dataPath);
        
        final String id = Util.getIdOfModel(dataPath);        
        logger.info(id);
        
        ModelLoader modelloader = new ModelLoader(dataPath);
        DataSpecification dataContainer = modelloader.loadDataSpecification();
        
        //final ContextHandler ch = new ContextHandler(id, dataContainer, logger);
        //ch.createPolicySet();
        
        return null;
	}
}
