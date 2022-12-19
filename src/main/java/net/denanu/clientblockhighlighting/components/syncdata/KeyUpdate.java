package net.denanu.clientblockhighlighting.components.syncdata;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class KeyUpdate {
	private final Identifier key;
	private final boolean add;
	private final Collection<BlockPos> poses;

	public KeyUpdate(final Identifier key, final Collection<BlockPos> poses, final boolean add) {
		this.key = key;
		this.poses = poses;
		this.add = add;
	}

	public KeyUpdate(final PacketByteBuf buf) {
		this.key = buf.readIdentifier();
		this.poses = buf.readCollection(ArrayList::new, PacketByteBuf::readBlockPos);
		this.add = buf.readBoolean();
	}

	public PacketByteBuf toBuf(final PacketByteBuf buf) {
		buf.writeBoolean(false);
		buf.writeIdentifier(this.key);
		buf.writeCollection(this.poses, PacketByteBuf::writeBlockPos);
		buf.writeBoolean(this.add);
		return buf;
	}

	public Identifier getKey() {
		return this.key;
	}

	public Collection<BlockPos> getPoses() {
		return this.poses;
	}
}
