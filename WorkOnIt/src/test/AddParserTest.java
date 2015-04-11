package test;

import static org.junit.Assert.*;

import org.junit.Test;

import data.InitFileIO;
import entity.Success;
import entity.Task;
import parser.AddParser;

public class AddParserTest {

	InitFileIO initFile = null;

	/**
	 * This method initiates the required information before executing the test
	 * cases.
	 */
	// @author A0111916M
	public void initTestEnvironment() {
		initFile = new InitFileIO();
		initFile.checkAndProcessFile();
	}

	/**
	 * This method test perform the necessary steps to publicize a method and
	 * pass in the parameter. It also test the expected output against the
	 * obtained output.
	 */
	// @author A0111916M
	private void executeTestEquals(String command, String expected) {

		@SuppressWarnings("rawtypes")
		Class[] argClasses = { String.class };
		Object[] argObjects = { command };

		Success status = PrivateMethodTester.invokePrivateMethod(
				AddParser.class, "parseAddCommand", argClasses, argObjects);

		Task task = (Task) status.getObj();
		assertEquals(expected, task.toDisplay());
	}

	/**
	 * This method test the addition of normal task correctly.
	 */
	// @author A0111916M
	@Test
	public void addNormalTask() {
		initTestEnvironment();

		String command = "this week from today to tomorrow";

		executeTestEquals(command, command);
	}

	/**
	 * This method test the addition of deadline task correctly.
	 */
	// @author A0111916M
	@Test
	public void addDeadlineTask() {
		initTestEnvironment();

		String command = "this week by tomorrow";

		executeTestEquals(command, command);
	}

	/**
	 * This method test the addition of floating task correctly.
	 */
	// @author A0111916M
	@Test
	public void addFloatingTask() {
		initTestEnvironment();

		String command = "this week";

		executeTestEquals(command, command);
	}

}
