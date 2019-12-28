package framework;


import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import tests.SimpleTest;
import util.MyLogger;


public class ButtonHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        Objects.requireNonNull(event);
        
        MyLogger.info("Tests-Start");

    	//TODO do properly, currently use active workspace, only 1 project allowed
//        final String currentPath = Util.getCurrentDir();        
//        MyLogger.info(currentPath);
//        
//        //Call Mainhandler with datapath
//        new MainHandler().execute(currentPath);

        JUnitCore junit = new JUnitCore(); 
        junit.addListener(new TextListener(System.out)); 
        junit.run(SimpleTest.class);

        MyLogger.info("Tests-End");
        
        return null;
	}
}
