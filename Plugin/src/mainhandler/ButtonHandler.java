package mainhandler;

import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import setting.PreferenceHandler;
import util.Logger;

/**
 * Connection between GUI and MainHandler
 * 
 * Referenced in plugin.xml
 * 
 * @author Thomas Lieb
 *
 */
public class ButtonHandler extends AbstractHandler {

    /**
     * Executed when GUI button is pressed
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Objects.requireNonNull(event);

        Logger.infoDetailed("ButtonHandler-Start");
        Logger.disable();

        // Needed here to ensure default values are set if PreferencesPage has never been opened
        PreferenceHandler.setDefault();

        String path = PreferenceHandler.getProjectPath();
        Logger.info2(path);
        new MainHandler().execute(path);

        Logger.enable();
        Logger.infoDetailed("ButtonHandler-End");

        return null;
    }
}
