package frc.robot;

import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicSensor {

    public static Ultrasonic ultrasonicSensor;
    public int pingChannel = 3;
    public int echoChannel = 2;

    public void initializeUltrasonic() {
        ultrasonicSensor = new Ultrasonic(pingChannel, echoChannel);
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
}