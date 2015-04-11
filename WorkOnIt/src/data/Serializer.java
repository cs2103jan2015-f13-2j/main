package data;

import com.google.gson.Gson;

public class Serializer {
	/**
	 *
	 * convert the object into JSon to store inside the text file
	 *
	 * @param  	Object
	 * 				object that need to be convert into JSon String
	 * @return  String
	 * 				Json string that need to be store inside the text file
	 */				
	//@author A0112694
	public static String serializeToJson(Object object) {

		String json;

		Gson gson = new Gson();
		json = gson.toJson(object);

		return json;
	}
	/**
	 *
	 * convert the Json into Object from the text file
	 *
	 * @param  	String
	 * 				Json String that need to be convert into Object
	 * @param  	Class<T>
	 * 				The class type of object that need to be converted
	 * @return  Object
	 * 				The object that converted from the Json String.
	 */				
	//@author A0112694
	public static <T> Object deserializeFromJson(String json, Class<T> type) {

		Gson gson = new Gson();
		Object object = gson.fromJson(json, type);

		return object;
	}
}
