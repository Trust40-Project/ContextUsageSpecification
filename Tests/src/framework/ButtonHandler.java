package framework;


import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import tests.usecase1;
import util.MyLogger;


public class ButtonHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        Objects.requireNonNull(event);
        
        MyLogger.info("Tests-Start");
        MyLogger.disable();

        JUnitCore junit = new JUnitCore(); 
        junit.addListener(new TextListener(System.out)); 
        junit.run(usecase1.class);

        MyLogger.enable();
        MyLogger.info("Tests-End");
        
        return null;
	}
}
