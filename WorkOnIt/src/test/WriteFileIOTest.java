package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.junit.Test;

import data.InitFileIO;
import data.ReadFileIO;
import data.WriteFileIO;
import parser.DataParser;
import parser.RetrieveParser;
import resource.FileName;
import resource.KeywordConstant;
import validator.Validator;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.Success;
import entity.Task;

public class WriteFileIOTest {

	InitFileIO initFile = null;
	ReadFileIO readFile = null;

	/**
	 * This method test if can get a correct file name with the task type
	 * correctly.
	 */
	//@author A0112694E
	@Test
	public void getNormalFileWithTaskType() {

		initTestEnvironment();

		Date startDate = new Date();
		startDate.setHours(0);
		Date endDate = new Date();
		endDate.setHours(1);

		NormalTask normalTask = new NormalTask("eat noodle", 1, startDate,
				endDate);
		String fileName = FileName.getFilenameNormal();

		executeTestEquals(normalTask, fileName);
	}

	/**
	 * This method test if can get a correct file name with the task type
	 * correctly.
	 */
	@Test
	public void getDeadlineFileWithTaskType() {

		initTestEnvironment();

		Date date = new Date();
		date.setHours(0);

		DeadlineTask deadlineTask = new DeadlineTask("cook noodle", 1, date);
		String fileName = FileName.getFilenameDeadline();

		executeTestEquals(deadlineTask, fileName);
	}

	/**
	 * This method test if can get a correct file name with the task type
	 * correctly.
	 */
	@Test
	public void getFloatingFileWithTaskType() {

		initTestEnvironment();

		FloatingTask floatingTask = new FloatingTask("sell noodle", 0);
		String fileName = FileName.getFilenameFloating();

		executeTestEquals(floatingTask, fileName);
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
	private void executeTestEquals(Object obj, String fileName) {

		@SuppressWarnings("rawtypes")
		Class[] argClasses = { Task.class };
		Object[] argObjects = { obj };

		String expectedFileName = invokePrivateMethod(WriteFileIO.class,
				"getFileType", argClasses, argObjects);

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
	//@author f13-2j-reused
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
