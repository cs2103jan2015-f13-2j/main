package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import parser.CommandParser;
import validator.Validator;
import data.InitFileIO;

public class ValidatorTest {

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

	/**
	 * This method will test the validation of individual keyword.
	 */
	// @author A0111916M
	@Test
	public void testKeyword() {

		initTestEnvironment();

		String keyword = "add";

		boolean isKeyword = keywordValidator.validateKeyword(keyword);

		assertTrue(isKeyword);
	}

	/**
	 * This method will test for invalid individual keyword.
	 */
	// @author A0111916M
	@Test
	public void testInvalidKeyword() {

		initTestEnvironment();

		String command = "insert";

		boolean isKeyword = keywordValidator.validateKeyword(command);

		assertFalse(isKeyword);
	}

	/**
	 * This method will test for a valid keyword sequence for add command.
	 */
	// @author A0111916M
	@Test
	public void testKeywordSequenceAdd() {

		initTestEnvironment();

		List<String> keywordList = new ArrayList<String>();

		keywordList.add("add");
		keywordList.add("on");
		keywordList.add("from");
		keywordList.add("to");
		keywordList.add("priority");

		boolean isKeyword = keywordValidator
				.validateKeywordSequence(keywordList);

		assertTrue(isKeyword);
	}

	/**
	 * This method will test for a valid keyword sequence for retrieve command.
	 */
	// @author A0111916M
	@Test
	public void testKeywordSequenceRetrieve() {

		initTestEnvironment();

		List<String> keywordList = new ArrayList<String>();

		keywordList.add("retrieve");
		keywordList.add("priority");
		keywordList.add("from");
		keywordList.add("to");
		keywordList.add("at");

		boolean isKeyword = keywordValidator
				.validateKeywordSequence(keywordList);

		assertTrue(isKeyword);
	}

}
