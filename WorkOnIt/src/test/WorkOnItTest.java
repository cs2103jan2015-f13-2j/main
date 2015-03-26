package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Scanner;

import logic.Validator;
import data.InitFileIO;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import resource.Message;
import entity.FloatingTask;
import entity.Success;
import entity.Task;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkOnItTest {
	Validator testValidator = null;
	InitFileIO initFile = null;
	
	private ArrayList addItems (ArrayList<Task> list, int num){
		
		for(int i=0;i<num;i++){
			list.add(new FloatingTask("do homework",1));
		}
		return list;
	}
	 
	public void initTestEnvironment(){
		initFile = new InitFileIO();
		initFile.checkAndProcessFile();
		testValidator = new Validator();
	}
	
	private void executeTestEquals(String testInput, Success expectedSuccess) {		
		Scanner sc = new Scanner(testInput);
		Success testSuccess = null;
		
		testSuccess = testValidator.parseCommand(testInput);
		
		assertEquals(testSuccess, expectedSuccess);
	}

	@Test
	public void test1RetrieveEmpty() {
		initTestEnvironment();
		String testInput = "retrieve all";
		
		Success expectedSuccess = new Success(true, Message.SUCCESS_RETRIEVE_LIST);
		ArrayList <Task> testList = new ArrayList<Task>();
		addItems(testList, 0);
		expectedSuccess.setObj(testList);
		executeTestEquals(testInput, expectedSuccess);
	}
	
	@Test
	public void test2Add() {
		initTestEnvironment();
		String testInput = "add do homework";

		
		Success expectedSuccess = new Success(true, Message.SUCCESS_ADDED);
		
		executeTestEquals(testInput, expectedSuccess);
	}
	
	@Test
	public void test3Retrieve() {
		initTestEnvironment();
		String testInput = "retrieve all";
		
		Success expectedSuccess = new Success(true, Message.SUCCESS_RETRIEVE_LIST);
		ArrayList <Task> testList = new ArrayList<Task>();
		addItems(testList, 1);
		expectedSuccess.setObj(testList);
		executeTestEquals(testInput, expectedSuccess);
	}
	
	@Test
	public void test4Delete() {
		initTestEnvironment();
		String testInput = "delete 1";
		ArrayList <Task> testList = new ArrayList<Task>();
		
		testValidator.parseCommand("retrieve all");
		Success expectedSuccess = new Success(null, true, Message.SUCCESS_DELETE);
		
		executeTestEquals(testInput, expectedSuccess);
	}

}
