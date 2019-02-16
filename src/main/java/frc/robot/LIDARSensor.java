package frc.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class LIDARSensor implements PIDSource{

    public Counter counter;

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
        distanceCentimeters = (counter.getPeriod() * 1000000.0 / 10.0) + Statics.LIDAR_CALIBRATION_OFFSET; //gets centimeters from pulse width
        distanceInches = distanceCentimeters * Statics.CM_TO_IN; //conversion to inches
        return distanceInches;
    }

    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    public double pidGet() {
        return this.getDistance();
    }

    public void setPIDSourceType(PIDSourceType sourceType) {
        
    }

}