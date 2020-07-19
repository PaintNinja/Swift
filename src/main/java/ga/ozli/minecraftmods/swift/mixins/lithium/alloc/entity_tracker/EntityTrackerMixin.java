package ga.ozli.minecraftmods.swift.mixins.lithium.alloc.entity_tracker;
/* Disabled as memory usage seems lower without it, might be a fluke though

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ChunkManager; // import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ChunkManager.EntityTracker.class)
public class EntityTrackerMixin {
    @Mutable
    @Shadow
    @Final
    private Set<ServerPlayerEntity> trackingPlayers;

    @SuppressWarnings("InvalidInjectorMethodSignature") // The McDev plugin can't handle non-static classes
    @Inject(method = "<init>", at = @At("RETURN"))
    private void reinit(ChunkManager parent /* non-static class parent *//*, Entity entity, int maxDistance,
                        int tickInterval, boolean alwaysUpdateVelocity, CallbackInfo ci) {
        // Uses less memory, and will cache the returned iterator
        this.trackingPlayers = new ObjectOpenHashSet<>(this.trackingPlayers);
    }
}
*/