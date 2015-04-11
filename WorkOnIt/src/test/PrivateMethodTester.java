package test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import entity.Success;

public class PrivateMethodTester {

	/**
	 * This method initiates the required information before executing the test
	 * cases.
	 */
	/**
	 * @param targetClass
	 *            the class of the method
	 * @param methodName
	 *            name of the method that want to be publicized
	 * @param argClasses
	 *            classes of the objects that is to be passed into the method
	 * @param argObjects
	 *            objects that is to be passed into the method
	 * @return Success object
	 */
	// @author A0111916M
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Success invokePrivateMethod(Class targetClass,
			String methodName, Class[] argClasses, Object[] argObjects) {

		Success status = null;

		try {
			Object t = targetClass.getDeclaredConstructor(Map.class)
					.newInstance(new HashMap<String, String>());
			Method method = targetClass.getDeclaredMethod(methodName,
					argClasses);
			System.out.println(method.getName());
			method.setAccessible(true);

			t = method.invoke(t, argObjects);

			status = (Success) t;

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
		} catch (InstantiationException e) {
			System.err.println(e.getMessage());
		}

		return status;
	}
}
