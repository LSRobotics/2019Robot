package frc.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalSource;

public class LIDARSensor {

    public Counter counter;
    public double CALIBRATION_OFFSET = -18; //TODO figure out what this value is or if we need it at all.

    public LIDARSensor(DigitalSource source) {
        counter = new Counter(source);
        counter.setMaxPeriod(1.0);
        counter.setSemiPeriodMode(true);
        counter.reset();
    }

    public double getDistance() {
        double distanceCentimeters;
        double distanceInches;
        if(counter.get() < 1) {
            return 0;
        }
        distanceCentimeters = (counter.getPeriod() * 1000000.0 / 10.0) + CALIBRATION_OFFSET; //gets centimeters from pulse width
        distanceInches = distanceCentimeters * 0.393701; //conversion to inches
        return distanceInches;
    }
}