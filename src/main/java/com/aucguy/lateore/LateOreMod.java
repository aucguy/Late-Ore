package com.aucguy.lateore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import com.aucguy.lateore.data.BlockDeserializer;
import com.aucguy.lateore.data.GeneratorAdapterFactory;
import com.aucguy.lateore.data.LateOreProperties;
import com.aucguy.lateore.generate.ChunkHook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;

/**
 * the main mod class
 * 
 * @author aucguy
 */
@Mod(modid = LateOreMod.MODID, useMetadata = true)
public class LateOreMod {
	public static final String MODID = "lateore";

	@Instance("lateore")
	public static LateOreMod instance;

	/**
	 * the properties for this mod
	 */
	public LateOreProperties properties;

	/**
	 * the IO handler
	 */
	public ChunkHook chunkHook;

	public LateOreMod() {
		this.chunkHook = new ChunkHook();
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void serverStarting(FMLServerAboutToStartEvent event) throws FileNotFoundException, IOException {
		MinecraftServer server = event.getServer();
		String mcDir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath().replace("\\.", "");
		File settings = new File(new File(new File(mcDir, "saves"), server.getFolderName()), "lateore.json");
		// settings = new File(server.getDataDirectory() ,"") //TODO add for server
		if(settings.exists()) {
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapterFactory(new GeneratorAdapterFactory());
			builder.registerTypeAdapter(Block.class, new BlockDeserializer());
			Gson gson = builder.create();
			FileReader reader = new FileReader(settings);
			this.properties = gson.fromJson(reader, LateOreProperties.class);
			reader.close();

			FMLCommonHandler.instance().bus().register(this.chunkHook);
			MinecraftForge.EVENT_BUS.register(this.chunkHook);
			MinecraftForge.ORE_GEN_BUS.register(this.chunkHook);
			this.chunkHook.onServerStarting();
		}

		// TODO validate properties
	}

	@EventHandler
	public void serverStopped(@SuppressWarnings("unused") FMLServerStoppedEvent event) {
		if(this.properties != null) {
			FMLCommonHandler.instance().bus().unregister(this.chunkHook);
			MinecraftForge.EVENT_BUS.unregister(this.chunkHook);
			MinecraftForge.ORE_GEN_BUS.unregister(this.chunkHook);
		}
	}
}
