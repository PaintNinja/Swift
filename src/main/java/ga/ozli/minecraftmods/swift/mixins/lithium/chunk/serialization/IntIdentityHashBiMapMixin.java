package ga.ozli.minecraftmods.swift.mixins.lithium.chunk.serialization;

import com.google.common.collect.Iterators;
import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.IntIdentityHashBiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;

@Mixin(IntIdentityHashBiMap.class)
public class IntIdentityHashBiMapMixin<K> implements IObjectIntIterable<K> {
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

    /**
     * @reason Replaced Guava's functional primitive with Java API, remove Lambda
     * @author Paint_Ninja
     */
    @Overwrite
    @Nonnull
    public Iterator<K> iterator() {
        return Iterators.filter(Iterators.forArray(this.byId), Objects::nonNull);
    }

}