package net.denanu.blockhighlighting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.denanu.blockhighlighting.comands.HighlightCommands;
import net.denanu.blockhighlighting.config.HighlightTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class BlockHighlighting implements ModInitializer {
	public static final String MOD_ID = "blockhighlighting";
	public static final Logger LOGGER = LoggerFactory.getLogger(BlockHighlighting.MOD_ID);


	@Override
	public void onInitialize() {
		HighlightTypes.setup();

		CommandRegistrationCallback.EVENT.register(HighlightCommands::register);
	}
}
