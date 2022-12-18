package net.denanu.clientblockhighlighting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.dy.masa.malilib.config.ConfigManager;
import net.denanu.clientblockhighlighting.config.Config;
import net.denanu.clientblockhighlighting.rendering.ClientPosHighlighter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.BlockPos;

public class Mod implements ClientModInitializer {
	public static final String MOD_ID = "clientblockhighlighting";
	public static final Logger LOGGER = LoggerFactory.getLogger(Mod.MOD_ID);


	@Override
	public void onInitializeClient() {
		ConfigManager.getInstance().registerConfigHandler(Mod.MOD_ID, new Config());		
		WorldRenderEvents.BEFORE_DEBUG_RENDER.register(ClientPosHighlighter::render);
	}
}
