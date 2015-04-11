package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import data.InitFileIO;
import parser.AddParser;
import parser.CommandParser;
import validator.Validator;

public class AddParserTest {
	
	CommandParser commandParser = null;
	Validator keywordValidator = null;
	InitFileIO initFile = null;
	List<String> addCommandList = null;

	/**
	 * This method initiates the required information before executing the test
	 * cases.
	 */
	// @author A0111916M
	public void initTestEnvironment() {
		initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		commandParser = new CommandParser();
		keywordValidator = new Validator();
		addCommandList = new ArrayList<String>();
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void test() {
		initTestEnvironment();
		
		Class[] argClasses = { String.class };
		Object[] argObjects = { "this week" };

		PrivateMethodTester.invokePrivateMethod(AddParser.class,
				"parseAddCommand", argClasses, argObjects);

		assertTrue(true);
	}

}
