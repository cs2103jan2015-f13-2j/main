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

	private static boolean isHotkeyFirst = false;
	private static boolean isHotkeySecond = false;
	private static String hotkeyValueFirst = "Space";
	private static String hotkeyValueSecond = "Left Control";
	private static String hotkeyValueClose = "Escape";
	private Logger logger;

	public KeyListener() {
		// disable jnativehook logging
		logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}

	public void nativeKeyPressed(NativeKeyEvent e) {

		String keyPressed = NativeKeyEvent.getKeyText(e.getKeyCode());

		if (keyPressed.equals(hotkeyValueSecond)) {
			isHotkeySecond = true;
			if (isHotkeyFirst == true) {
				new JFXPanel();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						new Main().start(new Stage());
					}
				});
			}
		}
		if (keyPressed.equals(hotkeyValueFirst)) {
			isHotkeyFirst = true;
			if (isHotkeySecond == true) {
				
				new JFXPanel();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						new Main().start(new Stage());
					}
				});
			}
		}
		if (keyPressed.equals(hotkeyValueClose)) {
			try {
//				 Platform.exit();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(hotkeyValueSecond)) {
			isHotkeySecond = false;
		}
		if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals(hotkeyValueFirst)) {
			isHotkeyFirst = false;
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
			System.exit(1);
		} catch (Exception e) {
			System.out.println("hook registration fail");
			System.exit(1);
		}
	}
}
