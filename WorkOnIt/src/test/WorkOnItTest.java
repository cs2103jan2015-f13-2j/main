package test;

import static org.junit.Assert.*;
import logic.Validator;
import data.InitFileIO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import resource.Message;
import entity.Success;

public class WorkOnItTest {

	
	 

	private void executeTestEquals(String testInput, Success expectedSuccess) {
		
		Validator testValidator = new Validator();
		System.out.println(testInput);
		InitFileIO initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		
		Success testSuccess = testValidator.parseCommand(testInput);
		
		assertEquals(testSuccess, expectedSuccess);
	}

	
	@Test
	public void testAdd() {
		String testInput = "add do homework";
		
		Success expectedSuccess = new Success(true, Message.SUCCESS_ADDED);
		
		executeTestEquals(testInput, expectedSuccess);
	}
	/*
	@Test
	public void testSearchAfterAddWord() {
		String testInput = "search there";
		String testOutput = "1. alien there\n";
		testOutput += "3. hi there\n";
		executeTestEquals(testInput, testOutput);
	}
	*/
}
