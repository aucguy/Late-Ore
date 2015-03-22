package com.aucguy.lateore.util;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;

public class NBTHelper {
	public static NBTTagCompound getCompoundTag(NBTTagCompound tag, String ... names) {
		return getCompoundTag(tag, false, names);
	}
	
	/**
	 * does multiple calls on tag.getCompound to get the final compound tag
	 * @param tag - the base tag
	 * @param create - whether or not to create missing tags
	 * @param names - the names of the compound tags
	 */
	public static NBTTagCompound getCompoundTag(NBTTagCompound tag, boolean create, String ... names) {
		for(String name : names) {
			if(!tag.hasKey(name)) {
				if(!create) {
					return null;
				}
				tag.setTag(name, new NBTTagCompound());
			}
			tag = tag.getCompoundTag(name);
		}
		return tag;
	}
	
	/**
	 * returns if the block is in the chunk
	 */
	public static boolean isBlockInChunk(Chunk chunk, Block block) {
		for(int y=0; y<256; y++) {
			for(int x=0; x<16; x++) {
				for(int z=0; z<16; z++) {
					if(chunk.getBlock(x, y, z) == block) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
