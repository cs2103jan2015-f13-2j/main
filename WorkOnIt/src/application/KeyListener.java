package application;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.sun.javafx.application.PlatformImpl;

public class KeyListener implements NativeKeyListener {

	private static boolean ctrl = false;
	private static boolean space = false;
	private Main mainApp;
	private Logger logger;
	private Thread thread;

	public KeyListener() {
		mainApp = new Main();
		// disable jnativehook logging
		logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}

	public void nativeKeyPressed(NativeKeyEvent e) {

		String keyPressed = NativeKeyEvent.getKeyText(e.getKeyCode());

		if (keyPressed.equals("Space")) {
			space = true;
			if (ctrl == true) {

				new JFXPanel();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						new Main().start(new Stage());
					}
				});
			}
		}
		if (keyPressed.equals("Left Control")) {
			ctrl = true;
			if (space == true) {

				new JFXPanel();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						new Main().start(new Stage());
					}
				});
			}
		}
		if (keyPressed.equals("Escape")) {
			try {
				Platform.exit();
				System.out.println("die");

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
