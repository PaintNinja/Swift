package ga.ozli.minecraftmods.swift.mixins.sodium.features.gui.font;

import ga.ozli.minecraftmods.swift.client.sodium.model.consumer.GlyphVertexConsumer;
import ga.ozli.minecraftmods.swift.client.sodium.util.color.ColorABGR;
import net.minecraft.client.gui.fonts.TexturedGlyph; // import net.minecraft.client.font.GlyphRenderer;
import com.mojang.blaze3d.vertex.IVertexBuilder; // import net.minecraft.client.render.VertexConsumer; - dafu?
import net.minecraft.util.math.vector.Matrix4f; // import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TexturedGlyph.class)
public class MixinGlyphRenderer {
    @Shadow
    @Final
    private float minX;

    @Shadow
    @Final
    private float maxX;

    @Shadow
    @Final
    private float minY;

    @Shadow
    @Final
    private float maxY;

    @Shadow
    @Final
    private float u0;

    @Shadow
    @Final
    private float v0;

    @Shadow
    @Final
    private float v1;

    @Shadow
    @Final
    private float u1;

    /**
     * @reason Use intrinsics
     * @author JellySquid
     */
    @Overwrite
    public void render(boolean italic, float x, float y, Matrix4f matrix, IVertexBuilder vertexConsumer, float red, float green, float blue, float alpha, int light) {
        float x1 = x + this.minX;
        float x2 = x + this.maxX;
        float y1 = this.minY - 3.0F;
        float y2 = this.maxY - 3.0F;
        float h1 = y + y1;
        float h2 = y + y2;
        float w1 = italic ? 1.0F - 0.25F * y1 : 0.0F;
        float w2 = italic ? 1.0F - 0.25F * y2 : 0.0F;

        int color = ColorABGR.pack(red, green, blue, alpha);

        GlyphVertexConsumer glyphs = ((GlyphVertexConsumer) vertexConsumer);
        glyphs.vertexGlyph(matrix, x1 + w1, h1, 0.0F, color, this.u0, this.v0, light);
        glyphs.vertexGlyph(matrix, x1 + w2, h2, 0.0F, color, this.u0, this.v1, light);
        glyphs.vertexGlyph(matrix, x2 + w2, h2, 0.0F, color, this.u1, this.v1, light);
        glyphs.vertexGlyph(matrix, x2 + w1, h1, 0.0F, color, this.u1, this.v0, light);
    }
}
