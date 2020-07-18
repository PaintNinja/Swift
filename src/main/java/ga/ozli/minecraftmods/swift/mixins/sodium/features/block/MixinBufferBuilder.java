package ga.ozli.minecraftmods.swift.mixins.sodium.features.block;

import ga.ozli.minecraftmods.swift.client.sodium.model.quad.ModelQuadViewMutable;
import ga.ozli.minecraftmods.swift.client.sodium.model.quad.sink.ModelQuadSink;
import ga.ozli.minecraftmods.swift.client.sodium.util.ModelQuadUtil;
import net.minecraft.client.renderer.BufferBuilder; // import net.minecraft.client.render.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultColorVertexBuilder; // import net.minecraft.client.render.FixedColorVertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteBuffer;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder extends DefaultColorVertexBuilder implements ModelQuadSink {
    @Shadow
    private int nextElementBytes;

    @Shadow
    private ByteBuffer byteBuffer;

    @Shadow
    protected abstract void growBuffer(int size);

    @Shadow
    private int vertexCount;

    @Override
    public void write(ModelQuadViewMutable quad) {
        this.growBuffer(ModelQuadUtil.VERTEX_SIZE_BYTES);

        quad.copyInto(this.byteBuffer, this.nextElementBytes);

        this.nextElementBytes += ModelQuadUtil.VERTEX_SIZE_BYTES;
        this.vertexCount += 4;
    }
}
