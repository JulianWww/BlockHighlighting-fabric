package net.denanu.blockhighlighting.config;

import java.util.ArrayList;

import net.denanu.blockhighlighting.BlockHighlighting;
import net.minecraft.util.Identifier;

public class HighlightIds {
	public static ArrayList<Identifier> HIGHLIGHT_TYPES = new ArrayList<>();

	public static final Identifier DEFAULT_HIGHLIGHTER = HighlightIds.register(Identifier.of(BlockHighlighting.MOD_ID, "default"));

	public static Identifier register(final Identifier id) {
		HighlightIds.HIGHLIGHT_TYPES.add(id);
		return id;
	}

	public static void setup() {}
}
