package net.denanu.clientblockhighlighting.components;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.denanu.clientblockhighlighting.Mod;
import net.denanu.clientblockhighlighting.components.component.ChunkComponent;
import net.denanu.clientblockhighlighting.components.component.IChunkComponent;
import net.minecraft.util.Identifier;

public class ChunkComponents implements ChunkComponentInitializer {
	public static final ComponentKey<IChunkComponent> HIGHLIGHTS = ComponentRegistry.getOrCreate(Identifier.of(Mod.MOD_ID, "chunkhighlight"), IChunkComponent.class);

	@Override
	public void registerChunkComponentFactories(final ChunkComponentFactoryRegistry registry) {
		registry.register(ChunkComponents.HIGHLIGHTS, ChunkComponent::new);
	}
}
