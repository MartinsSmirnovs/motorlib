package net.alex9849.motorlib.sensor;

import java.util.concurrent.TimeUnit;
import net.alex9849.motorlib.pin.Pi4JInputPin;
import com.pi4j.io.gpio.digital.DigitalState;

public class Flow {
    private volatile long pulseCount = 0;
    private long previousPulseCount = 0;
    private long pulsesPerSecond = 0;

    private final int pulsesPerLiter;
    private final Pi4JInputPin pin;

    private long previousMillis = 0;

    /*
     * Flow meter outputs pulses. The resulting count of pulses per time unit
     * divided by the pulses per liter gives us the flow value.
     */
    public Flow(Pi4JInputPin pin, int pulsesPerLiter) {
        this.pulsesPerLiter = pulsesPerLiter;

        this.pin = pin;
        this.pin.getHandle().addListener(e -> {
            if (e.state() == DigitalState.HIGH) {
                pulseCount++;
            }
        });
    }

    /**
     * Returns true if new value can be read(), false otherwise.
     */
    public synchronized boolean run(long millis) {

        // If at least a second has passed since the last update.
        if (millis - previousMillis >= TimeUnit.SECONDS.toMillis(1)) {

            pulsesPerSecond = pulseCount - previousPulseCount;

            previousPulseCount = pulseCount;
            previousMillis = millis;

            return true;
        }

        return false;
    }

    public synchronized double read() {
        // Return current flow rate in liters/sec or similar
        return (double) pulsesPerSecond / pulsesPerLiter;
    }
}
