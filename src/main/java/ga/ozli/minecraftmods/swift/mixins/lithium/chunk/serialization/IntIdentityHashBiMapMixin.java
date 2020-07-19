package ga.ozli.minecraftmods.swift.mixins.lithium.chunk.serialization;

import net.minecraft.util.IntIdentityHashBiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(IntIdentityHashBiMap.class)
public class IntIdentityHashBiMapMixin<K> {
    @Shadow
    private K[] byId;

    /**
     * @reason Remove unnecessary casting
     * @author Paint_Ninja
     */
    @Overwrite
    @Nullable
    public K getByValue(int value) {
        return value >= 0 && value < this.byId.length ? this.byId[value] : null;
    }
}