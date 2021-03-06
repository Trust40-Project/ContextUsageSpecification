package framework;

import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import setting.PreferenceHandler;
import tests.usecase1;
import tests.usecase10;
import tests.usecase11;
import tests.usecase2;
import tests.usecase3;
import tests.usecase4;
import tests.usecase5;
import tests.usecase6;
import tests.usecase7;
import tests.usecase8;
import tests.usecase9;
import util.Logger;

public class ButtonHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Objects.requireNonNull(event);

        Logger.info("Tests-Start");
        Logger.setActive(false);

        // Needed here to ensure default values are set if PreferencesPage has never been opened
        PreferenceHandler.setDefault();

        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(usecase1.class);
        junit.run(usecase2.class);
        junit.run(usecase3.class);
        junit.run(usecase4.class);
        junit.run(usecase5.class);
        junit.run(usecase6.class);
        junit.run(usecase7.class);
        junit.run(usecase8.class);
        junit.run(usecase9.class);
        junit.run(usecase10.class);
        junit.run(usecase11.class);

        Logger.setActive(true);
        Logger.info("Tests-End");

        return null;
    }
}
