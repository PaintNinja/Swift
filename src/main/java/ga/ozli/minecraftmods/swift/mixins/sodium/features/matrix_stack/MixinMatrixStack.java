package ga.ozli.minecraftmods.swift.mixins.sodium.features.matrix_stack;

import ga.ozli.minecraftmods.swift.client.sodium.util.math.Matrix3fExtended;
import ga.ozli.minecraftmods.swift.client.sodium.util.math.Matrix4fExtended;
import ga.ozli.minecraftmods.swift.client.sodium.util.math.MatrixUtil;
import com.mojang.blaze3d.matrix.MatrixStack; // import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.vector.Quaternion; // import net.minecraft.util.math.Quaternion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Deque;

@Mixin(MatrixStack.class)
public class MixinMatrixStack {
    @Shadow
    @Final
    private Deque<MatrixStack.Entry> stack;

    /**
     * @reason Use our faster specialized function
     * @author JellySquid
     */
    @Overwrite
    public void translate(double x, double y, double z) {
        MatrixStack.Entry entry = this.stack.getLast();

        Matrix4fExtended mat = MatrixUtil.getExtendedMatrix(entry.getMatrix());
        mat.translate((float) x, (float) y, (float) z);
    }

    /**
     * @reason Use our faster specialized function
     * @author JellySquid
     */
    @Overwrite
    public void rotate(Quaternion q) {
        MatrixStack.Entry entry = this.stack.getLast();

        Matrix4fExtended mat4 = MatrixUtil.getExtendedMatrix(entry.getMatrix());
        mat4.rotate(q);

        Matrix3fExtended mat3 = MatrixUtil.getExtendedMatrix(entry.getNormal());
        mat3.rotate(q);
    }
}
