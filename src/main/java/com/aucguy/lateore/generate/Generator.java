package com.aucguy.lateore.generate;

import java.util.Random;

import net.minecraft.world.chunk.Chunk;

/**
 * applies transformations on chunks
 * 
 * @author aucguy
 */
public abstract class Generator {
	public int dimension;
	String modid;
	public String id;
	
	public Generator() {
	}
	
	public abstract void generate(Chunk chunk, Random random);

	public abstract boolean hasBeenApplied(Chunk chunk);
}