package net.denanu.clientblockhighlighting.components;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.denanu.clientblockhighlighting.Mod;
import net.denanu.clientblockhighlighting.components.components.BlockHighlighterComponent;
import net.denanu.clientblockhighlighting.components.components.IBlockHighlighterComponent;
import net.minecraft.util.Identifier;

public class ChunkComponents implements ChunkComponentInitializer {
	public static final ComponentKey<IBlockHighlighterComponent> CHUNK_HIGHLIGHTS = ComponentRegistry.getOrCreate(Identifier.of(Mod.MOD_ID, "chungkhighlights"), IBlockHighlighterComponent.class);

	@Override
	public void registerChunkComponentFactories(final ChunkComponentFactoryRegistry registry) {
		registry.register(ChunkComponents.CHUNK_HIGHLIGHTS, BlockHighlighterComponent::new);
	}

}
