package ga.ozli.minecraftmods.swift;

import ga.ozli.minecraftmods.swift.config.SwiftConfig;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PerformanceDataCollector {

    private static final Logger LOGGER = LogManager.getLogger(Swift.MODID + " Performance Data Collection & Processing");

    // Shorts and bytes rather than ints in Java seem to be controversial, but the general consensus (including Oracle's
    // documentation for Java SE 8) seems that they are useful for reducing memory usage in arrays and when initialising
    // lot of variables. They can be slightly slower to perform calculations on than ints due to the Java Bytecode not
    // directly supporting operations on shorts/bytes causing explicit type casting to be needed, however the
    // performance loss is negligible due to better cache locality caused by smaller data allocations, the JVM runtime
    // bytecode optimisation process and lower memory use.
    //
    // TL;DR: Using shorts and bytes instead of ints for Arrays can be beneficial. Elsewhere it doesn't make a
    // meaningful difference, in those cases it's more about self-documenting code (tells the reader that the value
    // should/will never exceed a certain value).
    public static List<Short> loggedFrames = new ArrayList<>(SwiftConfig.framerateSamples);

    public static void collectFramerateSample() {
        final short currentFramerate = (short) Minecraft.debugFPS;
        loggedFrames.add(currentFramerate);
        LOGGER.debug("Added sample with currentFramerate of " + currentFramerate);
    }

    public static int calculateAverage() {
        return calculateAverage(loggedFrames);
    }

    public static int calculateAverage(List<Short> values) {
        final int sumOfValues = values.parallelStream().mapToInt(Short::shortValue).sum();
        return sumOfValues / values.size();
    }

    public static short calculateOnePercentile() {
        return calculateOnePercentile(loggedFrames);
    }

    public static short calculateOnePercentile(List<Short> values) {
        Collections.sort(values); // sort from lowest to highest
        final short indexNumber = (short) Math.ceil(0.01 * values.size()); // get the index number of the 1% position
        final Object[] loggedFramesArray = values.toArray(); // convert the ArrayList to an array so we can directly access an index
        return (short) loggedFramesArray[indexNumber];
    }
}
