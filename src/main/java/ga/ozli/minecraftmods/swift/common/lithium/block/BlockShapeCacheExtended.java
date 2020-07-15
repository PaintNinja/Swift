package ga.ozli.minecraftmods.swift.common.lithium.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader; // import net.minecraft.world.BlockView;
import net.minecraft.world.IWorldReader; // import net.minecraft.world.WorldView;

public interface BlockShapeCacheExtended {
    /**
     * Cached version of {@link net.minecraft.block.Block#sideCoversSmallSquare(IWorldReader, BlockPos, Direction)}
     */
    boolean sideCoversSmallSquare(Direction facing);

    /**
     * Cached and directional version of {@link net.minecraft.block.Block#hasTopRim(IBlockReader, BlockPos)}
     */
    boolean hasTopRim();
}
