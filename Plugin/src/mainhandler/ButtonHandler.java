package mainhandler;

import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import setting.PreferenceHandler;
import util.MyLogger;

public class ButtonHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Objects.requireNonNull(event);

        MyLogger.info("ButtonHandler-Start");
        MyLogger.disable();

        PreferenceHandler.setDefault();

        String path = PreferenceHandler.getProjectPath();
        MyLogger.info2(path);
        new MainHandler().execute(path);

        MyLogger.enable();
        MyLogger.info("ButtonHandler-End");

        return null;
    }
}
