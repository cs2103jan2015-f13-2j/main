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
import entity.Success;


public class Engine {
	

	//save task into database
	public  Success insertIntoFile(Task task) {
		
		Success status = null;
		
		FileIO dataStorage = new FileIO();
		status = dataStorage.saveIntoFile(task);

		return status;
	}
	
	//retrieve task from database
	public Task retrieveFromFile() {
		
		Task object = null;
		
//		to be implemented
		
		return object;
	}
	
	
	
	
}
