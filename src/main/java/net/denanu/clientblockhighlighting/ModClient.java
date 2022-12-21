package net.denanu.clientblockhighlighting;

import fi.dy.masa.malilib.config.ConfigManager;
import net.denanu.clientblockhighlighting.config.Config;
import net.denanu.clientblockhighlighting.rendering.ClientPosHighlighter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class ModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ConfigManager.getInstance().registerConfigHandler(Mod.MOD_ID, new Config());
		WorldRenderEvents.BEFORE_DEBUG_RENDER.register(ClientPosHighlighter::render);
	}

}
