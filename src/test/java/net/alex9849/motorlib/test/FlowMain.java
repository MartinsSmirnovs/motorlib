package net.alex9849.motorlib.test;

import net.alex9849.motorlib.sensor.Flow;

public class FlowMain {

    public static void main(String... args) throws InterruptedException {

        var sensor = new Flow(9, 1000);

        while (true) {
            sensor.run(System.currentTimeMillis());
            System.out.println("Output: " + sensor.read());
            Thread.sleep(1000);
        }
    }

}
