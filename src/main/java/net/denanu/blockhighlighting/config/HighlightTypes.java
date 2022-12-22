package net.denanu.blockhighlighting.config;

import java.util.ArrayList;

import net.denanu.blockhighlighting.BlockHighlighting;

public class HighlightTypes {
	public static ArrayList<HighlightType> HIGHLIGHT_TYPES = new ArrayList<>();


	public static HighlightType DEFAULT_HIGHLIGHTER = HighlightTypes.register("default");


	private static HighlightType register(final String name) {
		return HighlightTypes.register(BlockHighlighting.MOD_ID, name);
	}

	public static HighlightType register(final String mod, final String name) {
		return HighlightTypes.register(HighlightType.of(name, name));
	}

	public static HighlightType register(final String mod, final String name, final String outlineColor, final String fillColor) {
		return HighlightTypes.register(HighlightType.of(name, name, fillColor, outlineColor));
	}

	public static HighlightType register(final HighlightType highlighter) {
		HighlightTypes.HIGHLIGHT_TYPES.add(highlighter);
		return highlighter;
	}

	public static void setup() {
	}
}
