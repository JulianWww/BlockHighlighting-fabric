package net.denanu.clientblockhighlighting.components.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import net.denanu.clientblockhighlighting.config.HighlightTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ChunkComponent implements IChunkComponent {
	Object provider;

	HashMap<Identifier, HashSet<BlockPos>> highlights;

	public ChunkComponent(final Object provider) {
		this.provider = provider;

		this.highlights = new HashMap<>();
		for (final Identifier ident : HighlightTypes.HIGHLIGHT_TYPES) {
			this.highlights.put(ident, new HashSet<>());
		}
	}

	@Override
	public void add(final Identifier key, final BlockPos pos) {
		this.highlights.get(key).add(pos);
	}

	@Override
	public void addAll(final Identifier key, final Collection<BlockPos> poses) {
		this.highlights.get(key).addAll(poses);

	}

	@Override
	public void remove(final Identifier key, final BlockPos pos) {
		this.highlights.get(key).remove(pos);

	}

	@Override
	public void removeAll(final Identifier key, final Collection<BlockPos> poses) {
		this.highlights.get(key).removeAll(poses);

	}

	@Override
	public Collection<BlockPos> getPoses(final Identifier ident) {
		return this.highlights.get(ident);
	}

	@Override
	public Map<Identifier, HashSet<BlockPos>> getPoses() {
		return this.highlights;
	}

	@Override
	public void readFromNbt(final NbtCompound tag) {
		for (final Entry<Identifier, HashSet<BlockPos>> highlighter : this.highlights.entrySet()) {

		}
	}

	@Override
	public void writeToNbt(final NbtCompound tag) {
		// TODO Auto-generated method stub

	}

	private NbtList putToNbt(final Collection<BlockPos> pos) {

	}

}
