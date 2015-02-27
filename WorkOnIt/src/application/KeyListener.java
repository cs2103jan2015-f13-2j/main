package application;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyListener implements NativeKeyListener{
	
	private static boolean ctrl = false;
	private static boolean space = false;
	public void nativeKeyPressed(NativeKeyEvent e) {
          System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
          if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Space")){
        	  space = true;
        	  if(ctrl==true){
        		  System.out.println("Hotkey Activated");
        	  }
          }
          if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Left Control")){
        	  ctrl = true;
        	  if(space==true){
        		  System.out.println("Hotkey Activated");
        	  }
          }
         
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
        if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Space")){
        	space = false;
        }
        if(NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Left Control")){
        	ctrl = false;
        }
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
          System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
	}
	
	
	public void registerHook(){
		try {
			System.out.println("hey");
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			System.out.println("hook registration fail");
			
		}
		
	}
	
}
