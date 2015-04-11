package application;

import org.jnativehook.GlobalScreen;

import data.InitFileIO;

public class Start {

	/**
	 * The starting point of this application
	 * 
	 * @param args
	 */
	// @author A0111837J
	public static void main(String[] args) {

		InitFileIO initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		KeyListener listener = new KeyListener();

		listener.registerHook();
		GlobalScreen.addNativeKeyListener(listener);
	}
}