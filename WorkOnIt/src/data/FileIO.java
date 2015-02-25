package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ArrayList;

import com.google.gson.Gson;

import entity.FloatingTask;
import entity.NormalTask;
import entity.DeadlineTask;
import entity.RecurrenceTask;
import entity.Task;
import entity.Success;

public class FileIO {
	
	
	final static String FILE_NAME_DEADLINE = "datafile_deadLine.txt";
	final static String FILE_NAME_FLOATING = "datafile_floating.txt";
	final static String FILE_NAME_NORMAL = "datafile_normal.txt";
	final static String FILE_NAME_RECUR = "datafile_recur.txt";
	final static String FILE_NAME = "datafile.txt";
	private static String file_type;
	
	
	public Success saveIntoFile(Task task) {
		
		Success status = null;
		
		if(task instanceof FloatingTask)
		{
			file_type = FILE_NAME_FLOATING;
		};
		if(task instanceof NormalTask)
		{
			file_type = FILE_NAME_NORMAL;
		};
		if(task instanceof RecurrenceTask)
		{
			file_type = FILE_NAME_RECUR ;
		};
		if(task instanceof DeadlineTask)
		{
			file_type = FILE_NAME_DEADLINE;
		};
		
		
		
		//body
		String gsonSerial = serializeToJson(task);
		System.out.println(gsonSerial);	
		PrintWriter filewrite = null;
		
		
		
		try {
			filewrite = new PrintWriter(new BufferedWriter(
					new FileWriter(file_type, true)));
			filewrite.println(gsonSerial);
			
			status = new Success(true, null);
			
		} catch (IOException e) {
			System.err.println("saveIntoFile: IO error. Please check R/W/X access.");
			status = new Success(false, "saveIntoFile: IO error. Please check R/W/X access.");
		} finally {
			filewrite.close();
		}	
		
		return status;
	}
	
	
	public ArrayList<Task> loadFromFileFloatingTask() throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		
		BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_FLOATING));
		String printLine;
		
		while ((printLine = reader.readLine()) != null) 
		{
				FloatingTask task = (FloatingTask) deserializeFromJson(printLine, FloatingTask.class);
				taskList.add(task);
		}	
			
		return taskList;
	}
	
	public ArrayList<Task> loadFromFileNormalTask() throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
				
		BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_NORMAL));
		String printLine;
		
		while ((printLine = reader.readLine()) != null) 
		{
				NormalTask task = (NormalTask) deserializeFromJson(printLine, NormalTask.class);
				taskList.add(task);
		}	
		
		
		return taskList;
	}
	
	public ArrayList<Task> loadFromFileDeadlineTask() throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
				
		BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_DEADLINE));
		String printLine;
		
		while ((printLine = reader.readLine()) != null) 
		{
				DeadlineTask task = (DeadlineTask) deserializeFromJson(printLine, DeadlineTask.class);
				taskList.add(task);
		}	
		
		
		return taskList;
	}

	public ArrayList<Task> loadFromFileRecurTask() throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
				
		BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_RECUR));
		String printLine;
		
		while ((printLine = reader.readLine()) != null) 
		{
			RecurrenceTask task = (RecurrenceTask) deserializeFromJson(printLine, RecurrenceTask.class);
			taskList.add(task);
		}	
		
		return taskList;
	}
	
	
	
	public ArrayList<Task> loadFromStartDate(Date date) throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
				
		BufferedReader recurReader = new BufferedReader(new FileReader(FILE_NAME_RECUR));
		BufferedReader deadlineReader = new BufferedReader(new FileReader(FILE_NAME_DEADLINE));
		BufferedReader normalReader = new BufferedReader(new FileReader(FILE_NAME_NORMAL));
		
		String printLine;
		
		while ((printLine = normalReader.readLine()) != null) 
		{
				NormalTask task = (NormalTask) deserializeFromJson(printLine, NormalTask.class);
				
				if(task.getStartDateTime().getDate() == date.getDate() &&
						task.getStartDateTime().getMonth() == date.getMonth() &&
						 task.getStartDateTime().getYear() == date.getYear() )
				{
					
					taskList.add(task);
				}		
		}	
		
		while ((printLine = deadlineReader.readLine()) != null) 
		{
			    DeadlineTask task = (DeadlineTask) deserializeFromJson(printLine, DeadlineTask.class);
				// if haven't reach deadline yet.
			    if(task.getDeadline().compareTo(date) > 0)
				{
					taskList.add(task);
				}		
		}	
		
		while ((printLine = recurReader.readLine()) != null) 
		{
				RecurrenceTask task = (RecurrenceTask) deserializeFromJson(printLine, RecurrenceTask.class);
				if(task.getStartRecurrenceDate().getDate() == date.getDate() &&
						task.getStartRecurrenceDate().getMonth() == date.getMonth() &&
						 task.getStartRecurrenceDate().getYear() == date.getYear() )
				{
					taskList.add(task);
				}		
		}	
		
		return taskList;
	}
	
	//Issues : Change to check startDate or endDate fall inbetween.
	//Issues : startDate and endDate should'nt indicate the exact date timing. (Should set to day_end_value )
	//Issues : recurrenceTask endRecurrenceDate should have default value.
	//Issues : we need priority and date.
	//Issues : RecurrenceRetrieve not accurate. Because of various cases such as no default endRecurrence value
	
	public ArrayList<Task> loadFromBetweenDate(Date startDate , Date endDate) throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		
				
		BufferedReader recurReader = new BufferedReader(new FileReader(FILE_NAME_RECUR));
		BufferedReader deadlineReader = new BufferedReader(new FileReader(FILE_NAME_DEADLINE));
		BufferedReader normalReader = new BufferedReader(new FileReader(FILE_NAME_NORMAL));
		
		String printLine;
		
		while ((printLine = normalReader.readLine()) != null) 
		{
				NormalTask task = (NormalTask) deserializeFromJson(printLine, NormalTask.class);
				if(task.getStartDateTime().compareTo(startDate) > 0
						&& task.getStartDateTime().compareTo(endDate) < 0)
				{
					taskList.add(task);
				}		
		}	
		
		while ((printLine = deadlineReader.readLine()) != null) 
		{
			    DeadlineTask task = (DeadlineTask) deserializeFromJson(printLine, DeadlineTask.class);
				// if haven't reach deadline yet.
			    if(task.getDeadline().compareTo(endDate) < 0)
				{
					taskList.add(task);
				}		
		}	
		
		while ((printLine = recurReader.readLine()) != null) 
		{
				RecurrenceTask task = (RecurrenceTask) deserializeFromJson(printLine, RecurrenceTask.class);
				if(task.getStartRecurrenceDate().compareTo(startDate) > 0
						&& task.getStartRecurrenceDate().compareTo(endDate) < 0)
				{
					taskList.add(task);
				}		
		}	
		
		return taskList;
	}
	
	
	
	public ArrayList<Task> loadFromPriority(int priority) throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
				
		BufferedReader recurReader = new BufferedReader(new FileReader(FILE_NAME_RECUR));
		BufferedReader deadlineReader = new BufferedReader(new FileReader(FILE_NAME_DEADLINE));
		BufferedReader normalReader = new BufferedReader(new FileReader(FILE_NAME_NORMAL));
		BufferedReader floatReader = new BufferedReader(new FileReader(FILE_NAME_FLOATING));
		
		String printLine;
		
		while ((printLine = normalReader.readLine()) != null) 
		{
				NormalTask task = (NormalTask) deserializeFromJson(printLine, NormalTask.class);
				if(task.getPriority() == priority)
				{
					taskList.add(task);
				}		
		}	
		
		while ((printLine = deadlineReader.readLine()) != null) 
		{
			    DeadlineTask task = (DeadlineTask) deserializeFromJson(printLine, DeadlineTask.class);
				// if haven't reach deadline yet.
			    if(task.getPriority() == priority)
				{
					taskList.add(task);
				}		
		}	
		
		while ((printLine = recurReader.readLine()) != null) 
		{
				RecurrenceTask task = (RecurrenceTask) deserializeFromJson(printLine, RecurrenceTask.class);
				if(task.getPriority() == priority)
				{
					taskList.add(task);
				}		
		}	
		
		while ((printLine = floatReader.readLine()) != null) 
		{
				FloatingTask task = (FloatingTask) deserializeFromJson(printLine, FloatingTask.class);
				if(task.getPriority() == priority)
				{
					taskList.add(task);
				}		
		}	
		
		return taskList;
	}
	
	
	public ArrayList<Task> loadFromStartDateWithTask(Task taskObj, Date date) throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		String printLine;
		
		if(taskObj instanceof NormalTask)
		{
			BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_NORMAL));
			while ((printLine = reader.readLine()) != null) 
			{
					NormalTask task = (NormalTask) deserializeFromJson(printLine, NormalTask.class);
					
					if(task.getStartDateTime().getDate() == date.getDate() &&
							task.getStartDateTime().getMonth() == date.getMonth() &&
							 task.getStartDateTime().getYear() == date.getYear() )
					{
						
						taskList.add(task);
					}		
			}	
			
			
		}
		if(taskObj instanceof DeadlineTask)
		{
			BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_DEADLINE));
			while ((printLine = reader.readLine()) != null) 
			{
				    DeadlineTask task = (DeadlineTask) deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
				    if(task.getDeadline().compareTo(date) > 0)
					{
						taskList.add(task);
					}		
			}	
			
		
			
			
		}
		if(taskObj instanceof RecurrenceTask)
		{
			BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_RECUR));
			while ((printLine = reader.readLine()) != null) 
			{
					RecurrenceTask task = (RecurrenceTask) deserializeFromJson(printLine, RecurrenceTask.class);
					if(task.getStartRecurrenceDate().getDate() == date.getDate() &&
							task.getStartRecurrenceDate().getMonth() == date.getMonth() &&
							 task.getStartRecurrenceDate().getYear() == date.getYear() )
					{
						taskList.add(task);
					}		
			}	
			
		}
		return taskList;
	}
	
	public ArrayList<Task> loadFromBetweenDateWithTask(Task taskObj, Date startDate , Date endDate) throws IOException {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		String printLine;
		
		if(taskObj instanceof NormalTask)
		{
			BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_NORMAL));
			while ((printLine = reader.readLine()) != null) 
			{
					NormalTask task = (NormalTask) deserializeFromJson(printLine, NormalTask.class);
					if(task.getStartDateTime().compareTo(startDate) > 0
							&& task.getStartDateTime().compareTo(endDate) < 0)
					{
						taskList.add(task);
					}		
			}	
			
		
			
		}
		if(taskObj instanceof DeadlineTask)
		{
			BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_DEADLINE));
			while ((printLine = reader.readLine()) != null) 
			{
				    DeadlineTask task = (DeadlineTask) deserializeFromJson(printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
				    if(task.getDeadline().compareTo(endDate) < 0)
					{
						taskList.add(task);
					}		
			}	
			
			
			
			
		}
		if(taskObj instanceof RecurrenceTask)
		{
			BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_RECUR));
			while ((printLine = reader.readLine()) != null) 
			{
					RecurrenceTask task = (RecurrenceTask) deserializeFromJson(printLine, RecurrenceTask.class);
					if(task.getStartRecurrenceDate().compareTo(startDate) > 0
							&& task.getStartRecurrenceDate().compareTo(endDate) < 0)
					{
						taskList.add(task);
					}		
			}	
			
		}
		
		return taskList;
	}
	/*
	 * Sample Usage of GSON:
	 * 
	 *  Serializing :
	 * 	Error e1 = new Error(true, "first message..");
	 *	String json = serializeToJson(e1);
	 *	System.out.println(json);
	 *	
	 *  Deserializing :
	 *	Error e2 = (Error) deserializeFromJson(json, e1.getClass());
	 *	System.out.println(e2.toString());
	 */
	
	
	private static String serializeToJson(Object object) {
		
		String json;
		
		Gson gson = new Gson();
		json = gson.toJson(object);
		
		return json;
	}
	
	private static <T> Object deserializeFromJson(String json, Class<T> type) {
		
		Gson gson = new Gson();
		Object object = gson.fromJson(json, type);
		
		return object;
	}


	
}