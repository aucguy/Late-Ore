package com.aucguy.lateore.generate;

import java.util.Random;

import com.aucguy.lateore.util.NBTHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class OreGenerator extends Generator {
	public int veinsPerChunk;
	public int blocksPerVein;
	public int maximumHeight;
	public Block blockToGenerate;
	public int blockMetadata = 0;

	@Override
	public void generate(Chunk chunk, Random random) {
		// thanks to aidancbrady for this code in his mekanism mod
		for(int i = 0; i < this.veinsPerChunk; i++) {
			int randPosX = random.nextInt(16);
			int randPosY = random.nextInt(this.maximumHeight);
			int randPosZ = random.nextInt(16);
			this.generate(chunk.worldObj, random, randPosX, randPosY, randPosZ, chunk);
		}
	}

	public boolean generate(World world, Random rand, int veinX, int veinY, int veinZ, Chunk chunk) {
		float angle = rand.nextFloat() * (float) Math.PI; // the angle the vein will be parrelel to
		// the start and ends determines the 'box' the vein is in
		double startX = veinX + 8 + MathHelper.sin(angle) * this.blocksPerVein / 8.0F;
		double endX = veinX + 8 - MathHelper.sin(angle) * this.blocksPerVein / 8.0F;
		double startZ = ((veinZ + 8) + MathHelper.cos(angle) * this.blocksPerVein / 8.0F);
		double endZ = ((veinZ + 8) - MathHelper.cos(angle) * this.blocksPerVein / 8.0F);
		double startY = (veinY + rand.nextInt(3) - 2); // veinY + randint(-2, 1)
		double endY = (veinY + rand.nextInt(3) - 2); // same as d5, but a different random

		for(int blockCount = 0; blockCount <= this.blocksPerVein; ++blockCount) {
			// blockCount / this.blocksPerVein = completion percentage
			// d0 = start value
			// (d1 - d0) = end - start
			// centerX/Y/Z = the propable place for the ore to go
			double centerX = startX + (endX - startX) * blockCount / this.blocksPerVein;
			double centerY = startY + (endY - startY) * blockCount / this.blocksPerVein;
			double centerZ = startZ + (endZ - startZ) * blockCount / this.blocksPerVein;
			double d9 = rand.nextDouble() * this.blocksPerVein / 16.0D; // some random number
			// the sin is slowly rotating through the vein. I.E the first block is pointing one direction
			// and it eventually goes the other way
			double hypotenuse = (MathHelper.sin(blockCount * (float) Math.PI / this.blocksPerVein) + 1.0F) * d9 + 1.0D;
			double hypotenuse2 = (MathHelper.sin(blockCount * (float) Math.PI / this.blocksPerVein) + 1.0F) * d9 + 1.0D; // same
																															// as
			// d10
			int minX = MathHelper.floor_double(centerX - hypotenuse / 2.0D);
			int minY = Math.max(0, MathHelper.floor_double(centerY - hypotenuse2 / 2.0D));
			int minZ = MathHelper.floor_double(centerZ - hypotenuse / 2.0D);
			int maxX = MathHelper.floor_double(centerX + hypotenuse / 2.0D);
			int maxY = Math.min(255, MathHelper.floor_double(centerY + hypotenuse2 / 2.0D));
			int maxZ = MathHelper.floor_double(centerZ + hypotenuse / 2.0D);

			for(int blockX = minX; blockX <= maxX; ++blockX) {
				double distX = (blockX + 0.5D - centerX) / (hypotenuse / 2.0D);

				if(distX * distX < 1.0D) {
					for(int blockY = minY; blockY <= maxY; ++blockY) {
						double distY = (blockY + 0.5D - centerY) / (hypotenuse2 / 2.0D);

						if(distX * distX + distY * distY < 1.0D) {
							for(int blockZ = minZ; blockZ <= maxZ; ++blockZ) {
								double distZ = (blockZ + 0.5D - centerZ) / (hypotenuse / 2.0D);
								if(distX * distX + distY * distY + distZ * distZ < 1.0D) {
									int realX = blockX & 15;
									int realZ = blockZ & 15;

									if(chunk.getBlock(realX, blockY, realZ).isReplaceableOreGen(world, blockX, blockY,
											blockZ, Blocks.stone)) {
										chunk.func_150807_a(realX, blockY, realZ, this.blockToGenerate,
												this.blockMetadata);
									}
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public boolean hasBeenApplied(Chunk chunk) {
		return NBTHelper.isBlockInChunk(chunk, this.blockToGenerate);
	}
}