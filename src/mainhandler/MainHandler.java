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

public class MainHandler {
	//TODO needed?
    //public static final boolean IS_ECLIPSE_RUNNING = Platform.isRunning();
    
	public void execute(String dataPath) {
        Objects.requireNonNull(dataPath);
                        
        ModelHandler modelloader = new ModelHandler(dataPath);
        DataSpecification dataSpec = modelloader.loadDataSpecification();
        
        final ContextHandler ch = new ContextHandler(dataSpec);
        
        ch.execute();
        
        modelloader.saveDataSpecification();
	}
}
