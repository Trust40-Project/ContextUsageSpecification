package framework;

import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import tests.usecase1;
import tests.usecase2;
import tests.usecase3;
import tests.usecase4;
import tests.usecase5;
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
        junit.run(usecase2.class);
        junit.run(usecase3.class);
        junit.run(usecase4.class);
        junit.run(usecase5.class);
//        junit.run(usecase6.class);

        MyLogger.enable();
        MyLogger.info("Tests-End");

        return null;
    }
}
