package application;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener {

	private static boolean ctrl = false;
	private static boolean space = false;
	private static boolean isStarted = false;
	private Main mainApp;
	private Logger logger;
	private Thread thread;
	public KeyListener() {
		mainApp = new Main();
		// disable jnativehook logging
		logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}

	@SuppressWarnings("deprecation")
	public void nativeKeyPressed(NativeKeyEvent e) {
	
		String keyPressed = NativeKeyEvent.getKeyText(e.getKeyCode());
	
		
		if (keyPressed.equals("Space")) {
			space = true;
			if (ctrl == true) {
				System.out.println("Hotkey Activated");
				thread = new Thread(mainApp);
				thread.start();
			}
		}
		if (keyPressed.equals("Left Control")) {
			ctrl = true;
			if (space == true) {
				System.out.println("Hotkey Activated");
				thread = new Thread(mainApp);
				thread.start();
			}
		}
		if (keyPressed.equals("Escape")) {
			try {
				System.out.println("Killing application");
				//Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				//Iterator iter = threadSet.iterator();
				
				
				//deprecated but able to stop for now
				thread.stop(); 
			
				//mainApp.hide();
				//Platform.exit();
			} catch (Exception e1) {
				e1.printStackTrace();
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
			System.out.println("key hook registered successfully");
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.out.println("hook registration fail");

		}
	}

	// @Override
	// public void update(Observable o, Object arg) {
	// Platform.runLater(new Runnable() {
	// public void run() {
	// new Main().start(new Stage());
	// }
	// });
	// }

}
