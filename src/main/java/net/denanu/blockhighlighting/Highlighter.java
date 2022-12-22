package net.denanu.blockhighlighting;

import java.util.Collection;

import net.denanu.blockhighlighting.components.ChunkComponents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class Highlighter {
	public static void highlight(final Chunk chunk, final Identifier key, final BlockPos pos) {
		ChunkComponents.HIGHLIGHTS.get(chunk).add(key, pos);
	}

	public static void highlight(final Chunk chunk, final Identifier key, final Collection<BlockPos> pos) {
		ChunkComponents.HIGHLIGHTS.get(chunk).addAll(key, pos);
	}

	public static void unhighlight(final Chunk chunk, final Identifier key, final BlockPos pos) {
		ChunkComponents.HIGHLIGHTS.get(chunk).remove(key, pos);
	}

	public static void unhighlight(final Chunk chunk, final Identifier key, final Collection<BlockPos> pos) {
		ChunkComponents.HIGHLIGHTS.get(chunk).removeAll(key, pos);
	}

	public static void highlight(final ServerWorld world, final Identifier key, final BlockPos pos) {
		ChunkComponents.HIGHLIGHTS.get(Highlighter.getChunk(world, pos)).add(key, pos);
	}


	public static void unhighlight(final ServerWorld world, final Identifier key, final BlockPos pos) {
		ChunkComponents.HIGHLIGHTS.get(Highlighter.getChunk(world, pos)).remove(key, pos);
	}

	private static Chunk getChunk(final ServerWorld world, final BlockPos pos) {
		return world.getChunk(pos);
	}
}
