package com.aucguy.lateore.dev;

import net.minecraft.block.Block;

import com.aucguy.lateore.dev.block.BlockTransStone;
import com.aucguy.lateore.dev.command.CommandCountBlock;
import com.aucguy.lateore.dev.command.CommandLoadedChunks;
import com.aucguy.lateore.dev.command.CommandRemoveBlock;
import com.aucguy.lateore.dev.command.CommandTransparent;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * the mod for development enviroments
 * 
 * @author aucguy
 */
@Mod(modid = LateOreDevMod.MODID, version = LateOreDevMod.VERSION)
public class LateOreDevMod {
	public static final String MODID = "lateoredev";
	public static final String VERSION = "0.0.0";
	
	@Instance("lateoredev")
	public static LateOreDevMod instance;
	
	public BlockTransStone transStoneBlock = new BlockTransStone();

	@EventHandler
	public void init(@SuppressWarnings("unused") FMLInitializationEvent event) {
		this.registerBlock(this.transStoneBlock);
	}

	@SuppressWarnings("static-method")
	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTransparent()); //register commands
		event.registerServerCommand(new CommandLoadedChunks());
		event.registerServerCommand(new CommandRemoveBlock());
		event.registerServerCommand(new CommandCountBlock());
	}

	/**
	 * registers a block with forge
	 * @param block - the block to register
	 */
	protected void registerBlock(Block block) {
		GameRegistry.registerBlock(block, this.transStoneBlock.getUnlocalizedName().substring(5));
	}
}
