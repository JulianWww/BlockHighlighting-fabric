package net.denanu.blockhighlighting.comands;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.blockhighlighting.components.ChunkComponents;
import net.denanu.blockhighlighting.config.HighlightTypes;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

public class HighlightCommands {
	public static void register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access, final CommandManager.RegistrationEnvironment env) {
		dispatcher.register(CommandManager.literal("highlight").then(
				CommandManager.argument("pos", BlockPosArgumentType.blockPos()).executes(
						HighlightCommands::highlight
						)
				));

		dispatcher.register(CommandManager.literal("unhighlight").then(
				CommandManager.argument("pos", BlockPosArgumentType.blockPos()).executes(
						HighlightCommands::unhighlight
						)
				));
	}

	private static int highlight(final CommandContext<ServerCommandSource> context) {
		final BlockPos pos = HighlightCommands.getBlockPos(context);
		if (pos == null) {
			return -1;
		}
		ChunkComponents.HIGHLIGHTS.get(context.getSource().getWorld().getChunk(pos)).add(HighlightTypes.DEFAULT_HIGHLIGHTER, pos);
		return 1;
	}

	private static int unhighlight(final CommandContext<ServerCommandSource> context) {
		final BlockPos pos = HighlightCommands.getBlockPos(context);
		if (pos == null) {
			return -1;
		}
		ChunkComponents.HIGHLIGHTS.get(context.getSource().getWorld().getChunk(pos)).remove(HighlightTypes.DEFAULT_HIGHLIGHTER, pos);
		return 1;
	}

	@Nullable
	private static BlockPos getBlockPos(final CommandContext<ServerCommandSource> context) {
		try {
			return BlockPosArgumentType.getBlockPos(context, "pos");
		}
		catch (final Exception e) {
			return null;
		}
	}
}
