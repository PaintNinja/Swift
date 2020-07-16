package ga.ozli.minecraftmods.swift.mixins.lithium.shapes.blockstate_cache;

import ga.ozli.minecraftmods.swift.common.lithium.block.BlockShapeCacheExtended;
import ga.ozli.minecraftmods.swift.common.lithium.block.BlockShapeCacheExtendedProvider;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin implements BlockShapeCacheExtendedProvider {
    @Shadow
    protected BlockState.Cache cache;

    @SuppressWarnings("ConstantConditions")
    @Override
    public BlockShapeCacheExtended getExtendedShapeCache() {
        return (BlockShapeCacheExtended) (Object) this.cache;
    }
}