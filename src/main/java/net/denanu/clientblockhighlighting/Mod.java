package net.denanu.clientblockhighlighting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.denanu.clientblockhighlighting.config.HighlightTypes;
import net.fabricmc.api.ModInitializer;

public class Mod implements ModInitializer {
	public static final String MOD_ID = "clientblockhighlighting";
	public static final Logger LOGGER = LoggerFactory.getLogger(Mod.MOD_ID);


	@Override
	public void onInitialize() {
		HighlightTypes.setup();
	}
}
