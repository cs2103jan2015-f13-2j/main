package data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.Gson;

import entity.Task;
import entity.Success;

public class FileIO {
	
	
	final static String FILE_NAME = "datafile.txt";
	
	public Success saveIntoFile(Task task) {
		
		Success status = null;
		
		//body
		String gsonSerial = serializeToJson(task);
		System.out.println(gsonSerial);	
		PrintWriter filewrite = null;
		
		try {
			filewrite = new PrintWriter(new BufferedWriter(
					new FileWriter(FILE_NAME, true)));
			filewrite.println(gsonSerial);
			
			status = new Success(true, null);
			
		} catch (IOException e) {
			System.err.println("saveIntoFile: IO error. Please check.");
			status = new Success(false, "saveIntoFile: IO error. Please check.");
		} finally {
			filewrite.close();
		}	
		
		return status;
	}
	
	
	public ArrayList<Task> loadFromFile() {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		
//		to be implemented
		
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
