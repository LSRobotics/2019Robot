package frc.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalSource;

public class IRSensor {

    public Counter counter;

    public IRSensor(DigitalSource source) {
        counter = new Counter(source);
        counter.setMaxPeriod(1.0);
        counter.setSemiPeriodMode(true);
        counter.reset();
    }
    
    public boolean tapeDetected() {
        if(counter.get() > 1900 && counter.get() < 2000) {
            return true;
        }
        else {
            return false;
        }
    }
}