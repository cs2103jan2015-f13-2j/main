package data;

import com.google.gson.Gson;

public class Serializer {

	public static String serializeToJson(Object object) {

		String json;

		Gson gson = new Gson();
		json = gson.toJson(object);

		return json;
	}

	public static <T> Object deserializeFromJson(String json, Class<T> type) {

		Gson gson = new Gson();
		Object object = gson.fromJson(json, type);

		return object;
	}
}
