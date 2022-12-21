package net.denanu.clientblockhighlighting.components.components;

import java.util.Collection;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public interface IBlockHighlighterComponent extends AutoSyncedComponent {
	Collection<BlockPos> getPoses(Identifier key);
	void addPos(Identifier key, BlockPos pos);
	void addPoses(Identifier key, Collection<BlockPos> poses);

	void removePos(Identifier key, BlockPos pos);
}
