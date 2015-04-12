package application;

import java.util.logging.Handler;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;

import data.InitFileIO;

public class Start {

	private static final Logger LOGGER = Logger
			.getLogger(Start.class.getName());

	/**
	 * The starting point of this application
	 * 
	 * @param args
	 */
	//@author A0111837J
	public static void main(String[] args) {

		setLogging(false);

		InitFileIO initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		KeyListener listener = new KeyListener();

		listener.registerHook();
		GlobalScreen.addNativeKeyListener(listener);
	}

	/**
	 * May enable or disable logging.
	 * 
	 * @param isLogging
	 *            boolean value to enable or disable logging
	 */
	private static void setLogging(boolean isLogging) {

		if (!isLogging) {
			LOGGER.setUseParentHandlers(false);

			Logger globalLogger = Logger.getLogger("global");
			Handler[] handlers = globalLogger.getHandlers();
			for (Handler handler : handlers) {
				globalLogger.removeHandler(handler);
			}
		}
	}
}