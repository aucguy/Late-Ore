package com.aucguy.lateore.data;

import java.io.IOException;

import com.aucguy.lateore.generate.Generator;
import com.aucguy.lateore.generate.OreGenerator;
import com.aucguy.lateore.generate.SaltGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * adapter for generating generators
 * 
 * @author aucguy
 */
class GeneratorTypeAdapter extends TypeAdapter<Generator> {
	protected Gson gson;

	public GeneratorTypeAdapter(Gson gson) {
		this.gson = gson;
	}

	@Override
	public void write(JsonWriter out, Generator value) throws IOException {
		throw new RuntimeException("not implemented");
	}

	@Override
	public Generator read(JsonReader in) throws IOException {
		JsonObject object = Streams.parse(in).getAsJsonObject();

		String type = object.get("type").getAsString();
		Class<? extends Generator> clazz;
		if(type.equals("ore")) {
			clazz = OreGenerator.class;
		} else if(type.equals("salt")) {
			clazz = SaltGenerator.class;
		} else {
			throw new JsonParseException("invalid generator type");
		}
		return gson.fromJson(object, clazz);
	}
}
