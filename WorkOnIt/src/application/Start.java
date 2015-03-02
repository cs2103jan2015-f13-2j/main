package application;

import org.jnativehook.GlobalScreen;

public class Start {

	public static void main(String[] args) {

		KeyListener listener = new KeyListener();

		listener.registerHook();
		GlobalScreen.addNativeKeyListener(listener);
	}

}