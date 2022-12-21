package net.denanu.clientblockhighlighting.config;

import java.util.ArrayList;

import net.denanu.clientblockhighlighting.Mod;
import net.minecraft.util.Identifier;

public class HighlighterTypes {
	public static ArrayList<HighlighterType> HIGHLIGHTERS = new ArrayList<>();

	public static Identifier DEFAULT_HIGHLIGHT = HighlighterTypes.register("default");




	private static Identifier register(final String key) {
		return HighlighterTypes.register(Mod.MOD_ID, key);
	}

	public static Identifier register(final String mod, final String key) {
		return HighlighterTypes.register(HighlighterType.of(mod, key));
	}

	public static Identifier register(final HighlighterType ident) {
		HighlighterTypes.HIGHLIGHTERS.add(ident);
		return ident;
	}

	public static void build() {
		HighlighterTypes.HIGHLIGHTERS.trimToSize();
	}

	public static void setup() {
		HighlighterTypes.build();
	}
}
