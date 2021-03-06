package ga.ozli.minecraftmods.swift.common.lithium.world.chunk.palette;

/**
 * Due to the package-private nature of {@link net.minecraft.util.palette.IResizeCallback}, this re-implements
 * the aforementioned interface publicly.
 */
public interface LithiumResizeCallback<T> {
    int onLithiumPaletteResized(int size, T obj);

}
