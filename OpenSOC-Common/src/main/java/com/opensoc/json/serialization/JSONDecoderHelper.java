package com.opensoc.json.serialization;

import java.io.DataInputStream;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONDecoderHelper {

	public static String getString(DataInputStream data) throws IOException {

		int strSize = data.readInt();

		byte[] bytes = new byte[strSize];
		data.read(bytes);
		return new String(bytes);
	}

	public static Number getNumber(DataInputStream data) throws IOException {
		// Treating all ints,shorts, long as long.
		// Everything else as Double
		int flag = data.readByte();
		if (flag == 0)
			return data.readDouble();

		return data.readLong();
	}

	public static Boolean getBoolean(DataInputStream data) throws IOException {

		return data.readBoolean();
	}

	@SuppressWarnings("unchecked")
	public static JSONArray getArray(DataInputStream data) throws IOException {
		// TODO Auto-generated method stub
		JSONArray output = new JSONArray();
		int size = data.readInt();

		for (int i = 0; i < size; i++) {
			Object value = getObject(data);
			output.add(value);
		}

		return output;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getJSON(DataInputStream data) throws IOException {
		// TODO Auto-generated method stub
		JSONObject output = new JSONObject();
		int size = data.readInt();

		for (int i = 0; i < size; i++) {
			String key = (String) getObject(data);
			Object value = getObject(data);
			output.put(key, value);
		}

		return output;
	}

	public static Object getObject(DataInputStream data) throws IOException {
		// TODO Auto-generated method stub
		byte objID = data.readByte();

		if (objID == JSONKafkaSerializer.StringID)
			return getString(data);

		if (objID == JSONKafkaSerializer.JSONObjectID)
			return getJSON(data);

		if (objID == JSONKafkaSerializer.NumberID)
			return getNumber(data);

		if (objID == JSONKafkaSerializer.BooleanID)
			return getBoolean(data);

		if (objID == JSONKafkaSerializer.NULLID)
			return null;

		if (objID == JSONKafkaSerializer.JSONArrayID)
			return getArray(data);

		return null;
	}

}
