package ga.ozli.minecraftmods.swift.common.lithium.util.math;

public class Color4 {
    public final int r, g, b, a;

    public Color4(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static ga.ozli.minecraftmods.swift.common.lithium.util.math.Color4 fromRGBA(int color) {
        return new ga.ozli.minecraftmods.swift.common.lithium.util.math.Color4((color >> 16 & 255), (color >> 8 & 255), (color & 255), (color >> 24 & 255));
    }
}
