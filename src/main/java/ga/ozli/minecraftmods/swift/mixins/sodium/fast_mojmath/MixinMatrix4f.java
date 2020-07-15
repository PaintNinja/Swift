package ga.ozli.minecraftmods.swift.mixins.sodium.fast_mojmath;

import ga.ozli.minecraftmods.swift.client.sodium.util.UnsafeUtil;
import ga.ozli.minecraftmods.swift.common.sodium.util.matrix.Matrix4fExtended;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import sun.misc.Unsafe;

import java.nio.BufferUnderflowException;
import java.nio.FloatBuffer;

@SuppressWarnings("PointlessArithmeticExpression")
@Mixin(Matrix4f.class)
public abstract class MixinMatrix4f implements Matrix4fExtended {
    private static final boolean USE_UNSAFE = UnsafeUtil.isAvailable();
    private static final Unsafe UNSAFE = UnsafeUtil.instanceNullable();

    @Shadow
    protected float m00;
    @Shadow
    protected float m01;
    @Shadow
    protected float m02;
    @Shadow
    protected float m03;
    @Shadow
    protected float m10;
    @Shadow
    protected float m11;
    @Shadow
    protected float m12;
    @Shadow
    protected float m13;
    @Shadow
    protected float m20;
    @Shadow
    protected float m21;
    @Shadow
    protected float m22;
    @Shadow
    protected float m23;
    @Shadow
    protected float m30;
    @Shadow
    protected float m31;
    @Shadow
    protected float m32;
    @Shadow
    protected float m33;

    @Override
    public void rotate(Quaternion quaternion) {
        boolean x = quaternion.getX() != 0.0F;
        boolean y = quaternion.getY() != 0.0F;
        boolean z = quaternion.getZ() != 0.0F;

        // Try to determine if this is a simple rotation on one axis component only
        if (x) {
            if (!y && !z) {
                this.rotateX(quaternion);
            } else {
                this.rotateXYZ(quaternion);
            }
        } else if (y) {
            if (!z) {
                this.rotateY(quaternion);
            } else {
                this.rotateXYZ(quaternion);
            }
        } else if (z) {
            this.rotateZ(quaternion);
        }
    }

    private void rotateX(Quaternion quaternion) {
        float x = quaternion.getX();
        float w = quaternion.getW();

        float xx = 2.0F * x * x;
        float ta11 = 1.0F - xx;
        float ta22 = 1.0F - xx;

        float xw = x * w;

        float ta21 = 2.0F * xw;
        float ta12 = 2.0F * -xw;

        float m01 = this.m01 * ta11 + this.m02 * ta21;
        float m02 = this.m01 * ta12 + this.m02 * ta22;
        float m11 = this.m11 * ta11 + this.m12 * ta21;
        float m12 = this.m11 * ta12 + this.m12 * ta22;
        float m21 = this.m21 * ta11 + this.m22 * ta21;
        float m22 = this.m21 * ta12 + this.m22 * ta22;
        float m31 = this.m31 * ta11 + this.m32 * ta21;
        float m32 = this.m31 * ta12 + this.m32 * ta22;

        this.m01 = m01;
        this.m02 = m02;
        this.m11 = m11;
        this.m12 = m12;
        this.m21 = m21;
        this.m22 = m22;
        this.m31 = m31;
        this.m32 = m32;
    }

    private void rotateY(Quaternion quaternion) {
        float y = quaternion.getY();
        float w = quaternion.getW();

        float yy = 2.0F * y * y;
        float ta00 = 1.0F - yy;
        float ta22 = 1.0F - yy;
        float yw = y * w;
        float ta20 = 2.0F * -yw;
        float ta02 = 2.0F * yw;

        float m00 = this.m00 * ta00 + this.m02 * ta20;
        float m02 = this.m00 * ta02 + this.m02 * ta22;
        float m10 = this.m10 * ta00 + this.m12 * ta20;
        float m12 = this.m10 * ta02 + this.m12 * ta22;
        float m20 = this.m20 * ta00 + this.m22 * ta20;
        float m22 = this.m20 * ta02 + this.m22 * ta22;
        float m30 = this.m30 * ta00 + this.m32 * ta20;
        float m32 = this.m30 * ta02 + this.m32 * ta22;

        this.m00 = m00;
        this.m02 = m02;
        this.m10 = m10;
        this.m12 = m12;
        this.m20 = m20;
        this.m22 = m22;
        this.m30 = m30;
        this.m32 = m32;
    }

    private void rotateZ(Quaternion quaternion) {
        float z = quaternion.getZ();
        float w = quaternion.getW();

        float zz = 2.0F * z * z;
        float ta00 = 1.0F - zz;
        float ta11 = 1.0F - zz;
        float zw = z * w;
        float ta10 = 2.0F * zw;
        float ta01 = 2.0F * -zw;

        float m00 = this.m00 * ta00 + this.m01 * ta10;
        float m01 = this.m00 * ta01 + this.m01 * ta11;
        float m10 = this.m10 * ta00 + this.m11 * ta10;
        float m11 = this.m10 * ta01 + this.m11 * ta11;
        float m20 = this.m20 * ta00 + this.m21 * ta10;
        float m21 = this.m20 * ta01 + this.m21 * ta11;
        float m30 = this.m30 * ta00 + this.m31 * ta10;
        float m31 = this.m30 * ta01 + this.m31 * ta11;

        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        this.m20 = m20;
        this.m21 = m21;
        this.m30 = m30;
        this.m31 = m31;
    }

