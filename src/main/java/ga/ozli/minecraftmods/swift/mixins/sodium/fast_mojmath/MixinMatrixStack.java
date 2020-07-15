package ga.ozli.minecraftmods.swift.mixins.sodium.fast_mojmath;

import ga.ozli.minecraftmods.swift.common.sodium.util.matrix.Matrix3fExtended;
import ga.ozli.minecraftmods.swift.common.sodium.util.matrix.Matrix4fExtended;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Quaternion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Deque;

@SuppressWarnings("ConstantConditions")
@Mixin(MatrixStack.class)
public class MixinMatrixStack {
    @Shadow
    @Final
    private Deque<MatrixStack.Entry> stack;

    /**
     * @reason Use specialized function
     * @author JellySquid
     */
    @Overwrite
    public void translate(double x, double y, double z) {
        MatrixStack.Entry entry = this.stack.getLast();

        ((Matrix4fExtended) (Object) entry.getMatrix()).translate((float) x, (float) y, (float) z);
    }

    /**
     * @reason Use specialized function
     * @author JellySquid
     */
    @Overwrite
    public void rotate(Quaternion q) {
        MatrixStack.Entry entry = this.stack.getLast();

        ((Matrix4fExtended) (Object) entry.getMatrix()).rotate(q);
        ((Matrix3fExtended) (Object) entry.getNormal()).rotate(q);
    }

}
