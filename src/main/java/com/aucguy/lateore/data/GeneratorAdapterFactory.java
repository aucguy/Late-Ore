package com.aucguy.lateore.data;

import com.aucguy.lateore.generate.Generator;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class GeneratorAdapterFactory implements TypeAdapterFactory {
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		if(type.getRawType().equals(Generator.class)) {
			return (TypeAdapter<T>) new GeneratorTypeAdapter(gson);
		}
		return null;		
	}
}