    private void rotateXYZ(Quaternion quaternion) {
        float x = quaternion.getX();
        float y = quaternion.getY();
        float z = quaternion.getZ();
        float w = quaternion.getW();

        float xx = 2.0F * x * x;
        float yy = 2.0F * y * y;
        float zz = 2.0F * z * z;
        float ta00 = 1.0F - yy - zz;
        float ta11 = 1.0F - zz - xx;
        float ta22 = 1.0F - xx - yy;
        float xy = x * y;
        float yz = y * z;
        float zx = z * x;
        float xw = x * w;
        float yw = y * w;
        float zw = z * w;
        float ta10 = 2.0F * (xy + zw);
        float ta01 = 2.0F * (xy - zw);
        float ta20 = 2.0F * (zx - yw);
        float ta02 = 2.0F * (zx + yw);
        float ta21 = 2.0F * (yz + xw);
        float ta12 = 2.0F * (yz - xw);

        float m00 = this.m00 * ta00 + this.m01 * ta10 + this.m02 * ta20;
        float m01 = this.m00 * ta01 + this.m01 * ta11 + this.m02 * ta21;
        float m02 = this.m00 * ta02 + this.m01 * ta12 + this.m02 * ta22;
        float m10 = this.m10 * ta00 + this.m11 * ta10 + this.m12 * ta20;
        float m11 = this.m10 * ta01 + this.m11 * ta11 + this.m12 * ta21;
        float m12 = this.m10 * ta02 + this.m11 * ta12 + this.m12 * ta22;
        float m20 = this.m20 * ta00 + this.m21 * ta10 + this.m22 * ta20;
        float m21 = this.m20 * ta01 + this.m21 * ta11 + this.m22 * ta21;
        float m22 = this.m20 * ta02 + this.m21 * ta12 + this.m22 * ta22;
        float m30 = this.m30 * ta00 + this.m31 * ta10 + this.m32 * ta20;
        float m31 = this.m30 * ta01 + this.m31 * ta11 + this.m32 * ta21;
        float m32 = this.m30 * ta02 + this.m31 * ta12 + this.m32 * ta22;

        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
    }

    @Override
    public void translate(float x, float y, float z) {
        this.m03 = this.m00 * x + this.m01 * y + this.m02 * z + this.m03;
        this.m13 = this.m10 * x + this.m11 * y + this.m12 * z + this.m13;
        this.m23 = this.m20 * x + this.m21 * y + this.m22 * z + this.m23;
        this.m33 = this.m30 * x + this.m31 * y + this.m32 * z + this.m33;
    }

    /**
     * @reason Optimize
     * @author JellySquid
     */
    @Overwrite
    public void write(FloatBuffer buf) {
        if (buf.remaining() < 16) {
            throw new BufferUnderflowException();
        }

        if (USE_UNSAFE) {
            this.writeToBufferUnsafe(buf);
        } else {
            this.writeToBufferSafe(buf);
        }
    }

    private void writeToBufferUnsafe(FloatBuffer buf) {
        long addr = MemoryUtil.memAddress(buf);

        UNSAFE.putFloat(addr + 0, this.m00);
        UNSAFE.putFloat(addr + 4, this.m10);
        UNSAFE.putFloat(addr + 8, this.m20);
        UNSAFE.putFloat(addr + 12, this.m30);
        UNSAFE.putFloat(addr + 16, this.m01);
        UNSAFE.putFloat(addr + 20, this.m11);
        UNSAFE.putFloat(addr + 24, this.m21);
        UNSAFE.putFloat(addr + 28, this.m31);
        UNSAFE.putFloat(addr + 32, this.m02);
        UNSAFE.putFloat(addr + 36, this.m12);
        UNSAFE.putFloat(addr + 40, this.m22);
        UNSAFE.putFloat(addr + 44, this.m32);
        UNSAFE.putFloat(addr + 48, this.m03);
        UNSAFE.putFloat(addr + 52, this.m13);
        UNSAFE.putFloat(addr + 56, this.m23);
        UNSAFE.putFloat(addr + 60, this.m33);
    }

    private void writeToBufferSafe(FloatBuffer buf) {
        buf.put(0, this.m00);
        buf.put(1, this.m10);
        buf.put(2, this.m20);
        buf.put(3, this.m30);
        buf.put(4, this.m01);
        buf.put(5, this.m11);
        buf.put(6, this.m21);
        buf.put(7, this.m31);
        buf.put(8, this.m02);
        buf.put(9, this.m12);
        buf.put(10, this.m22);
        buf.put(11, this.m32);
        buf.put(12, this.m03);
        buf.put(13, this.m13);
        buf.put(14, this.m23);
        buf.put(15, this.m33);
    }

    @Override
    public void writeTranslation(FloatBuffer buf, float x, float y, float z) {
        float m03 = this.m00 * x + this.m01 * y + this.m02 * z + this.m03;
        float m13 = this.m10 * x + this.m11 * y + this.m12 * z + this.m13;
        float m23 = this.m20 * x + this.m21 * y + this.m22 * z + this.m23;
        float m33 = this.m30 * x + this.m31 * y + this.m32 * z + this.m33;

        buf.put(12, m03);
        buf.put(13, m13);
        buf.put(14, m23);
        buf.put(15, m33);
    }
}