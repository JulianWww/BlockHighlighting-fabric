package net.denanu.clientblockhighlighting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.denanu.clientblockhighlighting.rendering.ClientBoxHighlighter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class Mod implements ClientModInitializer {
	public static final String MOD_ID = "client-block-highlighting";
	public static final Logger LOGGER = LoggerFactory.getLogger(Mod.MOD_ID);


	@Override
	public void onInitializeClient() {
		WorldRenderEvents.BEFORE_DEBUG_RENDER.register(ClientBoxHighlighter::render);
	}
}
