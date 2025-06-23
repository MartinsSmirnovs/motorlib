package net.alex9849.motorlib.pin;

import com.pi4j.io.gpio.analog.AnalogInput;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.PullResistance;

public class Pi4JInputPin implements IInputPin {
    private DigitalInput input;

    public Pi4JInputPin(DigitalInput input) {
        this.input = input;
    }

    @Override
    public boolean isHigh() {
        return input.isHigh();
    }

    @Override
    public boolean isPull() {
        return input.pull() == PullResistance.PULL_UP;
    }

    public DigitalInput getHandle() {
        return input;
    }
}
