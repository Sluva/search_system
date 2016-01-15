package system.controller;

import com.google.api.client.util.BackOff;
import com.google.api.client.util.ExponentialBackOff;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * CustomBackOff.
 */
public class CustomBackOff implements BackOff {

    /**
     * initialInterval.
     */
    private final int initialInterval = 500;

    /**
     * maxElapsedTime.
     */
    private final int maxElapsedTime = 3000;

    /**
     * maxInterval.
     */
    private final int maxInterval = 1000;

    /**
     * multiplier.
     */
    private final double multiplier = 1.5;

    /**
     * randomization.
     */
    private final double randomization = 0.5;

    /**
     * backoff.
     */
    private final ExponentialBackOff backoff = new ExponentialBackOff.Builder()
            .setInitialIntervalMillis(initialInterval)
    .setMaxElapsedTimeMillis(maxElapsedTime)
    .setMaxIntervalMillis(maxInterval).setMultiplier(multiplier)
    .setRandomizationFactor(randomization).build();

    /**
     * nextBackOffMillis method.
     * @return nextBack.
     * @throws IOException intercepts exceptions.
     */
    @Override
    public final long nextBackOffMillis() throws IOException {
        long nexTime = backoff.nextBackOffMillis();
        Logger.getGlobal().info("call nextBackOffMillis: " + nexTime);
       return nexTime;
    }


    @Override
    public final void reset() throws IOException {
        Logger.getGlobal().info("call reset");
        backoff.reset();
    }
}
