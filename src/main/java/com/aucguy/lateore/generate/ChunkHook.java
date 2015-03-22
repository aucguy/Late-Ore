package com.aucguy.lateore.generate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.world.ChunkDataEvent;

import org.apache.commons.lang3.ArrayUtils;

import com.aucguy.lateore.LateOreMod;
import com.aucguy.lateore.util.ChunkData;
import com.aucguy.lateore.util.ChunkPos;
import com.aucguy.lateore.util.NBTHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * hook for chunks
 * 
 * @author aucguy
 */
public class ChunkHook {
	/**
	 * chunks that have just loaded
	 */
	public List<ChunkData> chunksJustLoaded;

	/**
	 * one lonely object for a pool
	 */
	protected List<ChunkData> justLoadedPool;

	String[] activeGenSets;

	/**
	 * binds chunk locations to chunk metadata
	 */
	public Map<ChunkPos, ChunkData> chunkData;

	/**
	 * the random object
	 */
	Random random;

	public ChunkHook() {
		this.chunksJustLoaded = new LinkedList<ChunkData>();
		this.justLoadedPool = new LinkedList<ChunkData>();
		this.chunkData = new HashMap<ChunkPos, ChunkData>();
		this.random = new Random();
	}

	/**
	 * no chunks haven't just loaded!
	 */
	public void onServerStarting() {
		this.chunksJustLoaded.clear();
		this.activeGenSets = new String[LateOreMod.instance.properties.generators.length];
		for(int i = 0; i < LateOreMod.instance.properties.generators.length; i++) {
			this.activeGenSets[i] = LateOreMod.instance.properties.generators[i++].id;
		}
	}

	/**
	 * chunk loading hook
	 */
	@SubscribeEvent
	public void onChunkLoading(ChunkDataEvent.Load event) {
		NBTTagCompound lateoreTag = NBTHelper.getCompoundTag(event.getData(), "mods", "lateore");
		String[] mods;
		if(lateoreTag == null || !lateoreTag.hasKey("appliedGenSets")) {
			mods = null; // we must check for generated things via searching the chunk for the ore
		} else {
			mods = lateoreTag.getString("appliedGenSets").split(",");
		}

		Chunk chunk = event.getChunk();
		this.chunksJustLoaded.add(ChunkData.borrow(chunk, mods));
	}

	/**
	 * generating stuff late
	 */
	@SuppressWarnings("unused")
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		if(!this.chunksJustLoaded.isEmpty()) {
			List<ChunkData> justLoaded = this.chunksJustLoaded;
			this.chunksJustLoaded = this.justLoadedPool;
			
			for(ChunkData chunkdata : justLoaded) {
				if(chunkdata.genIds == null) { // chunk hasn't been loaded by late ores yet
					this.findAppliedGenSets(chunkdata);
				}
				this.generateChunk(chunkdata);
				this.chunkData.put(getCoordsForChunk(chunkdata.getChunk()), chunkdata);
			}
			this.justLoadedPool = justLoaded;
			this.justLoadedPool.clear();
		}
		ChunkData.free(); // slowly diminishes the pool size
	}

	/**
	 * applies genSets to a chunk
	 */
	protected void generateChunk(ChunkData chunkdata) {
		List<String> genList = Arrays.asList(chunkdata.genIds);
		for(Generator generator : LateOreMod.instance.properties.generators) {
			if(!genList.contains(generator.id)) {
				generator.generate(chunkdata.getChunk(), this.random);
			}
		}
		chunkdata.genIds = ArrayUtils.addAll(chunkdata.genIds, this.activeGenSets);
	}

	/**
	 * finds the genSets applied to a chunk via other mods normal generating
	 */
	@SuppressWarnings("static-method")
	protected void findAppliedGenSets(ChunkData chunkdata) {
		List<String> genList = new LinkedList<String>();
		for(Generator generator : LateOreMod.instance.properties.generators) {
			if(generator.hasBeenApplied(chunkdata.getChunk())) {
				genList.add(generator.id);
			}
		}

		chunkdata.genIds = new String[genList.size()];
		genList.toArray(chunkdata.genIds);
	}

	/**
	 * saving chunks
	 */
	@SubscribeEvent
	public void onChunkSaving(ChunkDataEvent.Save event) {
		ChunkPos pos = getCoordsForChunk(event.getChunk());
		ChunkData chunkdata = this.chunkData.get(pos);
		if(chunkdata == null) {
			chunkdata = ChunkData.borrow(event.getChunk(), this.activeGenSets);
		}
		NBTTagCompound lateOreTag = NBTHelper.getCompoundTag(event.getData(), true, "mods", "lateore");
		String appliedGenSets = String.join(",", chunkdata.genIds);
		lateOreTag.setString("appliedGenSets", appliedGenSets);
		this.chunkData.remove(pos);
		chunkdata.recycle();
	}

	/**
	 * prevents lateore from taking effect on chunks that are being generated
	 */
	@SubscribeEvent
	public void onChunkGenerated(OreGenEvent.Pre event) {
		this.chunksJustLoaded.add(ChunkData.borrow(event.world.getChunkFromChunkCoords(event.worldX, event.worldZ),
				this.activeGenSets));
	}

	/**
	 * gets the coordinates for the chunk as a ChunkPos object
	 */
	public static ChunkPos getCoordsForChunk(Chunk chunk) {
		return new ChunkPos(chunk);
	}
}
