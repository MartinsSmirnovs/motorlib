package net.alex9849.motorlib.sensor;

import java.util.concurrent.TimeUnit;

import com.pi4j.Pi4J;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;

public class Flow {
    private volatile long pulseCount = 0;
    private long previousPulseCount = 0;
    private long pulsesPerSecond = 0;

    private final int pulsesPerLiter;
    private final DigitalInput pin;

    private long previousMillis = 0;

    /*
     * Flow meter outputs pulses. The resulting count of pulses per time unit
     * divided by the pulses per liter gives us the flow value.
     */
    public Flow(int pin, int pulsesPerLiter) {
        this.pulsesPerLiter = pulsesPerLiter;

        var pi4j = Pi4J.newAutoContext();

        this.pin = pi4j.create(DigitalInput.newConfigBuilder(pi4j)
                .address(pin)
                .build());

        this.pin.addListener(e -> {
            if (e.state() == DigitalState.HIGH) {
                pulseCount++;
            }
        });
    }

    public synchronized void run(long millis) {

        // If at least a second has passed since the last update.
        if (millis - previousMillis >= TimeUnit.SECONDS.toMillis(1)) {

            pulsesPerSecond = pulseCount - previousPulseCount;

            previousPulseCount = pulseCount;
            previousMillis = millis;
        }
    }

    public synchronized double read() {
        // Return current flow rate in liters/sec or similar
        return (double) pulsesPerSecond / pulsesPerLiter;
    }
}
