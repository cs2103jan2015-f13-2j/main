package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PrivateMethodTester {

	@SuppressWarnings("rawtypes")
	public static void invokePrivateMethod(Class targetClass,
			String methodName, Class[] argClasses, Object[] argObjects) {

		try {
			@SuppressWarnings("unchecked")
			Method method = targetClass.getDeclaredMethod(methodName,
					argClasses);
			method.setAccessible(true);
			method.invoke(targetClass, argObjects);
		} catch (NoSuchMethodException e) {
			System.err.println(e.getMessage());
		} catch (SecurityException e) {
			System.err.println(e.getMessage());
		} catch (IllegalAccessException e) {
			System.err.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		} catch (InvocationTargetException e) {
			System.err.println(e.getMessage());
		}
	}
}
