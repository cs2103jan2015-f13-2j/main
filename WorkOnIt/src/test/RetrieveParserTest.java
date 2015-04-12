package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;

import data.InitFileIO;
import parser.DataParser;
import parser.RetrieveParser;
import validator.Validator;
import entity.Success;

public class RetrieveParserTest {

	InitFileIO initFile = null;

	/**
	 * This method test if can retrieve a task with the following keyword
	 * correctly.
	 */
	//@author A0111837J
	@Test
	public void retrieveKeyword() {

		initTestEnvironment();

		String command = "test";

		executeTestTrue(command);
	}

	/**
	 * This method test if can retrieve a task with a priority within a date
	 * range.
	 */
	@Test
	public void retrieveKeywordPiorityDateRange() {

		initTestEnvironment();

		String command = "priority 2 from 10 April to 12 April";

		executeTestTrue(command);
	}

	/**
	 * This method test if can retrieve a task with a priority within a date
	 * range.
	 */
	@Test
	public void retrieveKeywordDateRange() {

		initTestEnvironment();

		String command = "from 10 April to 12 April";

		executeTestTrue(command);
	}

	/**********************
	 * NON-TEST METHODS
	 *********************/

	/**
	 * This method initiates the required information before executing the test
	 * cases.
	 */
	//@author A0111916M
	public void initTestEnvironment() {
		initFile = new InitFileIO();
		initFile.checkAndProcessFile();
	}

	/**
	 * This method test perform the necessary steps to publicize a method and
	 * pass in the parameter. It also test the expected output against the
	 * obtained output.
	 */
	private void executeTestTrue(String command) {

		@SuppressWarnings("rawtypes")
		Class[] argClasses = { String.class };
		Object[] argObjects = { command };

		Success status = invokePrivateMethod(RetrieveParser.class,
				"parseRetrieveCommand", argClasses, argObjects);
		System.out.println(status == null);
		assertTrue(status.isSuccess());
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
	//@author A0111837J
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Success invokePrivateMethod(Class targetClass, String methodName,
			Class[] argClasses, Object[] argObjects) {

		Success status = null;

		try {
			Validator validator = new Validator();
			Object t = targetClass.getDeclaredConstructor(Map.class,
					DataParser.class).newInstance(
					validator.getKeywordFullMap(), new DataParser());
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
