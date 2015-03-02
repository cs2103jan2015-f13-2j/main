package application;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {

	private static boolean ctrl = false;
	private static boolean space = false;

	public void nativeKeyPressed(NativeKeyEvent e) {
		
		String keyPressed = NativeKeyEvent.getKeyText(e.getKeyCode());
		
		if (keyPressed.equals("Space")) {
			space = true;
			if (ctrl == true) {
				System.out.println("Hotkey Activated");
			}
		}
		if (keyPressed.equals("Left Control")) {
			ctrl = true;
			if (space == true) {
				System.out.println("Hotkey Activated");
			}
		}

	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Space")) {
			space = false;
		}
		if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Left Control")) {
			ctrl = false;
		}
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		
	}

	public void registerHook() {
		try {
			GlobalScreen.registerNativeHook();
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF); 
		} catch (NativeHookException e) {
			System.out.println("hook registration fail");

		}

	}

}
