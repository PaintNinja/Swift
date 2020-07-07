package ga.ozli.minecraftmods.swift.client;

import ga.ozli.minecraftmods.swift.CriteriaChecker;
import ga.ozli.minecraftmods.swift.PerformanceDataAnalyser;
import ga.ozli.minecraftmods.swift.PerformanceDataCollector;
import ga.ozli.minecraftmods.swift.Swift;
import ga.ozli.minecraftmods.swift.config.SwiftConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(modid = Swift.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEventSubscriber {

	private static final Logger LOGGER = LogManager.getLogger(Swift.MODID + " Client Forge Event Subscriber");

	// this is for tracking both tick intervals and cooldowns
	private static int checkingCooldown = SwiftConfig.checkInterval;

	private static int samplingRate = SwiftConfig.framerateSamplingRate;

	/**
	 * SWIFT EXPLAINED
	 * ===============
	 * There are 4 stages to Swift.
	 * Stage 1) Criteria checking
	 * Stage 2) Performance data collection and processing
	 * Stage 3) Data analysis
	 * Stage 4) Swift intervention action
	 *
	 * Stage 1
	 * -------
	 * In stage 1, we make sure to only perform performance analysis once we're fairly certain that the data won't be
	 * influenced by known causes of lag outside of our control, such as chunks loading in for the first time or having
	 * a Swift action performed just a second ago.
	 *
	 * The following criteria must be met to proceed to stage 2:
	 * 1) Swift is enabled in the config
	 * 2) A world is loaded
	 * 3) The client's player entity is loaded
	 * 4) The client's player entity is old enough (has existed for at least ticksExisted in the config)
	 * 4) The game window is in focus
	 * 5) The game is not paused
	 * 6) The checking cooldown has expired (reaches 0)
	 *
	 * We check the criteria every tick and cancel/return early if a check fails so that subsequent unnecessary checks
	 * aren't ran. The checkingCooldown is decremented every tick as long as criteria 1 through 3 are met and
	 * checkingCooldown is more than 0.
	 *
	 * Stage 2
	 * -------
	 * In stage 2, we add the checkInterval to the checkingCooldown and start collecting FPS samples every other tick.
	 * Once we've collected enough data, we calculate the mean (average), one percentile (1% low) and standard deviation.
	 *
	 * This data is then sent to stage 3 for analysis.
	 *
	 * Stage 3
	 * -------
	 * In stage 3, the data from stage 2 is analysed to determine what settings should be changed to get us closer to
	 * the targetAvgFramerate specified in the config. The standard deviation and one percentile is also used to detect
	 * inconsistent frametimes and adjust the settings accordingly.
	 *
	 * If the conclusion is that a Swift intervention action is necessary, we proceed onto stage 4, otherwise, we go
	 * back to stage 1.
	 *
	 * Stage 4
	 * -------
	 * In stage 4, Swift intervenes and changes the settings it thinks could help get the game running closer to its
	 * goal. As the game settings may impact performance temporarily while the game adjusts, the cooldownTicks from the
	 * config is added to the cooldown counter for use in stage 1.
	 *
	 * Now that we're done, we go back to stage 1 incase any further adjustments are made after the new settings have
	 * kicked in. (e.g. the player switched worlds, the intervention action wasn't drastic enough, some other reason...)
	 *
	 * SPECIAL CASES
	 * =============
	 * - Idle detection
	 * - FreeSync/GSync boundaries
	 *
	 * Idle detection
	 * --------------
	 * TODO
	 *
	 * FreeSync/GSync boundaries
	 * -------------------------
	 * TODO
	 *
	 * MISSING FEATURES
	 * ================
	 * These things should eventually be added:
	 * - Idle mode/idle detection
	 * - FreeSync/GSync boundaries support
	 * - Remembering the last couple of actions and data associated with them to better inform the analyser at stage 3
	 * - Standard deviation calculation at stage 2
	 */

	/*@SubscribeEvent
	public static void ClientTickEvent(final TickEvent.ClientTickEvent event) {

		final Minecraft mc = Minecraft.getInstance();

		LOGGER.debug("checkingCooldown: " + checkingCooldown);

		// Stage 1
		// -------
		final byte criteria = CriteriaChecker.checkCriteriaPassed(mc);
		if (criteria == 0) {
			LOGGER.debug("Criteria 0");
			// criteria 1 through 3 not met, skip this tick
			return;
		} else if (criteria >= 1) {
			LOGGER.debug("Criteria >=1");
			// criteria to reduce the checkingCooldown counter is met, decrement it by one if checkingCooldown is > 0
			if (checkingCooldown > 0) {
				checkingCooldown--;
				return;
			} else {
				// we're already at 0, so check if we meet all the criteria and if not, reset the cooldown to the
				// checkInterval to avoid spamming checks too often
				if (criteria != 2) {
					LOGGER.debug("Criteria == 1");
					checkingCooldown += SwiftConfig.checkInterval;
					return;
				}
			}
		} else {
			LOGGER.error("");
			LOGGER.error("**************************************");
			LOGGER.error("Unreachable condition somehow reached! Please report this to the Swift mod's developer.");
			LOGGER.error("**************************************");
			LOGGER.error("criteria: " + criteria);
			LOGGER.error("checkingCooldown: " + checkingCooldown);
			LOGGER.error("");
			return;
		}

		// Stage 2
		// -------
		// get the current framerate and add it to loggedFrames for later. This must be done often for accurate results.
		samplingRate--;
		if (samplingRate < 1) {
			PerformanceDataCollector.collectFramerateSample();
			samplingRate = SwiftConfig.framerateSamplingRate;
		}

		// once we've got at least sampleSize of frames collected, calculate the avg framerate and 1% low.
		final int avgFramerate;
		final short onePercentLow;
		if (PerformanceDataCollector.loggedFrames.size() >= SwiftConfig.framerateSamples) {
			// calculate the avg framerate
			avgFramerate = PerformanceDataCollector.calculateAverage();
			LOGGER.debug("Average framerate: " + avgFramerate);

			// calculate the 1% low
			onePercentLow = PerformanceDataCollector.calculateOnePercentile();
			LOGGER.debug("1% low: " + onePercentLow);

			PerformanceDataCollector.loggedFrames.clear();
		} else {
			// not enough samples yet, don't proceed to stage 3
			return;
		}

		// Stage 3
		// -------
		PerformanceDataAnalyser.analyseData(avgFramerate, onePercentLow,SwiftConfig.targetAvgFramerate,
				SwiftConfig.targetOnePercentLow);
		// go back to Stage 1 now that we're done
		checkingCooldown += SwiftConfig.cooldownTicks;
		checkingCooldown += SwiftConfig.checkInterval;

		// old code that used to try to measure framerate drops using the frametime graph - overly complicated solution.
		/*FrameTimer frameTimer = Minecraft.getInstance().getFrameTimer();
		long[] last240FrameTimes = frameTimer.getFrames();
		int ouchCount = 0;
		int oofCount = 0;
		int mehCount = 0;
		int goodCount = 0;
		int greatCount = 0;

		// for each frame in the last240FrameTimes
		for (int i = 0; i < 240; i++) {

			// calculate its line height
			int lineHeight = frameTimer.getLineHeight(last240FrameTimes[i], 30, 60);

			// clamp it to between 0 and 100 so it's easy to measure, even if it's not super-accurate
			int normalisedLineHeight = MathHelper.clamp(lineHeight, 0, 100);
			LOGGER.info("Normalised line height: " + normalisedLineHeight);

			if (normalisedLineHeight > 75) {
				LOGGER.error("OUCH");
				ouchCount++;
			} else if (normalisedLineHeight < 75 && normalisedLineHeight > 50) {
				LOGGER.warn("Oof");
				oofCount++;
			} else if (normalisedLineHeight < 50 && normalisedLineHeight > 25) {
				LOGGER.warn("Meh");
				mehCount++;
			} else if (normalisedLineHeight < 25 && normalisedLineHeight > 10) {
				LOGGER.info("Good!");
				goodCount++;
			} else {
				LOGGER.info("Great!");
				greatCount++;
			}

			LOGGER.error("SUMMARY:" +
					"\nOuch:  " + ouchCount +
					"\nOof:   " + oofCount +
					"\nMeh:   " + mehCount +
					"\nGood:  " + goodCount +
					"\nGreat: " + greatCount);
		*/

		// even more complicated version that used the colour calculations in the debug graph to determine if frame times were good or bad
			/*int frameColour = new DebugOverlayGui(Minecraft.getInstance()).getFrameColor(MathHelper.clamp(lineHeight, 0, 100), 0, 100 / 2, 100);

			int alpha = frameColour >> 24 & 255;
			int red = frameColour >> 16 & 255;
			int green = frameColour >> 8 & 255;
			int blue = frameColour & 255;

			LOGGER.info("Frametime colour: argb(" + alpha + ", " + red + ", " + green + ", " + blue + ")");*/
		//}



		//int fps = Integer.parseInt(debug[0]); // grab the current FPS as shown on the debug screen
		//int fpsCap = Integer.parseInt(debug[2]); // grab the framerate cap as shown on the debug screen

		//int chunkUpdates = Integer.parseInt(debug[2].replaceFirst("\\(", "")); // do the same for chunk updates, but remove the preceeding '(' char first

		// Todo: Check if Optifine support is necessary

		// Don't run Swift when chunks are still being loaded and/or the player has recently entered the world
		/*if (chunkUpdates >= Swift.chunkUpdates || player.ticksExisted < Swift.ticksExisted)
			return;*/

		//System.out.println("fps: " + fps);
		//System.out.println("chunkUpdates: " + chunkUpdates);
	//}

	@SubscribeEvent
	public static void ClientChatEvent(final ClientChatEvent event) {
		final Minecraft mc = Minecraft.getInstance();
		final ClientPlayerEntity player = mc.player;
		assert player != null;

		final String message = event.getMessage();
		if (message.startsWith("/")) {
			final String[] splitMessage = message.split(" ");
			if (message.startsWith("/swift")) {
				if (splitMessage.length == 1) {
					// todo: proper command help message
					player.sendMessage(new TranslationTextComponent(Swift.MODID + ".command.swiftBase"));
					event.setCanceled(true); // don't send the message/command to the server as we're dealing with a client-side-only command here
				} else if (splitMessage.length == 2) {
					if (splitMessage[1].equals("enable")) {
						// todo: enable swift using this command
						mc.player.sendMessage(new TranslationTextComponent(Swift.MODID + ".command.enable"));
						event.setCanceled(true);
					} else if (splitMessage[1].equals("disable")) {
						// todo: disable swift using this command
						mc.player.sendMessage(new TranslationTextComponent(Swift.MODID + ".command.disable"));
						event.setCanceled(true);
					}
				}
			}
		}
	}
}
