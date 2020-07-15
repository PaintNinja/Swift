package ga.ozli.minecraftmods.swift.mixins.lithium.shapes.shape_merging;

import ga.ozli.minecraftmods.swift.Swift;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import ga.ozli.minecraftmods.swift.common.lithium.shapes.pairs.LithiumDoublePairList;
import net.minecraft.util.math.shapes.IDoubleListMerger; // import net.minecraft.util.shape.PairList;
import net.minecraft.util.math.shapes.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VoxelShapes.class)
public class VoxelShapesMixin {
    /**
     * Replaces the returned list pair with our own optimized type.
     */
    @Inject(method = "createListPair", at = @At(value = "NEW", target = "net/minecraft/util/math/shapes/IDoubleListMerger", shift = At.Shift.BEFORE), cancellable = true)
    private static void injectCustomListPair(int size, DoubleList a, DoubleList b, boolean flag1, boolean flag2, CallbackInfoReturnable<IDoubleListMerger> cir) {
        Swift.TmpLogger.error("VoxelShapesMixin");
        cir.setReturnValue(new LithiumDoublePairList(a, b, flag1, flag2));
    }
}
