package ga.ozli.minecraftmods.swift.mixins.lithium.shapes.blockstate_cache;

import ga.ozli.minecraftmods.swift.common.lithium.block.BlockShapeCacheExtended;
import ga.ozli.minecraftmods.swift.common.lithium.block.BlockShapeHelper;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags; // import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape; // import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EmptyBlockReader; // import net.minecraft.world.EmptyBlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Extends the ShapeCache to contain some additional properties as to avoid expensive computation from some Redstone
 * components which look to see if a block's shape can support it. This prompted an issue to be opened on the Mojang
 * issue tracker, which contains some additional information: https://bugs.mojang.com/browse/MC-174568
 */
@Mixin(BlockState.Cache.class)
public class BlockShapeCacheMixin implements BlockShapeCacheExtended {
    private static final Direction[] DIRECTIONS = Direction.values();

    @Shadow
    @Final
    protected boolean[] solidSides;

    @Shadow
    @Final
    protected boolean opaqueCollisionShape;

    private byte sideCoversSmallSquare;
    private boolean hasTopRim;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(BlockState state, CallbackInfo ci) {
        // [VanillaCopy] Leaf blocks are a special case which can never support other blocks
        // This is exactly how vanilla itself implements the check.
        if (!state.isIn(BlockTags.LEAVES)) {
            this.initSidedProperties(state);
        }
    }

    private void initSidedProperties(BlockState state) {
        VoxelShape shape = state.getRenderShape(EmptyBlockReader.INSTANCE, BlockPos.ZERO);

        // If the shape is a full cube and the top face is a full square, it can always support another component
        this.hasTopRim = (this.opaqueCollisionShape && this.solidSides[Direction.UP.ordinal()]) ||
                BlockShapeHelper.sideCoversSquare(shape.project(Direction.UP), BlockShapeHelper.SOLID_MEDIUM_SQUARE_SHAPE);

        for (Direction side : DIRECTIONS) {
            // [VanillaCopy] Block#sideCoversSmallSquare
            if (side == Direction.DOWN && state.isIn(BlockTags.UNSTABLE_BOTTOM_CENTER)) {
                continue;
            }

            if (this.solidSides[side.ordinal()] || BlockShapeHelper.sideCoversSquare(shape.project(side), BlockShapeHelper.SOLID_SMALL_SQUARE_SHAPE)) {
                this.sideCoversSmallSquare |= (1 << side.ordinal());
            }
        }
    }

    @Override
    public boolean sideCoversSmallSquare(Direction facing) {
        return (this.sideCoversSmallSquare & (1 << facing.ordinal())) != 0;
    }

    @Override
    public boolean hasTopRim() {
        return this.hasTopRim;
    }
}