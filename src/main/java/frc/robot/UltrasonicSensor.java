package frc.robot;

import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicSensor {

    public Ultrasonic ultrasonicSensor;

    public UltrasonicSensor(int PingChannel, int EchoChannel) {
        ultrasonicSensor = new Ultrasonic(PingChannel, EchoChannel);
        //initialization occurs within the constructor for this class
        ultrasonicSensor.setAutomaticMode(true);
        ultrasonicSensor.setDistanceUnits(Ultrasonic.Unit.kInches);
    }

    public Ultrasonic getActualSensor() {
        return ultrasonicSensor;
    }

    public double getRangeInches() {
        return ultrasonicSensor.getRangeInches();
    }
    
    public void destroySensor() {
        ultrasonicSensor.close();
    }
    
    public void ping() {
        ultrasonicSensor.ping();
    }

    public boolean isRangeValid() {
        return ultrasonicSensor.isRangeValid();
    }
}