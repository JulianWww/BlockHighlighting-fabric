package net.denanu.blockhighlighting.components.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import net.denanu.blockhighlighting.BlockHighlighting;
import net.denanu.blockhighlighting.components.ChunkComponents;
import net.denanu.blockhighlighting.config.HighlightTypes;
import net.denanu.blockhighlighting.utils.NbtUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkComponent implements IChunkComponent {
	Chunk provider;

	HashMap<Identifier, HashSet<BlockPos>> highlights;

	public ChunkComponent(final Chunk provider) {
		this.provider = provider;

		this.highlights = new HashMap<>();
		for (final Identifier ident : HighlightTypes.HIGHLIGHT_TYPES) {
			this.highlights.put(ident, new HashSet<>());
		}
	}

	@Override
	public void add(final Identifier key, final BlockPos pos) {
		this.addAll(key, ImmutableList.of(pos));
	}

	@Override
	public void addAll(final Identifier key, final Collection<BlockPos> poses) {
		this.highlights.get(key).addAll(poses);

		this.update(key, poses, true);
	}

	@Override
	public void remove(final Identifier key, final BlockPos pos) {
		this.removeAll(key, ImmutableList.of(pos));
	}

	@Override
	public void removeAll(final Identifier key, final Collection<BlockPos> poses) {
		this.highlights.get(key).removeAll(poses);

		this.update(key, poses, false);
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
		this.highlights = NbtUtils.fromNbt(tag, this.highlights);
	}

	@Override
	public void writeToNbt(final NbtCompound tag) {
		NbtUtils.toNbt(this.highlights, tag);
	}

	private void update(final Identifier key, final Collection<BlockPos> poses, final boolean action) {
		ChunkComponents.HIGHLIGHTS.sync(this.provider, (buf, p) -> ChunkComponent.minimalSync(buf, key, poses, action));
		this.provider.setNeedsSaving(true);
	}

	private static void minimalSync(final PacketByteBuf buf, final Identifier key, final Collection<BlockPos> poses, final boolean action) {
		buf.writeBoolean(false);
		buf.writeBoolean(action);
		buf.writeIdentifier(key);
		buf.writeCollection(poses, PacketByteBuf::writeBlockPos);
	}

	@Override
	public void writeSyncPacket(final PacketByteBuf buf, final ServerPlayerEntity player) {
		buf.writeBoolean(true);
		buf.writeCollection(this.highlights.entrySet(), (buf2, entry) -> {
			buf2.writeIdentifier(entry.getKey());
			buf2.writeCollection(entry.getValue(), PacketByteBuf::writeBlockPos);
		});
	}

	@Override
	public void applySyncPacket(final PacketByteBuf buf) {
		if (buf.readBoolean()) {
			this.applyFullSync(buf);
		}
		else {
			this.applyMinimalSync(buf);
		}
	}

	private void applyMinimalSync(final PacketByteBuf buf) {
		final boolean action = buf.readBoolean();
		final Identifier id = buf.readIdentifier();
		final List<BlockPos> poses = buf.readList(PacketByteBuf::readBlockPos);

		if (action) {
			this.highlights.get(id).addAll(poses);
		}
		else {
			this.highlights.get(id).removeAll(poses);
		}
	}

	private void applyFullSync(final PacketByteBuf buf) {
		buf.readList(buf2 -> {
			final Identifier id = buf2.readIdentifier();
			final HashSet<BlockPos> poses = buf2.readCollection(HashSet<BlockPos>::new, PacketByteBuf::readBlockPos);

			HashSet<BlockPos> set = this.highlights.get(id);
			if (set == null) {
				set = new HashSet<>();
				this.highlights.put(id, set);
				BlockHighlighting.LOGGER.warn("Unable to find key " + id.toString() + " in highlighter map");
			}
			set.clear();
			set.addAll(poses);
			return 1;
		});
	}
}
