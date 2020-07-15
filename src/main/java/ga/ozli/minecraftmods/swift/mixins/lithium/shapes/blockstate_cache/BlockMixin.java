package ga.ozli.minecraftmods.swift.mixins.lithium.shapes.blockstate_cache;

import ga.ozli.minecraftmods.swift.Swift;
import ga.ozli.minecraftmods.swift.common.lithium.block.BlockShapeCacheExtended;
import ga.ozli.minecraftmods.swift.common.lithium.block.BlockShapeCacheExtendedProvider;
import ga.ozli.minecraftmods.swift.common.lithium.block.BlockShapeHelper;
import ga.ozli.minecraftmods.swift.common.lithium.util.collections.Object2BooleanCacheTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape; // import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes; // import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.IBlockReader; // import net.minecraft.world.BlockView;
import net.minecraft.world.IWorldReader; // import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Replaces a number of functions in the Block class which are used to determine if some redstone components and other
 * blocks can stand on top of another block. These functions make use of additional cached data in BlockState$ShapeCache.
 */
@Mixin(Block.class)
public class BlockMixin {
    private static final Object2BooleanCacheTable<VoxelShape> FULL_CUBE_CACHE = new Object2BooleanCacheTable<>(
            512,
            shape -> !VoxelShapes.compare(VoxelShapes.fullCube(), shape, IBooleanFunction.NOT_SAME)
    );

    /**
     * @reason Use the shape cache
     * @author JellySquid
     */
    @Overwrite
    public static boolean hasSolidSideOnTop(IBlockReader world, BlockPos pos) {
        Swift.TmpLogger.error("BlockMixin");
        BlockState state = world.getBlockState(pos);
        BlockShapeCacheExtended shapeCache = ((BlockShapeCacheExtendedProvider) state).getExtendedShapeCache();

        if (shapeCache != null) {
            return shapeCache.hasTopRim();
        }

        return hasTopRimFallback(world, pos, state);
    }

    private static boolean hasTopRimFallback(IBlockReader world, BlockPos pos, BlockState state) {
        return BlockShapeHelper.sideCoversSquare(state.getCollisionShape(world, pos).project(Direction.UP), BlockShapeHelper.SOLID_MEDIUM_SQUARE_SHAPE);
    }

    /**
     * @reason Use the shape cache
     * @author JellySquid
     */
    @Overwrite
    public static boolean hasEnoughSolidSide(IWorldReader world, BlockPos pos, Direction side) {
        Swift.TmpLogger.error("BlockMixin");
        BlockState state = world.getBlockState(pos);
        BlockShapeCacheExtended shapeCache = ((BlockShapeCacheExtendedProvider) state).getExtendedShapeCache();

        if (shapeCache != null) {
            return shapeCache.sideCoversSmallSquare(side);
        }

        return sideCoversSmallSquareFallback(world, pos, side, state);
    }

    private static boolean sideCoversSmallSquareFallback(IWorldReader world, BlockPos pos, Direction side, BlockState state) {
        return BlockShapeHelper.sideCoversSquare(state.getCollisionShape(world, pos).project(side), BlockShapeHelper.SOLID_SMALL_SQUARE_SHAPE);
    }

    /**
     * @reason Use a faster cache implementation
     * @author gegy1000
     */
    @Overwrite
    public static boolean isOpaque(VoxelShape shape) {
        Swift.TmpLogger.error("BlockMixin");
        return FULL_CUBE_CACHE.get(shape);
    }
}
