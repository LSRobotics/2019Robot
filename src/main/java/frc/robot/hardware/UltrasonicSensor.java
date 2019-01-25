package frc.robot.hardware;

import edu.wpi.first.wpilibj.Ultrasonic;
import frc.robot.Statics;

public class UltrasonicSensor {

    private Ultrasonic sensor;

    //With this, multiple Ultrasonic sensors would be achievable
    public UltrasonicSensor(int ping, int echo) {
        sensor = new Ultrasonic(ping, echo);
        sensor.setEnabled(true);
        sensor.setAutomaticMode(true);
    }

    public double getRange() {
        if(sensor.isRangeValid()) {
            return sensor.getRangeInches();
        }
        return -1;
    }

    public Ultrasonic getSensor() {
        return sensor;
    }

    public void stop() {
        sensor.setEnabled(false);
        sensor.close();
    }



}