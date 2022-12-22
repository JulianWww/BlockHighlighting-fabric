package net.denanu.blockhighlighting.components.component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public interface IChunkComponent extends AutoSyncedComponent {
	void add(Identifier key, BlockPos pos);
	void addAll(Identifier key, Collection<BlockPos> poses);

	void remove(Identifier key, BlockPos pos);
	void removeAll(Identifier key, Collection<BlockPos> poses);

	Collection<BlockPos> getPoses(Identifier ident);
	Map<Identifier, HashSet<BlockPos>> getPoses();
}
