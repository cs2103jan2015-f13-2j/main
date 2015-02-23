package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.Gson;

import entity.Task;

public class FileIO {
	
	
	final static String FILE_NAME = "datafile.txt";
	
	public boolean saveIntoFile(Task task) {
		
		boolean isSuccess = false;
		
		//body
		String gsonSerial = serializeToJson(task);
		System.out.println(gsonSerial);	
		PrintWriter filewrite = null;
		try {
			filewrite = new PrintWriter(new BufferedWriter(
					new FileWriter(FILE_NAME, true)));
			filewrite.println(gsonSerial);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			filewrite.close();
		}	
		return isSuccess;
	}
	
	
	public ArrayList<Task> loadFromFile() {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		String getLineFromFile;
		//body
		try
		{
		BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
		
		while ((getLineFromFile = reader.readLine()) != null) {
			Task task = null;
			task = (Task)deserializeFromJson(getLineFromFile , task.getClass());
			System.out.println(task.getTaskName());
		}
		
		}
		catch(IOException e)
		{
			e.printStackTrace();
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
