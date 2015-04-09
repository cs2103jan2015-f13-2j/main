package application;

import org.jnativehook.GlobalScreen;

import data.InitFileIO;

public class Start {
	/**
	 *
	 * @param  	
	 * @return      
	 */
	//@author 
	public static void main(String[] args) {
		
		InitFileIO initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		KeyListener listener = new KeyListener();

		listener.registerHook();
		GlobalScreen.addNativeKeyListener(listener);
	}
}