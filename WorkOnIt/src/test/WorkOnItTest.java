package test;

import static org.junit.Assert.*;
import logic.Validator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import resource.Message;
import entity.Success;

public class WorkOnItTest {
	Validator testValidator = new Validator();
	
	private void executeTestEquals(String testInput, Success expectedSuccess) {
		Success testSuccess = testValidator.parseCommand(testInput);
		System.out.println(testInput);
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
