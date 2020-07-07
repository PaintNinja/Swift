package ga.ozli.minecraftmods.swift.config;

import ga.ozli.minecraftmods.swift.Swift;
import net.minecraftforge.common.ForgeConfigSpec;

final class ClientConfig {

	final ForgeConfigSpec.BooleanValue
			enableSwift,
			enableIdleChecking;

	final ForgeConfigSpec.ConfigValue<Integer>
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

	ClientConfig(final ForgeConfigSpec.Builder builder) {
		builder.push("general");
		enableSwift = builder
				.comment("Whether or not Swift is enabled. Note that profile commands will still work regardless of this setting.")
				.translation(Swift.MODID + ".config.enableSwift")
				.define("enableSwift", true);
		builder.pop();

		builder.push("idle");
		enableIdleChecking = builder
				.comment("Should Swift check if the game is minimised or inactive and limit the framerate to improve battery life and increase the hardware's thermal headroom for better performance?")
				.translation(Swift.MODID + ".config.enableIdleChecking")
				.define("enableIdleChecking", true);
		idleFramerateCap = builder
				.comment("The framerate cap to apply when the game window is in a minimised or inactive state. The lower the value, the better the efficiency gain but the longer to ramp up to the normal framerate again.")
				.translation(Swift.MODID + ".config.idleFramerate")
				.define("idleFramerate", 8);
		builder.pop();

		builder.push("timings");
		checkInterval = builder
				.comment("The frequency with which the check runs.")
				.translation(Swift.MODID + ".config.checkInterval")
				.define("checkInterval", 60);
		cooldownTicks = builder
				.comment("Cooldown between running of Swift actions.")
				.translation(Swift.MODID + ".config.cooldownTicks")
				.define("cooldownTicks", 120);
		pendingChunkUpdates = builder
				.comment("Swift actions will not run if pending chunk updates are greater than or equal to this value.")
				.translation(Swift.MODID + ".config.pendingChunkUpdates")
				.define("pendingChunkUpdates", 15);
		ticksExisted = builder
				.comment("Swift actions will not run if the player has been in the world for less than this value, given in ticks.")
				.translation(Swift.MODID + ".config.ticksExisted")
				.define("ticksExisted", 600);
		// 600 ticks is about 30 seconds assuming an avg of 20TPS.
		// It's a fairly long time to ensure that the chunks around where the player usually travels to have been loaded, rather than just the chunks the player is currently nearby.
		framerateSamplingRate = builder
				.comment("How often framerate samples should be collected when collecting performance data for analysis." +
						"\nMeasured as tick intervals, meaning 1 is to sample every other tick, 0 means to sample every tick, 5 means one sample every 5 ticks, etc..." +
						"\nToo low will make it harder to detect microstutters, too high will fill the sample data with duplicate values and skew the average framerate calculation.\n")
				.translation(Swift.MODID + ".config.framerateSamplingRate")
				.define("framerateSamplingRate", 1);
		builder.pop();

		builder.push("targets");
		targetAvgFramerate = builder
				.comment("The average framerate Swift should aim for. Warning: setting this too high could cause annoying lag spikes due to Swift constantly trying to hit a target it can never reach.")
				.translation(Swift.MODID + ".config.targetFramerate")
				.define("targetFramerate", 60);
		framerateSamples = builder
				.comment("How many samples should be collected before calculating the average framerate. A sample is collected every other tick.")
				.translation(Swift.MODID + ".config.framerateSamples")
				.define("framerateSamples", 500);
		// 500 factors in the last 5 seconds worth of frames assuming a consistent 20TPS
		targetOnePercentLow = builder
				.comment("The one percent low framerate that Swift should aim to be above.")
				.translation(Swift.MODID + ".config.targetOnePercentLow")
				.define("targetOnePercentLow", 10);
		builder.pop();

		builder.push("actions");
		minRenderDist = builder
				.comment("The minimum render distance you can tolerate in order to achieve the requested targetFramerate.")
				.translation(Swift.MODID + ".config.minRenderDistance")
				.define("minRenderDistance", 4);
		maxRenderDist = builder
				.comment("The maximum render distance you can tolerate in order to achieve the requested targetFramerate.")
				.translation(Swift.MODID + ".config.maxRenderDistance")
				.define("maxRenderDistance", 30);
		builder.pop();
	}

}
