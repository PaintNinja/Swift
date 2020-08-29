package ga.ozli.minecraftmods.swift;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PerformanceDataAnalyser {

    private static final Logger LOGGER = LogManager.getLogger(Swift.MODID + " Performance Data Analysis");

    public static void analyseData(int avgFramerate, short onePercentLow, int targetAvgFPS, int targetOnePercentLow) {
        // very basic algorithm for now as a proof-of-concept
        if (avgFramerate < targetAvgFPS) {
            // lower render dist
            //LOGGER.info("Lower render dist");
        } else {
            // higher render dist
            //LOGGER.info("Higher render dist");
        }
    }
}
