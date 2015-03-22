package com.aucguy.lateore.data;

import java.lang.reflect.Type;

import net.minecraft.block.Block;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class BlockDeserializer implements JsonDeserializer<Block> {
	@Override
	public Block deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return Block.getBlockFromName(json.getAsString());
	}
}
