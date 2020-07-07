package ga.ozli.minecraftmods.swift.config;

import net.minecraftforge.fml.config.ModConfig;

public final class SwiftConfig {

	public static boolean
			enableSwift,
			enableIdleChecking;

	public static int
			idleFramerateCap,
			checkInterval,
			cooldownTicks,
			pendingChunkUpdates,
			targetAvgFramerate,
			targetOnePercentLow,
			ticksExisted,
			framerateSamples,
			framerateSamplingRate,
			minRenderDist,
			maxRenderDist;

	public static void bakeClient(final ModConfig config) {
		enableSwift = ConfigHolder.CLIENT.enableSwift.get();

		enableIdleChecking = ConfigHolder.CLIENT.enableIdleChecking.get();
		idleFramerateCap = ConfigHolder.CLIENT.idleFramerateCap.get();

		checkInterval = ConfigHolder.CLIENT.checkInterval.get();
		cooldownTicks = ConfigHolder.CLIENT.cooldownTicks.get();
		pendingChunkUpdates = ConfigHolder.CLIENT.pendingChunkUpdates.get();
		ticksExisted = ConfigHolder.CLIENT.ticksExisted.get();
		framerateSamplingRate = ConfigHolder.CLIENT.framerateSamplingRate.get();

		targetAvgFramerate = ConfigHolder.CLIENT.targetAvgFramerate.get();
		targetOnePercentLow = ConfigHolder.CLIENT.targetOnePercentLow.get();
		framerateSamples = ConfigHolder.CLIENT.framerateSamples.get();

		minRenderDist = ConfigHolder.CLIENT.minRenderDist.get();
		maxRenderDist = ConfigHolder.CLIENT.maxRenderDist.get();
	}

}
