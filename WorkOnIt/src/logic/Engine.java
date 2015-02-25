package logic;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import data.FileIO;
import entity.DeadlineTask;
import entity.FloatingTask;
import entity.NormalTask;
import entity.RecurrenceTask;
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
	
	//retrieve all task from database
	public ArrayList<Task> retrieveTask() throws IOException {
			ArrayList<Task> taskList = new ArrayList<Task>();
	
			FileIO dataStorage = new FileIO();
	
			taskList.addAll(dataStorage.loadFromFileFloatingTask());
			taskList.addAll(dataStorage.loadFromFileNormalTask());
			taskList.addAll(dataStorage.loadFromFileRecurTask());
			taskList.addAll(dataStorage.loadFromFileDeadlineTask());
			
			return taskList;
		}
	
	
	// retrieve selected task entity from database. 
	public ArrayList<Task> retrieveTask(Task task) throws IOException {
		
		Task object = null;
		ArrayList<Task> taskList = new ArrayList<Task>();
		FileIO dataStorage = new FileIO();
		
		if(task instanceof FloatingTask)
		{
			taskList = dataStorage.loadFromFileFloatingTask();
		};
		if(task instanceof NormalTask)
		{
			taskList = dataStorage.loadFromFileNormalTask();
		};
		if(task instanceof RecurrenceTask)
		{
			taskList = dataStorage.loadFromFileRecurTask();
		};
		if(task instanceof DeadlineTask)
		{
			taskList = dataStorage.loadFromFileDeadlineTask();
		};
		
		return taskList;
	}
	
	//retrieve task with specific START DATE
	//Affected File > NormalTask, DeadlineTask, RecurTask
	public ArrayList<Task> retrieveTask(Date date) throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		FileIO dataStorage = new FileIO();
		
		taskList = dataStorage.loadFromStartDate(date);
		
		return taskList;
	}
	
	//retrieve task with specific INBETWEEN DATE 
	//Affected File > NormalTask, DeadlineTask, RecurTask
	public ArrayList<Task> retrieveTask(Date startDate , Date endDate) throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		FileIO dataStorage = new FileIO();
		
		taskList = dataStorage.loadFromBetweenDate(startDate,endDate);
		
		return taskList;
	}
	
	//retrieve task with specific priority (EG. URGENT)
	//Affected File > NormalTask, DeadlineTask, RecurTask
	public ArrayList<Task> retrieveTask(int priority) throws IOException {
			
			ArrayList<Task> taskList = new ArrayList<Task>();
			FileIO dataStorage = new FileIO();
			
			taskList = dataStorage.loadFromPriority(priority);
			
			return taskList;
	}
	
	//retrieve task with specific task and date (EG. URGENT)
	//Affected File > NormalTask, DeadlineTask, RecurTask
	public ArrayList<Task> retrieveTask(Task task, Date date) throws IOException {
				
				ArrayList<Task> taskList = new ArrayList<Task>();
				FileIO dataStorage = new FileIO();
				
				taskList = dataStorage.loadFromStartDateWithTask(task,date);
				
				return taskList;
		}
	
	//retrieve task with specific priority (EG. URGENT)
	//Affected File > NormalTask, DeadlineTask, RecurTask
	public ArrayList<Task> retrieveTask(Task task, Date startDate , Date endDate) throws IOException {
					
				ArrayList<Task> taskList = new ArrayList<Task>();
				FileIO dataStorage = new FileIO();
					
				taskList = dataStorage.loadFromBetweenDateWithTask(task,startDate, endDate);
					
				return taskList;
		}
	
	
}