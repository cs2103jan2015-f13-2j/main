package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;

import data.InitFileIO;
import entity.Success;
import entity.Task;
import parser.AddParser;
import validator.Validator;

public class AddParserTest {

	InitFileIO initFile = null;

	/**
	 * This method test the addition of normal task correctly.
	 */
	//@author A0111916M
	@Test
	public void addNormalTask() {
		initTestEnvironment();

		String command = "this week from 12 April to 13 April";
		String expected = "this week from 12 Apr 2015,  12:00:01 AM to 13 Apr 2015,  11:59:59 PM";

		executeTestEquals(command, expected);
	}

	/**
	 * This method test the addition of deadline task correctly.
	 */

	@Test
	public void addDeadlineTask() {
		initTestEnvironment();

		String command = "this week by 13 April";
		String expected = "this week by 13 Apr 2015,  11:59:59 PM";

		executeTestEquals(command, expected);
	}

	/**
	 * This method test the addition of floating task correctly.
	 */

	@Test
	public void addFloatingTask() {
		initTestEnvironment();

		String command = "this week";
		String expected = "this week";

		executeTestEquals(command, expected);
	}

	/**********************
	 * NON-TEST METHODS
	 *********************/

	/**
	 * This method initiates the required information before executing the test
	 * cases.
	 */

	public void initTestEnvironment() {
		initFile = new InitFileIO();
		initFile.checkAndProcessFile();
	}

	/**
	 * This method test perform the necessary steps to publicize a method and
	 * pass in the parameter. It also test the expected output against the
	 * obtained output.
	 */

	private void executeTestEquals(String command, String expected) {

		@SuppressWarnings("rawtypes")
		Class[] argClasses = { String.class };
		Object[] argObjects = { command };

		Success status = invokePrivateMethod(AddParser.class,
				"parseAddCommand", argClasses, argObjects);

		Task task = (Task) status.getObj();
		assertEquals(expected, task.toDisplay());
	}

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
	//@author f13-2j-reused
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Success invokePrivateMethod(Class targetClass,
			String methodName, Class[] argClasses, Object[] argObjects) {

		Success status = null;

		try {
			Validator validator = new Validator();
			Object t = targetClass.getDeclaredConstructor(Map.class)
					.newInstance(validator.getKeywordFullMap());
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
