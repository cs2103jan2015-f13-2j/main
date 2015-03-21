package application;

import org.jnativehook.GlobalScreen;

import data.InitFileIO;

public class Start {

	public static void main(String[] args) {
		
		InitFileIO initFile = new InitFileIO();
		KeyListener listener = new KeyListener();

		listener.registerHook();
		GlobalScreen.addNativeKeyListener(listener);
	}

}