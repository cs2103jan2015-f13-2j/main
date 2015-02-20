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


public class Engine {
	

	//save task into database
	public static boolean insertIntoFile(Object task) throws IOException
	{
		FileIO dataStorage = new FileIO();
		dataStorage.saveIntoFile((Task) task);
		return true;
	}
	
	//retrieve task from database
	public Task retrieveFromFile()
	{
		Task object = null;
		return object;
	}
	
	
	
	
}
