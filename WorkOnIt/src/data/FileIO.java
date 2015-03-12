package data;
// gan
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

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
	final static String SUCCESS_MESSAGE = "List successfully retrived";
	final static String FAIL_MESSAGE = "List fail to retrived";
	final static String TEMP_FILE = "temp_file.txt";
	private static String file_type;

	public Success saveIntoFile(Task task) {

		Success status = null;

		if (task instanceof FloatingTask) {
			file_type = FILE_NAME_FLOATING;
		} else if (task instanceof NormalTask) {
			file_type = FILE_NAME_NORMAL;
		} else if (task instanceof RecurrenceTask) {
			file_type = FILE_NAME_RECUR;
		} else if (task instanceof DeadlineTask) {
			file_type = FILE_NAME_DEADLINE;
		}

		// body
		String gsonSerial = serializeToJson(task);
		System.out.println(gsonSerial);
		PrintWriter filewrite = null;

		try {
			filewrite = new PrintWriter(new BufferedWriter(new FileWriter(
					file_type, true)));
			filewrite.println(gsonSerial);

			status = new Success(true, null);

		} catch (IOException e) {
			System.err
					.println("saveIntoFile: IO error. Please check R/W/X access.");
			status = new Success(false,
					"saveIntoFile: IO error. Please check R/W/X access.");
		} finally {
			filewrite.close();
		}

		return status;
	}

	public Success loadFromFileFloatingTask() {

		Success successObj;
		try {
			List<Task> taskList = new ArrayList<Task>();

			BufferedReader reader = new BufferedReader(new FileReader(
					FILE_NAME_FLOATING));
			String printLine;

			while ((printLine = reader.readLine()) != null) {
				Task task = (FloatingTask) deserializeFromJson(printLine,
						FloatingTask.class);
				taskList.add(task);
			}

			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	public Success loadFromFileNormalTask() {

		Success successObj;
		try {
			List<Task> taskList = new ArrayList<Task>();

			BufferedReader reader = new BufferedReader(new FileReader(
					FILE_NAME_NORMAL));
			String printLine;

			while ((printLine = reader.readLine()) != null) {
				Task task = (NormalTask) deserializeFromJson(printLine,
						NormalTask.class);
				taskList.add(task);
			}

			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	public Success loadFromFileDeadlineTask() {

		Success successObj;
		try {
			List<Task> taskList = new ArrayList<Task>();

			BufferedReader reader = new BufferedReader(new FileReader(
					FILE_NAME_DEADLINE));
			String printLine;

			while ((printLine = reader.readLine()) != null) {
				Task task = (DeadlineTask) deserializeFromJson(printLine,
						DeadlineTask.class);
				taskList.add(task);
			}
			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;

	}

	public Success loadFromFileRecurTask() {

		Success successObj;

		try {
			List<Task> taskList = new ArrayList<Task>();

			BufferedReader reader = new BufferedReader(new FileReader(
					FILE_NAME_RECUR));
			String printLine;

			while ((printLine = reader.readLine()) != null) {
				Task task = (RecurrenceTask) deserializeFromJson(printLine,
						RecurrenceTask.class);
				taskList.add(task);
			}
			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	public Success loadFromStartDate(Date date) {

		Success successObj;

		try {
			List<Task> taskList = new ArrayList<Task>();

			BufferedReader recurReader = new BufferedReader(new FileReader(
					FILE_NAME_RECUR));
			BufferedReader deadlineReader = new BufferedReader(new FileReader(
					FILE_NAME_DEADLINE));
			BufferedReader normalReader = new BufferedReader(new FileReader(
					FILE_NAME_NORMAL));

			String printLine;

			while ((printLine = normalReader.readLine()) != null) {
				NormalTask task = (NormalTask) deserializeFromJson(printLine,
						NormalTask.class);

				if (task.getStartDateTime().getDate() == date.getDate()
						&& task.getStartDateTime().getMonth() == date
								.getMonth()
						&& task.getStartDateTime().getYear() == date.getYear()) {

					taskList.add(task);
				}
			}

			while ((printLine = deadlineReader.readLine()) != null) {
				DeadlineTask task = (DeadlineTask) deserializeFromJson(
						printLine, DeadlineTask.class);
				// if haven't reach deadline yet.
				if (task.getDeadline().compareTo(date) > 0) {
					taskList.add(task);
				}
			}

			while ((printLine = recurReader.readLine()) != null) {
				RecurrenceTask task = (RecurrenceTask) deserializeFromJson(
						printLine, RecurrenceTask.class);
				if (task.getStartRecurrenceDate().getDate() == date.getDate()
						&& task.getStartRecurrenceDate().getMonth() == date
								.getMonth()
						&& task.getStartRecurrenceDate().getYear() == date
								.getYear()) {
					taskList.add(task);
				}
			}

			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	public Success loadFromBetweenDate(Date startDate, Date endDate) {

		Success successObj;

		try {
			List<Task> taskList = new ArrayList<Task>();

			BufferedReader recurReader = new BufferedReader(new FileReader(
					FILE_NAME_RECUR));
			BufferedReader deadlineReader = new BufferedReader(new FileReader(
					FILE_NAME_DEADLINE));
			BufferedReader normalReader = new BufferedReader(new FileReader(
					FILE_NAME_NORMAL));

			String printLine;

			while ((printLine = normalReader.readLine()) != null) {
				NormalTask task = (NormalTask) deserializeFromJson(printLine,
						NormalTask.class);
				if (task.getStartDateTime().compareTo(startDate) > 0
						&& task.getStartDateTime().compareTo(endDate) < 0) {
					taskList.add(task);
				}
			}

			while ((printLine = deadlineReader.readLine()) != null) {
				DeadlineTask task = (DeadlineTask) deserializeFromJson(
						printLine, DeadlineTask.class);
				// if haven't reach deadline yet.
				if (task.getDeadline().compareTo(endDate) < 0) {
					taskList.add(task);
				}
			}

			while ((printLine = recurReader.readLine()) != null) {
				RecurrenceTask task = (RecurrenceTask) deserializeFromJson(
						printLine, RecurrenceTask.class);
				if (task.getStartRecurrenceDate().compareTo(startDate) > 0
						&& task.getStartRecurrenceDate().compareTo(endDate) < 0) {
					taskList.add(task);
				}
			}

			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	public Success loadFromPriority(int priority) {

		Success successObj;

		try {
			List<Task> taskList = new ArrayList<Task>();

			BufferedReader recurReader = new BufferedReader(new FileReader(
					FILE_NAME_RECUR));
			BufferedReader deadlineReader = new BufferedReader(new FileReader(
					FILE_NAME_DEADLINE));
			BufferedReader normalReader = new BufferedReader(new FileReader(
					FILE_NAME_NORMAL));
			BufferedReader floatReader = new BufferedReader(new FileReader(
					FILE_NAME_FLOATING));

			String printLine;

			while ((printLine = normalReader.readLine()) != null) {
				NormalTask task = (NormalTask) deserializeFromJson(printLine,
						NormalTask.class);
				if (task.getPriority() == priority) {
					taskList.add(task);
				}
			}

			while ((printLine = deadlineReader.readLine()) != null) {
				DeadlineTask task = (DeadlineTask) deserializeFromJson(
						printLine, DeadlineTask.class);
				// if haven't reach deadline yet.
				if (task.getPriority() == priority) {
					taskList.add(task);
				}
			}

			while ((printLine = recurReader.readLine()) != null) {
				RecurrenceTask task = (RecurrenceTask) deserializeFromJson(
						printLine, RecurrenceTask.class);
				if (task.getPriority() == priority) {
					taskList.add(task);
				}
			}

			while ((printLine = floatReader.readLine()) != null) {
				FloatingTask task = (FloatingTask) deserializeFromJson(
						printLine, FloatingTask.class);
				if (task.getPriority() == priority) {
					taskList.add(task);
				}
			}

			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	public Success loadFromStartDateWithTask(Task taskObj, Date date) {

		Success successObj;

		try {
			List<Task> taskList = new ArrayList<Task>();
			String printLine;

			if (taskObj instanceof NormalTask) {
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_NORMAL));
				while ((printLine = reader.readLine()) != null) {
					NormalTask task = (NormalTask) deserializeFromJson(
							printLine, NormalTask.class);

					if (task.getStartDateTime().getDate() == date.getDate()
							&& task.getStartDateTime().getMonth() == date
									.getMonth()
							&& task.getStartDateTime().getYear() == date
									.getYear()) {

						taskList.add(task);
					}
				}

			}
			if (taskObj instanceof DeadlineTask) {
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_DEADLINE));
				while ((printLine = reader.readLine()) != null) {
					DeadlineTask task = (DeadlineTask) deserializeFromJson(
							printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
					if (task.getDeadline().compareTo(date) > 0) {
						taskList.add(task);
					}
				}

			}
			if (taskObj instanceof RecurrenceTask) {
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_RECUR));
				while ((printLine = reader.readLine()) != null) {
					RecurrenceTask task = (RecurrenceTask) deserializeFromJson(
							printLine, RecurrenceTask.class);
					if (task.getStartRecurrenceDate().getDate() == date
							.getDate()
							&& task.getStartRecurrenceDate().getMonth() == date
									.getMonth()
							&& task.getStartRecurrenceDate().getYear() == date
									.getYear()) {
						taskList.add(task);
					}
				}

			}
			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	public Success loadFromBetweenDateWithTask(Task taskObj, Date startDate,
			Date endDate) {

		Success successObj;
		try {
			List<Task> taskList = new ArrayList<Task>();
			String printLine;

			if (taskObj instanceof NormalTask) {
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_NORMAL));
				while ((printLine = reader.readLine()) != null) {
					NormalTask task = (NormalTask) deserializeFromJson(
							printLine, NormalTask.class);
					if (task.getStartDateTime().compareTo(startDate) > 0
							&& task.getStartDateTime().compareTo(endDate) < 0) {
						taskList.add(task);
					}
				}

			}
			if (taskObj instanceof DeadlineTask) {
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_DEADLINE));
				while ((printLine = reader.readLine()) != null) {
					DeadlineTask task = (DeadlineTask) deserializeFromJson(
							printLine, DeadlineTask.class);
					// if haven't reach deadline yet.
					if (task.getDeadline().compareTo(endDate) < 0) {
						taskList.add(task);
					}
				}

			}
			if (taskObj instanceof RecurrenceTask) {
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_RECUR));
				while ((printLine = reader.readLine()) != null) {
					RecurrenceTask task = (RecurrenceTask) deserializeFromJson(
							printLine, RecurrenceTask.class);
					if (task.getStartRecurrenceDate().compareTo(startDate) > 0
							&& task.getStartRecurrenceDate().compareTo(endDate) < 0) {
						taskList.add(task);
					}
				}

			}

			successObj = new Success(taskList, true, SUCCESS_MESSAGE);

		} catch (IOException e) {
			successObj = new Success(false, e.getMessage());
		}

		return successObj;
	}

	public Success deleteFromFile(Task taskObj)
	{
		Success  successObj = null;
		
		
		if (taskObj instanceof FloatingTask) {
			file_type = FILE_NAME_FLOATING;
		} else if (taskObj instanceof NormalTask) {
			file_type = FILE_NAME_NORMAL;
		} else if (taskObj instanceof RecurrenceTask) {
			file_type = FILE_NAME_RECUR;
		} else if (taskObj instanceof DeadlineTask) {
			file_type = FILE_NAME_DEADLINE;
		}
		
		try
		{
			File tempFile = new File(TEMP_FILE);
			List<Task> taskList = new ArrayList<Task>();
			String printLine;
			
			PrintWriter filewrite = new PrintWriter(new BufferedWriter(
					new FileWriter(TEMP_FILE, true)));
			
			if (taskObj instanceof NormalTask) {
				
				File outFile = new File(FILE_NAME_NORMAL);
				
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_NORMAL));
				while ((printLine = reader.readLine()) != null) {
					NormalTask task = (NormalTask) deserializeFromJson(
							printLine, NormalTask.class);
					if (task.getTaskId() != taskObj.getTaskId()) 
					{
						taskList.add(task);	
					}
					else
					{
						successObj = new Success(taskList, true, "Delete Successful");
					}
				}
				
				
				File newFile = new File(FILE_NAME_NORMAL);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);
				
				for(int i = 0 ; i < taskList.size() ; i ++)
				{
					System.out.println(taskList.size());
					String gsonSerial = serializeToJson(taskList.get(i));
				
					try 
					{
						filewriteIntoFile.println(gsonSerial);
					}
					catch(Exception e)
					{
						
					}
					
				}
				
				filewrite.close();
				filewriteIntoFile.close();

			}

			if (taskObj instanceof FloatingTask) {
				
				File outFile = new File(FILE_NAME_FLOATING);
				
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_FLOATING));
				while ((printLine = reader.readLine()) != null) {
					FloatingTask task = (FloatingTask) deserializeFromJson(
							printLine, FloatingTask.class);
					if (task.getTaskId() != taskObj.getTaskId()) 
					{
						taskList.add(task);	
					}
					else
					{
						successObj = new Success(taskList, true, "Delete Successful");
					}
				}
				
				File newFile = new File(FILE_NAME_FLOATING);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);
				
				for(int i = 0 ; i < taskList.size() ; i ++)
				{
					System.out.println(taskList.size());
					String gsonSerial = serializeToJson(taskList.get(i));
				
					try 
					{					
						filewriteIntoFile.println(gsonSerial);
					}
					catch(Exception e)
					{
						
					}
					
				}
				
				filewrite.close();
				filewriteIntoFile.close();}
			
			if (taskObj instanceof DeadlineTask) {
				
				File outFile = new File(FILE_NAME_DEADLINE);
				
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_DEADLINE));
				while ((printLine = reader.readLine()) != null) {
					DeadlineTask task = (DeadlineTask) deserializeFromJson(
							printLine, DeadlineTask.class);
					if (task.getTaskId() != taskObj.getTaskId()) 
					{
						taskList.add(task);	
					}
					else
					{
						successObj = new Success(taskList, true, "Delete Successful");
					}
				}
				
				File newFile = new File(FILE_NAME_DEADLINE);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);
				
				for(int i = 0 ; i < taskList.size() ; i ++)
				{
					System.out.println(taskList.size());
					String gsonSerial = serializeToJson(taskList.get(i));
				
					try 
					{					
						filewriteIntoFile.println(gsonSerial);
					}
					catch(Exception e)
					{
						
					}
					
				}
				
				filewrite.close();
				filewriteIntoFile.close();}
			
			if (taskObj instanceof RecurrenceTask) {
				
				File outFile = new File(FILE_NAME_RECUR);
				
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_RECUR));
				while ((printLine = reader.readLine()) != null) {
					RecurrenceTask task = (RecurrenceTask) deserializeFromJson(
							printLine, RecurrenceTask.class);
					if (task.getTaskId() != taskObj.getTaskId()) 
					{
						taskList.add(task);	
					}
					else
					{
						successObj = new Success(taskList, true, "Delete Successful");
					}
				}
				
				File newFile = new File(FILE_NAME_RECUR);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);
				
				for(int i = 0 ; i < taskList.size() ; i ++)
				{
					System.out.println(taskList.size());
					String gsonSerial = serializeToJson(taskList.get(i));
				
					try 
					{					
						filewriteIntoFile.println(gsonSerial);
					}
					catch(Exception e)
					{
						
					}
					
				}
				
				filewrite.close();
				filewriteIntoFile.close();}
			
			
			
		}
		
		
		catch(Exception e)
		{
			
		}
		
		return successObj;
	}
	
	
	public Success updateFromFile(Task taskUpdate, Task taskObj)
	{
		Success  successObj = null;
		
		
		if (taskObj instanceof FloatingTask) {
			file_type = FILE_NAME_FLOATING;
		} else if (taskObj instanceof NormalTask) {
			file_type = FILE_NAME_NORMAL;
		} else if (taskObj instanceof RecurrenceTask) {
			file_type = FILE_NAME_RECUR;
		} else if (taskObj instanceof DeadlineTask) {
			file_type = FILE_NAME_DEADLINE;
		}
		
		try
		{
			File tempFile = new File(TEMP_FILE);
			List<Task> taskList = new ArrayList<Task>();
			String printLine;
			
			PrintWriter filewrite = new PrintWriter(new BufferedWriter(
					new FileWriter(TEMP_FILE, true)));
			
			if (taskObj instanceof NormalTask) {
				
				File outFile = new File(FILE_NAME_NORMAL);
				
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_NORMAL));
				while ((printLine = reader.readLine()) != null) {
					NormalTask task = (NormalTask) deserializeFromJson(
							printLine, NormalTask.class);
					if (task.getTaskId() != taskObj.getTaskId()) 
					{
						taskList.add(task);	
					}
					else
					{
						successObj = new Success(taskList, true, "Update Successful");
					}
				}
				
				File newFile = new File(FILE_NAME_NORMAL);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);
				taskList.add(taskUpdate);
				
				for(int i = 0 ; i < taskList.size() ; i ++)
				{
					System.out.println(taskList.size());
					String gsonSerial = serializeToJson(taskList.get(i));
				
					try 
					{		
						filewriteIntoFile.println(gsonSerial);
					}
					catch(Exception e)
					{
						
					}
					
				}
				
				
				filewrite.close();
				filewriteIntoFile.close();

			}

			if (taskObj instanceof FloatingTask) {
				
				File outFile = new File(FILE_NAME_FLOATING);
				
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_FLOATING));
				while ((printLine = reader.readLine()) != null) {
					FloatingTask task = (FloatingTask) deserializeFromJson(
							printLine, FloatingTask.class);
					if (task.getTaskId() != taskObj.getTaskId()) 
					{
						taskList.add(task);	
					}
					else
					{
						successObj = new Success(taskList, true, "Update Successful");
					}
				}
				
				File newFile = new File(FILE_NAME_FLOATING);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);
				
				taskList.add(taskUpdate);
				for(int i = 0 ; i < taskList.size() ; i ++)
				{
					System.out.println(taskList.size());
					String gsonSerial = serializeToJson(taskList.get(i));
				
					try 
					{					
						filewriteIntoFile.println(gsonSerial);
					}
					catch(Exception e)
					{
						
					}
					
				}
				
				filewrite.close();
				filewriteIntoFile.close();}
			
			if (taskObj instanceof DeadlineTask) {
				
				File outFile = new File(FILE_NAME_DEADLINE);
				
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_DEADLINE));
				while ((printLine = reader.readLine()) != null) {
					DeadlineTask task = (DeadlineTask) deserializeFromJson(
							printLine, DeadlineTask.class);
					if (task.getTaskId() != taskObj.getTaskId()) 
					{
						taskList.add(task);	
					}
					else
					{
						successObj = new Success(taskList, true, "Update Successful");
					}
				}
				
				File newFile = new File(FILE_NAME_DEADLINE);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);
				
				taskList.add(taskUpdate);
				for(int i = 0 ; i < taskList.size() ; i ++)
				{
					System.out.println(taskList.size());
					String gsonSerial = serializeToJson(taskList.get(i));
				
					try 
					{					
						filewriteIntoFile.println(gsonSerial);
					}
					catch(Exception e)
					{
						
					}
					
				}
				
				filewrite.close();
				filewriteIntoFile.close();}
			
			if (taskObj instanceof RecurrenceTask) {
				
				File outFile = new File(FILE_NAME_RECUR);
				
				BufferedReader reader = new BufferedReader(new FileReader(
						FILE_NAME_RECUR));
				while ((printLine = reader.readLine()) != null) {
					RecurrenceTask task = (RecurrenceTask) deserializeFromJson(
							printLine, RecurrenceTask.class);
					if (task.getTaskId() != taskObj.getTaskId()) 
					{
						taskList.add(task);	
					}
					else
					{
						successObj = new Success(taskList, true, "Update Successful");
					}
				}
				
				File newFile = new File(FILE_NAME_RECUR);
				PrintWriter filewriteIntoFile = new PrintWriter(newFile);
				
				taskList.add(taskUpdate);
				
				for(int i = 0 ; i < taskList.size() ; i ++)
				{
					System.out.println(taskList.size());
					String gsonSerial = serializeToJson(taskList.get(i));
				
					try 
					{					
						filewriteIntoFile.println(gsonSerial);
					}
					catch(Exception e)
					{
						
					}
					
				}
				
				filewrite.close();
				filewriteIntoFile.close();}
		}
		
		
		catch(Exception e)
		{
			
		}
		
		return successObj;
	}
	
	/*
	 * Sample Usage of GSON:
	 * 
	 * Serializing : Error e1 = new Error(true, "first message.."); String json
	 * = serializeToJson(e1); System.out.println(json);
	 * 
	 * Deserializing : Error e2 = (Error) deserializeFromJson(json,
	 * e1.getClass()); System.out.println(e2.toString());
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