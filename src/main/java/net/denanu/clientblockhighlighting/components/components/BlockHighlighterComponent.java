package net.denanu.clientblockhighlighting.components.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import net.denanu.clientblockhighlighting.components.ChunkComponents;
import net.denanu.clientblockhighlighting.components.syncdata.KeyUpdate;
import net.denanu.clientblockhighlighting.config.HighlighterTypes;
import net.denanu.clientblockhighlighting.utils.NbtUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class BlockHighlighterComponent implements IBlockHighlighterComponent {
	HashMap<Identifier, HashSet<BlockPos>> positionMap;
	private final Object provider;

	public BlockHighlighterComponent(final Object provider) {
		this.provider = provider;
		this.positionMap = new HashMap<>();

		for (final Identifier key : HighlighterTypes.HIGHLIGHTERS) {
			this.positionMap.put(key, new HashSet<BlockPos>());
		}
	}

	@Override
	public Collection<BlockPos> getPoses(final Identifier key) {
		return this.positionMap.get(key);
	}

	@Override
	public void addPos(final Identifier key, final BlockPos pos) {
		this.positionMap.get(key).add(pos);

		this.sync(new KeyUpdate(key, ImmutableList.of(pos), true));
	}

	@Override
	public void addPoses(final Identifier key, final Collection<BlockPos> poses) {
		this.positionMap.get(key).addAll(poses);

		this.sync(new KeyUpdate(key, poses, true));
	}

	@Override
	public void removePos(final Identifier key, final BlockPos pos) {
		this.positionMap.get(key).remove(pos);

		this.sync(new KeyUpdate(key, ImmutableList.of(pos), false));
	}






	private void sync(final KeyUpdate updater) {
		ChunkComponents.CHUNK_HIGHLIGHTS.sync(this.provider, (buf, p) -> updater.toBuf(buf));
	}

	@Override
	public void readFromNbt(final NbtCompound tag) {
		for (final String key : tag.getKeys()) {
			this.positionMap.put(
					new Identifier(key),
					NbtUtils.toBlockPosSet(tag.getList(key, NbtElement.LIST_TYPE)));
		}
	}

	@Override
	public void writeToNbt(final NbtCompound tag) {
		for (final Entry<Identifier, HashSet<BlockPos>> entries : this.positionMap.entrySet()) {
			tag.put(entries.getKey().toString(), NbtUtils.toNbt(entries.getValue()));
		}
	}

	@Override
	public void writeSyncPacket(final PacketByteBuf buf, final ServerPlayerEntity player) {
		buf.writeBoolean(true);
		buf.writeCollection(this.positionMap.entrySet(), (buf2, entry) -> {
			buf2.writeIdentifier(entry.getKey());
			buf2.writeCollection(entry.getValue(), PacketByteBuf::writeBlockPos);
		});
	}

	@Override
	public void applySyncPacket(final PacketByteBuf buf) {
		if (buf.readBoolean()) {
			this.applyFullSyncUpdate(buf);
		}
		else {
			this.applyMinSyncUpdate(buf);
		}
	}

	private void applyFullSyncUpdate(final PacketByteBuf buf) {
		buf.readList(buf2 -> {
			this.positionMap.put(
					buf2.readIdentifier(),
					buf2.readCollection(HashSet::new, PacketByteBuf::readBlockPos)
					);
			return 1;
		});
	}

	private void applyMinSyncUpdate(final PacketByteBuf buf) {
		final var ident = buf.readIdentifier();
		final List<BlockPos> poses = buf.readList(PacketByteBuf::readBlockPos);
		if (buf.readBoolean()) {
			this.positionMap.get(ident).addAll(poses);
		}
		else {
			this.positionMap.get(ident).removeAll(poses);
		}
	}

	@Override
	public boolean shouldSyncWith(final ServerPlayerEntity player) {
		return true;
	}
}
