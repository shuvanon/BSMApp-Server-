package main.java.com.betelguese.utils;

import org.json.JSONException;
import org.json.JSONObject;

import main.java.com.betelguese.utils.helpers.Log;

public class JSONBuilder {

	private static final String TAG = JSONBuilder.class.getSimpleName();

	/**
	 * 
	 * @param objects
	 * @return the created JSON from the objects
	 * @throws JSONException
	 */
	public static final JSONObject createJsonObject(Object... objects)
			throws JSONException {
		Log.d(TAG, "parsing started...");
		if (objects.length % 2 != 0) {
			Log.e(TAG, "parsing failed", new Exception("parsing failed"));
			throw new JSONException("not enough element or tag.");
		}
		JSONObject jsonObject = new JSONObject();
		int mid = objects.length / 2;
		for (int i = 0; i < objects.length / 2; i++) {
			try {
				jsonObject.put((String) objects[i], objects[i + mid]);
			} catch (ClassCastException e) {
				Log.e(TAG, "parsing failed." + e.getClass().getSimpleName()
						+ " occured.", e);
			} catch (ArrayIndexOutOfBoundsException e) {
				Log.e(TAG, "parsing failed." + e.getClass().getSimpleName()
						+ " occured.", e);
			}
		}
		Log.d(TAG, "parsing finished...");
		return jsonObject;
	}

	public static final String handMadeJSONObject() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{\"done\":0");
		stringBuilder.append(",");
		stringBuilder
				.append("\"message\":\"Unable to give the service by the server.\"}");
		return stringBuilder.toString();
	}

}
