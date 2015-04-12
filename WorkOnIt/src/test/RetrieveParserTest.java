package test;

import static org.junit.Assert.*;

import org.junit.Test;

import data.InitFileIO;
import parser.AddParser;
import parser.RetrieveParser;
import entity.Success;
import entity.Task;

public class RetrieveParserTest {

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
	private void executeTestTrue(String command) {

		@SuppressWarnings("rawtypes")
		Class[] argClasses = { String.class };
		Object[] argObjects = { command };

		Success status = PrivateMethodTester.invokePrivateMethod(
				RetrieveParser.class, "parseRetrieveCommand", argClasses,
				argObjects);

		assertTrue(status.isSuccess());
	}

	/**
	 * This method test if can retrieve a task with the following keyword
	 * correctly.
	 */
	// @author A0111837J
	@Test
	public void retrieveKeyword() {
		
		initTestEnvironment();
		
		String command = "test";

		executeTestTrue(command);
	}

}
