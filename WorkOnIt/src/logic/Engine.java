package logic;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import data.FileIO;
import entity.NormalTask;
import entity.Task;
import entity.Error;


public class Engine {
	

	//save task into database
	public  Error insertIntoFile(Task task) {
		
		Error errorStatus = null;
		
		FileIO dataStorage = new FileIO();
		errorStatus = dataStorage.saveIntoFile(task);

		return errorStatus;
	}
	
	//retrieve task from database
	public Task retrieveFromFile() {
		
		Task object = null;
		
//		to be implemented
		
		return object;
	}
	
	
	
	
}
