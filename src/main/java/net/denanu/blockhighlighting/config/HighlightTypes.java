package net.denanu.blockhighlighting.config;

import java.util.ArrayList;

import net.denanu.blockhighlighting.BlockHighlighting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HighlightTypes {
	public static ArrayList<HighlightType> HIGHLIGHT_TYPES = new ArrayList<>();

	public static HighlightType DEFAULT_HIGHLIGHTER = HighlightTypes.register("default");


	private static HighlightType register(final String name) {
		return HighlightTypes.register(BlockHighlighting.MOD_ID, name);
	}

	public static HighlightType register(final String mod, final String name) {
		return HighlightTypes.register(HighlightType.of(mod, name));
	}

	public static HighlightType register(final String mod, final String name, final String outlineColor, final String fillColor) {
		return HighlightTypes.register(HighlightType.of(mod, name, fillColor, outlineColor));
	}

	public static HighlightType register(final HighlightType type) {
		HighlightTypes.HIGHLIGHT_TYPES.add(type);
		return type;
	}

	public static void setup() {
	}
}
