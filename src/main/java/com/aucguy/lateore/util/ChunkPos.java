package com.aucguy.lateore.util;

import net.minecraft.world.chunk.Chunk;

public class ChunkPos {
	public int dimension;
	public int posX;
	public int posZ;
	
	public ChunkPos(Chunk chunk) {
		this(chunk.worldObj.provider.dimensionId, chunk.xPosition, chunk.zPosition);
	}
	
	public ChunkPos(int dimension, int posX, int posZ) {
		this.dimension = dimension;
		this.posX = posX;
		this.posZ = posZ;
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof ChunkPos) {
			ChunkPos pos = (ChunkPos) other;
			return this.dimension == pos.dimension && this.posX == pos.posX && this.posZ == pos.posZ;
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + this.dimension + ", " + this.posX + ", " + this.posZ + "]";
	}
	
	@Override
	public int hashCode() {
		return this.posX | (this.posZ << 16);
	}
}
