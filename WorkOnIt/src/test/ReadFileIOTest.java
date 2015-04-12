package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;

import data.InitFileIO;
import data.ReadFileIO;
import parser.DataParser;
import parser.RetrieveParser;
import resource.FileName;
import resource.KeywordConstant;
import validator.Validator;
import entity.Success;

public class ReadFileIOTest {

	InitFileIO initFile = null;
	ReadFileIO readFile = null;

	/**
	 * This method test if can get a correct file name with the following
	 * keyword correctly.
	 */
	//@author A0112694E
	@Test
	public void getNormalFileWithKeyword() {

		initTestEnvironment();

		String keyword = KeywordConstant.KEYWORD_NORMAL_TASK;
		String fileName = FileName.getFilenameNormal();

		executeTestEquals(keyword, fileName);
	}

	/**
	 * This method test if can get a correct file name with the following
	 * keyword correctly.
	 */
	@Test
	public void getDeadlineFileWithKeyword() {

		initTestEnvironment();

		String keyword = KeywordConstant.KEYWORD_DEADLINE_TASK;
		String fileName = FileName.getFilenameDeadline();

		executeTestEquals(keyword, fileName);
	}

	/**
	 * This method test if can get a correct file name with the following
	 * keyword correctly.
	 */
	@Test
	public void getFloatingFileWithKeyword() {

		initTestEnvironment();

		String keyword = KeywordConstant.KEYWORD_FLOATING_TASK;
		String fileName = FileName.getFilenameFloating();

		executeTestEquals(keyword, fileName);
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
		readFile = new ReadFileIO();

	}

	/**
	 * This method test perform the necessary steps to publicize a method and
	 * pass in the parameter. It also test the expected output against the
	 * obtained output.
	 */
	private void executeTestEquals(String keyword, String fileName) {

		@SuppressWarnings("rawtypes")
		Class[] argClasses = { String.class };
		Object[] argObjects = { keyword };

		String expectedFileName = invokePrivateMethod(ReadFileIO.class,
				"getFileTypeWithKeyword", argClasses, argObjects);

		assertEquals(expectedFileName, fileName);

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
	 * @return String
	 */
	//@author A0111837J
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String invokePrivateMethod(Class targetClass, String methodName,
			Class[] argClasses, Object[] argObjects) {

		String status = null;

		try {

			Object t = targetClass.getDeclaredConstructor().newInstance();

			Method method = targetClass.getDeclaredMethod(methodName,
					argClasses);
			System.out.println(method.getName());
			method.setAccessible(true);

			t = method.invoke(t, argObjects);

			status = (String) t;

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
