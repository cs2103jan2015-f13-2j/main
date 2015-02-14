package data;

import java.util.ArrayList;

import com.google.gson.Gson;

import entity.Task;

public class FileIO {
	
	public boolean saveIntoFile(Task task) {
		
		boolean isSuccess = false;
		
		//body
		
		return isSuccess;
	}
	
	public ArrayList<Task> loadFromFile() {
		
		ArrayList<Task> taskList = new ArrayList<Task>();
		
		//body
		
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
