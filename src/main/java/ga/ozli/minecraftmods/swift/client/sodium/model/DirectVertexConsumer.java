package ga.ozli.minecraftmods.swift.client.sodium.model;
/*
import net.minecraft.client.renderer.BufferVertexConsumer;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public interface DirectVertexConsumer extends BufferVertexConsumer {
    /**
     * Writes a vertex directly into the consumer with no additional processing. This requires callers to do some
     * upfront work to encode their values.
     *
     * @param x       The x-position of the vertex
     * @param y       The y-position of the vertex
     * @param z       The z-position of the vertex
     * @param color   The color of the vertex in little-endian RGBA format
     * @param u       The u-position of the texture
     * @param v       The v-position of the texture
     * @param overlay The overlay (shadow) of the vertex
     * @param light   The light of the vertex
     * @param normal  The normal of the vertex
     */
    /*void vertex(float x, float y, float z, int color, float u, float v, int overlay, int light, int normal);

    /**
     * Writes a vertex directly into the consumer with no additional processing. This requires callers to do some
     * upfront work to encode their values.
     *
     * @param x     The x-position of the vertex
     * @param y     The y-position of the vertex
     * @param z     The z-position of the vertex
     * @param u     The u-position of the texture
     * @param v     The v-position of the texture
     * @param color The color of the vertex in little-endian RGBA format
     * @param light The light of the vertex
     */
    /*void vertexParticle(float x, float y, float z, float u, float v, int color, int light);

    /**
     * @return True if direct writing can be used on this buffer (i.e. the entire pipeline supports it)
     */
    /*boolean canUseDirectWriting();

    static ga.ozli.minecraftmods.swift.client.sodium.model.DirectVertexConsumer getDirectVertexConsumer(IVertexBuilder consumer) {
        if (!(consumer instanceof ga.ozli.minecraftmods.swift.client.sodium.model.DirectVertexConsumer)) {
            return null;
        }

        ga.ozli.minecraftmods.swift.client.sodium.model.DirectVertexConsumer dConsumer = (ga.ozli.minecraftmods.swift.client.sodium.model.DirectVertexConsumer) consumer;

        if (!dConsumer.canUseDirectWriting()) {
            return null;
        }

        return dConsumer;
    }
}
*/