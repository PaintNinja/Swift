package ga.ozli.minecraftmods.swift;

import ga.ozli.minecraftmods.swift.config.SwiftConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CriteriaChecker {

    private static final Logger LOGGER = LogManager.getLogger(Swift.MODID + " Criteria Checking");

    public static boolean isWorldLoaded(final Minecraft mc) {
        return mc.world != null;
    }

    public static boolean isPlayerEntityLoadedAndOldEnough(final Minecraft mc) {
        final ClientPlayerEntity player = mc.player;
        if (player == null) {
            //LOGGER.debug("Player entity is not loaded yet.");
            return false;
        } else {
            // player entity is loaded, check if old enough
            return isPlayerEntityOldEnough(player);
        }
    }

    public static boolean isPlayerEntityLoaded(final Minecraft mc) {
        final ClientPlayerEntity player = mc.player;
        if (player == null) {
            //LOGGER.debug("Player entity is not loaded yet.");
            return false;
        } else {
            // player entity is loaded
            return true;
        }
    }

    /*public static Optional<ClientPlayerEntity> getPlayerEntityIfLoaded(Minecraft mc) {
        final ClientPlayerEntity player = mc.player;
        if (player == null) {
            LOGGER.debug("Player entity is not loaded yet.");
            return Optional.empty();
        } else {
            return Optional.of(player);
        }
    }*/

    public static boolean isPlayerEntityOldEnough(ClientPlayerEntity player) {
        final int ticksExisted = player.ticksExisted;

        if (ticksExisted < SwiftConfig.ticksExisted) {
            //LOGGER.debug("Player entity not existed for long enough yet.");
            //LOGGER.debug("ticksExisted: " + ticksExisted);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isSwiftEnabled() {
        if (SwiftConfig.enableSwift) {
            return true;
        } else {
            LOGGER.debug("Swift is disabled.");
            return false;
        }
    }

    public static boolean areNotTooManyChunkUpdatesPending(final Minecraft mc) {
        // get the amount of chunks still needing to be loaded/updated
        final int pendingChunkUpdates = mc.worldRenderer.chunksToUpdate.size();

        // todo: change from >= to > and adjust default config value accordingly
        if (pendingChunkUpdates >= SwiftConfig.pendingChunkUpdates) {
            //LOGGER.debug("Too many chunk updates pending to proceed.");
            //LOGGER.debug("pendingChunkUpdates: " + pendingChunkUpdates);
            return false;
        } else {
            return true;
        }
    }

    /** Returns 0 early (by using the double ampersands) if any of the criteria fails, returns 1 early if only criteria
     * 1 through 3 are met, returns 2 if all criteria is met. */
    public static byte checkCriteriaPassed(final Minecraft mc) {
        if (isSwiftEnabled() && isWorldLoaded(mc) && isPlayerEntityLoaded(mc)) {
            // Swift enabled: Yes
            // World loaded: Yes
            // Player entity loaded: Yes
            if (isPlayerEntityLoadedAndOldEnough(mc) && areNotTooManyChunkUpdatesPending(mc) &&
                    !mc.isGamePaused() && mc.isGameFocused()) {
                // Player entity old enough: Yes
                // OR Not too many updates pending: Yes
                // OR Game not paused: Yes
                // OR Game focused: Yes
                return 2;
            } else {
                // Player entity old enough: No
                // OR Not too many chunk updates pending: No
                // OR Game not paused: No
                // OR Game focused: No
                return 1;
            }
        } else {
            // Swift enabled: No
            // OR World loaded: No
            // OR Player entity loaded: No
            return 0;
        }
    }
}
