package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class PrivateMethodTester {

	@SuppressWarnings("rawtypes")
	public static void invokePrivateMethod(Class targetClass,
			String methodName, Class[] argClasses, Object[] argObjects) {

		try {
			Object t = targetClass.getDeclaredConstructor(Map.class).newInstance(new HashMap<String, String>());
			Method method = targetClass.getDeclaredMethod(methodName, argClasses);
			System.out.println(method.getName());
			method.setAccessible(true);
						
			t = method.invoke(t, argObjects);
			System.out.println(t.toString());
		
		} catch (NoSuchMethodException e) {
			System.err.println("0"+e.getMessage());
		} catch (SecurityException e) {
			System.err.println("1"+e.getMessage());
		} catch (IllegalAccessException e) {
			System.err.println("2"+e.getMessage());
		} catch (IllegalArgumentException e) {
			System.err.println("3"+e.getMessage());
		} catch (InvocationTargetException e) {
			System.err.println("4"+e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
