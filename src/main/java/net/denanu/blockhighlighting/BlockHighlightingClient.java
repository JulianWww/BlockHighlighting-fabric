package net.denanu.blockhighlighting;

import fi.dy.masa.malilib.config.ConfigManager;
import net.denanu.blockhighlighting.config.Config;
import net.denanu.blockhighlighting.rendering.ClientPosHighlighter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class BlockHighlightingClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ConfigManager.getInstance().registerConfigHandler(BlockHighlighting.MOD_ID, new Config());
		WorldRenderEvents.BEFORE_DEBUG_RENDER.register(ClientPosHighlighter::render);
	}

}
