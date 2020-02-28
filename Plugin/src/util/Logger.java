package util;

/**
 * Logger Class to print to system.out
 * 
 * @author Thomas Lieb
 *
 */
public class Logger {

    private static Logger instance = null;
    private static boolean isActive = true;

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public static void error(String string) {
        getInstance().println2(string);
    }

    public static void infoDetailed(String string) {
        getInstance().println(string);
    }

    private void println(String string) {
        // LOGGER.info(string);
        if (isActive) {
            System.out.println(string);
        }
    }

    public static void info2(String string) {
        getInstance().println2(string);
    }

    private void println2(String string) {
        // LOGGER.info(string);
        System.out.println(string);
    }

    public static void disable() {
        isActive = false;
    };

    public static void enable() {
        isActive = true;
    };
}
