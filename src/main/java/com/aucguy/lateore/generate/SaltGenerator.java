package com.aucguy.lateore.generate;

import java.util.Random;

import com.aucguy.lateore.util.NBTHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;

/**
 * generates salt
 * 
 * @author aucguy
 */
public class SaltGenerator extends Generator {
	public int veinsPerChunk;
	public int blocksPerVein;
	public Block blockToGenerate;

	@Override
	public void generate(Chunk chunk, Random random) {
		for(int i = 0; i < this.veinsPerChunk; i++) {
			int randPosX = random.nextInt(16); // also from the Mekanism source (and modified)
			int randPosZ = random.nextInt(16);
			int randPosY = chunk.worldObj.getTopSolidOrLiquidBlock(randPosX, randPosZ);
			this.generateVein(chunk, random, randPosX, randPosY, randPosZ);
		}
	}

	/**
	 * code taken (and modified) from Mekanism
	 */
	public void generateVein(Chunk chunk, Random random, int x, int y, int z) {
		if(chunk.getBlock(x, y, z).getMaterial() != Material.water) {
			return;
		}
		int toGenerate = random.nextInt(this.blocksPerVein - 2) + 2;
		byte yOffset = 1;
		
		int minX = x - toGenerate;
		int minY = Math.max(0, y - yOffset);
		int minZ = z - toGenerate;
		int maxX = x + toGenerate;
		int maxY = Math.min(255, y + yOffset);
		int maxZ =  z + toGenerate;
		
		for(int xPos = minX; xPos <= maxX; xPos++) {
			for(int zPos = minZ; zPos <= maxZ; zPos++) {
				int xOffset = xPos - x;
				int zOffset = zPos - z;

				if((xOffset * xOffset) + (zOffset * zOffset) <= toGenerate * toGenerate) {
					for(int yPos = minY; yPos <= maxY; yPos++) {
						int realX = xPos & 15;
						int realZ = yPos & 15;
						Block block = chunk.getBlock(realX, yPos, realZ);

						if(block == Blocks.dirt || block == Blocks.clay) {
							chunk.func_150807_a(realX, yPos, realZ, this.blockToGenerate, 0);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean hasBeenApplied(Chunk chunk) {
		return NBTHelper.isBlockInChunk(chunk, this.blockToGenerate);
	}
}
