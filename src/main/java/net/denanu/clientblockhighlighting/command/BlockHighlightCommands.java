package net.denanu.clientblockhighlighting.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.clientblockhighlighting.Mod;
import net.denanu.clientblockhighlighting.components.ChunkComponents;
import net.denanu.clientblockhighlighting.config.HighlighterTypes;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class BlockHighlightCommands {
	public static void register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access, final CommandManager.RegistrationEnvironment env) {
		LiteralArgumentBuilder<ServerCommandSource> namespace = CommandManager.literal("highlight");

		namespace.then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
				.executes(
						BlockHighlightCommands::highlight
						)
				);

		dispatcher.register(namespace);
		namespace = CommandManager.literal("unhighlight");

		namespace.then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
				.executes(
						BlockHighlightCommands::unhighlight
						)
				);

		dispatcher.register(namespace);
	}

	private static int highlight(final CommandContext<ServerCommandSource> context) {
		final Pair<Chunk, BlockPos> chunkPos = BlockHighlightCommands.getChunk(context);
		if (chunkPos != null) {
			ChunkComponents.CHUNK_HIGHLIGHTS.get(chunkPos.getLeft()).addPos(HighlighterTypes.DEFAULT_HIGHLIGHT, chunkPos.getRight());
			return 1;
		}
		return 0;
	}

	private static int unhighlight(final CommandContext<ServerCommandSource> context) {
		final Pair<Chunk, BlockPos> chunkPos = BlockHighlightCommands.getChunk(context);
		if (chunkPos != null) {
			ChunkComponents.CHUNK_HIGHLIGHTS.get(chunkPos.getLeft()).removePos(HighlighterTypes.DEFAULT_HIGHLIGHT, chunkPos.getRight());
			return 1;
		}
		return 0;
	}

	private static BlockPos getHighlightPos(final CommandContext<ServerCommandSource> context) {
		try {
			return BlockPosArgumentType.getBlockPos(context, "pos");
		}
		catch (final Exception e) {
			Mod.LOGGER.info(e.toString());
			return null;
		}
	}

	private static Pair<Chunk, BlockPos> getChunk(final CommandContext<ServerCommandSource> context) {
		final BlockPos pos = BlockHighlightCommands.getHighlightPos(context);

		return pos == null ? null : new Pair<>(context.getSource().getWorld().getChunk(pos), pos);
	}
}
