package util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * Helper functions and static strings
 * 
 * @author Thomas Lieb
 *
 */
public class Util {

    /**
     * 
     */
    public static String x;

    /**
     * @return Path of first active projects in workspace
     */
    public static String getCurrentDir() {
        IWorkspace ws = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot wsr = ws.getRoot();
        for (IProject p : wsr.getProjects()) {
            return p.getLocation().toString();
        }
        return "";
    }

    /**
     * @return List of all paths of current active projects in workspace
     */
    public static List<String> getAllDir() {
        ArrayList<String> list = new ArrayList<>();
        IWorkspace ws = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot wsr = ws.getRoot();
        for (IProject p : wsr.getProjects()) {
            list.add(p.getLocation().toString());
        }
        return list;
    }
}
