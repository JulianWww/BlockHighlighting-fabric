package net.denanu.clientblockhighlighting.utils;

import java.util.HashSet;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

public class NbtUtils {

	public static <E extends Iterable<BlockPos>> NbtList toNbt(final E list) {
		final var nbt = new NbtList();
		for (final BlockPos pos: list) {
			nbt.add(NbtUtils.toNbt(pos));
		}
		return nbt;
	}

	public static NbtElement toNbt(final BlockPos pos) {
		final var out = new NbtList();
		if (pos!= null) {
			out.add(NbtInt.of(pos.getX()));
			out.add(NbtInt.of(pos.getY()));
			out.add(NbtInt.of(pos.getZ()));
		}
		return out;
	}

	public static HashSet<BlockPos> toBlockPosSet(final NbtList tag) {
		final HashSet<BlockPos> list = new HashSet<>();
		for (var idx=0; idx < tag.size(); idx++) {
			final var pos = NbtUtils.toBlockPos(tag.getList(idx));
			if (pos != null) {
				list.add(pos);
			}
		}
		return list;
	}

	public static BlockPos toBlockPos(final NbtList list) {
		if (list.size() == 0) {
			return null;
		}
		return new BlockPos(list.getInt(0), list.getInt(1),  list.getInt(2));
	}
}
