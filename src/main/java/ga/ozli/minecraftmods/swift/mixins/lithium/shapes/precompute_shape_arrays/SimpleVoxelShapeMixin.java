package ga.ozli.minecraftmods.swift.mixins.lithium.shapes.precompute_shape_arrays;

import ga.ozli.minecraftmods.swift.Swift;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import ga.ozli.minecraftmods.swift.common.lithium.shapes.lists.FractionalDoubleList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShapeCube;
import net.minecraft.util.math.shapes.VoxelShapePart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VoxelShapeCube.class)
public class SimpleVoxelShapeMixin {
    private static final Direction.Axis[] AXIS = Direction.Axis.values();

    private DoubleList[] list;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(VoxelShapePart voxels, CallbackInfo ci) {
        Swift.TmpLogger.error("SimpleVoxelShapeMixin");
        this.list = new DoubleList[AXIS.length];

        for (Direction.Axis axis : AXIS) {
            this.list[axis.ordinal()] = new FractionalDoubleList(voxels.getSize(axis));
        }
    }

    /**
     * @reason Use the cached array.
     * @author JellySquid
     */
    @Overwrite
    public DoubleList getValues(Direction.Axis axis) {
        Swift.TmpLogger.error("SimpleVoxelShapeMixin");
        return this.list[axis.ordinal()];
    }

}
