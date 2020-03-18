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
	private static boolean isDetailed = false;

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public static void error(String string) {
        getInstance().printError(string);
    }

    public static void info(String string) {
        getInstance().printInfo(string);
    }

    public static void infoDetailed(String string) {
        getInstance().printDetailed(string);
    }

    private void printError(String string) {
        System.out.println(string);
    }

    private void printInfo(String string) {
        if (isActive) {
            System.out.println(string);
        }
    }

    private void printDetailed(String string) {
        if (isActive) {
        	if(isDetailed) {
                System.out.println(string);
        	}
        }
    }

    public static void disable() {
        isActive = false;
    };

    public static void enable() {
        isActive = true;
    };
}
