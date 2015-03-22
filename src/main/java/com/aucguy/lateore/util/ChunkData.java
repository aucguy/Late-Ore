package com.aucguy.lateore.util;

import net.minecraft.world.chunk.Chunk;

/**
 * stores chunk metadata
 * 
 * @author aucguy
 */
public class ChunkData {
	/**
	 * stores the minecraft chunk in alive state or the next ChunkData in a pooled state
	 */
	private Object chunk;
	public String[] genIds;

	@SuppressWarnings("hiding")
	private void init(Chunk chunk, String[] genSets) {
		this.chunk = chunk;
		this.genIds = genSets;
	}

	public Chunk getChunk() {
		return (Chunk) this.chunk;
	}

	/**
	 * puts the instance back into the pool
	 */
	public void recycle() {
		this.genIds = null;
		this.chunk = poolStart;
		poolStart = this;
	}

	/**
	 * the top of the pool stack
	 */
	private static ChunkData poolStart;
	
	/**
	 * get an instance from the pool stack
	 */
	private static ChunkData get() {
		ChunkData chunkdata = null;
		if(poolStart == null) {
			chunkdata = new ChunkData(); //no avalible instances
		} else {
			chunkdata = poolStart;
			poolStart = (ChunkData) poolStart.chunk;
		}
		return chunkdata;
	}
	
	/**
	 * get and initialize a ChunkData object
	 */
	public static ChunkData borrow(Chunk chunk, String[] genSets) {
		ChunkData chunkdata = get();
		chunkdata.init(chunk, genSets);
		return chunkdata;
	}
	
	/**
	 * frees an instance from the pool stack
	 */
	public static void free() {
		get();
	}
}